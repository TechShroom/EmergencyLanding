package com.techshroom.emergencylanding.library.eventful.window;

import com.techshroom.emergencylanding.library.eventful.Event;
import com.techshroom.emergencylanding.library.lwjgl.control.Cursor;

public interface MouseEvent {

    interface Click extends MouseEvent {

        interface Press extends Click {

        }

        interface Release extends Click {

        }

        interface Repeat extends Click {

        }

        int getMouseButton();

        int getModifiers();
        
        double getX();
        
        double getY();

    }

    interface Motion extends MouseEvent {

        double getOldX();

        double getNewX();

        double getOldY();

        double getNewY();

        default double getDX() {
            return getNewX() - getOldX();
        }

        default double getDY() {
            return getNewY() - getOldY();
        }

    }

    interface CursorChange extends MouseEvent, Event.Cancellable {

        Cursor getOldCursor();

        Cursor getNewCursor();

    }

}
