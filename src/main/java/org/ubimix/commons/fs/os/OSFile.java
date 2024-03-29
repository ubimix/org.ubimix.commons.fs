/**
 * 
 */
package org.ubimix.commons.fs.os;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ubimix.commons.fs.IFile;

/**
 * @author kotelnikov
 */
public class OSFile extends OSEntry implements IFile {

    /**
     * @param fileSystem
     * @param file
     */
    protected OSFile(OSFileSystem fileSystem, File file) {
        super(fileSystem, file);
    }

    /**
     * @see org.ubimix.commons.fs.IFile#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(fFile);
        // return new BufferedInputStream(new FileInputStream(fFile));
    }

    /**
     * @see org.ubimix.commons.fs.IFile#getLength()
     */
    public long getLength() {
        return fFile.length();
    }

    public OutputStream getOutputStream() throws IOException {
        return getOutputStream(false);
    }

    /**
     * @see org.ubimix.commons.fs.IFile#getOutputStream(boolean)
     */
    public OutputStream getOutputStream(boolean append) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(fFile));
    }

}
