package org.webreformatter.commons.fs.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompositeChangeListener
    implements
    IChangeListener,
    Iterable<IChangeListener> {

    private final static Logger log = Logger
        .getLogger(CompositeChangeListener.class.getName());

    protected List<IChangeListener> fListeners;

    public synchronized void addListener(IChangeListener listener) {
        List<IChangeListener> l = fListeners;
        List<IChangeListener> listeners = l != null
            ? new ArrayList<IChangeListener>(l)
            : new ArrayList<IChangeListener>();
        listeners.add(listener);
        fListeners = listeners;
    }

    // All exception silently ignored! Each listener has manage
    // its own errors!
    protected void handleError(Throwable t) {
        log.log(
            Level.WARNING,
            "An exception was rised by a file system change listener.",
            t);
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<IChangeListener> iterator() {
        List<IChangeListener> l = fListeners;
        return l != null ? l.iterator() : Collections.EMPTY_LIST.iterator();
    }

    public void onAdded(FileInfo newInfo) {
        List<IChangeListener> listeners = fListeners;
        if (listeners != null) {
            for (IChangeListener listener : listeners) {
                try {
                    listener.onAdded(newInfo);
                } catch (Throwable t) {
                    handleError(t);
                }
            }
        }
    }

    public void onChanged(FileInfo oldInfo, FileInfo newInfo) {
        List<IChangeListener> listeners = fListeners;
        if (listeners != null) {
            for (IChangeListener listener : listeners) {
                try {
                    listener.onChanged(oldInfo, newInfo);
                } catch (Throwable t) {
                    handleError(t);
                }
            }
        }
    }

    public void onNotChanged(FileInfo info) {
        List<IChangeListener> listeners = fListeners;
        if (listeners != null) {
            for (IChangeListener listener : listeners) {
                try {
                    listener.onNotChanged(info);
                } catch (Throwable t) {
                    handleError(t);
                }
            }
        }
    }

    public void onRemoved(FileInfo info) {
        List<IChangeListener> listeners = fListeners;
        if (listeners != null) {
            for (IChangeListener listener : listeners) {
                try {
                    listener.onRemoved(info);
                } catch (Throwable t) {
                    handleError(t);
                }
            }
        }
    }

    public synchronized void removeListener(IChangeListener listener) {
        List<IChangeListener> l = fListeners;
        if (l == null)
            return;
        List<IChangeListener> listeners = new ArrayList<IChangeListener>(l);
        listeners.remove(listener);
        fListeners = listeners;
    }

}