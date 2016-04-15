package com.techshroom.emergencylanding.library.eventful.render;

import com.techshroom.emergencylanding.library.eventful.Event;

public interface RenderEvent extends Event {

    long getRenderDelta();

    interface Setup extends RenderEvent {

    }

    interface TearDown extends RenderEvent {

    }

}
