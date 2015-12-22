package com.noomtech.thebody.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Abstract class that handles event listeners.  Any {@Link Communicator} that wants this functionality for
 * free should extend this class
 */
public abstract class AbstractCommunicator implements Communicator {


    private static final ExecutorService FIRE_LISTENER_SERVICE =
            new ThreadPoolExecutor(15,50,5000,TimeUnit.MILLISECONDS,new SynchronousQueue<Runnable>());

    private static final Map<String,List<CommunicatorEventListener>> listenerMap =
            new HashMap<>();
    private static final ReentrantReadWriteLock.ReadLock READ_LOCK;
    private static final ReentrantReadWriteLock.WriteLock WRITE_LOCK;
    static {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        READ_LOCK = lock.readLock();
        WRITE_LOCK = lock.writeLock();
    }


    public void postEvent(Object notifier, String message, String subject) {
        fireEvent(subject, message);
    }

    public abstract void postExceptionEvent(String message, Exception e);

    protected void fireEvent(final String subject, final String message) {
        READ_LOCK.lock();
        List<CommunicatorEventListener> listenerList = listenerMap.get(subject);
        if(listenerList != null) {
            for(final CommunicatorEventListener c : listenerList) {
                FIRE_LISTENER_SERVICE.execute(new Runnable(){public void run() {c.onEvent(subject,message);}});
            }
        }
        READ_LOCK.unlock();
    }

    public void addEventListener(CommunicatorEventListener listener, String subject) {
        WRITE_LOCK.lock();
        List<CommunicatorEventListener> listenerListForSubject = listenerMap.get(subject);
        if(listenerListForSubject == null) {
            listenerListForSubject = new ArrayList<>();
            listenerMap.put(subject, listenerListForSubject);
        }
        listenerListForSubject.add(listener);
        WRITE_LOCK.unlock();
    }
}
