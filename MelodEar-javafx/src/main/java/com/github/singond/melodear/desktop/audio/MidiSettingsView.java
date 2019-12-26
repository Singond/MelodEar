package com.github.singond.melodear.desktop.audio;

import javax.inject.Inject;

import com.github.singond.melodear.desktop.settings.AllSettings;
import com.github.singond.melodear.desktop.settings.BasicSettingsView;
import com.github.singond.melodear.desktop.settings.SettingsView;

public class MidiSettingsView extends BasicSettingsView<MidiSettings>
		implements SettingsView<MidiSettings> {

	@Inject
	MidiAudioDevice device;

	@Inject
	public MidiSettingsView() {
		super("midi");
	}

	@Override
	protected Object makeController(AllSettings settings) {
		return new MidiSettingsController(settings.midi(), device);
	}

}
