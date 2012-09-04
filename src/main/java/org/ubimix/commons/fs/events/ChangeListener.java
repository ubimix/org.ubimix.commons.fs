/**
 * 
 */
package org.ubimix.commons.fs.events;

/**
 * Default do-nothing implementation of the {@link IChangeListener} interface.
 * Could be used as a super-class when only some methods should be defined.
 * 
 * @author kotelnikov
 */
public class ChangeListener implements IChangeListener {

    /**
     * 
     */
    public ChangeListener() {
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListener#onAdded(org.ubimix.commons.fs.events.FileInfo)
     */
    public void onAdded(FileInfo newInfo) {
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListener#onChanged(org.ubimix.commons.fs.events.FileInfo,
     *      org.ubimix.commons.fs.events.FileInfo)
     */
    public void onChanged(FileInfo oldInfo, FileInfo newInfo) {
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListener#onNotChanged(org.ubimix.commons.fs.events.FileInfo)
     */
    public void onNotChanged(FileInfo info) {
    }

    /**
     * @see org.ubimix.commons.fs.events.IChangeListener#onRemoved(org.ubimix.commons.fs.events.FileInfo)
     */
    public void onRemoved(FileInfo info) {
    }

}
