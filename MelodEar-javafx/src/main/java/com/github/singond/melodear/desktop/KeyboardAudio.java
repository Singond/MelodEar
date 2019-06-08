package com.github.singond.melodear.desktop;

import javax.sound.midi.InvalidMidiDataException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.components.KeyboardListener;
import com.github.singond.music.Pitch;

public class KeyboardAudio implements KeyboardListener {

	private static Logger logger = LogManager.getLogger(KeyboardAudio.class);

	private AudioDevice audio;

	public KeyboardAudio(AudioDevice audio) {
		if (audio == null) {
			throw new NullPointerException("Audio device must not be null");
		}
		this.audio = audio;
	}

	@Override
	public void keyDown(Pitch pitch) {
		try {
			audio.playNote(pitch);
		} catch (InvalidMidiDataException e) {
			logger.error("Error playing note", e);
		}
	}

	@Override
	public void keyUp(Pitch pitch) {
		try {
			audio.stopNote(pitch);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			logger.error("Error stopping note", e);
		}
	}

}
