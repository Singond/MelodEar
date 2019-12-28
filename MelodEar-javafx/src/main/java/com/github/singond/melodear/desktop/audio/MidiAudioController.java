package com.github.singond.melodear.desktop.audio;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.sound.midi.MidiUnavailableException;

import javafx.scene.control.Alert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.audio.MidiAudioDevice.SoundbankStatus;

/**
 * A manager of the MIDI audio system and its settings.
 *
 * @author Singon
 */
class MidiAudioController implements AudioController {

	private static Logger logger = LogManager.getLogger(MidiAudioController.class);

	private ResourceBundle bundle = ResourceBundle.getBundle("loc/audio");

	@Inject
	MidiAudioDevice device;

	@Inject
	MidiSettings settings;

	@Inject
	public MidiAudioController() {}

	@Inject
	void init() {
		logger.debug("Initializing MidiAudioController");
		settings.soundbankProperty().addListener(o -> updateDevice(settings));
		settings.synthProperty().addListener(o -> updateDevice(settings));
	}

	private void updateDevice(MidiSettings settings) {
		try {
			device.configure(settings);
			SoundbankStatus sbStatus = device.getSoundbankStatus();
			if (!sbStatus.valid()) {
				String key = "soundbank_status." + sbStatus.key();
				error(key, key + ".desc", settings.getSoundbank());
			}
		} catch (MidiUnavailableException e) {
			error("soundbank.midi_unavailable",
					"soundbank.midi_unavailable.desc");
			logger.error("Unable to initialize MIDI device", e);
		}
	}

	private void error(String titleKey, String msgKey) {
		errorBase(bundle.getString(titleKey), bundle.getString(msgKey));
	}

	/**
	 * Displays a parameterized error dialog.
	 * The messages are formatted with
	 * {@link MessageFormat#format(String, Object...)}.
	 *
	 * @param titleKey properties key to the dialog title
	 * @param msgKey properties key to the dialog message content
	 * @param params parameters to be used both in the title and the message
	 */
	private void error(String titleKey, String msgKey, Object... params) {
		errorBase(MessageFormat.format(bundle.getString(titleKey), params),
				MessageFormat.format(bundle.getString(msgKey), params));
	}

	private void errorBase(String title, String msg) {
		Alert a = new Alert(Alert.AlertType.ERROR, msg);
		a.setHeaderText(title);
		// TODO: Make dialog not truncate the text
		a.show();   // showAndWait() leaves the dialog empty
	}
}
