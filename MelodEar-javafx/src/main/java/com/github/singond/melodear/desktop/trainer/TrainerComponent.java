package com.github.singond.melodear.desktop.trainer;

import dagger.Subcomponent;

import com.github.singond.melodear.desktop.PaneScoped;

/**
 * Creates trainer models and related objects.
 *
 * @author Singon
 */
@PaneScoped
@Subcomponent
public interface TrainerComponent {

	MelodyTrainerModel getTrainerModel();
	MelodyTrainerKeyboardListener getTrainerKeyboardListener();

	@Subcomponent.Builder
	interface Builder {
		TrainerComponent build();
	}
}
