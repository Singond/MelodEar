package com.github.singond.melodear.desktop.keyboard;

import javax.inject.Inject;
import javax.sound.midi.InvalidMidiDataException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.AudioDevice;
import com.github.singond.melodear.desktop.components.KeyboardListener;
import com.github.singond.music.Pitch;

/**
 * Sound module for the piano keyboard which captures key strokes
 * and plays sounds in response.
 *
 * @author Singon
 */
public class KeyboardAudio implements KeyboardListener {

	private static Logger logger = LogManager.getLogger(KeyboardAudio.class);

	private AudioDevice audio;

	@Inject
	KeyboardSettings settings;

	@Inject
	public KeyboardAudio(AudioDevice audio, KeyboardSettings settings) {
		logger.debug("Creating KeyboardAudio");
		if (audio == null) {
			throw new NullPointerException("Audio device must not be null");
		}
		this.audio = audio;
		this.settings = settings;
	}

	@Override
	public void keyDown(Pitch pitch) {
		try {
			if (settings.getKeyDuration() == KeyPlayDuration.NEXT_KEY) {
				audio.stopAllNotes();
			}
			audio.playNote(pitch);
		} catch (InvalidMidiDataException e) {
			logger.error("Error playing note", e);
		}
	}

	@Override
	public void keyUp(Pitch pitch) {
		if (settings.getKeyDuration() == KeyPlayDuration.KEY_HELD) {
			try {
				audio.stopNote(pitch);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
				logger.error("Error stopping note", e);
			}
		}
	}

}
