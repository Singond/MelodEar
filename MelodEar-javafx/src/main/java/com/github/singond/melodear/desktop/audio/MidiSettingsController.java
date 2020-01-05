package com.github.singond.melodear.desktop.audio;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javax.sound.midi.MidiDevice;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.singond.melodear.desktop.settings.PathStringConverter;

/**
 * JavaFX controller class for the MIDI settings page.
 *
 * @author Singon
 */
public class MidiSettingsController {

	private static Logger logger
			= LogManager.getLogger(MidiSettingsController.class);

	private static final ResourceBundle bundle
			= ResourceBundle.getBundle("loc/audio");

	private final MidiSettings settings;

	@FXML
	TableView<MidiDevice.Info> synth;
	@FXML
	TableColumn<MidiDevice.Info, String> synthNameColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthVendorColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthDescriptionColumn;
	@FXML
	TableColumn<MidiDevice.Info, String> synthVersionColumn;

	@FXML
	TextField soundbankText;
	@FXML
	Button soundbankBtn;

	public MidiSettingsController(MidiSettings settings) {
		logger.debug("Creating MidiSettingsController");
		this.settings = settings;
	}

	public void initialize() {
		logger.debug("Initializing MIDI settings");
		initSynthList();

		soundbankBtn.addEventHandler(ActionEvent.ACTION,
				e -> selectSoundbankFile());
		soundbankText.textProperty().bindBidirectional(
				settings.soundbankProperty(), new PathStringConverter());
	}

	private void initSynthList() {
		logger.debug("Initializing synthesizer list");
		synth.setItems(settings.getAvailableSynthesizers());
		synth.getSelectionModel().select(settings.getSynth());
		settings.synthProperty().bind(
				synth.getSelectionModel().selectedItemProperty());

		// Set column values
		synthNameColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getName()));
		synthVendorColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getVendor()));
		synthDescriptionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getDescription()));
		synthVersionColumn.setCellValueFactory(f -> new SimpleStringProperty(
				f.getValue().getVersion()));
	}

	private void selectSoundbankFile() {
		logger.debug("Selecting soundbank file");
		// Setup file chooser
		FileChooser chooser = new FileChooser();
		chooser.setTitle(bundle.getString(
				"midi.settings.soundbank.browser.title"));
		Path defaultDir = settings.getSoundbankDefaultDir();
		if (defaultDir != null) {
			chooser.setInitialDirectory(defaultDir.toFile());
		}

		// Launch it and evaluate
		File file = chooser.showOpenDialog(soundbankBtn.getScene().getWindow());
		if (file != null) {
			logger.debug("Selected {}", file);
			Path path = file.toPath();
			if (Files.exists(path)) {
				logger.debug("File exists");
				settings.setSoundbankDefaultDir(path.getParent());
				settings.setSoundbank(path);
			} else {
				logger.warn("File does not exist");
			}
		} else {
			logger.debug("No selection");
		}
	}

}
