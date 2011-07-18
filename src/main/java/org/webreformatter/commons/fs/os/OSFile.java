/**
 * 
 */
package org.webreformatter.commons.fs.os;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.webreformatter.commons.fs.IFile;


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
     * @see org.webreformatter.commons.fs.IFile#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(fFile);
        // return new BufferedInputStream(new FileInputStream(fFile));
    }

    /**
     * @see org.webreformatter.commons.fs.IFile#getLength()
     */
    public long getLength() {
        return fFile.length();
    }

    public OutputStream getOutputStream() throws IOException {
        return getOutputStream(false);
    }

    /**
     * @see org.webreformatter.commons.fs.IFile#getOutputStream(boolean)
     */
    public OutputStream getOutputStream(boolean append) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(fFile));
    }

    /**
     * @see org.webreformatter.commons.fs.IFile#renameTo(org.webreformatter.commons.fs.IFile)
     */
    public boolean renameTo(IFile file) throws IOException {
        File f = ((OSFile) file).fFile;
        return fFile.renameTo(f);
    }

}
