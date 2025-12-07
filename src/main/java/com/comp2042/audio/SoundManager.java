package com.comp2042.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Manages all game audio including background music and sound effects.
 * Loads sound files from the resources directory and provides methods
 * to play them on demand. Supports both short sound effects (AudioClip)
 * and looping background music (MediaPlayer).
 * 
 * <p>Available sound effects:
 * <ul>
 *   <li>Move - played when a piece moves successfully</li>
 *   <li>Clear - played when one or more lines are completed</li>
 *   <li>Game over - played when the game ends</li>
 * </ul>
 * 
 * <p>Background music loops continuously during gameplay and can be
 * muted via the isMuted flag.
 * 
 * @see AudioClip
 * @see MediaPlayer
 */
public class SoundManager {
    // File paths relative to src/main/resources/
    private static final String MOVE_SOUND = "/sounds/move.wav";
    private static final String CLEAR_SOUND = "/sounds/clear.wav";
    private static final String GAMEOVER_SOUND = "/sounds/gameover.wav";
    private static final String BGM_SOUND = "/sounds/music.wav";

    private MediaPlayer bgmPlayer;
    private boolean isMuted = false;

    /**
     * Constructs a new SoundManager instance.
     * Sound files are loaded lazily when first played.
     */
    public SoundManager() {
        // Pre-load logic could go here if needed
    }

    /**
     * Plays the move sound effect when a piece successfully moves.
     */
    public void playMove() {
        playSound(MOVE_SOUND);
    }

    /**
     * Plays the line clear sound effect when lines are completed.
     */
    public void playClear() {
        playSound(CLEAR_SOUND);
    }

    public void playGameOver() {
        stopMusic();
        playSound(GAMEOVER_SOUND);
    }

    /**
     * Starts playing background music on an infinite loop.
     * Music is set to 50% volume and loops until stopped.
     * Does nothing if muted.
     */
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

    /**
     * Stops the background music if it is currently playing.
     */
    public void stopMusic() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    /**
     * Helper method to play a sound effect from a file path.
     * Loads and plays the audio clip if not muted and file exists.
     * 
     * @param fileName the resource path to the sound file
     */
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
