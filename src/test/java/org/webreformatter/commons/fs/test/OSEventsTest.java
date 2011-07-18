package org.webreformatter.commons.fs.test;

import junit.framework.TestCase;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.IFileSystemEntry;
import org.webreformatter.commons.fs.events.ChangeListener;
import org.webreformatter.commons.fs.events.FileInfo;
import org.webreformatter.commons.fs.os.OSFileSystem;

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
