package com.techshroom.emergencylanding.library.gui;

import java.awt.geom.Rectangle2D;

import com.techshroom.emergencylanding.library.eventful.window.MouseEvent;
import com.techshroom.emergencylanding.library.lwjgl.Shapes;
import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
import com.techshroom.emergencylanding.library.lwjgl.tex.ELTexture;
import com.techshroom.emergencylanding.library.util.DrawableUtils;

import net.engio.mbassy.listener.Handler;

public class Slider extends GuiElement {

    private static boolean anytracking = false;
    private final VBAO slide;
    private final VBAO base;
    // private final StringRenderer render;
    private final String displayText;
    private final double minimum;
    private final double maximum;
    private final double minXAdd;
    private final double maxXAdd;
    private final double middleXAdd;
    private final double middleYAdd;
    private double percentage;
    private boolean tracking;
    private String[] appendables;

    public Slider(float x, float y, float min, float max, ELTexture slideImgTex,
            ELTexture baseImgTex, String textForDisplay,
            String[] stringsToAppend/* , StringRenderer renderer */) {
        super(x, y);
        ELTexture slideTex = slideImgTex;
        ELTexture baseTex = baseImgTex;
        this.minimum = min;
        this.maximum = max;
        this.minXAdd = (slideTex.getWidth() / 2);
        this.maxXAdd = baseTex.getWidth() - (slideTex.getWidth() / 2);
        this.middleXAdd = baseTex.getWidth() / 2;
        this.middleYAdd = baseTex.getHeight() / 2;// - renderer.getHeight() / 2;
        // this.render = renderer;
        this.displayText = textForDisplay;
        if (stringsToAppend == null || stringsToAppend.length == 0) {
            createDefaultAppendables();
        } else {
            fillDefaults(stringsToAppend);
        }
        this.slide = Shapes.getQuad(
                new VertexData().setXYZ(0, 0, 0), new VertexData()
                        .setXYZ(slideTex.getWidth(), slideTex.getHeight(), 0),
                Shapes.XY);
        this.slide.setTexture(slideTex);
        this.slide.setStatic(false);
        this.base = Shapes.getQuad(new VertexData().setXYZ(0, 0, 0),
                new VertexData().setXYZ(baseTex.getWidth(), baseTex.getHeight(),
                        0),
                Shapes.XY);
        this.base.setTexture(baseTex);
        this.base.setStatic(false);
    }

    private void fillDefaults(String[] stringsToAppend) {
        int cap = Math.min(101, stringsToAppend.length);
        this.appendables = new String[101];
        for (float i = 0; i < 101; i++) {
            int ii = (int) i;
            if (i > cap - 1 || stringsToAppend[ii] == null) {
                this.appendables[ii] =
                        ((i * (this.maximum - this.minimum) / 100)
                                + this.minimum) + "";
            } else {
                this.appendables[ii] = stringsToAppend[ii];
            }
        }
    }

    private void createDefaultAppendables() {
        fillDefaults(new String[0]);
    }

    @Override
    public void draw() {
        double slideX = calcSlideX(this.xPos);
        DrawableUtils.glBeginTrans(this.xPos, this.yPos, 0);
        this.base.draw();
        DrawableUtils.glEndTrans();
        DrawableUtils.glBeginTrans(slideX, this.yPos, 0);
        this.slide.draw();
        DrawableUtils.glEndTrans();
        float percentInt = Math.round(this.percentage * 100);
        String append = "";
        append = this.appendables[(int) percentInt];
        String dext = this.displayText + append;
        // this.render.drawString(
        // this.xPos + this.middleXAdd - this.render.getWidth(dext) / 4,
        // this.yPos + this.middleYAdd, dext, 1, 1);
    }

    @Override
    public void update() {
    }

    @Handler
    public void onMouseEvent(MouseEvent event) {
        Rectangle2D rect = new Rectangle2D.Double(this.xPos, this.xPos,
                this.base.tex.getWidth(), this.base.tex.getHeight());
        double x = 0;
        double y = 0;
        if (event instanceof MouseEvent.Click) {
            x = ((MouseEvent.Click) event).getX();
            y = ((MouseEvent.Click) event).getY();
            if (rect.contains(x, y)) {
                if (event instanceof MouseEvent.Click.Press && !anytracking) {
                    anytracking = this.tracking = true;
                } else if (event instanceof MouseEvent.Click.Release) {
                    anytracking = this.tracking = false;
                }
            }
        } else if (event instanceof MouseEvent.Motion && this.tracking) {
            x = ((MouseEvent.Motion) event).getNewX();
            y = ((MouseEvent.Motion) event).getNewY();
            updateTracker(this.xPos, x);
        }
    }

    private void updateTracker(double x, double mx) {
        if (mx < x + this.minXAdd) {
            this.percentage = 0;
        } else if (mx > x + this.maxXAdd) {
            this.percentage = 1;
        } else {
            this.percentage =
                    (mx - x + this.minXAdd) / (this.maxXAdd + this.minXAdd);
        }
    }

    private double calcSlideX(double x) {
        double xres = x + this.percentage * this.base.tex.getWidth()
                - this.minXAdd * 2;
        if (this.percentage == 0) {
            xres = x;
        }
        return xres;
    }

    public static int getPercentClosestToValue(int value, int start, int end) {
        float vF = (float) value, sF = (float) start, eF = (float) end;
        float vFF = ((vF + sF) / (eF + sF) * 100);
        return Math.round(vFF);
    }
}
