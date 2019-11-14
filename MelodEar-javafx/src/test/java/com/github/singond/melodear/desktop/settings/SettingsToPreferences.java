package com.github.singond.melodear.desktop.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.singond.settings.MockSettings;

public class SettingsToPreferences {

	private static final SimpleDateFormat DATE_FMT
			= new SimpleDateFormat("yyyy-MM-dd");

	private String name = "Some name";
	private Path path = Paths.get("/home/user/thefile.txt");
	private Date date;
	private Integer integer = Integer.valueOf(68);

	private Preferences userPrefs;
	private PreferencesStorage storage;
	private MockSettings settings;

	@Before
	public void init() {
		try {
			date = DATE_FMT.parse("2013-06-30");
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Could not initialize test");
		}

		settings = new MockSettings();
		settings.setName(name);
		settings.setPath(path);
		settings.setDate(new Date(date.getTime()));
		settings.getNested().setDate(new Date(date.getTime()));
		settings.getNested().setInteger(integer);

		userPrefs = Preferences.userNodeForPackage(SettingsToPreferences.class);
		storage = new PreferencesStorage(userPrefs);
	}

	@Test
	public void writeSettings() {
		storage.writeSettings(settings);
		assertEquals(name, userPrefs.get("MockSettings.name", ""));
		assertEquals(path.toString(), userPrefs.get("MockSettings.path", ""));
		assertEquals(DATE_FMT.format(date), userPrefs.get("MockSettings.date", ""));
		assertEquals(integer.toString(), userPrefs.get("MockSettings.nested.integer", ""));
		assertEquals(DATE_FMT.format(date), userPrefs.get("MockSettings.nested.date", ""));
	}

	@Test
	public void readSettings() {
		storage.writeSettings(settings);
		MockSettings saved = new MockSettings();
		storage.readSettings(saved);
		assertEquals(name, saved.getName());
		assertEquals(path, saved.getPath());
		assertEquals(date, saved.getDate());
		assertEquals(integer, saved.getNested().getInteger());
		assertEquals(date, saved.getNested().getDate());
	}

	@After
	public void clearPreferences() {
		try {
			userPrefs.removeNode();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

}
