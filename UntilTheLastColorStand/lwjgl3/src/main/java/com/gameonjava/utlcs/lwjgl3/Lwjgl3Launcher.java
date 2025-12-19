package com.gameonjava.utlcs.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.gameonjava.utlcs.Main;
import com.gameonjava.utlcs.gui.Assets;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
            if (StartupHelper.startNewJvmIfRequired()) return;
            createApplication();

    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        configuration.setTitle("Until The Last Color Stands");

        configuration.useVsync(true);
        configuration.setForegroundFPS(60);

        configuration.setWindowedMode(1280, 720);

        configuration.setResizable(false);

        configuration.setWindowIcon(Assets.WAR,Assets.WAR,Assets.WAR,Assets.WAR,Assets.WAR);

        return configuration;
    }
}
