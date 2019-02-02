package com.github.singond.melodear;

import java.util.Scanner;

public abstract class InteractiveCli {

	private String prompt = ">";
	private boolean terminate = false;

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public final void run() {
		try (Scanner scanner = new Scanner(System.in)) {
    		System.out.println("InterativeCli demo");
    		while (!terminate) {
    			System.out.print(prompt);
    			String line = scanner.nextLine();
    			processLine(line);
    		}
		}
	}

	protected abstract void processLine(String line);

	public final void terminate() {
		terminate = true;
	}
}