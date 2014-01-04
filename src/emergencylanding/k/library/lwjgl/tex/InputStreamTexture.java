package emergencylanding.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.PNGDecoder.Format;

import emergencylanding.k.library.util.LUtils;

public class InputStreamTexture extends ELTexture {
    private InputStream tex = null;

    public InputStreamTexture(String parentDir, String name) {
        if (parentDir == null) {
            parentDir = System.getProperty("user.home", "/");
        }
        if (name == null) {
            LUtils.print("Creating a null src!");
            return;
        }
        try {
            tex = LUtils.getInputStream(parentDir + "/" + name);
        } catch (IOException e) {
            throw new RuntimeException("Error retriving stream", e);
        }
        super.init();
    }

    @Override
    public void setup() {
        try {
            // Open the PNG file as an InputStream
            InputStream in = tex;
            // Link the PNG decoder to this stream
            PNGDecoder decoder = new PNGDecoder(in);

            // Get the width and height of the texture
            dim = new Dimension(decoder.getWidth(), decoder.getHeight());

            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(4 * decoder.getWidth()
                    * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
            buf.flip();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public boolean isLookAlike(ELTexture t) {
        if (t instanceof InputStreamTexture) {
            return tex.equals(((InputStreamTexture) t).tex);
        } else {
            return super.isLookAlike(t);
        }
    }

    @Override
    protected BufferedImage toBufferedImageAbstract() {
        try {
            return ImageIO.read(tex);
        } catch (IOException e) {
            throw new RuntimeException("Error reading image", e);
        }
    }

    @Override
    protected void onDestruction() {
        try {
            tex.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
