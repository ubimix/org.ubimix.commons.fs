/**
 * 
 */
package org.webreformatter.commons.fs.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFile;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.IFileSystemEntry;
import org.webreformatter.commons.fs.os.OSFileSystem;
import org.webreformatter.commons.fs.wrapper.WrapperFileSystem;

import junit.framework.TestCase;


/**
 * @author kotelnikov
 */
public class OSFSTest extends TestCase {

    public OSFSTest(String name) {
        super(name);
    }

    private IFile addContent(IDirectory root, String path, String content)
        throws IOException {
        IFile file = root.getFile(path);
        file.getParentDirectory().mkdirs();
        InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
        OutputStream out = file.getOutputStream(false);
        copy(in, out);
        return file;
    }

    private void checkFile(IDirectory wroot, String path, String content)
        throws IOException {
        IFile file = wroot.getFile(path);
        assertNotNull(file);
        assertEquals(path, file.getPath());
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        String str = new String(out.toByteArray(), "UTF-8");
        assertEquals(content, str);
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
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

    private OSFileSystem newFileSystem() throws FileSystemException {
        return new OSFileSystem("./tmp");
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

        IFile file = root.getFile("a/b/c.txt/");
        assertNotNull(file);
        assertEquals("/a/b/c.txt", file.getPath());

        IDirectory dir = root.getDirectory("a/b/");
        assertNotNull(dir);
        assertTrue(dir instanceof IDirectory);
        assertEquals("/a/b/", dir.getPath());

        // Bad requests
        try {
            dir = root.getDirectory("a/b/c.txt");
            fail();
        } catch (Exception e) {
        }
        try {
            file = root.getFile("a/b/");
            fail();
        } catch (Exception e) {
        }

    }

    public void testNonExistingEntries() throws Exception {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();

        IFile file = root.getFile("/a/b/c");
        assertNotNull(file);
        assertFalse(file.exists());
        assertTrue(file instanceof IFile);

        IDirectory dir = root.getDirectory("/a/b/c");
        assertNotNull(dir);
        assertFalse(dir.exists());
        assertTrue(dir instanceof IDirectory);

        // This operations is failed because there is no file nor directory
        // with such a path
        IFileSystemEntry entry = root.getEntry("/a/b/c");
        assertNull(entry);
    }

    public void testWrapperFS() throws IOException {
        IFileSystem fs = newFileSystem();
        IDirectory root = fs.getRootDirectory();
        addContent(root, "/x/a/b/1.txt", "Hello, world! - 1");
        addContent(root, "/x/a/b/2.txt", "Hello, world! - 2");
        addContent(root, "/x/a/b/3.txt", "Hello, world! - 3");
        addContent(root, "/x/a/README.txt", "Readme please...");
        IDirectory dir = root.getDirectory("x/a");
        WrapperFileSystem wfs = new WrapperFileSystem(dir);
        IDirectory wroot = wfs.getRootDirectory();
        assertEquals("/", wroot.getPath());
        checkFile(wroot, "/b/1.txt", "Hello, world! - 1");
        checkFile(wroot, "/b/2.txt", "Hello, world! - 2");
        checkFile(wroot, "/b/3.txt", "Hello, world! - 3");
        checkFile(wroot, "/README.txt", "Readme please...");
    }
}
