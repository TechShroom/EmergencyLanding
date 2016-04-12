package com.techshroom.emergencylanding.library.util;

/**
 * Thrown when a deallocated/destoryed object is used.
 */
public class DeallocatedException extends RuntimeException {

    private static final long serialVersionUID = 6025269979731310031L;

    /**
     * 
     */
    public DeallocatedException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public DeallocatedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public DeallocatedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DeallocatedException(Throwable cause) {
        super(cause);
    }

}
