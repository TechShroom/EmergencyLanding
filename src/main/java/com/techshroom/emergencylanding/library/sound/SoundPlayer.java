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

import org.lwjgl.openal.ALC;

public class SoundPlayer {

    static {
        ALC.create();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ALC.destroy();
            }
        });
    }

    /* TODO implement the hell out of this */
//    /**
//     * Plays .wav file
//     * 
//     * @param soundFile
//     */
//    public static Audio playWAV(String soundFile) {
//        return playWAV(soundFile, 1.0f);
//    }
//
//    /**
//     * Plays .wav file
//     * 
//     * @param soundFile
//     * @param volume
//     */
//    public static Audio playWAV(String soundFile, float volume) {
//        return playWAV(soundFile, volume, 1.0f);
//    }
//
//    /**
//     * Plays .wav file
//     * 
//     * @param soundFile
//     * @param volume
//     * @param pitch
//     */
//    public static Audio playWAV(String soundFile, float volume, float pitch) {
//        return playWAV(soundFile, volume, pitch, false);
//    }
//
//    /**
//     * Plays .wav file
//     * 
//     * @param soundFile
//     * @param volume
//     * @param pitch
//     * @param loop
//     */
//    public static Audio playWAV(String soundFile, float volume, float pitch,
//            boolean loop) {
//        Audio wavA = null;
//        try {
//            InputStream iStream = LUtils.getInputStream(soundFile);
//            BufferedInputStream bStream = new BufferedInputStream(iStream);
//            wavA = AudioLoader.getAudio("WAV", bStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        SoundStore.get().setSoundVolume(volume);
//
//        wavA.playAsSoundEffect(pitch, 1.0f, loop);
//        return wavA;
//    }
}
