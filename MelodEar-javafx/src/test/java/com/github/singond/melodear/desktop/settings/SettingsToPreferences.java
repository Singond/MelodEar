package com.github.singond.melodear.desktop.settings;

import java.nio.file.Paths;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.github.singond.melodear.desktop.Main;
import com.github.singond.settings.MockSettings;

public class SettingsToPreferences {

	private MockSettings settings;

	@Before
	public void init() {
		settings = new MockSettings();
		settings.setName("Some name");
		settings.setPath(Paths.get("/home/user/thefile.txt"));
		settings.setDate(new Date());
		settings.getNested().setDate(new Date());
		settings.getNested().setInteger(Integer.valueOf(68));
	}

	@Test
	public void writeSettings() {
		new PreferencesStorage(Main.class).writeUserSettings(settings);
	}

}
