package com.comp2042;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    // File paths relative to src/main/resources/
    private static final String MOVE_SOUND = "/sounds/move.wav";
    private static final String CLEAR_SOUND = "/sounds/clear.wav";
    private static final String GAMEOVER_SOUND = "/sounds/gameover.wav";
    private static final String BGM_SOUND = "/sounds/music.wav";

    private MediaPlayer bgmPlayer;
    private boolean isMuted = false;

    public SoundManager() {
        // Pre-load logic could go here if needed
    }

    public void playMove() {
        playSound(MOVE_SOUND);
    }

    // playRotate removed as requested

    public void playClear() {
        playSound(CLEAR_SOUND);
    }

    public void playGameOver() {
        stopMusic();
        playSound(GAMEOVER_SOUND);
    }

    public void startMusic() {
        if (isMuted) return;
        try {
            URL resource = getClass().getResource(BGM_SOUND);
            if (resource != null) {
                if (bgmPlayer == null) {
                    Media sound = new Media(resource.toString());
                    bgmPlayer = new MediaPlayer(sound);
                    bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                    bgmPlayer.setVolume(0.5); // 50% volume
                }
                bgmPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Could not play music: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    private void playSound(String fileName) {
        if (isMuted) return;
        try {
            URL resource = getClass().getResource(fileName);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toString());
                clip.play();
            } else {
                System.err.println("Sound file missing: " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Error playing sound: " + fileName);
        }
    }
}