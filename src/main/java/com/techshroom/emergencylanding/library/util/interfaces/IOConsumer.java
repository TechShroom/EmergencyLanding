package com.techshroom.emergencylanding.library.util.interfaces;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface IOConsumer<R> {
    
    R consumeStream(InputStream stream) throws IOException;

}
