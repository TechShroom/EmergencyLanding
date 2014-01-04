package emergencylanding.k.library.exceptions.lwjgl;

public class TextureBindException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2846619232967293980L;

    public TextureBindException() {
        super();
    }

    public TextureBindException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextureBindException(String message) {
        super(message);
    }

    public TextureBindException(Throwable cause) {
        super(cause);
    }
}
