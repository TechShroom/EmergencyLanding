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

import static com.google.common.base.Preconditions.checkState;

import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALUtil;

import com.techshroom.emergencylanding.imported.WaveData;

public class SoundPlayer {

    private final Thread startedThread = Thread.currentThread();

    public SoundPlayer() {
        long device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (device == 0) {
            throw new IllegalStateException(
                    "Failed to open the default device.");
        }
        long context = ALC10.alcCreateContext(device, (IntBuffer) null);
        if (context == 0) {
            throw new IllegalStateException("no context?");
        }
        ALC10.alcMakeContextCurrent(context);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        AL.createCapabilities(deviceCaps);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     */
    public void playWAV(String soundFile) {
        playWAV(soundFile, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     */
    public void playWAV(String soundFile, float volume) {
        playWAV(soundFile, volume, 1.0f);
    }

    /**
     * Plays .wav file
     * 
     * @param soundFile
     * @param volume
     * @param pitch
     */
    public void playWAV(String soundFile, float volume, float pitch) {
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
    public void playWAV(String soundFile, float volume, float pitch,
            boolean loop) {
        checkState(this.startedThread == Thread.currentThread(),
                "cross-thread AL usage!");
        int buffer = AL10.alGenBuffers();
        ALUtil.checkALError();

        WaveData waveFile;
        try {
            waveFile = WaveData.create(Paths.get(soundFile).toUri().toURL())
                    .orElseThrow(() -> new IllegalStateException(
                            "file " + soundFile + " caused errors"));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("bad file " + e);
        }
        AL10.alBufferData(buffer, waveFile.format, waveFile.data,
                waveFile.samplerate);
        waveFile.dispose();
        ALUtil.checkALError();

        int source = AL10.alGenSources();
        ALUtil.checkALError();

        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
        AL10.alSourcef(source, AL10.AL_PITCH, pitch);
        AL10.alSourcef(source, AL10.AL_GAIN, volume);
        AL10.alSource3f(source, AL10.AL_POSITION, 0, 0, 0);
        AL10.alSource3f(source, AL10.AL_VELOCITY, 0, 0, 0);
        AL10.alSourcei(source, AL10.AL_LOOPING,
                loop ? AL10.AL_TRUE : AL10.AL_FALSE);
        ALUtil.checkALError();

        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
        FloatBuffer buf = BufferUtils.createFloatBuffer(6);
        buf.put(new float[] { 0, 0, -1, 0, 1, 0 });
        buf.flip();
        AL10.alListenerfv(AL10.AL_ORIENTATION, buf);

        AL10.alSourcePlay(source);
    }

}
