package org.ubimix.commons.fs.events;

import org.ubimix.commons.fs.FileSystemException;

/**
 * @author kotelnikov
 */
public interface IChangeListenerRegistry {

    void removeChangeListener(String path, IChangeListener listener)
        throws FileSystemException;

    void addChangeListener(String path, IChangeListener listener)
        throws FileSystemException;

}