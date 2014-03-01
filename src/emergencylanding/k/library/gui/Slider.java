package emergencylanding.k.library.gui;

import java.awt.Rectangle;

import emergencylanding.k.library.lwjgl.Shapes;
import emergencylanding.k.library.lwjgl.control.MouseHelp;
import emergencylanding.k.library.lwjgl.render.*;
import emergencylanding.k.library.lwjgl.tex.ELTexture;
import emergencylanding.k.library.util.DrawableUtils;

public class Slider extends GuiElement {
    private VBAO slide, base;
    private float minimum, maximum, minXAdd, maxXAdd, percentage, middleXAdd,
            middleYAdd;
    private boolean tracking;
    private static boolean anytracking = false;
    private StringRenderer render = null;
    private String displayText = "";
    private String[] appendables;

    public Slider(float x, float y, float min, float max,
            ELTexture slideImgTex, ELTexture baseImgTex, String textForDisplay,
            String[] stringsToAppend, StringRenderer renderer) {
        super(x, y);
        ELTexture slideTex = slideImgTex;
        ELTexture baseTex = baseImgTex;
        minimum = min;
        maximum = max;
        minXAdd = (slideTex.getWidth() / 2);
        maxXAdd = baseTex.getWidth() - (slideTex.getWidth() / 2);
        middleXAdd = baseTex.getWidth() / 2;
        middleYAdd = baseTex.getHeight() / 2 - renderer.getHeight() / 2;
        render = renderer;
        displayText = textForDisplay;
        if (stringsToAppend == null || stringsToAppend.length == 0) {
            createDefaultAppendables();
        } else {
            fillDefaults(stringsToAppend);
        }
        slide = Shapes.getQuad(
                new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ(slideTex.getWidth(),
                        slideTex.getHeight(), 0), Shapes.XY);
        slide.setTexture(slideTex);
        base = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ(baseTex.getWidth(),
                        baseTex.getHeight(), 0), Shapes.XY);
        base.setTexture(baseTex);
    }

    private void fillDefaults(String[] stringsToAppend) {
        int cap = Math.min(101, stringsToAppend.length);
        appendables = new String[101];
        for (float i = 0; i < 101; i++) {
            int ii = (int) i;
            if (i > cap - 1 || stringsToAppend[ii] == null) {
                appendables[ii] = ((i * (maximum - minimum) / 100) + minimum)
                        + "";
            } else {
                appendables[ii] = stringsToAppend[ii];
            }
        }
    }

    private void createDefaultAppendables() {
        fillDefaults(new String[0]);
    }

    @Override
    public void updateAt(float x, float y) {
        Rectangle recalcBB = new Rectangle((int) Math.floor(xPos),
                (int) Math.floor(this.yPos), base.tex.getWidth(),
                base.tex.getHeight());
        if (MouseHelp.clickingInRect(recalcBB, MouseHelp.LMB) && !tracking
                && !anytracking) {
            tracking = true;
            anytracking = true;
        } else if (tracking && !MouseHelp.isButtonDown(MouseHelp.LMB)) {
            updateTracker(xPos);
            anytracking = tracking = false;
        } else if (tracking) {
            updateTracker(xPos);
        }
    }

    private void updateTracker(float x) {
        int mx = MouseHelp.getX();
        if (mx < x + minXAdd) {
            percentage = 0;
        } else if (mx > x + maxXAdd) {
            percentage = 1;
        } else {
            percentage = (mx - x + minXAdd) / (maxXAdd + minXAdd);
        }
    }

    @Override
    public void drawAt(float x, float y) {
        float slideX = calcSlideX(xPos);
        DrawableUtils.glBeginTrans(xPos, yPos, 0);
        base.draw();
        DrawableUtils.glEndTrans();
        DrawableUtils.glBeginTrans(slideX, yPos, 0);
        slide.draw();
        DrawableUtils.glEndTrans();
        float percentInt = Math.round(percentage * 100);
        String append = "";
        append = appendables[(int) percentInt];
        String dext = displayText + append;
        render.drawString(xPos + middleXAdd - render.getWidth(dext) / 4, yPos
                + middleYAdd, dext, 1, 1);
    }

    private float calcSlideX(float x) {
        float xres = x + percentage * base.tex.getWidth() - minXAdd * 2;
        if (percentage == 0) {
            xres = x;
        }
        System.err.println(xres);
        return xres;
    }

    public static int getPercentClosestToValue(int value, int start, int end) {
        float vF = (float) value, sF = (float) start, eF = (float) end;
        float vFF = ((vF + sF) / (eF + sF) * 100);
        return Math.round(vFF);
    }
}
