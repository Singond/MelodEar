package com.github.singond.melodear.desktop.trainer;

import dagger.Subcomponent;

/**
 * Creates instances of {@link TrainerModel}.
 *
 * @author Singon
 */
@Subcomponent
public interface TrainerComponent {

	TrainerModel getTrainerModel();
	TrainerKeyboardListener getTrainerKeyboardListener();

	@Subcomponent.Builder
	interface Builder {
		TrainerComponent build();
	}
}
