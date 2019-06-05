package com.github.singond.melodear.desktop.components;

import com.github.singond.music.Pitch;

public interface KeyboardListener {

	void keyDown(Pitch pitch);

	void keyUp(Pitch pitch);
}
