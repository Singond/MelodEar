package com.github.singond.melodear;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.singond.music.Accidental;
import com.github.singond.music.BasePitchClass;
import com.github.singond.music.Pitch;
import com.github.singond.music.PitchClass;

/**
 * Parses pitch names given in scientific notation
 * (e.g. {@code C4} for the middle C).
 *
 * @author Singon
 */
public class ScientificPitchParser implements PitchParser {

	private static final Pattern pitchName = Pattern.compile("([A-G])([#xb]*)(-?\\d+)");
//	private static final Pattern pitchName = Pattern.compile("([A-G])");
	private static final Pattern flatPattern = Pattern.compile("b+");
	private static final Pattern sharpPattern = Pattern.compile("#+");
	private static final Pattern doubleSharpPattern = Pattern.compile("x");

	@Override
	public Pitch parse(CharSequence seq) {
		Matcher m = pitchName.matcher(seq);
		if (m.find()) {
			int octave = Integer.parseInt(m.group(3));
			PitchClass pc = parsePitchClass(m.group(1), m.group(2));
			return Pitch.of(pc, octave);
		}
		throw new IllegalArgumentException("Wrong pitch format: " + seq);
	}

	private PitchClass parsePitchClass(CharSequence pc, CharSequence acc) {
		if (pc.length() < 1) {
			throw new IllegalArgumentException("Empty pitch class name");
		}
		final BasePitchClass b = parseBasePitchClass(pc.charAt(0));
		final Accidental a;
		if (acc.length() == 0) {
			a = Accidental.NATURAL;
		} else if (flatPattern.matcher(acc).matches()) {
			a = Accidental.ofSteps(-acc.length());
		} else if (sharpPattern.matcher(acc).matches()) {
			a = Accidental.ofSteps(acc.length());
		} else if (doubleSharpPattern.matcher(acc).matches()) {
			a = Accidental.DOUBLE_SHARP;
		} else {
			throw new IllegalArgumentException("Wrong accidental: " + acc);
		}
		return PitchClass.of(b, a);
	}

	private BasePitchClass parseBasePitchClass(char name) {
		switch (name) {
			case 'C': return BasePitchClass.C;
			case 'D': return BasePitchClass.D;
			case 'E': return BasePitchClass.E;
			case 'F': return BasePitchClass.F;
			case 'G': return BasePitchClass.G;
			case 'A': return BasePitchClass.A;
			case 'B': return BasePitchClass.B;
			default: throw new IllegalArgumentException(
					"Unknown base pitch class name: " + name);
		}
	}

}
