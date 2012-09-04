package org.ubimix.commons.fs.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author kotelnikov
 */
public class DispatcherChangeListener
    implements
    IChangeListener,
    IChangeListenerRegistry {

    private static class Slot extends CompositeChangeListener
        implements
        Comparable<Slot> {

        public final String path;

        private Slot(String path) {
            this.path = normalize(path);
        }

        public int compareTo(Slot o) {
            return path.compareTo(o.path);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Slot)) {
                return false;
            }
            return 0 == compareTo((Slot) obj);
        }

        public boolean isEmpty() {
            return fListeners.isEmpty();
        }

        @Override
        public String toString() {
            return path;
        }

    }

    private static String normalize(String p) {
        if (p == null) {
            return "/";
        }
        p = p.trim();
        if ("".equals(p)) {
            return "/";
        }
        p = p.replace('\\', '/');
        if (!p.startsWith("/")) {
            p = "/" + p;
        }
        if (!p.endsWith("/")) {
            p += "/";
        }
        return p;
    }

    private List<Slot> fSlots = new ArrayList<Slot>();

    /**
     * 
     */
    public DispatcherChangeListener() {
    }

    public synchronized void addChangeListener(
        String path,
        IChangeListener listener) {
        Slot slot = new Slot(path);
        int pos = Collections.binarySearch(fSlots, slot);
        if (pos < 0) {
            pos = -(pos + 1);
            List<Slot> slots = new ArrayList<Slot>(fSlots);
            slots.add(pos, slot);
            fSlots = slots;
        } else {
            slot = fSlots.get(pos);
        }
        slot.addListener(listener);
    }

    private Iterable<Slot> getSlots(String path) {
        path = normalize(path);
        List<Slot> slots = fSlots;
        int len = slots.size();
        if (len == 0) {
            return null;
        }
        // FIXME: this is a very inefficient implementation
        // FIXME: re-implement it using trees
        List<Slot> result = new ArrayList<Slot>();
        for (Slot s : slots) {
            if (path.startsWith(s.path)) {
                result.add(s);
            }
        }
        return result;
        // Slot slot = new Slot(path);
        // int pos = Collections.binarySearch(slots, slot);
        // if (pos < 0) {
        // pos = -(pos + 1);
        // if (pos > 0)
        // pos--;
        // }
        // slot = slots.get(pos);
        // if (!path.startsWith(slot.path))
        // return null;
        // String prefix = slot.path;
        // int i;
        // for (i = pos; i < len; i++) {
        // Slot s = slots.get(i);
        // if (!s.path.startsWith(prefix))
        // break;
        // }
        // return slots.subList(pos, i);
    }

    public boolean isEmpty() {
        return fSlots.isEmpty();
    }

    public void onAdded(FileInfo newInfo) {
        Iterable<Slot> slots = getSlots(newInfo.path);
        if (slots == null) {
            return;
        }
        for (Slot slot : slots) {
            slot.onAdded(newInfo);
        }
    }

    public void onChanged(FileInfo oldInfo, FileInfo newInfo) {
        Iterable<Slot> slots = getSlots(newInfo.path);
        if (slots == null) {
            return;
        }
        for (Slot slot : slots) {
            slot.onChanged(oldInfo, newInfo);
        }
    }

    public void onNotChanged(FileInfo info) {
        Iterable<Slot> slots = getSlots(info.path);
        if (slots == null) {
            return;
        }
        for (Slot slot : slots) {
            slot.onNotChanged(info);
        }
    }

    public void onRemoved(FileInfo info) {
        Iterable<Slot> slots = getSlots(info.path);
        if (slots == null) {
            return;
        }
        for (Slot slot : slots) {
            slot.onRemoved(info);
        }
    }

    public synchronized void removeChangeListener(
        String path,
        IChangeListener listener) {
        Slot slot = new Slot(path);
        int pos = Collections.binarySearch(fSlots, slot);
        if (pos < 0) {
            return;
        }
        slot = fSlots.get(pos);
        slot.removeListener(listener);
        if (slot.isEmpty()) {
            List<Slot> slots = new ArrayList<Slot>(fSlots);
            slots.remove(pos);
            fSlots = slots;
        }
    }

}
