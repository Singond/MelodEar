package com.github.singond.melodear.desktop.trainer;

import dagger.Subcomponent;

import com.github.singond.melodear.desktop.PaneScoped;

/**
 * Creates objects related to the trainer view.
 *
 * @author Singon
 */
@PaneScoped
@Subcomponent
public interface TrainerComponent {

	MelodyTrainerModel getTrainerModel();
	MelodyTrainerController getTrainerController();
	MelodyTrainerKeyboardListener getTrainerKeyboardListener();

	@Subcomponent.Builder
	interface Builder {
		TrainerComponent build();
	}
}
