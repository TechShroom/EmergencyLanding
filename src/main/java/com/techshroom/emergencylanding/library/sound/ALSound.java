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

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FREQUENCY;
import static org.lwjgl.openal.AL10.AL_PAUSED;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alGetBufferi;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSource3f;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL11.AL_SAMPLE_OFFSET;

import java.util.Optional;

import org.lwjgl.openal.AL10;

/**
 * Base implementation of Sound for AL based audio.
 */
public abstract class ALSound implements Sound {

    private final int alSource;
    private final int alBuffer;
    private final int sampleRate;
    private float volume = 1;
    private float pitch = 1;
    private boolean loop = false;

    protected ALSound(int alSource, int alBuffer) {
        this.alSource = alSource;
        this.alBuffer = alBuffer;
        this.sampleRate = alGetBufferi(alBuffer, AL_FREQUENCY);
    }

    public int getAlSource() {
        return this.alSource;
    }

    public int getAlBuffer() {
        return this.alBuffer;
    }

    @Override
    public float getVolume() {
        return this.volume;
    }

    @Override
    public ALSound setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public ALSound setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public boolean isLooping() {
        return this.loop;
    }

    @Override
    public ALSound setLooping(boolean looping) {
        this.loop = looping;
        return this;
    }

    private boolean isSourceUsingOurBuffer() {
        return alGetSourcei(this.alSource, AL_BUFFER) == this.alBuffer;
    }

    @Override
    public ALSound setTime(int time) {
        if (isSourceUsingOurBuffer()) {
            alSourcei(this.alSource, AL_SAMPLE_OFFSET, time * this.sampleRate);
        }
        return this;
    }

    @Override
    public int getTime() {
        if (!isSourceUsingOurBuffer()) {
            return 0;
        }
        return alGetSourcei(this.alSource, AL_SAMPLE_OFFSET) / this.sampleRate;
    }

    @Override
    public boolean isPlaying() {
        if (!isSourceUsingOurBuffer()) {
            return false;
        }
        return alGetSourcei(this.alSource, AL_SOURCE_STATE) == AL_PLAYING;
    }

    @Override
    public boolean isPaused() {
        if (!isSourceUsingOurBuffer()) {
            return false;
        }
        return alGetSourcei(this.alSource, AL_SOURCE_STATE) == AL_PAUSED;
    }

    @Override
    public ALSound play() {
        // bind our buffer first
        alSourcei(this.alSource, AL_BUFFER, this.alBuffer);
        alSourcef(this.alSource, AL10.AL_PITCH, this.pitch);
        alSourcef(this.alSource, AL10.AL_GAIN, this.volume);
        alSource3f(this.alSource, AL10.AL_POSITION, 0, 0, 0);
        alSource3f(this.alSource, AL10.AL_VELOCITY, 0, 0, 0);
        alSourcei(this.alSource, AL10.AL_LOOPING, this.loop ? AL10.AL_TRUE : AL10.AL_FALSE);
        alSourcePlay(this.alSource);
        return this;
    }

    @Override
    public ALSound pause() {
        if (!isSourceUsingOurBuffer()) {
            return this;
        }
        alSourcePause(this.alSource);
        return this;
    }

    @Override
    public ALSound stop() {
        if (!isSourceUsingOurBuffer()) {
            return this;
        }
        alSourceStop(this.alSource);
        return this;
    }

    @Override
    public abstract Optional<ALSound> copy();

    @Override
    public ALSound requiredCopy() {
        return copy().orElseThrow(() -> new IllegalArgumentException("A copy is required here."));
    }

}
