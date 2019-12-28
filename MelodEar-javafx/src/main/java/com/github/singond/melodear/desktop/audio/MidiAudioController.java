package com.github.singond.melodear.desktop.audio;

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
				error(key, key + ".desc");
			}
		} catch (MidiUnavailableException e) {
			error("soundbank.midi_unavailable",
					"soundbank.midi_unavailable.desc");
			logger.error("Unable to initialize MIDI device", e);
		}
	}

	public void error(String titleKey, String msgKey) {
		Alert a = new Alert(Alert.AlertType.ERROR, bundle.getString(msgKey));
		a.setHeaderText(bundle.getString(titleKey));
		a.show();   // showAndWait() leaves the dialog empty
	}
}
