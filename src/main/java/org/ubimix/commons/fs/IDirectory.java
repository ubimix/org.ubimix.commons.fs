package org.ubimix.commons.fs;

/**
 * This entity corresponds to a directory in the underlying file system. Each
 * directory can contain other directories and/or files.
 * 
 * @author kotelnikov
 */
public interface IDirectory
    extends
    IFileSystemEntry,
    Iterable<IFileSystemEntry> {

    /**
     * Creates and returns a directory corresponding to the specified path. If
     * there is already an entry (file or directory) corresponding to this path
     * then this method rises the {@link FileSystemException}.
     * 
     * @param path the path of the directory to create
     * @return a new directory corresponding to the specified path
     * @throws FileSystemException
     */
    IDirectory createDirectory(String path) throws FileSystemException;

    /**
     * Creates and returns a file corresponding to the specified path. If there
     * is already an entry (file or directory) corresponding to this path then
     * this method rises the {@link FileSystemException}.
     * 
     * @param path the path of the file to create
     * @return a new file corresponding to the specified path
     * @throws FileSystemException
     */
    IFile createFile(String path) throws FileSystemException;

    /**
     * Returns a newly created temporary file in this directory
     * 
     * @param prefix the prefix of the file name
     * @param suffix the suffix of the file name
     * @return a newly created temporary file in this directory
     * @throws FileSystemException
     */
    IFile createTempFile(String prefix, String suffix)
        throws FileSystemException;

    /**
     * Returns an existing file system entry (file or directory) corresponding
     * to the specified path. If there is no items with this path then this
     * method returns <code>null</code>.
     * 
     * @param path the path of the entry to return
     * @return a file entry corresponding to the specified path
     * @throws FileSystemException
     */
    IFileSystemEntry getEntry(String path) throws FileSystemException;

}
