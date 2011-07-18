package org.webreformatter.commons.fs.events.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFile;
import org.webreformatter.commons.fs.IFileSystemEntry;
import org.webreformatter.commons.fs.events.FileInfo;
import org.webreformatter.commons.fs.events.IChangeListener;

/**
 * @author kotelnikov
 */
public class ChangeDetector {

    private static boolean load(
        IFileSystemEntry entry,
        Map<String, FileInfo> oldMap,
        Map<String, FileInfo> newMap,
        IChangeListener listener) throws FileSystemException {
        if (entry == null) {
            return false;
        }
        String path = entry.getPath();
        boolean isDir = entry instanceof IDirectory;
        long time = entry.lastModified();
        boolean changed = false;
        if (isDir) {
            IDirectory dir = (IDirectory) entry;
            for (IFileSystemEntry child : dir) {
                changed |= load(child, oldMap, newMap, listener);
            }
            FileInfo newInfo = new FileInfo(path, time);
            changed = update(newInfo, oldMap, newMap, listener, changed);
        } else {
            IFile file = (IFile) entry;
            long len = file.getLength();
            FileInfo newInfo = new FileInfo(path, time, len);
            changed = update(newInfo, oldMap, newMap, listener, false);
        }
        return changed;
    }

    /**
     * @param newInfo the new info about a file
     * @param oldMap the map containing old information about file modifications
     * @param newMap the new map containing update information about files
     * @param listener the listener used to notify about changes
     * @return <code>true</code> if the file was modified
     */
    private static boolean update(
        FileInfo newInfo,
        Map<String, FileInfo> oldMap,
        Map<String, FileInfo> newMap,
        IChangeListener listener,
        boolean forceChanged) {
        FileInfo oldInfo = oldMap.remove(newInfo.path);
        boolean changed = true;
        if (oldInfo != null) {
            if (oldInfo.equals(newInfo) && !forceChanged) {
                listener.onNotChanged(newInfo);
                changed = false;
            } else {
                listener.onChanged(oldInfo, newInfo);
            }
        } else {
            listener.onAdded(newInfo);
        }
        newMap.put(newInfo.path, newInfo);
        return changed;
    }

    public static Map<String, FileInfo> update(
        IDirectory dir,
        Map<String, FileInfo> oldMap,
        IChangeListener listener) throws FileSystemException {
        Map<String, FileInfo> newMap = new LinkedHashMap<String, FileInfo>();
        load(dir, oldMap, newMap, listener);
        for (FileInfo info : oldMap.values()) {
            listener.onRemoved(info);
        }
        return newMap;
    }

}
