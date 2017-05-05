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

import static com.google.common.base.Preconditions.checkState;
import static org.lwjgl.openal.ALC10.ALC_FREQUENCY;
import static org.lwjgl.openal.ALC10.ALC_REFRESH;
import static org.lwjgl.openal.ALC10.ALC_SYNC;
import static org.lwjgl.openal.ALC10.ALC_TRUE;
import static org.lwjgl.openal.ALC10.alcGetInteger;
import static org.lwjgl.openal.ALC11.ALC_MONO_SOURCES;
import static org.lwjgl.openal.ALC11.ALC_STEREO_SOURCES;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.Configuration;

import com.techshroom.emergencylanding.library.lwjgl.ErrUtil;
import com.techshroom.emergencylanding.library.util.LUtils;

public class SoundPlayer implements AutoCloseable {

    static {
        // Temporary work-around for AL being broken on OSX
        // Must run before any OpenAL functions are called!
        Configuration.OPENAL_LIBRARY_NAME.set("openal.1.17.2");
        Configuration.LIBRARY_PATH.set(Configuration.LIBRARY_PATH.get() + File.pathSeparator + "./libs");
    }

    private final Thread startedThread = Thread.currentThread();
    private final long device;
    private final long context;
    // factories
    private final SoundClip soundClipFactory = new SoundClip(this);

    public SoundPlayer() {
        this.device = ALC10.alcOpenDevice((ByteBuffer) null);
        if (this.device == 0) {
            throw new IllegalStateException("Failed to open the default device.");
        }
        this.context = ALC10.alcCreateContext(this.device, (IntBuffer) null);
        if (this.context == 0) {
            throw new IllegalStateException("no context?");
        }
        ALC10.alcMakeContextCurrent(this.context);
        ALCCapabilities deviceCaps = ALC.createCapabilities(this.device);
        if (!deviceCaps.OpenALC11) {
            throw new IllegalStateException("OpenAL 1.1 is required.");
        }
        AL.createCapabilities(deviceCaps);
        String deviceSpecifier = ALC10.alcGetString(this.device, ALC10.ALC_DEVICE_SPECIFIER);
        System.err.println("Using device " + deviceSpecifier);
        System.out.println("ALC_FREQUENCY: " + alcGetInteger(this.device, ALC_FREQUENCY) + "Hz");
        System.out.println("ALC_REFRESH: " + alcGetInteger(this.device, ALC_REFRESH) + "Hz");
        System.out.println("ALC_SYNC: " + (alcGetInteger(this.device, ALC_SYNC) == ALC_TRUE));
        System.out.println("ALC_MONO_SOURCES: " + alcGetInteger(this.device, ALC_MONO_SOURCES));
        System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(this.device, ALC_STEREO_SOURCES));
    }

    public SoundClip getSoundClipFactory() {
        return this.soundClipFactory;
    }

    public void setupListener() {
        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
        FloatBuffer buf = BufferUtils.createFloatBuffer(6);
        buf.put(new float[] { 0, 0, -1, 0, 1, 0 });
        buf.flip();
        AL10.alListenerfv(AL10.AL_ORIENTATION, buf);
    }

    private void checkCrossThread() {
        checkState(this.startedThread == Thread.currentThread(), "cross-thread AL usage!");
    }

    public int genBuffers(ALBufferData data) {
        checkCrossThread();
        int buffer = AL10.alGenBuffers();
        ErrUtil.checkALError();
        LUtils.print("genBuffers from " + data);
        if (data instanceof ALBufferData.Short) {
            AL10.alBufferData(buffer, data.getFormat(), ((ALBufferData.Short) data).getData(), data.getSampleRate());
        } else if (data instanceof ALBufferData.Byte) {
            AL10.alBufferData(buffer, data.getFormat(), ((ALBufferData.Byte) data).getData(), data.getSampleRate());
        } else {
            throw new IllegalArgumentException("unknown ALBufferData class " + data.getClass());
        }
        ErrUtil.checkALError();
        return buffer;
    }

    @Override
    public void close() {
        ALC10.alcDestroyContext(this.context);
        ALC10.alcCloseDevice(this.device);
    }

}
