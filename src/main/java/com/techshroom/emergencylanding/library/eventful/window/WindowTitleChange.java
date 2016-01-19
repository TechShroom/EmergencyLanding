package com.techshroom.emergencylanding.library.eventful.window;

import com.techshroom.emergencylanding.library.eventful.Event;

public interface WindowTitleChange extends WindowEvent, Event.Cancellable {
    
    String getOldTitle();
    
    String getNewTitle();

}
