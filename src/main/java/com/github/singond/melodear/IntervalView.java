package com.github.singond.melodear;

import java.util.ResourceBundle;

import com.github.singond.music.Interval;
import com.github.singond.music.SimpleInterval;

public class IntervalView {

	private final Interval value;
	private final String nameKey;

	public static final IntervalView UNSET = new IntervalView(
			null, "interval.unset");
	public static final IntervalView UNISON = new IntervalView(
			SimpleInterval.UNISON, "interval.unison");
	public static final IntervalView MINOR_SECOND = new IntervalView(
			SimpleInterval.MINOR_SECOND, "interval.minor_second");
	public static final IntervalView MAJOR_SECOND = new IntervalView(
			SimpleInterval.MAJOR_SECOND, "interval.major_second");
	public static final IntervalView MINOR_THIRD = new IntervalView(
			SimpleInterval.MINOR_THIRD, "interval.minor_third");
	public static final IntervalView MAJOR_THIRD = new IntervalView(
			SimpleInterval.MAJOR_THIRD, "interval.major_third");
	public static final IntervalView PERFECT_FOURTH = new IntervalView(
			SimpleInterval.PERFECT_FOURTH, "interval.perfect_fourth");
	public static final IntervalView PERFECT_FIFTH = new IntervalView(
			SimpleInterval.PERFECT_FIFTH, "interval.perfect_fifth");
	public static final IntervalView MINOR_SIXTH = new IntervalView(
			SimpleInterval.MINOR_SIXTH, "interval.minor_sixth");
	public static final IntervalView MAJOR_SIXTH = new IntervalView(
			SimpleInterval.MAJOR_SIXTH, "interval.major_sixth");
	public static final IntervalView MINOR_SEVENTH = new IntervalView(
			SimpleInterval.MINOR_SEVENTH, "interval.minor_seventh");
	public static final IntervalView MAJOR_SEVENTH = new IntervalView(
			SimpleInterval.MAJOR_SEVENTH, "interval.major_seventh");
	public static final IntervalView PERFECT_OCTAVE = new IntervalView(
			SimpleInterval.PERFECT_OCTAVE, "interval.perfect_octave");

	public IntervalView(Interval value, String nameKey) {
		this.value = value;
		this.nameKey = nameKey;
	}

	public Interval value() {
		return value;
	}

	public String nameKey() {
		return nameKey;
	}

	public String name(ResourceBundle bundle) {
		if (bundle == null || !bundle.containsKey(nameKey)) {
			return nameKey;
		}
		return bundle.getString(nameKey);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameKey == null) ? 0 : nameKey.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		IntervalView other = (IntervalView) obj;
		if (nameKey == null) {
			if (other.nameKey != null) return false;
		} else if (!nameKey.equals(other.nameKey)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}
}
