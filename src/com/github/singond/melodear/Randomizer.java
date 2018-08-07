package com.github.singond.melodear;

import java.util.List;
import java.util.Random;

class Randomizer {

	/** Pseudo-random number generator. */
	private Random rnd;

	Randomizer() {
		rnd = new Random();
	}

	public <T> T randomFrom(List<T> list) {
		return list.get(rnd.nextInt(list.size()));
	}
}
