package com.github.singond.melodear.desktop.settings;

import dagger.Subcomponent;

/**
 * Creates instances of {@link SettingsController}.
 *
 * @author Singon
 */
@Subcomponent
public interface SettingsControllerComponent {

	SettingsController getSettingsController();

	@Subcomponent.Builder
	interface Builder {
		SettingsControllerComponent build();
	}
}
