package com.game.mario;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static final String BACKGROUND_MUSIC_PATH = "assets/audio/backsound_menu.wav";
    public static final String SFX_JUMP = "jump";
    public static final String SFX_COIN = "coin";
    public static final String SFX_ENEMY_STOMP = "stomp_enemy";
    public static final String SFX_BOSS_HIT = "boss_hit";
    public static final String SFX_LEVEL_CLEAR = "level_clear";

    private static AudioManager instance;
    private final AudioClip backgroundClip;
    private AudioClip currentClip;
    private double masterVolume = 0.6;
    private double effectsVolume = 0.7;

    private final Map<String, AudioClip> effectClips = new HashMap<>();

    // 🌟 Tambahan: multiplier volume untuk efek tertentu
    private final Map<String, Double> effectVolumeMultiplier = new HashMap<>();

    private AudioManager() {
        this.backgroundClip = loadClip(BACKGROUND_MUSIC_PATH, true);
        preloadEffects();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private void preloadEffects() {
        registerEffect(SFX_JUMP, "assets/audio/jump.wav");
        registerEffect(SFX_COIN, "assets/audio/coin.wav");
        registerEffect(SFX_ENEMY_STOMP, "assets/audio/stomp_enemy.wav");
        registerEffect(SFX_BOSS_HIT, "assets/audio/boss_hit.wav");
        registerEffect(SFX_LEVEL_CLEAR, "assets/audio/level_clear.wav");

        // 🌟 Atur multiplier suara efek
        effectVolumeMultiplier.put(SFX_JUMP, 1.3);
        effectVolumeMultiplier.put(SFX_COIN, 1.7); // Coin lebih keras
        effectVolumeMultiplier.put(SFX_ENEMY_STOMP, 2.5); // Stomp musuh lebih keras
        effectVolumeMultiplier.put(SFX_LEVEL_CLEAR, 1.8); // Checkpoint / level clear lebih keras
    }

    private void registerEffect(String key, String path) {
        AudioClip clip = loadClip(path, false);
        if (clip != null) {
            effectClips.put(key, clip);
        }
    }

    private AudioClip loadClip(String path, boolean loop) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("AudioManager: File not found -> " + path);
                return null;
            }
            AudioClip clip = new AudioClip(file.toURI().toString());
            clip.setCycleCount(loop ? AudioClip.INDEFINITE : 1);
            clip.setVolume(loop ? masterVolume : effectsVolume);
            System.out.println("AudioManager: Loaded " + path);
            return clip;
        } catch (Exception ex) {
            System.err.println("AudioManager: Failed to load " + path + " -> " + ex.getMessage());
            return null;
        }
    }

    private void stopCurrentClip() {
        if (currentClip != null) {
            currentClip.stop();
        }
    }

    private void playClip(AudioClip clip) {
        if (clip == null) {
            return;
        }
        if (clip == currentClip && clip.isPlaying()) {
            return;
        }
        stopCurrentClip();
        currentClip = clip;
        clip.setVolume(masterVolume);
        clip.play();
    }

    // 🌟 NEW: versi playEffect yang ada multiplier-nya
    public void playEffect(String key) {
        AudioClip clip = effectClips.get(key);
        if (clip == null) {
            System.err.println("AudioManager: Missing effect clip -> " + key);
            return;
        }

        double multiplier = effectVolumeMultiplier.getOrDefault(key, 1.0);
        double finalVolume = Math.min(1.0, effectsVolume * multiplier);

        clip.setVolume(finalVolume);
        clip.play();
    }

    public void playMenuMusic() {
        playClip(backgroundClip);
    }

    public void stopMenuMusic() {
        stopClip(backgroundClip);
    }

    public void playGameMusic() {
        playClip(backgroundClip);
    }

    public void stopGameMusic() {
        stopClip(backgroundClip);
    }

    public void stopAll() {
        stopCurrentClip();
        currentClip = null;
    }

    private void stopClip(AudioClip clip) {
        if (clip != null) {
            clip.stop();
            if (currentClip == clip) {
                currentClip = null;
            }
        }
    }

    public void setMasterVolume(double volume) {
        masterVolume = Math.max(0, Math.min(1, volume));
        if (currentClip != null) {
            currentClip.setVolume(masterVolume);
        }
    }

    public void setEffectsVolume(double volume) {
        effectsVolume = Math.max(0, Math.min(1, volume));
    }
}
