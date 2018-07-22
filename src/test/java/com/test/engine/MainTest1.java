package com.test.engine;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.test.database.DbSessionFactory;
import com.test.entities.EventEntry;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Engine.class, DbSessionFactory.class, LogManager.class, Main.class })
@PowerMockIgnore("javax.management.*")
public class MainTest1 {

	@Test
	public void testGo() throws Exception {

		PowerMockito.mockStatic(DbSessionFactory.class);
		PowerMockito.mockStatic(Engine.class);
		PowerMockito.spy(LogManager.class);
		Logger logger = Mockito.spy(Logger.class);

		PowerMockito.when(logger.getLevel()).thenReturn(Level.DEBUG);
		Mockito.when(LogManager.getLogger(Matchers.any(Class.class))).thenReturn(logger);
		Main main = Mockito.spy(Main.class);
		Mockito.doNothing().when(main).process();
		BufferedReader mockReader = Mockito.mock(BufferedReader.class);
		Main.setLogFile(mockReader);

		HashMap<String, EventEntry> eventsMap = new HashMap<>();
		Main.setUncoupledEventsMap(eventsMap);
		main.go();
		Mockito.verify(logger).info("All processed events consumed");
		EventEntry ee = new EventEntry();
		ee.setId("test001");
		eventsMap.put(ee.getId(), ee);
		main.go();
		Mockito.verify(logger).warn("There are orphaned events: 1");

	}

	

}
