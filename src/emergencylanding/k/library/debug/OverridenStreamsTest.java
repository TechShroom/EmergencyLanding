package emergencylanding.k.library.debug;

import emergencylanding.k.library.util.LUtils;

public class OverridenStreamsTest {
    public static void main(String[] args) {
        LUtils.print("Test");
        System.out.println("test");
        System.err
                .print("testing print with a newline!\nhere's the next line!\n");
        System.err.print("testing print");
        System.err.println(" followed by println");
        System.err.println("throwable:");
        new Throwable().printStackTrace();

    }
}
