package org.ubimix.commons.fs.events;

/**
 * Listener for file events. Note that all listeners should be as "light-weight"
 * as possible. To perform long-running tasks it is better to use
 * {@link AsyncChangeListener} wrappers for listeners.
 * 
 * @author kotelnikov
 */
public interface IChangeListener {

    void onAdded(FileInfo newInfo);

    void onChanged(FileInfo oldInfo, FileInfo newInfo);

    void onNotChanged(FileInfo info);

    void onRemoved(FileInfo info);
}