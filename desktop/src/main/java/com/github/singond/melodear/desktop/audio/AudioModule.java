package com.github.singond.melodear.desktop.audio;

import javax.inject.Singleton;
import javax.sound.midi.MidiUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Module;
import dagger.Provides;

import com.github.singond.melodear.desktop.settings.AllSettings;

@Module
public class AudioModule {

	private static Logger logger = LogManager.getLogger(AudioModule.class);

	@Provides @Singleton
	static AudioDevice provideAudioDevice(MidiAudioDevice midiDev) {
		// If multiple audio devices are available, determine the device
		// to be returned here.
		return midiDev;
	}

	@Provides @Singleton
	static AudioController provideAudioController(MidiAudioController ctrl) {
		// If multiple audio devices are available, determine the controller
		// to be returned here.
		return ctrl;
	}

	@Provides @Singleton
	static MidiAudioDevice provideMidiAudioDevice(AllSettings settings) {
		try {
			return new MidiAudioDevice(settings.midi());
		} catch (MidiUnavailableException e) {
			logger.error("Error obtaining MIDI receiver from system", e);
			throw new RuntimeException("Error constructing MidiAudioDevice", e);
		}
	}
}
