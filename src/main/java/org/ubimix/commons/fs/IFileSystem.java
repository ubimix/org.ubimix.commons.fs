package org.ubimix.commons.fs;

/**
 * The file system providing access to files and directories.
 * 
 * @author kotelnikov
 */
public interface IFileSystem {

    /**
     * Returns the root directory of this file system
     * 
     * @return the root directory of this file system
     * @throws FileSystemException
     */
    IDirectory getRootDirectory() throws FileSystemException;

}
