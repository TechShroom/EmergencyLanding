package com.techshroom.emergencylanding.library.eventful.render;

public interface NanoVGRenderEvent extends RenderEvent {

    interface Begin extends NanoVGRenderEvent {
    }

    interface End extends NanoVGRenderEvent {
    }

}
