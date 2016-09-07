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

import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.techshroom.emergencylanding.library.util.LUtils;
import com.techshroom.emergencylanding.library.util.interfaces.InputStreamSupplier;

public class SoundUtil {

    private static ALBufferData readVorbis(InputStreamSupplier resource) {
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ByteBuffer vorbis;
            try {
                vorbis = LUtils.inputStreamToDirectByteBuffer(resource);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            IntBuffer error = BufferUtils.createIntBuffer(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == MemoryUtil.NULL)
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return ALBufferData.create(channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm,
                    info.sample_rate());
        }
    }

    private static ALInfo readAudioInputStream(InputStreamSupplier streamCons) throws UnsupportedAudioFileException {
        try (InputStream stream = streamCons.get()) {
            AudioInputStream ais = AudioSystem.getAudioInputStream(stream);
            // Reload in a new format
            AudioFormat audioformat = ais.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioformat.getSampleRate(),
                    16, audioformat.getChannels(), audioformat.getChannels() * 2, audioformat.getSampleRate(),
                    ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
            ais = AudioSystem.getAudioInputStream(decodedFormat, ais);
            audioformat = ais.getFormat();

            // get channels
            int channels = 0;
            if (audioformat.getChannels() == 1) {
                if (audioformat.getSampleSizeInBits() == 8) {
                    channels = AL10.AL_FORMAT_MONO8;
                } else if (audioformat.getSampleSizeInBits() == 16) {
                    channels = AL10.AL_FORMAT_MONO16;
                } else {
                    throw new IllegalStateException("Illegal sample size: " + audioformat.getSampleSizeInBits());
                }
            } else if (audioformat.getChannels() == 2) {
                if (audioformat.getSampleSizeInBits() == 8) {
                    channels = AL10.AL_FORMAT_STEREO8;
                } else if (audioformat.getSampleSizeInBits() == 16) {
                    channels = AL10.AL_FORMAT_STEREO16;
                } else {
                    throw new IllegalStateException("Illegal sample size: " + audioformat.getSampleSizeInBits());
                }
            } else {
                throw new IllegalStateException("Only mono or stereo is supported: " + audioformat.getChannels());
            }

            AudioInputStream ret = ais;
            return ALInfo.create(channels, (int) audioformat.getSampleRate(), () -> ret);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static ByteBuffer convertAudioBytes(ByteBuffer src, boolean twoBytes, ByteOrder order) {
        ByteBuffer dest = MemoryUtil.memCalloc(src.remaining());
        src.order(order);
        if (twoBytes) {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        } else {
            while (src.hasRemaining()) {
                dest.put(src.get());
            }
        }
        dest.rewind();
        return dest;
    }

    public static ALInfo createALInfo(InputStreamSupplier supplier) throws IOException {
        // This stream will be closed here in only some cases.
        @SuppressWarnings("resource")
        // Wrap stream in buffer to ensure mark support
        InputStream stream = new BufferedInputStream(supplier.get());
        try {
            // Try OGG
            if (isVorbis(stream)) {
                ALBufferData data = readVorbis(() -> stream);
                return alBufferDataToInfo(data);
            }
            // Try reading with AIS
            UnsupportedAudioFileException noJavaAudio;
            try {
                return readAudioInputStream(() -> stream);
            } catch (UnsupportedAudioFileException e) {
                noJavaAudio = e;
            }
            // TODO: FFmpeg? something else?
            RuntimeException e = new IllegalArgumentException("Don't know what the stream contains");
            e.addSuppressed(noJavaAudio);
            throw e;
        } catch (RuntimeException | IOException e) {
            // only close stream on exceptions -- it might be used otherwise
            stream.close();
            throw e;
        }
    }

    private static ALInfo alBufferDataToInfo(ALBufferData data) {
        return ALInfo.create(data.getFormat(), data.getSampleRate(), () -> {
            ByteBuffer buf;
            if (data instanceof ALBufferData.Short) {
                ShortBuffer s = ((ALBufferData.Short) data).getData();
                if (!s.hasRemaining()) {
                    return ByteSource.empty().openStream();
                }
                buf = BufferUtils.createByteBuffer(s.capacity() * Short.BYTES);
                while (s.hasRemaining()) {
                    buf.putShort(s.get());
                }
                buf.flip();
            } else {
                buf = ((ALBufferData.Byte) data).getData();
                if (!buf.hasRemaining()) {
                    return ByteSource.empty().openStream();
                }
            }
            return new InputStream() {

                @Override
                public int read() throws IOException {
                    if (!buf.hasRemaining()) {
                        return -1;
                    }
                    return buf.get() & 0xFF;
                }
            };
        });
    }

    private static boolean isVorbis(InputStream stream) throws IOException {
        byte[] fourBytes = new byte[4];
        int read = -1;
        try {
            stream.mark(4);
            read = ByteStreams.read(stream, fourBytes, 0, 4);
            if (read < 4) {
                return false;
            }
            return fourBytes[0] == 'O' && fourBytes[1] == 'g' && fourBytes[2] == 'g' && fourBytes[3] == 'S';
        } finally {
            stream.reset();
        }
    }

}
