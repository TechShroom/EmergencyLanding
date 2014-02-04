package emergencylanding.k.library.lwjgl.tex;

import java.awt.Dimension;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import k.core.util.Helper;

public class BufferedTexture extends ELTexture {
    BufferedImage img = null;

    public BufferedTexture(BufferedImage image) {
        img = image;
        dim = new Dimension(img.getWidth(), img.getHeight());
        super.init();
    }

    @Override
    public void setup() {
        int bpp = (byte) img.getColorModel().getPixelSize();
        ByteBuffer byteBuffer;
        // DataBuffer db = img.getData().getDataBuffer();
        int width = dim.width;
        int height = dim.height;
        if (img.getType() == BufferedImage.TYPE_INT_ARGB) {
            int intI[] = ((DataBufferInt) (img.getData().getDataBuffer()))
                    .getData();
            byte newI[] = new byte[intI.length * 4];
            for (int i = 0; i < intI.length; i++) {
                byte b[] = Helper.BetterArrays.intToByteArray(intI[i]);
                int newIndex = i * 4;

                newI[newIndex] = b[1];
                newI[newIndex + 1] = b[2];
                newI[newIndex + 2] = b[3];
                newI[newIndex + 3] = b[0];
            }

            byteBuffer = ByteBuffer.allocateDirect(width * height * (bpp / 8))
                    .order(ByteOrder.nativeOrder()).put(newI);
        } else {
            byteBuffer = ByteBuffer
                    .allocateDirect(width * height * (bpp / 8))
                    .order(ByteOrder.nativeOrder())
                    .put(((DataBufferByte) (img.getData().getDataBuffer()))
                            .getData());
        }
        byteBuffer.flip();
        buf = byteBuffer;
    }

    @Override
    public boolean isLookAlike(ELTexture t) {
        if (t instanceof BufferedTexture) {
            BufferedTexture other = (BufferedTexture) t;
            if (other.dim.equals(dim)) {
                boolean equal = true;
                for (int x = 0; x < dim.width; x++) {
                    for (int y = 0; y < dim.height; y++) {
                        int c1 = img.getRGB(x, y);
                        int c2 = other.img.getRGB(x, y);
                        equal = c1 == c2 && equal;
                    }
                }
                return equal;
            }
        }
        return false;
    }

    @Override
    protected BufferedImage toBufferedImageAbstract() {
        return img;
    }

    @Override
    protected void onDestruction() {
    }
}
