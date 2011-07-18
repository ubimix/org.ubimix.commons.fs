/**
 * 
 */
package org.webreformatter.commons.fs.events.impl;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.events.IChangeListener;
import org.webreformatter.commons.fs.events.IFileSystemWithEvents;
import org.webreformatter.commons.fs.wrapper.WrapperFileSystem;

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
     * @see org.webreformatter.commons.fs.events.IChangeListenerRegistry#addChangeListener(java.lang.String,
     *      org.webreformatter.commons.fs.events.IChangeListener)
     */
    public void addChangeListener(String path, IChangeListener listener)
        throws FileSystemException {
        fChangeDetector.addChangeListener(path, listener);
    }

    /**
     * @see org.webreformatter.commons.fs.events.IChangeListenerRegistry#removeChangeListener(java.lang.String,
     *      org.webreformatter.commons.fs.events.IChangeListener)
     */
    public void removeChangeListener(String path, IChangeListener listener)
        throws FileSystemException {
        fChangeDetector.removeChangeListener(path, listener);
    }

}
