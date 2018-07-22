package com.test.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.hibernate.Session;

import com.test.database.DbSessionFactory;
import com.test.entities.EventEntry;
import com.test.exceptions.ProvidePathException;

public class Main {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	private static AtomicLong counter = new AtomicLong(0);
	private static HashMap<String, EventEntry> uncoupledEventsMap = new HashMap<>();
	private static BufferedReader logFile;
	private static final int numThreads = 1;
	private static final long startTime = (System.currentTimeMillis());
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(getNumThreads(), getNumThreads(), 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	private static final int batchInfoSize = 10000;
	private static final int maxThreadQueueSize = 10000;

	public static void main(String[] args) {
		logger.info("Started");
		try {
			new Main().initiate(args);
		} catch (ProvidePathException e) {
			System.exit(0);
		} catch (Exception e) {
			logger.error("General Exception " + e.getMessage());
			System.exit(1);
		}
		logger.info("Finished");
		System.exit(0);
	}

	 void initiate(String... args) throws Exception {
		if (args.length != 1) {
			logger.warn("Please provide log file path as the first parameter.");
			throw new ProvidePathException("File not found");
		}
		String eventLogFilePath = args[0];
		logFile = new BufferedReader(new FileReader(eventLogFilePath));
		go();
	}

	void go() throws Exception {
		DbSessionFactory.start();
		process();
		logFile.close();
		DbSessionFactory.close();

		long now = (System.currentTimeMillis());
		logger.info("Processed: " + counter.get() + " Time from start: " + (now - startTime) + "ms");
		if (uncoupledEventsMap.size() > 0) {
			logger.warn("There are orphaned events: " + uncoupledEventsMap.size());
			if (logger.getLevel().intLevel() >= Level.DEBUG.intLevel()) {
				for (EventEntry ee : uncoupledEventsMap.values()) {
					logger.debug("Orphaned: " + ee);
				}
			}
		} else {
			logger.info("All processed events consumed");

		}
	}

	void process() throws Exception {
		logger.debug("Process started");
		String line;
		while ((line = logFile.readLine()) != null) {
			String submitLine = line;
			while (executor.getQueue().size() > maxThreadQueueSize) {
				logger.debug("Too many waiting threads in the executor queue, stopping for 1ms");
				Thread.sleep(1);
			}
			executor.submit(() -> {
				long i = counter.incrementAndGet();
				logger.debug("Iteration: " + i);
				if ((i % batchInfoSize) == 0) {
					long now = (System.currentTimeMillis());
					logger.info("Processed a batch of: " + batchInfoSize + " Total processed: " + i
							+ " Time from start: " + (now - startTime) + "ms");
				}
				try {
					logger.debug("Trying to create EventEntry from the current line");
					EventEntry ee = Engine.makeEventEntry(submitLine);
					logger.debug("EventEntry created, sending over for the coupling process");
					coupling(ee);
				} catch (Exception e) {
					logger.error("error while processing line: " + submitLine + " error: " + e.getMessage());
				}
			});
		}
		executor.shutdown();
		logger.debug("Awaiting termination...");
		executor.awaitTermination(30, TimeUnit.SECONDS);
		logger.debug("Process finished, All threads done");
	}

	void coupling(EventEntry ee) {
		logger.debug("Coupling started");

		synchronized (this) {
			if (uncoupledEventsMap.containsKey(ee.getId())) {
				logger.debug("Matching event found for: " + ee.getId());
				try {
					Engine.process(uncoupledEventsMap.get(ee.getId()), ee);
					uncoupledEventsMap.remove(ee.getId());
					logger.debug("Coupled and ready to store: " + ee.getId());
					Engine.store(ee);

				} catch (Exception e) {
					logger.error("Error during coupling: " + e.getMessage());
				}
			} else {
				logger.debug("This event is first of his ID: " + ee.getId());
				uncoupledEventsMap.put(ee.getId(), ee);
			}
		}

		logger.debug("Coupling finished");
	}

	public static int getNumThreads() {
		return numThreads;
	}

	static void setLogFile(BufferedReader br) {
		logFile = br;
	}

	static HashMap<String, EventEntry> getUncoupledEventsMap() {
		return new HashMap<String, EventEntry>(uncoupledEventsMap);
	}

	static void setUncoupledEventsMap(HashMap<String, EventEntry> newMap) {
		uncoupledEventsMap = newMap;
	}



}
