package com.pointlessgames.kingdomoflove.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pointlessgames.kingdomoflove.KingdomOfLove;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new KingdomOfLove(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "KingdomOfLove";
        configuration.width = 480;
        configuration.height = 800;
        configuration.samples = 16;
        configuration.resizable = false;
//        configuration.fullscreen = true; //TODO add pause screen
        configuration.addIcon("images/logo.png", FileType.Internal);
        return configuration;
    }
}