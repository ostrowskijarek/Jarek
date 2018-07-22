package com.test.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.test.database.DbSessionFactory;
import com.test.entities.EventEntry;
import com.test.entities.EventEntryApplicationServer;
import com.test.enums.EntryState;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DbSessionFactory.class)
@PowerMockIgnore("javax.management.*")
public class EngineTest {
	
	@Test(expected = com.test.exceptions.DuplicateEventException.class)
	public void processTest() throws Exception {
		EventEntry e1 = new EventEntry();
		e1.setId("id1");
		e1.setState(EntryState.STARTED);
		e1.setTimestamp(10);
		EventEntry e2 = new EventEntry();
		e2.setId("id1");
		e2.setState(EntryState.FINISHED);
		e2.setTimestamp(15);
		Engine.process(e1, e2);
		assertTrue(e2.isAlert());
		e1.setState(EntryState.FINISHED);
		Engine.process(e1, e2);
	}

	@Test(expected = com.fasterxml.jackson.core.JsonParseException.class)
	public void makeEventEntryTest() throws Exception {
		String line1 = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}";
		String line2 = "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}";
		String line3 = "{\"id\":\"scsmbstgrate\":\"FINISHED\", \"timestamp\":1491377495216}";
		EventEntry e1 = Engine.makeEventEntry(line1);
		assertTrue((e1 instanceof EventEntry) && (e1 instanceof EventEntryApplicationServer));
		assertEquals(e1.getId(), "scsmbstgra");
		EventEntry e2 = Engine.makeEventEntry(line2);
		assertTrue((e2 instanceof EventEntry) && !(e2 instanceof EventEntryApplicationServer));
		assertEquals(e2.getId(), "scsmbstgrb");
		EventEntry e3 = Engine.makeEventEntry(line3);
		assertNull(e3);
	}
	
	@Test
	public void storeTest() {
		PowerMockito.mockStatic(DbSessionFactory.class);
		SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
		Session session = Mockito.mock(Session.class);
		Transaction transaction = Mockito.mock(Transaction.class);
		Mockito.when(DbSessionFactory.getSessionFactory()).thenReturn(sessionFactory);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.getTransaction()).thenReturn(transaction);
		EventEntry ee = new EventEntry();
		Engine.store(ee);
		Mockito.verify(session, Mockito.times(1)).close();		
	}
}
