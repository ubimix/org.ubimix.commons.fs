/**
 * 
 */
package org.ubimix.commons.fs.os;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ubimix.commons.fs.FileSystemException;
import org.ubimix.commons.fs.IDirectory;
import org.ubimix.commons.fs.IFileSystem;
import org.ubimix.commons.fs.IFileSystemEntry;

/**
 * @author kotelnikov
 */
public class OSEntry implements IFileSystemEntry {

    private final static Logger log = Logger.getLogger(OSEntry.class.getName());

    /**
     * The file on the disc corresponding to this entry.
     */
    protected final File fFile;

    /**
     * The file system owning this entry
     */
    protected OSFileSystem fFileSystem;

    /**
     * The normalized path to this entry.
     */
    protected final String fPath;

    /**
     * @param fileSystem
     * @param file
     */
    public OSEntry(OSFileSystem fileSystem, File file) {
        fFileSystem = fileSystem;
        fFile = file;
        fPath = buildPath();
    }

    /**
     * @return
     */
    protected String buildPath() {
        File root = fFileSystem.getRootDir();
        String rootPath = root.getPath();
        String path = fFile.getPath();
        int len = rootPath.length();
        if (len > 0) {
            path = path.substring(len);
        }
        path = path.replace('\\', '/');
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.length() > 0 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#delete()
     */
    public boolean delete() {
        return delete(fFile);
    }

    /**
     * Deletes the file and all its children (in the case of directories) and
     * returns <code>true</code> if the file was successfully deleted.
     * 
     * @param file the file to delete
     * @return <code>true</code> if the given file was successfully deleted
     */
    private boolean delete(File file) {
        if (file.isDirectory()) {
            File[] array = file.listFiles();
            if (array != null) {
                for (File child : array) {
                    if (!delete(child)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        Class<? extends OSEntry> cls = getClass();
        if (!(cls.isInstance(obj))) {
            return false;
        }
        OSEntry entry = (OSEntry) obj;
        return fFile.equals(entry.fFile);
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#exists()
     */
    public boolean exists() {
        return fFile.exists();
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#getFileSystem()
     */
    public IFileSystem getFileSystem() {
        return fFileSystem;
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#getName()
     */
    public String getName() {
        return fFile.getName();
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#getParentDirectory()
     */
    public IDirectory getParentDirectory() throws FileSystemException {
        if (isRoot()) {
            return null;
        }
        return fFileSystem.newDirectory(fFile.getParentFile());
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#getPath()
     */
    public String getPath() {
        return fPath;
    }

    protected FileSystemException handleError(String msg, Throwable e) {
        log.log(Level.FINE, msg, e);
        if (e instanceof FileSystemException) {
            return (FileSystemException) e;
        }
        return new FileSystemException(msg, e);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fFile.hashCode();
    }

    /**
     * Reutrns <code>true</code> if this entry is the root directory
     * 
     * @return <code>true</code> if this entry is the root directory
     */
    public boolean isRoot() {
        File file = fFileSystem.getRootDir();
        return file.equals(fFile);
    }

    /**
     * @see org.ubimix.commons.fs.IFile#lastModified()
     */
    public long lastModified() {
        return fFile.lastModified();
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#renameTo(java.lang.String)
     */
    public boolean renameTo(String name) throws IOException {
        File f = new File(fFile.getParentFile(), name);
        return fFile.renameTo(f);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getPath();
    }

    /**
     * @see org.ubimix.commons.fs.IFile#touch()
     */
    public boolean touch() throws IOException {
        return touch(System.currentTimeMillis());
    }

    /**
     * @see org.ubimix.commons.fs.IFileSystemEntry#touch(long)
     */
    public boolean touch(long date) throws IOException {
        if (!fFile.exists()) {
            fFile.createNewFile();
        }
        return fFile.setLastModified(date);
    }

}
