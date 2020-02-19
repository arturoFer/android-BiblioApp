package org.afgl.biblioapp.libs.base;


/**
 * Created by arturo on 06/06/2017.
 * Interface para MyEventbus
 */

public interface Eventbus {
    void register(Object  subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
