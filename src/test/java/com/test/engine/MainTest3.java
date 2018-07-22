package com.test.engine;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;

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
public class MainTest3 {
	@Test
	public void testProcess() throws Exception {
		Main main = Mockito.spy(Main.class);
		PowerMockito.mockStatic(Engine.class);
		Mockito.doNothing().when(main).coupling(Matchers.any(EventEntry.class));
		String json = "";
		json += "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}\r\n";
		json += " {\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}";
		BufferedReader bufferedReader = new BufferedReader(new StringReader(json));
		Main.setLogFile(bufferedReader);
		main.process();
		Mockito.verify(main, Mockito.times(2)).coupling(Matchers.any(EventEntry.class));		
		PowerMockito.verifyStatic(Engine.class, Mockito.times(2));
		Engine.makeEventEntry(Matchers.any(String.class));
	}

}
