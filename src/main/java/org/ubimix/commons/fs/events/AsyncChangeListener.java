package org.ubimix.commons.fs.events;

import java.util.concurrent.Executor;

/**
 * This type of listeners are used to launch a specific listener in asynchronous
 * mode.
 * 
 * <pre>
 * IChangeListener listener = new MyChangeListener();
 * Executor executor = Executors.newSingleThreadExecutor();
 * AsyncChangeListener async  = new AsyncChangeListener(executor, listener);
 * 
 * </pre>
 * 
 * @author kotelnikov
 */
public class AsyncChangeListener implements IChangeListener {

    private Executor fExecutor;

    private IChangeListener fListener;

    public AsyncChangeListener(Executor executor, IChangeListener listener) {
        fExecutor = executor;
        fListener = listener;
    }

    public void onAdded(final FileInfo newInfo) {
        fExecutor.execute(new Runnable() {
            public void run() {
                fListener.onAdded(newInfo);
            }
        });
    }

    public void onChanged(final FileInfo oldInfo, final FileInfo newInfo) {
        fExecutor.execute(new Runnable() {
            public void run() {
                fListener.onChanged(oldInfo, newInfo);
            }
        });
    }

    public void onNotChanged(final FileInfo info) {
        fExecutor.execute(new Runnable() {
            public void run() {
                fListener.onNotChanged(info);
            }
        });
    }

    public void onRemoved(final FileInfo info) {
        fExecutor.execute(new Runnable() {
            public void run() {
                fListener.onRemoved(info);
            }
        });
    }

}