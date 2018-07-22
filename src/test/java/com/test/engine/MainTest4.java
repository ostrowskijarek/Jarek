package com.test.engine;

import org.junit.Test;

public class MainTest4 {
	@Test(expected = java.io.FileNotFoundException.class)
	public void testInitiateFileNotFoundException() throws Exception {
		new Main().initiate("fakePath");
	}

	@Test(expected = com.test.exceptions.ProvidePathException.class)
	public void testInitiateProvidePathException() throws Exception {
		new Main().initiate();
	}
}
