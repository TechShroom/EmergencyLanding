package com.techshroom.emergencylanding.library.exceptions.lwjgl;

public class RuntimeTextureBindException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5910806479992812731L;

    public RuntimeTextureBindException() {
        super();
    }

    public RuntimeTextureBindException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeTextureBindException(String message) {
        super(message);
    }

    public RuntimeTextureBindException(Throwable cause) {
        super(cause);
    }

}
