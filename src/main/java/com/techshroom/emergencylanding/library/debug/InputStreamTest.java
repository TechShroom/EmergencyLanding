package com.techshroom.emergencylanding.library.debug;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.common.io.CharStreams;
import com.techshroom.emergencylanding.library.util.LUtils;

public class InputStreamTest {

    public static void main(String[] args)
            throws IOException, ClassNotFoundException {
        String path = //
                LUtils.getELTop() + "/test2.zip/txt.txt";
        LUtils.processPathData(path, is -> {
            System.err.println("Got " + is + " for " + path);
            if (is == null) {
                System.err.println("No InputStream, no data?");
                return null;
            }
            System.err.println("The result text file contains this message:");
            System.err.println(CharStreams.toString(
                    new InputStreamReader(is, StandardCharsets.UTF_8)));
            return null;
        });
    }
}
