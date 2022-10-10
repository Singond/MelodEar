package com.github.singond.melodear.desktop.settings;

import javafx.util.StringConverter;

public class EnumStringConverter<E extends Enum<E>> extends StringConverter<E> {

	private final E typeref;

	public EnumStringConverter(E e) {
		this.typeref = e;
	}

	@Override
	public String toString(E e) {
		return e.name();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E fromString(String string) {
		return (E) Enum.valueOf(typeref.getClass(), string);
	}

}
