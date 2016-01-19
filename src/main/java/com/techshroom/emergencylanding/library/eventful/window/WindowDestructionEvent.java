package com.techshroom.emergencylanding.library.eventful.window;

public interface WindowDestructionEvent extends WindowEvent {

    interface Pre extends WindowDestructionEvent {
    }

    interface Post extends WindowDestructionEvent {
    }

}
