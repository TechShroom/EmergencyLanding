package com.techshroom.emergencylanding.library.eventful;

import net.engio.mbassy.bus.MBassador;

public final class EventBuses {

    /**
     * Mouse + key event bus.
     */
    public static final MBassador<Event> INPUT_EVENTS = new MBassador<>();
    /**
     * Event bus for all remaining events, i.e. setting titles or positions on
     * windows.
     */
    public static final MBassador<Event> OTHER_EVENTS = new MBassador<>();

    private EventBuses() {
    }

}
