package com.test.playground;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
	public static void main(String... args) throws IOException {
		/*
		 * Generates large files for testing
		 * unpaired -> generates unpaired entries
		 */
		
		boolean unpaired=true;
		
		FileWriter fw = new FileWriter("C:\\Users\\Jarek\\log.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		Random rand = new Random();
		long time = 1491377495210L;
		for (int i = 0; i < 400000; i++) {
			time++;
			long delayed = (time + rand.nextInt(5));
			String line1 = "{\"id\":\"" + i + "\", \"state\":\"STARTED\", \"timestamp\":" + time + "}";
			String line2 = "{\"id\":\"" + i + "\", \"state\":\"FINISHED\", \"timestamp\":" + delayed + "}";
			
			if (unpaired) {
				if (i % 5 == 0) {
					String line3 = "{\"id\":\"aaa" + i + "\", \"state\":\"FINISHED\", \"timestamp\":" + delayed + "}";
					bw.write(line3);
					bw.newLine();
				}
				if (i % 333 == 0) {
					String line3 = "{\"id\":\"bbb" + i + "\", \"state\":\"STARTED\", \"timestamp\":" + delayed + "}";
					bw.write(line3);
					bw.newLine();
				}
			}
			bw.write(line1);
			bw.newLine();
			bw.write(line2);
			bw.newLine();

		}
		bw.close();
		fw.close();
	}

}
