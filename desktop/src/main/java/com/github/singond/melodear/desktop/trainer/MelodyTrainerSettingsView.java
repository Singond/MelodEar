package com.github.singond.melodear.desktop.trainer;

import java.util.ResourceBundle;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.BasicSettingsView;
import com.github.singond.melodear.desktop.settings.SettingsView;

public class MelodyTrainerSettingsView
		extends BasicSettingsView<MelodyTrainerSettings>
		implements SettingsView<MelodyTrainerSettings> {

	@Inject
	protected MelodyTrainerSettingsView() {
		super("meldict", "/view/trainer/settings_meldict",
				ResourceBundle.getBundle("loc/trainer"));
	}

	@Override
	protected Object makeController(AllSettings s) {
		return new MelodyTrainerSettingsController(s.melodyTrainer());
	}

}
