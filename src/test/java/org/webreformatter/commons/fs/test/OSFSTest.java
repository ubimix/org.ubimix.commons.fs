/**
 * 
 */
package org.webreformatter.commons.fs.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.webreformatter.commons.fs.FSUtils;
import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFile;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.IFileSystemEntry;
import org.webreformatter.commons.fs.os.OSFileSystem;
import org.webreformatter.commons.fs.wrapper.WrapperFileSystem;

/**
 * @author kotelnikov
 */
public class OSFSTest extends TestCase {

    public OSFSTest(String name) {
        super(name);
    }

    private IFile addContent(IDirectory root, String path, String content)
        throws IOException {
        IFile file = FSUtils.getFile(root, path);
        InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream out = file.getOutputStream(false);
        FSUtils.copy(in, out);
        return file;
    }

    private void checkFile(IDirectory wroot, String path, String content)
        throws IOException {
        IFile file = FSUtils.getFile(wroot, path);
        assertNotNull(file);
        assertEquals(path, file.getPath());
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FSUtils.copy(in, out);
        String str = new String(out.toByteArray(), "UTF-8");
        assertEquals(content, str);
    }

    private OSFileSystem newFileSystem() throws FileSystemException {
        File dir = new File("./tmp");
        OSFileSystem fs = new OSFileSystem(dir, true);
        return fs;
    }

    public void testExistingEntries() throws Exception {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();
        addContent(root, "/a/b/c.txt", "Hello, world!");

        IFileSystemEntry entry = root.getEntry("a/b/c.txt");
        assertNotNull(entry);
        assertTrue(entry instanceof IFile);
        assertEquals("/a/b/c.txt", entry.getPath());
        entry = root.getEntry("a/b");
        assertNotNull(entry);
        assertTrue(entry instanceof IDirectory);
        assertEquals("/a/b/", entry.getPath());

        IFile file = FSUtils.getFile(root, "a/b/c.txt/");
        assertNotNull(file);
        assertEquals("/a/b/c.txt", file.getPath());

        IDirectory dir = FSUtils.getDirectory(root, "a/b/");
        assertNotNull(dir);
        assertTrue(dir instanceof IDirectory);
        assertEquals("/a/b/", dir.getPath());

        assertEquals(dir, file.getParentDirectory());

        // Bad requests
        try {
            dir = FSUtils.getDirectory(root, "a/b/c.txt");
            fail();
        } catch (Exception e) {
        }
        try {
            file = FSUtils.getFile(root, "a/b/");
            fail();
        } catch (Exception e) {
        }

    }

    public void testNonExistingEntries() throws Exception {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();

        IFileSystemEntry file = root.getEntry("/a/b/file.txt");
        assertNull(file);
        file = root.createFile("/a/b/file.txt");
        assertNotNull(file);
        assertTrue(file instanceof IFile);

        IFileSystemEntry dir = root.getEntry("/a/b/c");
        assertNull(dir);
        dir = root.createDirectory("/a/b/c");
        assertNotNull(dir);
        assertTrue(dir instanceof IDirectory);
    }

    public void testRenameFiles() throws IOException {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();
        IFile file = addContent(root, "/x/a/b/1.txt", "Hello, world! - 1");
        IFileSystemEntry test = root.getEntry("/x/a/b/1.txt");
        assertEquals(file, test);

        assertTrue(file.renameTo("abc.txt"));
        test = root.getEntry("/x/a/b/1.txt");
        assertNull(test);

        test = root.getEntry("/x/a/b/abc.txt");
        assertNotNull(test);
        assertEquals("/x/a/b/abc.txt", test.getPath());
    }

    public void testWrapperFS() throws IOException {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();
        addContent(root, "/x/a/b/1.txt", "Hello, world! - 1");
        addContent(root, "/x/a/b/2.txt", "Hello, world! - 2");
        addContent(root, "/x/a/b/3.txt", "Hello, world! - 3");
        addContent(root, "/x/a/README.txt", "Readme please...");
        IDirectory dir = FSUtils.getDirectory(root, "x/a");
        WrapperFileSystem wfs = new WrapperFileSystem(dir);
        IDirectory wroot = wfs.getRootDirectory();
        assertEquals("/", wroot.getPath());
        checkFile(wroot, "/b/1.txt", "Hello, world! - 1");
        checkFile(wroot, "/b/2.txt", "Hello, world! - 2");
        checkFile(wroot, "/b/3.txt", "Hello, world! - 3");
        checkFile(wroot, "/README.txt", "Readme please...");
    }
}
