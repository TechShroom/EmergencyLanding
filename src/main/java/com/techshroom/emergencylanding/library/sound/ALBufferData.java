package com.techshroom.emergencylanding.library.sound;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import com.google.auto.value.AutoValue;

public interface ALBufferData {

    public static Short create(int format, ShortBuffer data, int sampleRate) {
        return new AutoValue_ALBufferData_Short(format, sampleRate, data);
    }

    public static Byte create(int format, ByteBuffer data, int sampleRate) {
        return new AutoValue_ALBufferData_Byte(format, sampleRate, data);
    }

    @AutoValue
    abstract class Short implements ALBufferData {

        public abstract ShortBuffer getData();

        Short() {
        }

    }

    @AutoValue
    abstract class Byte implements ALBufferData {

        public abstract ByteBuffer getData();

        Byte() {
        }

    }

    int getFormat();

    int getSampleRate();

}
