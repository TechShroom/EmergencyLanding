//package com.techshroom.emergencylanding.library.debug;
//
//import java.util.HashMap;
//
//import org.lwjgl.opengl.Display;
//
//import com.techshroom.emergencylanding.library.internalstate.ELEntity;
//import com.techshroom.emergencylanding.library.lwjgl.DisplayLayer;
//import com.techshroom.emergencylanding.library.lwjgl.render.Render;
//import com.techshroom.emergencylanding.library.main.KMain;
//import com.techshroom.emergencylanding.library.sound.SoundPlayer;
//import com.techshroom.emergencylanding.library.util.LUtils;
//
//public class TestSound extends KMain {
//
//    public static void main(String[] args) throws Exception {
//        DisplayLayer.initDisplay(false, 800, 600, "Testing EL Sound", true,
//                args);
//        while (!Display.isCloseRequested()) {
//            DisplayLayer.loop(120);
//        }
//        DisplayLayer.destroy();
//        System.exit(0);
//    }
//
//    @Override
//    public void onDisplayUpdate(int delta) {
//        DisplayLayer.readDevices();
//    }
//
//    @Override
//    public void init(DisplayLayer layer, String[] args) {
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                .50f, true);
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                .10f, true);
//        SoundPlayer.playWAV(LUtils.getELTop() + "/wav/test.wav", 1.0f,
//                2.15f, true);
//    }
//
//    @Override
//    public void registerRenders(
//            HashMap<Class<? extends ELEntity>, Render<? extends ELEntity>> classToRender) {
//
//    }
//}
