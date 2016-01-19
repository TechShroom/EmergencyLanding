package com.techshroom.emergencylanding.library.eventful;

public interface Event {

    interface Cancellable extends Event {

        boolean isCanceled();

        void setCanceled(boolean canceled);

    }

}
