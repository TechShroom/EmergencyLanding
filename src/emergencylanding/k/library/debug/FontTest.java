package emergencylanding.k.library.debug;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

import emergencylanding.k.library.internalstate.ELEntity;
import emergencylanding.k.library.lwjgl.DisplayLayer;
import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.render.Render;
import emergencylanding.k.library.lwjgl.render.StringRenderer;
import emergencylanding.k.library.lwjgl.render.VBAO;
import emergencylanding.k.library.lwjgl.render.VertexData;
import emergencylanding.k.library.lwjgl.tex.BufferedTexture;
import emergencylanding.k.library.main.KMain;
import emergencylanding.k.library.util.DrawableUtils;

public class FontTest extends KMain {
    static StringRenderer strrend;
    static VBAO image = null;

    public static void main(String[] args) throws Exception {
        DisplayLayer.initDisplay(false, 800, 600, "Fonts!", false, args);
        while (!Display.isCloseRequested()) {
            DisplayLayer.loop(120);
        }
        strrend.destroy();
        DisplayLayer.destroy();
        System.exit(0);
    }

    @Override
    public void onDisplayUpdate(int delta) {
        image.draw();
        strrend.drawString(200, 200, "A", 1, 1);
        strrend.drawString(100, 100, "Font is TNR Bold!!", 1, 1);
    }

    @Override
    public void init(String[] args) {
        final Font f = new Font("times new roman", Font.PLAIN, 16);
        strrend = new StringRenderer(f, true);

        final BufferedImage strrendimg1 = DrawableUtils.getFontImage('A', true,
                f, 19);

        image = Shapes.getQuad(
                new VertexData().setXYZ(300, 300, 0),
                new VertexData().setXYZ(strrendimg1.getWidth(),
                        strrendimg1.getHeight(), 0), Shapes.XY).setTexture(
                new BufferedTexture(strrendimg1));
        
        System.err.println(image);

        // draw the matching data using a JFrame's graphics
        JFrame j = new JFrame("Fonts!") {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setFont(f);
                g.drawImage(strrendimg1, 200, 200, null);
                g.drawString("Font is TNR Bold!!", 100, 100);
            }
        };
        j.setSize(800, 600);
        j.setVisible(true);
    }

    @Override
    public void registerRenders(
            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
        // TODO Auto-generated method stub

    }
}
