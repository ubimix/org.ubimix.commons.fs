package org.webreformatter.commons.fs.test;

import java.util.HashMap;
import java.util.Map;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.events.CompositeChangeListener;
import org.webreformatter.commons.fs.events.FileInfo;
import org.webreformatter.commons.fs.events.IChangeListener;
import org.webreformatter.commons.fs.events.impl.ChangeDetector;

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
