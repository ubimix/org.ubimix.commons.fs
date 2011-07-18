package org.webreformatter.commons.fs.events;

import org.webreformatter.commons.fs.FileSystemException;

/**
 * @author kotelnikov
 */
public interface IChangeListenerRegistry {

    void removeChangeListener(String path, IChangeListener listener)
        throws FileSystemException;

    void addChangeListener(String path, IChangeListener listener)
        throws FileSystemException;

}