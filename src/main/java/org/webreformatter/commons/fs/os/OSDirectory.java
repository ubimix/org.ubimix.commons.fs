/**
 * 
 */
package org.webreformatter.commons.fs.os;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFile;
import org.webreformatter.commons.fs.IFileSystemEntry;


/**
 * @author kotelnikov
 */
public class OSDirectory extends OSEntry implements IDirectory {

    /**
     * @param fileSystem
     * @param file
     */
    protected OSDirectory(OSFileSystem fileSystem, File file) {
        super(fileSystem, file);
    }

    @Override
    protected String buildPath() {
        String path = super.buildPath();
        if (!path.endsWith("/"))
            path += "/";
        return path;
    }

    /**
     * @see org.webreformatter.commons.fs.IDirectory#createTempFile(java.lang.String,
     *      java.lang.String)
     */
    public IFile createTempFile(String prefix, String suffix)
        throws FileSystemException {
        try {
            File file = File.createTempFile(prefix, suffix, fFile);
            return new OSFile(fFileSystem, file);
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }

    /**
     * @see org.webreformatter.commons.fs.IDirectory#getDirectory(java.lang.String)
     */
    public IDirectory getDirectory(String path) throws FileSystemException {
        File file = new File(fFile, path);
        return fFileSystem.newDirectory(file);
    }

    /**
     * @see org.webreformatter.commons.fs.IDirectory#getEntry(java.lang.String)
     */
    public IFileSystemEntry getEntry(String path) throws FileSystemException {
        File file = new File(fFile, path);
        return fFileSystem.newEntry(file);
    }

    /**
     * @see org.webreformatter.commons.fs.IDirectory#getFile(java.lang.String)
     */
    public IFile getFile(String path) throws FileSystemException {
        File file = new File(fFile, path);
        return fFileSystem.newFile(file);
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<IFileSystemEntry> iterator() {
        File[] a = fFile.listFiles();
        final File[] array = a != null ? a : new File[0];
        Arrays.sort(array, new Comparator<File>() {
            public int compare(File o1, File o2) {
                String str1 = o1.getName();
                String str2 = o2.getName();
                return str1.compareTo(str2);
            }
        });
        return new Iterator<IFileSystemEntry>() {

            int fPos;

            public boolean hasNext() {
                return array != null && fPos < array.length;
            }

            public IFileSystemEntry next() {
                try {
                    if (array == null || fPos >= array.length)
                        return null;
                    return fFileSystem.newEntry(array[fPos++]);
                } catch (FileSystemException e) {
                    return null;
                }
            }

            public void remove() {
                throw new RuntimeException("Not implemented");
            }

        };
    }

    /**
     * @see org.webreformatter.commons.fs.IDirectory#mkdirs()
     */
    public boolean mkdirs() {
        return fFile.mkdirs();
    }

}
