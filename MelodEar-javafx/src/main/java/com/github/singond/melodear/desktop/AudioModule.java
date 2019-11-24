package com.github.singond.melodear.desktop;

import javax.inject.Singleton;
import javax.sound.midi.MidiUnavailableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dagger.Module;
import dagger.Provides;

@Module
class AudioModule {

	private static Logger logger = LogManager.getLogger(AudioModule.class);

	@Provides @Singleton
	static AudioDevice provideAudioDevice() {
		try {
			return new MidiAudioDevice();
		} catch (MidiUnavailableException e) {
			logger.error("Error obtaining MIDI receiver from system", e);
			throw new RuntimeException("Error constructing MidiAudioDevice", e);
		}
	}
}
