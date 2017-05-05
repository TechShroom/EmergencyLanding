/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshoom.com>
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

import java.util.Optional;

public interface Sound {

    float getVolume();

    Sound setVolume(float volume);

    float getPitch();

    Sound setPitch(float pitch);

    boolean isLooping();

    Sound setLooping(boolean looping);

    int getTime();

    Sound setTime(int time);

    boolean isPlaying();

    boolean isPaused();

    Sound play();

    Sound pause();

    Sound stop();

    /**
     * Copies this sound if possible. A copied sound will share the content of
     * the audio, but none of the characteristics, including play/pause/stop.
     * 
     * @return a copy of the sound, if applicable
     */
    Optional<? extends Sound> copy();

    /**
     * Copies this Sound if possible. Throws an exception if not possible.
     * 
     * @return the copy
     */
    default Sound requiredCopy() {
        return copy().orElseThrow(() -> new IllegalStateException("A copy is required here."));
    }

}
