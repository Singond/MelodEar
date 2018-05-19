package com.github.singond.melodear.demo;

import java.util.Scanner;

public class InterativeCli {

	private boolean terminate = false;
	
	public static void main(String[] args) {
		new InterativeCli().run();
	}
	
	public void run() {
		try (Scanner scanner = new Scanner(System.in)) {
    		System.out.println("InterativeCli demo");
    		while (!terminate) {
    			System.out.print('>');
    			String line = scanner.nextLine();
    			processLine(line);
    		}
		}
	}
	
	private void processLine(String line) {
		if (line.equals("exit")) {
			terminate = true;
			System.out.println("Exiting");
		} else {
			System.out.println("Got line " + line);
		}
	}
}