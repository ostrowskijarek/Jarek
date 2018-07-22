package com.test.engine;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import com.test.database.DbSessionFactory;
import com.test.entities.EventEntry;
import com.test.entities.EventEntryApplicationServer;
import com.test.exceptions.DuplicateEventException;
import com.test.tools.JsonTools;

public class Engine {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	private static final Pattern typePattern = Pattern.compile(".*\"type\":\".*\".*");
	private static final Pattern hostPattern = Pattern.compile(".*\"host\":\".*\".*");

	public static EventEntry makeEventEntry(String line) throws Exception {
		logger.debug("makeEventEntry called: " + line);
		EventEntry ee;
		Matcher typeMatcher = typePattern.matcher(line);
		Matcher hostMatcher = hostPattern.matcher(line);
		if (typeMatcher.matches() && hostMatcher.matches()) {
			logger.debug("casted as: EventEntryApplicationServer");
			ee = (EventEntryApplicationServer) JsonTools.toObject(line, EventEntryApplicationServer.class);
		} else {
			logger.debug("casted as: EventEntry");
			ee = (EventEntry) JsonTools.toObject(line, EventEntry.class);
		}
		if (ee == null) {
			throw new Exception("can't produce EventEntry");
		}
		logger.debug("makeEventEntry returning: " + ee.getId());
		return ee;
	}

	public static void process(EventEntry firstEe, EventEntry secondEe) throws Exception {
		logger.debug("process called with: firstEe: " + firstEe.getId() + " secondEe:" + secondEe.getId());
		logger.debug("their timestamps: firstEe: " + firstEe.getTimestamp() + " secondEe:" + secondEe.getTimestamp());
		int duration = (int) Math.abs(firstEe.getTimestamp() - secondEe.getTimestamp());
		logger.debug("duration: " + duration);
		if (duration >= 4) {
			secondEe.setAlert(true);
		}
		logger.debug("alert set: " + secondEe.isAlert());
		secondEe.setDuration(duration);
		if (firstEe.getState().equals(secondEe.getState())) {
			logger.warn("possible duplicate status for id: " + secondEe.getId());
			throw new DuplicateEventException();
		}
		logger.debug("process returns: " + secondEe.getId());
	}

	public static void store(EventEntry ee) {
		logger.debug("storing: " + ee.getId());
		Session session = DbSessionFactory.getSessionFactory().openSession();
		session.beginTransaction();
		session.persist(ee);
		session.getTransaction().commit();
		session.close();
		logger.debug("stored: " + ee.getId());

	}

}
