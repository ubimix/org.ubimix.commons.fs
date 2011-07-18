package org.webreformatter.commons.fs.events.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.webreformatter.commons.fs.FileSystemException;
import org.webreformatter.commons.fs.IDirectory;
import org.webreformatter.commons.fs.IFileSystem;
import org.webreformatter.commons.fs.events.AsyncChangeListener;
import org.webreformatter.commons.fs.events.CompositeChangeListener;
import org.webreformatter.commons.fs.events.DispatcherChangeListener;
import org.webreformatter.commons.fs.events.FileInfo;
import org.webreformatter.commons.fs.events.IChangeListener;
import org.webreformatter.commons.fs.events.IChangeListenerRegistry;

/**
 * @author kotelnikov
 */
public class PollingChangeDetector implements IChangeListenerRegistry {

    private final static Logger log = Logger
        .getLogger(PollingChangeDetector.class.getName());

    private AsyncChangeListener fAsyncListener;

    private IDirectory fDirectory;

    private DispatcherChangeListener fDispatchListener = new DispatcherChangeListener();

    private ScheduledExecutorService fExecutor;

    private ExecutorService fExecutorForListeners;

    private Map<String, FileInfo> fMap = new HashMap<String, FileInfo>();

    private int fRefreshTime;

    public PollingChangeDetector(IDirectory rootDirectory, int refreshTime) {
        fRefreshTime = refreshTime;
        fDirectory = rootDirectory;
    }

    public PollingChangeDetector(IFileSystem fs, int refreshTime)
        throws FileSystemException {
        this(fs.getRootDirectory(), refreshTime);
    }

    /**
     * @throws FileSystemException
     */
    public synchronized void activate() throws FileSystemException {
        if (fExecutorForListeners != null) {
            return;
        }
        fExecutorForListeners = newExecutorForListeners();
        fAsyncListener = new AsyncChangeListener(
            fExecutorForListeners,
            fDispatchListener);
        fExecutor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setPriority(Thread.NORM_PRIORITY - 1);
                return t;
            }
        });
        fExecutor.submit(new Runnable() {
            public void run() {
                initialize();
                log.log(Level.INFO, "File event system is activated.");
                fExecutor.scheduleWithFixedDelay(new Runnable() {
                    public void run() {
                        syncrhonize();
                    }
                }, 0, fRefreshTime, TimeUnit.MILLISECONDS);
            }

        });
    }

    /**
     * 
     */
    public synchronized void deactivate() {
        if (fDispatchListener.isEmpty()) {
            fExecutor.shutdown();
            fExecutorForListeners.shutdown();
            fExecutor = null;
            fExecutorForListeners = null;
            fAsyncListener = null;
            log.log(Level.INFO, "File event system is stopped.");
        }
    }

    protected void initialize() {
        try {
            if (fDirectory != null) {
                fMap = ChangeDetector.update(
                    fDirectory,
                    fMap,
                    new CompositeChangeListener());
            }
        } catch (FileSystemException e) {
            log.log(
                Level.WARNING,
                "Impossible to initialize file event system.",
                e);
        }
    }

    protected ExecutorService newExecutorForListeners() {
        return Executors.newSingleThreadExecutor();
    }

    /**
     * @see org.webreformatter.commons.fs.events.IChangeListenerRegistry#removeChangeListener(String,
     *      org.webreformatter.commons.fs.events.IChangeListener)
     */
    public synchronized void removeChangeListener(
        String path,
        IChangeListener listener) {
        fDispatchListener.removeChangeListener(path, listener);
        deactivate();
    }

    public void removeFileSystem(IFileSystem fs) {
        fDirectory = null;
    }

    /**
     * @throws FileSystemException
     * @see org.webreformatter.commons.fs.events.IChangeListenerRegistry#addChangeListener(String,
     *      org.webreformatter.commons.fs.events.IChangeListener)
     */
    public synchronized void addChangeListener(
        String path,
        IChangeListener listener) throws FileSystemException {
        fDispatchListener.addChangeListener(path, listener);
        activate();
    }

    private void syncrhonize() {
        try {
            if (fDirectory != null) {
                fMap = ChangeDetector.update(fDirectory, fMap, fAsyncListener);
            }
        } catch (Throwable e) {
            log.log(Level.WARNING, "Refresh failed.", e);
        }
    }

}
