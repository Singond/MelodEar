package com.github.singond.melodear.desktop.keyboard;

import javax.sound.midi.InvalidMidiDataException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.AudioDevice;
import com.github.singond.melodear.desktop.components.KeyboardListener;
import com.github.singond.music.Pitch;

/**
 * An abstract implementation of a piano keyboard listener which plays
 * sounds in response to pressed keys and allows extension.
 *
 * @author Singon
 */
public abstract class AbstractKeyboardListener implements KeyboardListener {

	private static Logger logger
			= LogManager.getLogger(AbstractKeyboardListener.class);

	private AudioDevice audio;

	private KeyboardSettings settings;

	public AbstractKeyboardListener(
			AudioDevice audio, KeyboardSettings settings) {
		logger.debug("Creating KeyboardAudio");
		if (audio == null) {
			throw new NullPointerException("Audio device must not be null");
		}
		this.audio = audio;
		this.settings = settings;
	}

	@Override
	public void keyDown(final Pitch pitch) {
		try {
			if (settings.getKeyDuration() == KeyPlayDuration.NEXT_KEY) {
				audio.stopAllNotes();
			}
			audio.playNote(pitch);
		} catch (InvalidMidiDataException e) {
			logger.error("Error stopping or playing note", e);
		}
		onKeyDown(pitch);
	}

	/**
	 * Called in {@link #keyDown} after the default action.
	 * Override this method in subclass to execute additional action
	 * on "key down" event apart from handling sound.
	 *
	 * @param pitch the pitch of the key pressed
	 */
	protected abstract void onKeyDown(Pitch pitch);

	@Override
	public void keyUp(final Pitch pitch) {
		if (settings.getKeyDuration() == KeyPlayDuration.KEY_HELD) {
			try {
				audio.stopNote(pitch);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
				logger.error("Error stopping note", e);
			}
		}
		onKeyUp(pitch);
	}

	/**
	 * Called in {@link #keyUp} after the default action.
	 * Override this method in subclass to execute additional action
	 * on "key up" event apart from handling sound.
	 *
	 * @param pitch the pitch of the key pressed
	 */
	protected abstract void onKeyUp(Pitch pitch);

}
