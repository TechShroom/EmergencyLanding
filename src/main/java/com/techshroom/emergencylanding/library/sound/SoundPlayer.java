/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.emergencylanding.library.sound;

import java.io.IOException;
import java.nio.file.Paths;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALContext;

public class SoundPlayer {

    static {
        ALContext.create();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ALC.destroy();
            }
        });
    }

    /* TODO implement the hell out of this */
    /**
     * Plays .wav file
     * 
     * @param soundFile
     */
    public static void playWAV(String soundFile) {
        playWAV(soundFile, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     */
    public static void playWAV(String soundFile, float volume) {
        playWAV(soundFile, volume, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     */
    public static void playWAV(String soundFile, float volume, float pitch) {
        playWAV(soundFile, volume, pitch, false);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     * @param loop
     */
    public static void playWAV(String soundFile, float volume, float pitch,
            boolean loop) {
        try (
                AudioInputStream data = AudioSystem
                        .getAudioInputStream(Paths.get(soundFile).toFile())) {
            Clip clip = AudioSystem.getClip();
            clip.open(data);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException
                | LineUnavailableException e) {
            e.printStackTrace();
            return;
        }
    }
}
