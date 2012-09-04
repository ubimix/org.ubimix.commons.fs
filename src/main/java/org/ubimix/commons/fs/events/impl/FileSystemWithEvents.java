/**
 * 
 */
package org.ubimix.commons.fs.events.impl;

import org.ubimix.commons.fs.FileSystemException;
import org.ubimix.commons.fs.IFileSystem;
import org.ubimix.commons.fs.events.IChangeListener;
import org.ubimix.commons.fs.events.IFileSystemWithEvents;
import org.ubimix.commons.fs.wrapper.WrapperFileSystem;

/**
 * @author kotelnikov
 */
public class FileSystemWithEvents extends WrapperFileSystem
    implements
    IFileSystemWithEvents {

    private PollingChangeDetector fChangeDetector;

    public FileSystemWithEvents(IFileSystem fileSystem)
        throws FileSystemException {
        super(fileSystem.getRootDirectory());
        WrapperDirectory rootDirectory = getRootDirectory();
        fChangeDetector = new PollingChangeDetector(rootDirectory, 300);
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListenerRegistry#addChangeListener(java.lang.String,
     *      org.ubimix.commons.fs.events.IChangeListener)
     */
    public void addChangeListener(String path, IChangeListener listener)
        throws FileSystemException {
        fChangeDetector.addChangeListener(path, listener);
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListenerRegistry#removeChangeListener(java.lang.String,
     *      org.ubimix.commons.fs.events.IChangeListener)
     */
    public void removeChangeListener(String path, IChangeListener listener)
        throws FileSystemException {
        fChangeDetector.removeChangeListener(path, listener);
    }

}
