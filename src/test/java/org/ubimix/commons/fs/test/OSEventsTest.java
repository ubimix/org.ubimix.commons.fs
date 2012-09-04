package org.ubimix.commons.fs.test;

import junit.framework.TestCase;

import org.ubimix.commons.fs.FileSystemException;
import org.ubimix.commons.fs.IDirectory;
import org.ubimix.commons.fs.IFileSystem;
import org.ubimix.commons.fs.IFileSystemEntry;
import org.ubimix.commons.fs.events.ChangeListener;
import org.ubimix.commons.fs.events.FileInfo;
import org.ubimix.commons.fs.os.OSFileSystem;

/**
 * @author kotelnikov
 */
public class OSEventsTest extends TestCase {

    private IFileSystem fs;

    public OSEventsTest(String name) {
        super(name);
    }

    private IFileSystem newFileSystem() throws FileSystemException {
        return new OSFileSystem("./tmp");
    }

    @Override
    protected void setUp() throws Exception {
        fs = newFileSystem();
    }

    public void testOne() throws Exception {
        IDirectory root = fs.getRootDirectory();
        FileSystemObserver observer = new FileSystemObserver(root);
        observer.addListener(new ChangeListener() {

            @Override
            public void onAdded(FileInfo newInfo) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onChanged(FileInfo oldInfo, FileInfo newInfo) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onNotChanged(FileInfo info) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onRemoved(FileInfo info) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void writeFile(String path, String content)
        throws FileSystemException {
        IFileSystemEntry file = fs.getRootDirectory().getEntry(path);
    }

}
