package com.github.singond.melodear.desktop.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.util.StringConverter;

import com.github.singond.melodear.IntervalView;

public class IntervalViews {

	private static final List<IntervalView> VALUES;
	static {
		VALUES = new ArrayList<>(13);
		VALUES.add(IntervalView.UNSET);
		VALUES.add(IntervalView.UNISON);
		VALUES.add(IntervalView.MINOR_SECOND);
		VALUES.add(IntervalView.MAJOR_SECOND);
		VALUES.add(IntervalView.MINOR_THIRD);
		VALUES.add(IntervalView.MAJOR_THIRD);
		VALUES.add(IntervalView.PERFECT_FOURTH);
		VALUES.add(IntervalView.PERFECT_FIFTH);
		VALUES.add(IntervalView.MINOR_SIXTH);
		VALUES.add(IntervalView.MAJOR_SIXTH);
		VALUES.add(IntervalView.MINOR_SEVENTH);
		VALUES.add(IntervalView.MAJOR_SEVENTH);
		VALUES.add(IntervalView.PERFECT_OCTAVE);
	}

	public static final StringConverter<IntervalView> CONVERTER
			= new StringConverter<IntervalView>() {

		@Override
		public String toString(IntervalView object) {
			return object.nameKey();
		}

		@Override
		public IntervalView fromString(String string) {
			for (IntervalView i : VALUES) {
				if (Objects.equals(i.nameKey(), string)) {
					return i;
				}
			}
			return IntervalView.UNSET;
		}
	};

	private IntervalViews() {
		throw new UnsupportedOperationException("Non-instantiable class");
	}

	public static List<IntervalView> values() {
		return Collections.unmodifiableList(VALUES);
	}
}
