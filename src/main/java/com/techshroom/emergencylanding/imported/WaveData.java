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
package com.techshroom.emergencylanding.imported;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Optional;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.AL10;

/**
 *
 * Utitlity class for loading wavefiles.
 * 
 * Significant changes to return types and some method code made by Kenzie
 * Togami.
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$ $Id$
 */
public class WaveData {

    private static final Logger LOGGER = LogManager.getLogger(WaveData.class);

    /** actual wave data */
    public final ByteBuffer data;

    /** format type of data */
    public final int format;

    /** sample rate of data */
    public final int samplerate;

    /**
     * Creates a new WaveData
     * 
     * @param data
     *            actual wavedata
     * @param format
     *            format of wave data
     * @param samplerate
     *            sample rate of data
     */
    private WaveData(ByteBuffer data, int format, int samplerate) {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    /**
     * Disposes the wavedata
     */
    public void dispose() {
        this.data.clear();
    }

    /**
     * Creates a WaveData container from the specified url
     * 
     * @param path
     *            URL to file
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(URL path) {
        try {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(path.openStream())));
        } catch (Exception e) {
            LOGGER.warn("Unable to create from: " + path, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Creates a WaveData container from the specified in the classpath
     * 
     * @param path
     *            path to file (relative, and in classpath)
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(String path) {
        return create(Thread.currentThread().getContextClassLoader().getResource(path));
    }

    /**
     * Creates a WaveData container from the specified inputstream
     * 
     * @param is
     *            InputStream to read from
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(InputStream is) {
        try {
            return create(AudioSystem.getAudioInputStream(is));
        } catch (Exception e) {
            LOGGER.warn("Unable to create from inputstream", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Creates a WaveData container from the specified bytes
     *
     * @param buffer
     *            array of bytes containing the complete wave file
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(byte[] buffer) {
        try {
            return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer))));
        } catch (Exception e) {
            LOGGER.warn("Unable to create from byte array", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Creates a WaveData container from the specified ByetBuffer. If the buffer
     * is backed by an array, it will be used directly, else the contents of the
     * buffer will be copied using get(byte[]).
     *
     * @param buffer
     *            ByteBuffer containing sound file
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(ByteBuffer buffer) {
        try {
            byte[] bytes = null;

            if (buffer.hasArray()) {
                bytes = buffer.array();
            } else {
                bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
            }
            return create(bytes);
        } catch (Exception e) {
            LOGGER.warn("Unable to create from ByteBuffer", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Creates a WaveData container from the specified stream
     * 
     * @param ais
     *            AudioInputStream to read from
     * @return WaveData containing data, or null if a failure occured
     */
    public static Optional<WaveData> create(AudioInputStream ais) {
        // get format of data
        AudioFormat audioformat = ais.getFormat();

        // get channels
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_MONO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL10.AL_FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL10.AL_FORMAT_STEREO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else {
            assert false : "Only mono or stereo is supported";
        }

        // read data into buffer
        ByteBuffer buffer = null;
        try {
            int available = ais.available();
            if (available <= 0) {
                available = ais.getFormat().getChannels() * (int) ais.getFrameLength()
                        * ais.getFormat().getSampleSizeInBits() / 8;
            }
            byte[] buf = new byte[ais.available()];
            int read = 0, total = 0;
            while ((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
                total += read;
            }
            buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16,
                    audioformat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        } catch (IOException ioe) {
            LOGGER.warn("IO error", ioe);
            return Optional.empty();
        }

        // create our result
        WaveData wavedata = new WaveData(buffer, channels, (int) audioformat.getSampleRate());

        // close stream
        try {
            ais.close();
        } catch (IOException ioe) {
        }

        return Optional.of(wavedata);
    }

    private static ByteBuffer convertAudioBytes(byte[] audio_bytes, boolean two_bytes_data, ByteOrder order) {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(order);
        if (two_bytes_data) {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining())
                dest_short.put(src_short.get());
        } else {
            while (src.hasRemaining())
                dest.put(src.get());
        }
        dest.rewind();
        return dest;
    }
}