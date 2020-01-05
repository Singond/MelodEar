package com.github.singond.melodear.desktop.keyboard;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.BasicSettingsView;
import com.github.singond.melodear.desktop.settings.SettingsView;

public class KeyboardSettingsView
		extends BasicSettingsView<KeyboardSettings>
		implements SettingsView<KeyboardSettings> {

	@Inject
	public KeyboardSettingsView() {
		super("keyboard");
	}

	@Override
	protected Object makeController(AllSettings s) {
		return new KeyboardSettingsController(s.keyboard());
	}

}
