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
    private class WrapperDirectory extends WrapperEntry<IDirectory>
        implements
        IDirectory {

        /**
         * @param entry
         */
        public WrapperDirectory(IDirectory entry) {
            super(entry);
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
            return new WrapperFile(file);
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
                ? new WrapperDirectory((IDirectory) entry)
                : new WrapperFile((IFile) entry);
            return (E) e;
        }

    }

    private abstract class WrapperEntry<X extends IFileSystemEntry>
        implements
        IFileSystemEntry {

        protected X fEntry;

        public WrapperEntry(X entry) {
            fEntry = entry;
        }

        public boolean delete() throws FileSystemException {
            if (isRoot()) {
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

        public IFileSystem getFileSystem() {
            return WrapperFileSystem.this;
        }

        public String getName() throws FileSystemException {
            return fEntry.getName();
        }

        public IDirectory getParentDirectory() throws FileSystemException {
            if (isRoot()) {
                return null;
            }
            IDirectory dir = fEntry.getParentDirectory();
            return dir != null ? new WrapperDirectory(dir) : null;
        }

        public String getPath() throws FileSystemException {
            String rootPath = fRoot.getPath();
            String path = fEntry.getPath();
            path = path.substring(rootPath.length() - 1);
            return path;
        }

        @Override
        public int hashCode() {
            return fEntry.hashCode();
        }

        private boolean isRoot() {
            return fEntry.equals(fRoot);
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

    private class WrapperFile extends WrapperEntry<IFile> implements IFile {

        public WrapperFile(IFile entry) {
            super(entry);
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

    private IDirectory fRoot;

    public WrapperFileSystem(IDirectory root) {
        fRoot = root;
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
        return fRoot.equals(fs.fRoot);
    }

    /**
     * @see org.webreformatter.commons.fs.IFileSystem#getRootDirectory()
     */

    public IDirectory getRootDirectory() throws FileSystemException {
        return new WrapperDirectory(fRoot);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fRoot.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WrapperFileSystem(" + fRoot + ")";
    }

}
