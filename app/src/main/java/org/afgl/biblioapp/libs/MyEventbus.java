package org.afgl.biblioapp.libs;

import org.afgl.biblioapp.libs.base.Eventbus;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by arturo on 06/06/2017.
 * Envoltura de eventbus
 */

public class MyEventbus implements Eventbus {

    private EventBus eventBus;

    public MyEventbus(EventBus eventBus){
        this.eventBus = eventBus;
    }

    @Override
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }

}
