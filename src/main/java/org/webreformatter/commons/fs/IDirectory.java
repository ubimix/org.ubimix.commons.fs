package org.webreformatter.commons.fs;

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
     * Returns a sub-directory corresponding to the specified path. This method
     * rises an {@link FileSystemException} if the specified path corresponds to
     * an existing file and not to a directory.
     * 
     * @param path the path of the directory to return
     * @return a sub-directory corresponding to the specified path
     * @throws FileSystemException
     */
    IDirectory getDirectory(String path) throws FileSystemException;

    /**
     * Returns a file system entry (file or directory) corresponding to the
     * specified path.
     * 
     * @param path the path of the entry to return
     * @return a file entry corresponding to the specified path
     * @throws FileSystemException
     */
    IFileSystemEntry getEntry(String path) throws FileSystemException;

    /**
     * Returns a new file corresponding to the given path. This method rises an
     * {@link FileSystemException} if the specified path corresponds to an
     * existing directory and not to a file.
     * 
     * @param path the path to the file
     * @return a new file corresponding to the given path
     * @throws FileSystemException
     */
    IFile getFile(String path) throws FileSystemException;

    /**
     * Creates if necessary this directory and all missing parent directories
     * from the root of the file system.
     * 
     * @return <code>true</code> if directory(-ies) was successfully created
     * @throws FileSystemException
     */
    boolean mkdirs() throws FileSystemException;

}
