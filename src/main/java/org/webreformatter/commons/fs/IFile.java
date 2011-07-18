/**
 * 
 */
package org.webreformatter.commons.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Entities of this type correspond to files in the file system. Files give
 * access to the underlying content using input and output streams.
 * 
 * @author kotelnikov
 */
public interface IFile extends IFileSystemEntry {

    /**
     * Returns the input stream used to read the content of this file. If the
     * file does not exists then this method rises an exception.
     * 
     * @return the input stream used to read the content of this file.
     * @throws IOException this method rises this exception if the file does not
     *         exists or it is impossible to open the stream.
     */
    InputStream getInputStream() throws IOException;

    /**
     * Returns the length of this file
     * 
     * @return the length of this file
     */
    long getLength();

    /**
     * Returns an output stream used to set a new content.
     * 
     * @return an output stream used to set a new content
     * @throws IOException
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Returns an output stream used to set a new content.
     * 
     * @param append if this flag is <code>true</code> then this method returns
     *        an output stream appending the content to the end of existing file
     *        (if it exists)
     * @return an output stream used to set a new content
     * @throws IOException
     */
    OutputStream getOutputStream(boolean append) throws IOException;

    /**
     * Returns <code>true</code> if the file was successfully renamed
     * 
     * @param file to which this file should be renamed
     * @return <code>true</code> if the file was successfully renamed
     * @throws IOException an exception can be thrown if an error occurred while
     *         renaming
     */
    boolean renameTo(IFile file) throws IOException;

}
