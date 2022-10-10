package com.github.singond.melodear.desktop;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * The scope of a single application pane.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope
public @interface PaneScoped {}
