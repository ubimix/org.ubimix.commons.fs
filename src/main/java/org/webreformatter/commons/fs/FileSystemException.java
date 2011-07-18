/**
 * 
 */
package org.webreformatter.commons.fs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author kotelnikov
 */
public class FileSystemException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = -2529376501016324034L;

    private Collection<Throwable> fCauses;

    /**
     * 
     */
    public FileSystemException() {
        super();
    }

    /**
     * This constructor is used to build a wrapper for multiple errors
     * 
     * @param causes
     */
    public FileSystemException(Collection<Throwable> causes) {
        fCauses = causes;
    }

    /**
     * @param message
     */
    public FileSystemException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public FileSystemException(String message, Throwable cause) {
        super(message);
        fCauses = new ArrayList<Throwable>();
        fCauses.add(cause);
    }

    /**
     * @param cause
     */
    public FileSystemException(Throwable cause) {
        super();
        fCauses = new ArrayList<Throwable>();
        fCauses.add(cause);
    }

    public Collection<Throwable> getCauses() {
        return fCauses;
    }

}
