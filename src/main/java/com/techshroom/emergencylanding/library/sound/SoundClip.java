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

import static org.lwjgl.openal.AL10.alGenSources;

import java.io.IOException;
import java.util.Optional;

import com.techshroom.emergencylanding.library.util.LUtils;

/**
 * Sounds created by this factory buffer all the audio from the source when it
 * is created. Use this type for short audio, like a coin pickup or explosion.
 */
public class SoundClip implements ALSoundFactory {

    private final SoundPlayer player;

    SoundClip(SoundPlayer player) {
        this.player = player;
    }

    @Override
    public ALSound create(ALInfo info) throws IOException {
        LUtils.print("Loading ALInfo: " + info);
        // Load now
        ALBufferData data = ALBufferData.create(info.getFormat(),
                LUtils.inputStreamToDirectByteBuffer(info.getStream()), info.getSampleRate());
        int buffer = this.player.genBuffers(data);
        LUtils.print("Returning sound clip");
        return new ALSoundClip(buffer);
    }

    private static final class ALSoundClip extends ALSound {

        protected ALSoundClip(int alBuffer) {
            super(alGenSources(), alBuffer);
        }

        @Override
        public Optional<ALSound> copy() {
            return Optional.of(new ALSoundClip(getAlBuffer()));
        }

    }

}
