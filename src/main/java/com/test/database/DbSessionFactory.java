package com.test.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.test.engine.Main;
import com.test.entities.EventEntry;
import com.test.entities.EventEntryApplicationServer;

public class DbSessionFactory {

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void start() {
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(EventEntry.class).addAnnotatedClass(EventEntryApplicationServer.class)
				.setProperty("hibernate.c3p0.min_size", Integer.toString(Main.getNumThreads()))
				.setProperty("hibernate.c3p0.max_size", Integer.toString(Main.getNumThreads()));
		configuration.configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static void close() {
		sessionFactory.close();
	}
}
