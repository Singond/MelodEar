package com.github.singond.melodear;

import static com.github.singond.music.PitchClass.*;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.singond.music.Pitch;

public class PitchParsing {

	private static ScientificPitchParser spp;

	@BeforeClass
	public static void init() {
		spp = new ScientificPitchParser();
	}

	@Test
	public void scientificFormat() {
		System.out.println("Scientific pitch names:");
		scientific("C4", Pitch.of(C, 4));
		scientific("Cb4", Pitch.of(C_FLAT, 4));
		scientific("D#4", Pitch.of(D_SHARP, 4));
		scientific("G3", Pitch.of(G, 3));
		scientific("Bb4", Pitch.of(B_FLAT, 4));
		scientific("Bx4", Pitch.of(B_DBL_SHARP, 4));
		System.out.println();
	}

	private void scientific(String str, Pitch expected) {
		Pitch parsed = spp.parse(str);
		System.out.println(str + " => " + parsed);
		assertEquals(expected, parsed);
	}
}
