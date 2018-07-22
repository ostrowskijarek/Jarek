package com.test.engine;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
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
public class MainTest2 {

	@Test
	public void testCoupling() throws Exception {
		Main main = Mockito.spy(Main.class);
		PowerMockito.mockStatic(Engine.class);
		EventEntry eeStartId = new EventEntry();
		eeStartId.setId("0001");
		main.coupling(eeStartId);
		assertEquals(Main.getUncoupledEventsMap().size(), 1);

		PowerMockito.verifyStatic(Mockito.times(0));
		Engine.process(Matchers.any(EventEntry.class), Matchers.any(EventEntry.class));
		PowerMockito.verifyStatic(Mockito.times(0));
		Engine.store(Matchers.any(EventEntry.class));

		EventEntry eeFinishedId = new EventEntry();
		eeFinishedId.setId("0001");
		main.coupling(eeFinishedId);
		assertEquals(Main.getUncoupledEventsMap().size(), 0);

		PowerMockito.verifyStatic(Mockito.times(1));
		Engine.process(Matchers.any(EventEntry.class), Matchers.any(EventEntry.class));
		PowerMockito.verifyStatic(Mockito.times(1));
		Engine.store(Matchers.any(EventEntry.class));

	}
}
