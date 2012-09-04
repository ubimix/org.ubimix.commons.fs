/**
 * 
 */
package org.ubimix.commons.fs;

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
     * Returns an output stream used to set a new content. The output stream
     * returned by this method replaces the content of the file. It works like
     * the <code>{@link #getOutputStream(boolean) getOutputStream(false)}</code>
     * call.
     * 
     * @return an output stream used to set a new content
     * @throws IOException
     * @see {@link #getOutputStream(boolean)}
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * Returns an output stream used to set a new content.
     * 
     * @param append if this flag is <code>true</code> then this method returns
     *        an output stream appending the content to the end of existing
     *        file; otherwise the returned stream replaces already existing
     *        content.
     * @return an output stream used to set a new content
     * @throws IOException
     * @see {@link #getOutputStream()}
     */
    OutputStream getOutputStream(boolean append) throws IOException;

}
