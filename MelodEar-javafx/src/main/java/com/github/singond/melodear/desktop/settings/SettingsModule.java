package com.github.singond.melodear.desktop.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.github.singond.melodear.desktop.audio.MidiSettings;
import com.github.singond.melodear.desktop.keyboard.KeyboardSettings;
import com.github.singond.melodear.desktop.trainer.MelodyTrainerSettings;

@Module(subcomponents = {SettingsControllerComponent.class})
public class SettingsModule {

	@Provides @Singleton
	public static SettingsLoader provideSettingsLoader() {
		return new SettingsLoader();
	}

	@Provides
	public static AllSettings provideSettings(SettingsLoader loader) {
		return loader.getSettings();
	}

	@Provides
	public static KeyboardSettings provideKeyboardSettings(AllSettings s) {
		return s.keyboard();
	}

	@Provides
	public static MidiSettings provideMidiSettings(AllSettings s) {
		return s.midi();
	}

	@Provides
	public static MelodyTrainerSettings provideMelodyTrainerSettings(AllSettings s) {
		return s.melodyTrainer();
	}

}
