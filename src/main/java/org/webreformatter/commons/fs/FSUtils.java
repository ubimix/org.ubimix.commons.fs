package org.webreformatter.commons.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author kotelnikov
 */
public class FSUtils {

    public static void copy(IFile from, IFile to) throws IOException {
        InputStream input = from.getInputStream();
        try {
            OutputStream output = to.getOutputStream();
            try {
                copy(input, output);
            } finally {
                output.close();
            }
        } finally {
            input.close();
        }
    }

    public static void copy(InputStream in, OutputStream out)
        throws IOException {
        try {
            try {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static IDirectory getDirectory(IDirectory root, String path)
        throws FileSystemException {
        IFileSystemEntry entry = root.getEntry(path);
        IDirectory file = null;
        if (!(entry instanceof IDirectory)) {
            file = root.createDirectory(path);
        } else {
            file = (IDirectory) entry;
        }
        return file;
    }

    public static IFile getFile(IDirectory root, String path)
        throws FileSystemException {
        IFileSystemEntry entry = root.getEntry(path);
        IFile file = null;
        if (!(entry instanceof IFile)) {
            file = root.createFile(path);
        } else {
            file = (IFile) entry;
        }
        return file;
    }
}