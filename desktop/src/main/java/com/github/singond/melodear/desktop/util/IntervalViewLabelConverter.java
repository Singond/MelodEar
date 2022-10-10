package com.github.singond.melodear.desktop.util;

import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;

import javafx.util.StringConverter;

import com.github.singond.melodear.IntervalView;

public class IntervalViewLabelConverter extends StringConverter<IntervalView> {

	@Inject @Named("main-resources")
	ResourceBundle bundle;

	@Inject
	public IntervalViewLabelConverter() {}

	@Override
	public String toString(IntervalView object) {
		return object.name(bundle);
	}

	@Override
	public IntervalView fromString(String string) {
		// TODO: Implement conversion if needed
		return IntervalView.UNSET;
	}
}
