package com.test.tools;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.test.entities.EventEntry;
import com.test.entities.EventEntryApplicationServer;

public class JsonToolsTest {
	
	@Test(expected = com.fasterxml.jackson.core.JsonParseException.class)
	public void testTransformError() throws Exception {
		JsonTools.toObject("wrong!", EventEntry.class);
	}
	
	@Test
	public void transformEventEntry() throws Exception {
		String eeString = "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}";
		EventEntry ee = (EventEntry) JsonTools.toObject(eeString, EventEntry.class);
		assertTrue(ee instanceof EventEntry);
	}
	@Test
	public void transformEventEntryApplicationServer() throws Exception {
		String eeString = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}";
		EventEntry ee = (EventEntryApplicationServer) JsonTools.toObject(eeString, EventEntryApplicationServer.class);
		assertTrue(ee instanceof EventEntryApplicationServer);
	}
}
