// package com.techshroom.emergencylanding.library.debug;
//
// import java.awt.Color;
// import java.awt.Font;
// import java.awt.Graphics;
// import java.awt.image.BufferedImage;
// import java.util.HashMap;
//
// import javax.swing.JFrame;
//
// import org.lwjgl.opengl.Display;
//
// import com.techshroom.emergencylanding.library.internalstate.ELEntity;
// import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
// import com.techshroom.emergencylanding.library.lwjgl.Shapes;
// import com.techshroom.emergencylanding.library.lwjgl.render.Render;
// import com.techshroom.emergencylanding.library.lwjgl.render.StringRenderer;
// import com.techshroom.emergencylanding.library.lwjgl.render.VBAO;
// import com.techshroom.emergencylanding.library.lwjgl.render.VertexData;
// import com.techshroom.emergencylanding.library.lwjgl.tex.BufferedTexture;
// import com.techshroom.emergencylanding.library.main.KMain;
// import com.techshroom.emergencylanding.library.util.DrawableUtils;
//
// public class FontTest extends KMain {
//
// static StringRenderer strrend;
// static VBAO image = null;
//
// public static void main(String[] args) throws Exception {
// DisplayLayer.initDisplay(false, 800, 600, "Fonts!", false, false, args);
// FPS.enable(0);
// while (!Display.isCloseRequested()) {
// DisplayLayer.loop(12000);
// }
// strrend.destroy();
// DisplayLayer.destroy();
// System.exit(0);
// }
//
// @Override
// public void onDisplayUpdate(int delta) {
// image.draw();
// strrend.drawString(200, 200, "A", 1, 1);
// strrend.drawString(100, 100, "Font is TNR Bold!!\nlorem ipsum", 1, 1);
// }
//
// @Override
// public void init(String[] args) {
// final Font f = new Font("times new roman", Font.PLAIN, 16);
// strrend = new StringRenderer(f, true, Color.RED);
//
// final BufferedImage strrendimg1 =
// DrawableUtils.getFontImage('A', true, f, 19, Color.BLUE);
//
// image = Shapes
// .getQuad(new VertexData().setXYZ(300, 300, 0),
// new VertexData().setXYZ(strrendimg1.getWidth(),
// strrendimg1.getHeight(), 0),
// Shapes.XY)
// .setTexture(new BufferedTexture(strrendimg1));
//
// System.err.println(image);
//
// // draw the matching data using a JFrame's graphics
// JFrame j = new JFrame("Fonts!") {
//
// private static final long serialVersionUID = 1L;
//
// @Override
// public void paint(Graphics g) {
// super.paint(g);
// g.setFont(f);
// g.drawImage(strrendimg1, 200, 200, null);
// g.drawString(
// "This is a test and a test and a test test test\nThis is a test and a test
// and a test test test\nThis is a test and a test and a test test test\nThis is
// a test and a test and a test test test\n",
// 100, 100);
// }
// };
// j.setSize(800, 600);
// j.setVisible(true);
// }
//
// @Override
// public void registerRenders(
// HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender)
// {
// // TODO Auto-generated method stub
//
// }
// }