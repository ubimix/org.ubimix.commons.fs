package org.ubimix.commons.fs.test;

import java.util.HashMap;
import java.util.Map;

import org.ubimix.commons.fs.FileSystemException;
import org.ubimix.commons.fs.IDirectory;
import org.ubimix.commons.fs.events.CompositeChangeListener;
import org.ubimix.commons.fs.events.FileInfo;
import org.ubimix.commons.fs.events.IChangeListener;
import org.ubimix.commons.fs.events.impl.ChangeDetector;

/**
 * @author kotelnikov
 */
public class FileSystemObserver {

    private IDirectory fDir;

    private CompositeChangeListener fListener = new CompositeChangeListener();

    private Map<String, FileInfo> fMap = new HashMap<String, FileInfo>();

    /**
     * @param fs
     */
    public FileSystemObserver(IDirectory dir) {
        fDir = dir;
    }

    public synchronized void addListener(IChangeListener listener) {
        fListener.addListener(listener);
    }

    public void reload() throws FileSystemException {
        // Map<String, FileInfo> info = new HashMap<String, FileInfo>();
        fMap = ChangeDetector.update(fDir, fMap, fListener);
    }

    public synchronized void removeListener(IChangeListener listener) {
        fListener.removeListener(listener);
    }

}
