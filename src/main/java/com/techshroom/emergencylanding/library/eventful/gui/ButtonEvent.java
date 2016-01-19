package com.techshroom.emergencylanding.library.eventful.gui;

import com.techshroom.emergencylanding.library.eventful.Event;
import com.techshroom.emergencylanding.library.gui.Button;

public interface ButtonEvent extends Event {
    
    interface Click extends ButtonEvent, Event.Cancellable {
        
    }
    
    Button getButton();

}
