package org.webreformatter.commons.fs.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFile;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.IFileSystemEntry;

/**
 * @author kotelnikov
 */
public class WrapperFileSystem implements IFileSystem {

    /**
     * A wrapper for directories.
     */
    public static class WrapperDirectory extends WrapperEntry<IDirectory>
        implements
        IDirectory {

        public WrapperDirectory(
            WrapperFileSystem fileSystem,
            String mountPath,
            IDirectory mountPoint,
            IDirectory entry) {
            super(fileSystem, mountPath, mountPoint, entry);
        }

        public IDirectory createDirectory(String path)
            throws FileSystemException {
            IDirectory entry = fEntry.createDirectory(path);
            return wrapEntry(entry);
        }

        public IFile createFile(String path) throws FileSystemException {
            IFile entry = fEntry.createFile(path);
            return wrapEntry(entry);
        }

        public IFile createTempFile(String prefix, String suffix)
            throws FileSystemException {
            IFile file = fEntry.createTempFile(prefix, suffix);
            return fFileSystem.getWrapperFile(file, fMountPath, fMountDir);
        }

        public IFileSystemEntry getEntry(String path)
            throws FileSystemException {
            IFileSystemEntry entry = fEntry.getEntry(path);
            return wrapEntry(entry);
        }

        public Iterator<IFileSystemEntry> iterator() {
            final Iterator<IFileSystemEntry> iterator = fEntry.iterator();
            return new Iterator<IFileSystemEntry>() {

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public IFileSystemEntry next() {
                    IFileSystemEntry entry = iterator.next();
                    return wrapEntry(entry);
                }

                public void remove() {
                    iterator.remove();
                }

            };
        }

        @SuppressWarnings("unchecked")
        private <E extends IFileSystemEntry> E wrapEntry(E entry) {
            if (entry == null) {
                return null;
            }
            WrapperEntry<? extends IFileSystemEntry> e = (entry instanceof IDirectory)
                ? fFileSystem.getWrapperDirectory(
                    (IDirectory) entry,
                    fMountPath,
                    fMountDir) : fFileSystem.getWrapperFile(
                    (IFile) entry,
                    fMountPath,
                    fMountDir);
            return (E) e;
        }

    }

    public static abstract class WrapperEntry<X extends IFileSystemEntry>
        implements
        IFileSystemEntry {

        protected X fEntry;

        protected WrapperFileSystem fFileSystem;

        protected IDirectory fMountDir;

        protected String fMountPath;

        public WrapperEntry(
            WrapperFileSystem fileSystem,
            String mountPath,
            IDirectory mountPoint,
            X entry) {
            fMountPath = mountPath;
            fMountDir = mountPoint;
            fFileSystem = fileSystem;
            fEntry = entry;
        }

        public boolean delete() throws FileSystemException {
            if (isMountPoint()) {
                return false;
            }
            return fEntry.delete();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WrapperEntry<?>)) {
                return false;
            }
            WrapperEntry<?> entry = (WrapperEntry<?>) obj;
            return fEntry.equals(entry.fEntry);
        }

        protected X getEntry() {
            return fEntry;
        }

        public WrapperFileSystem getFileSystem() {
            return fFileSystem;
        }

        public String getName() throws FileSystemException {
            return fEntry.getName();
        }

        public IDirectory getParentDirectory() throws FileSystemException {
            String path = getPath();
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            int idx = path.lastIndexOf('/');
            if (idx > 0) {
                path = path.substring(0, idx);
            }
            IFileSystemEntry entry = fFileSystem.getEntry(path);
            return entry instanceof IDirectory ? (IDirectory) entry : null;
        }

        public String getPath() throws FileSystemException {
            String rootPath = fMountDir.getPath();
            String path = fEntry.getPath();
            path = path.substring(rootPath.length() - 1);
            path = fMountPath + path;
            return path;
        }

        @Override
        public int hashCode() {
            return fEntry.hashCode();
        }

        private boolean isMountPoint() {
            return fEntry.equals(fMountDir);
        }

        public long lastModified() {
            return fEntry.lastModified();
        }

        @Override
        public String toString() {
            try {
                return getPath();
            } catch (FileSystemException e) {
                return "???";
            }
        }

        public boolean touch() throws IOException {
            return fEntry.touch();
        }

        public boolean touch(long date) throws IOException {
            return fEntry.touch(date);
        }

    }

    public static class WrapperFile extends WrapperEntry<IFile>
        implements
        IFile {

        public WrapperFile(
            WrapperFileSystem fileSystem,
            String mountPath,
            IDirectory mountPoint,
            IFile entry) {
            super(fileSystem, mountPath, mountPoint, entry);
        }

        public InputStream getInputStream() throws IOException {
            return fEntry.getInputStream();
        }

        public long getLength() {
            return fEntry.getLength();
        }

        public OutputStream getOutputStream() throws IOException {
            return fEntry.getOutputStream();
        }

        public OutputStream getOutputStream(boolean append) throws IOException {
            return fEntry.getOutputStream(append);
        }

        public boolean renameTo(String name) throws IOException {
            return fEntry.renameTo(name);
        }

    }

    private IDirectory fMountDir;

    private String fMountPath;

    public WrapperFileSystem(IDirectory mountDir) {
        this("", mountDir);
    }

    public WrapperFileSystem(String mountPoint, IDirectory mountDir) {
        fMountPath = mountPoint;
        fMountDir = mountDir;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WrapperFileSystem)) {
            return false;
        }
        WrapperFileSystem fs = (WrapperFileSystem) obj;
        return fMountDir.equals(fs.fMountDir);
    }

    public IFileSystemEntry getEntry(String path) throws FileSystemException {
        String mountPoint = getMountPath(path);
        if (mountPoint == null) {
            return null;
        }
        IDirectory mountDir = getMountDir(mountPoint);
        path = path.substring(mountPoint.length());
        IFileSystemEntry entry = mountDir.getEntry(path);
        if (entry != null) {
            if (entry instanceof IDirectory) {
                return getWrapperDirectory(
                    (IDirectory) entry,
                    fMountPath,
                    fMountDir);
            } else if (entry instanceof IFile) {
                return getWrapperFile((IFile) entry, fMountPath, fMountDir);
            }
        }
        return null;
    }

    protected IDirectory getMountDir(String mountPoint) {
        return fMountDir;
    }

    protected String getMountPath(String path) {
        if (path.startsWith(fMountPath)) {
            return fMountPath;
        }
        return null;
    }

    /**
     * @see org.webreformatter.commons.fs.IFileSystem#getRootDirectory()
     */

    public WrapperDirectory getRootDirectory() throws FileSystemException {
        return getWrapperDirectory(fMountDir, fMountPath, fMountDir);
    }

    protected WrapperDirectory getWrapperDirectory(
        IDirectory dir,
        String mountPath,
        IDirectory mountDir) {
        return new WrapperDirectory(this, mountPath, mountDir, dir);
    }

    protected WrapperFile getWrapperFile(
        IFile file,
        String mountPath,
        IDirectory mountDir) {
        return new WrapperFile(this, mountPath, mountDir, file);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fMountDir.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WrapperFileSystem(" + fMountDir + ")";
    }

}
