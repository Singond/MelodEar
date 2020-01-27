package com.github.singond.melodear.desktop.trainer;

import dagger.Subcomponent;

/**
 * Creates trainer models and related objects.
 *
 * @author Singon
 */
@Subcomponent
public interface TrainerComponent {

	MelodyTrainerModel getTrainerModel();
	MelodyTrainerKeyboardListener getTrainerKeyboardListener();

	@Subcomponent.Builder
	interface Builder {
		TrainerComponent build();
	}
}
