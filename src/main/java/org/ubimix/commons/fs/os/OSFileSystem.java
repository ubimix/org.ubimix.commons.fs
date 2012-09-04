/**
 * 
 */
package org.ubimix.commons.fs.os;

import java.io.File;
import java.io.IOException;

import org.ubimix.commons.fs.FileSystemException;
import org.ubimix.commons.fs.IDirectory;
import org.ubimix.commons.fs.IFile;
import org.ubimix.commons.fs.IFileSystem;
import org.ubimix.commons.fs.IFileSystemEntry;

/**
 * This is a simple disc-based implementation of the {@link IFileSystem}
 * interface.
 * 
 * @author kotelnikov
 */
public class OSFileSystem implements IFileSystem {

    /**
     * The root directory.
     */
    private final File fRoot;

    /**
     * Initializes this file system by the root directory. All files and
     * directories managed by this file system are contained in this root.
     * 
     * @param root the root directory.
     * @throws FileSystemException
     */
    public OSFileSystem(File root) throws FileSystemException {
        this(root, false);
    }

    /**
     * Initializes this file system by the root directory. All files and
     * directories managed by this file system are contained in this root. If
     * the specified <code>reset</code> flag is <code>true</code> then this
     * constructor will remove the existing content of the folder.
     * 
     * @param root the root directory.
     * @throws FileSystemException
     */
    public OSFileSystem(File root, boolean reset) throws FileSystemException {
        try {
            if (root.exists()) {
                if (!root.isDirectory()) {
                    throw new IllegalArgumentException(
                        "The specified path is not a directory. Path: "
                            + root.getPath());
                }
            }
            fRoot = checkFile(root);
            OSDirectory r = getRootDirectory();
            if (reset) {
                r.delete();
            }
            if (r.exists()) {
                r.mkdirs();
            }
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }

    /**
     * Initializes this file system by the root directory. All files and
     * directories managed by this file system are contained in this root.
     * 
     * @param root the root directory.
     * @throws FileSystemException
     */
    public OSFileSystem(String root) throws FileSystemException {
        this(new File(root), false);
    }

    /**
     * Initializes this file system by the root directory. All files and
     * directories managed by this file system are contained in this root.
     * 
     * @param root the root directory.
     * @param reset if this flag is <code>true</code> then the content of root
     *        folder (with all subdirectories) will be removed
     * @throws FileSystemException
     */
    public OSFileSystem(String root, boolean reset) throws FileSystemException {
        this(new File(root), reset);
    }

    private File checkFile(File file) throws IOException {
        file = file.getCanonicalFile();
        return file;
    }

    /**
     * Returns the root file directory containing all files and sub-directories
     * of this file system.
     * 
     * @return the root file directory
     */
    public File getRootDir() {
        return fRoot;
    }

    /**
     * @throws FileSystemException
     * @see org.ubimix.commons.fs.IFileSystem#getRootDirectory()
     */
    public OSDirectory getRootDirectory() throws FileSystemException {
        try {
            return new OSDirectory(this, fRoot);
        } catch (Exception e) {
            throw new FileSystemException(e);
        }
    }

    protected IDirectory newDirectory(File file) throws FileSystemException {
        try {
            file = checkFile(file);
            if (file.exists() && !file.isDirectory()) {
                throw new FileSystemException(
                    "The specified path does not corresponds to a directory. Path: "
                        + file);
            }
            return new OSDirectory(this, file);
        } catch (FileSystemException e) {
            throw e;
        } catch (Exception e) {
            throw new FileSystemException(e);
        }
    }

    protected IFileSystemEntry newEntry(File file) throws FileSystemException {
        try {
            file = checkFile(file);
            if (!file.exists() || (!file.isDirectory() && !file.isFile())) {
                return null;
            }
            return file.isDirectory()
                ? new OSDirectory(this, file)
                : new OSFile(this, file);
        } catch (FileSystemException e) {
            throw e;
        } catch (Exception e) {
            throw new FileSystemException(e);
        }
    }

    protected IFile newFile(File file) throws FileSystemException {
        try {
            file = checkFile(file);
            if (file.exists() && !file.isFile()) {
                throw new FileSystemException(
                    "The specified path does not corresponds to a file. Path: "
                        + file);
            }
            return new OSFile(this, file);
        } catch (FileSystemException e) {
            throw e;
        } catch (Exception e) {
            throw new FileSystemException(e);
        }
    }

    @Override
    public String toString() {
        return fRoot.getAbsolutePath();
    }
}
