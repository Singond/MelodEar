package com.github.singond.melodear.desktop.settings;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class SettingsTest {

	private static final SimpleDateFormat dateFmt
			= new SimpleDateFormat("dd.MM.yyyy");

	@Test
	public void stringValue() {
		MockSettings src = new MockSettings();
		src.setName("Foo");
		MockSettings copy = src.copy();
		assertEquals("Property 'name' not copied correctly", "Foo", copy.getName());
	}

	@Test
	public void immutableValue() {
		MockSettings src = new MockSettings();
		Path path = Paths.get("/home/user/somefile.txt");
		src.setPath(path);
		MockSettings copy = src.copy();
		assertEquals("Property 'path' not copied correctly", path, copy.getPath());
	}

	@Test
	public void mutableValue() {
		MockSettings src = new MockSettings();
		Date dateExp, dateNew;
		try {
			src.setDate(dateFmt.parse("15.03.2016"));
			dateExp = dateFmt.parse("15.03.2016");
			dateNew = dateFmt.parse("15.03.2014");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		MockSettings copy = src.copy();
		assertEquals("Property 'date' not copied correctly",
				dateExp, copy.getDate());
		src.getDate().setTime(dateNew.getTime());
		assertEquals("Property 'date' not protected from changes to original",
				dateExp, copy.getDate());
	}

	@Test
	public void nestedImmutableValue() {
		MockSettings src = new MockSettings();
		Integer integer = Integer.valueOf(64);
		src.getNested().setInteger(integer);
		MockSettings copy = src.copy();
		assertEquals("Property 'nested/integer' not copied correctly",
				integer, copy.getNested().getInteger());
	}

	@Test
	public void nestedMutableValue() {
		MockSettings src = new MockSettings();
		Date dateExp, dateNew;
		try {
			src.getNested().setDate(dateFmt.parse("15.03.2016"));
			dateExp = dateFmt.parse("15.03.2016");
			dateNew = dateFmt.parse("15.03.2014");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		MockSettings copy = src.copy();
		assertEquals("Property 'date' not copied correctly",
				dateExp, copy.getNested().getDate());
		src.getNested().getDate().setTime(dateNew.getTime());
		assertEquals("Property 'date' not protected from changes to original",
				dateExp, copy.getNested().getDate());
	}

}
