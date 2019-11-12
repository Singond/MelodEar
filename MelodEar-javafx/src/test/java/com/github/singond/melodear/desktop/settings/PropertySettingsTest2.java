package com.github.singond.melodear.desktop.settings;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class PropertySettingsTest2 {

	private static final SimpleDateFormat dateFmt
			= new SimpleDateFormat("dd.MM.yyyy");

	@Test
	public void wrappedStringValue() {
		PropertyMockSettings2 src = new PropertyMockSettings2();
		src.setName("Foo");
		PropertyMockSettings2 copy = src.copy();
		assertEquals("Field 'name' not copied correctly:", "Foo", copy.getName());
	}

	@Test
	public void wrappedImmutableValue() {
		PropertyMockSettings2 src = new PropertyMockSettings2();
		Path path1 = Paths.get("/home/user/somefile.txt");
		src.setPath(path1);
		// Copy
		PropertyMockSettings2 copy = src.copy();
		assertEquals("Field 'path' not copied correctly:", path1, copy.getPath());
		// Modify original node
		Path path2 = Paths.get("/home/user/anotherfile.txt");
		src.setPath(path2);
		assertEquals("Field 'path' not set correctly:", path2, src.getPath());
		assertEquals("Field 'path' not protected from changes to original wrapper:",
				path1, copy.getPath());
		// Modify copied node
		Path path3 = Paths.get("/home/user/yetanotherfile.txt");
		copy.setPath(path3);
		assertEquals("Field 'path' not set correctly:", path3, copy.getPath());
		assertEquals("Field 'path' not protected from changes to copied wrapper:",
				path2, src.getPath());
	}

	@Test
	public void wrappedMutableValue() {
		PropertyMockSettings2 src = new PropertyMockSettings2();
		Date date1, date2, date3, date4, date5;
		try {
			date1 = dateFmt.parse("15.03.2014");
			date2 = dateFmt.parse("15.03.2016");
			date3 = dateFmt.parse("15.03.2017");
			date4 = dateFmt.parse("15.03.2018");
			date5 = dateFmt.parse("15.03.2020");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		src.setDate(new Date(date1.getTime()));
		// Copy
		PropertyMockSettings2 copy = src.copy();
		assertEquals("Field 'date' not copied correctly:", date1, copy.getDate());
		// Modify original instance
		src.getDate().setTime(date2.getTime());
		assertEquals("Field 'date' not modified correctly:", date2, src.getDate());
		assertEquals("Field 'date' not protected from changes to original instance:",
				date1, copy.getDate());
		// Modify original node
		src.setDate(new Date(date3.getTime()));
		assertEquals("Field 'date' not set correctly:", date3, src.getDate());
		assertEquals("Field 'date' not protected from changes to original wrapper:",
				date1, copy.getDate());
		// Modify copied instance
		copy.getDate().setTime(date4.getTime());
		assertEquals("Field 'date' not modified correctly:", date4, copy.getDate());
		assertEquals("Field 'date' not protected from changes to copied instance:",
				date3, src.getDate());
		// Modify copied node
		copy.setDate(new Date(date5.getTime()));
		assertEquals("Field 'date' not set correctly:", date5, copy.getDate());
		assertEquals("Field 'date' not protected from changes to original wrapper:",
				date3, src.getDate());
	}

	@Test
	public void wrappedNestedImmutableValue() {
		PropertyMockSettings2 src = new PropertyMockSettings2();
		Integer int1 = Integer.valueOf(64);
		src.getNested().setInteger(int1);
		// Copy
		PropertyMockSettings2 copy = src.copy();
		assertEquals("Field 'nested/integer' not copied correctly:",
				int1, copy.getNested().getInteger());
		// Modify original node
		Integer int2 = Integer.valueOf(-64);
		src.getNested().setInteger(int2);
		assertEquals("Field 'nested/integer' not set correctly:",
				int2, src.getNested().getInteger());
		assertEquals("Field 'nested/integer' not protected from changes to original wrapper:",
				int1, copy.getNested().getInteger());
		// Modify copied node
		Integer int3 = Integer.valueOf(18);
		copy.getNested().setInteger(int3);
		assertEquals("Field 'nested/integer' not set correctly:",
				int3, copy.getNested().getInteger());
		assertEquals("Field 'nested/integer' not protected from changes to copied wrapper:",
				int2, src.getNested().getInteger());
	}

	@Test
	public void wrappedNestedMutableValue() {
		PropertyMockSettings2 src = new PropertyMockSettings2();
		Date date1, date2, date3, date4, date5;
		try {
			date1 = dateFmt.parse("15.03.2014");
			date2 = dateFmt.parse("15.03.2016");
			date3 = dateFmt.parse("15.03.2017");
			date4 = dateFmt.parse("15.03.2018");
			date5 = dateFmt.parse("15.03.2020");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		src.getNested().setDate(new Date(date1.getTime()));
		// Copy
		PropertyMockSettings2 copy = src.copy();
		assertEquals("Field 'nested/date' not copied correctly:",
				date1, copy.getNested().getDate());
		// Modify original instance
		src.getNested().getDate().setTime(date2.getTime());
		assertEquals("Field 'nested/date' not modified correctly:",
				date2, src.getNested().getDate());
		assertEquals("Field 'nested/date' not protected from changes to original instance:",
				date1, copy.getNested().getDate());
		// Modify original node
		src.getNested().setDate(new Date(date3.getTime()));
		assertEquals("Field 'nested/date' not set correctly:",
				date3, src.getNested().getDate());
		assertEquals("Field 'nested/date' not protected from changes to original wrapper:",
				date1, copy.getNested().getDate());
		// Modify copied instance
		copy.getNested().getDate().setTime(date4.getTime());
		assertEquals("Field 'nested/date' not modified correctly:",
				date4, copy.getNested().getDate());
		assertEquals("Field 'nested/date' not protected from changes to copied instance:",
				date3, src.getNested().getDate());
		// Modify copied node
		copy.getNested().setDate(new Date(date5.getTime()));
		assertEquals("Field 'nested/date' not set correctly:",
				date5, copy.getNested().getDate());
		assertEquals("Field 'nested/date' not protected from changes to original wrapper:",
				date3, src.getNested().getDate());
	}

}
