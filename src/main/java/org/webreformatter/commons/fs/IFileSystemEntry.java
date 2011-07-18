/**
 * 
 */
package org.webreformatter.commons.fs;

import java.io.IOException;

/**
 * This is a common super-class for all entities managed by a file system (see
 * {@link IFileSystem}).
 * 
 * @author kotelnikov
 */
public interface IFileSystemEntry {

    /**
     * Deletes this entry (and all its children, in the case of the
     * {@link IDirectory}) and returns <code>true</code> if this entry was
     * successfully deleted.
     * 
     * @return <code>true</code> if this entry was successfully deleted
     * @throws FileSystemException
     */
    boolean delete() throws FileSystemException;

    /**
     * Returns the file system owning this entry
     * 
     * @return the file system owning this entry
     */
    IFileSystem getFileSystem();

    /**
     * Returns the local name of this entry
     * 
     * @return the local name of this entry
     * @throws FileSystemException
     */
    String getName() throws FileSystemException;

    /**
     * Returns the parent directory owning this entry
     * 
     * @return the parent directory owning this entry
     * @throws FileSystemException
     */
    IDirectory getParentDirectory() throws FileSystemException;

    /**
     * Returns the full path (including the name of this entry) from the root of
     * the file system to this entry. The returned path should always start with
     * the "/" symbol and it equals to "/" for the root directory.
     * 
     * @return the full path (including the name of this entry) from the root of
     *         the file system to this entry
     * @throws FileSystemException
     */
    String getPath() throws FileSystemException;

    /**
     * Returns the time of the last modification of this file
     * 
     * @return the time of the last modification of this file
     */
    long lastModified();

    /**
     * Sets the current modification to the file; if the file does not exists
     * then this method will create a new empty file.
     * 
     * @return <code>true</code> if the file was successfully created or the
     *         modification time was changed.
     * @throws IOException
     */
    boolean touch() throws IOException;

    /**
     * Changes the date of this entry to the specified time-stamp.
     * 
     * @param date the time-stamp which is used as the date of the last
     *        modification of this file
     * @return <code>true</code> if the file was successfully created or the
     *         modification time was changed.
     * @throws IOException
     */
    boolean touch(long date) throws IOException;

}
