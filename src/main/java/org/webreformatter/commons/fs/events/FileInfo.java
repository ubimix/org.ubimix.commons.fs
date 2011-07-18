package org.webreformatter.commons.fs.events;

/**
 * @author kotelnikov
 */
public class FileInfo {

    public final String path;

    public final long size;

    public final long time;

    public FileInfo(String path, long time) {
        this(path, time, -1);
    }

    public FileInfo(String path, long time, long size) {
        this.path = path;
        this.time = time;
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FileInfo)) {
            return false;
        }
        FileInfo info = (FileInfo) obj;
        return path.equals(info.path) && time == info.time && size == info.size;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getTime() {
        return time;
    }

    @Override
    public int hashCode() {
        Long hash = time ^ size;
        return hash.hashCode() ^ path.hashCode();
    }

    public boolean isDirectory() {
        return size < 0;
    }

    @Override
    public String toString() {
        return path;
    }
}