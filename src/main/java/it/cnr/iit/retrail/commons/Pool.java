/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 * @param <E>
 */
public abstract class Pool<E extends Object> {
    protected static final Logger log = LoggerFactory.getLogger(Pool.class);
    protected int maxPoolSize = 10;
    private final IdentityHashSet busy;
    private final LinkedList<E> available;

    public Pool() {
        log.debug("creating pool (maxPoolSize = {})", maxPoolSize);
        busy = new IdentityHashSet(maxPoolSize);
        available = new LinkedList();
    }

    public Pool(int maxPoolSize) {
        log.debug("creating pool (maxPoolSize = {})", maxPoolSize);
        this.maxPoolSize = maxPoolSize;
        busy = new IdentityHashSet(maxPoolSize);
        available = new LinkedList();
    }

    protected abstract E newObject();
    
    public synchronized E obtain() {
        E a = null;
        if (available.isEmpty()) {
            a = newObject();
        } else {
            a = available.removeFirst();
        }
        busy.add(a);
        if (busy.size() > maxPoolSize) {
            log.warn("running objects > {}, consider enlarging Pool.maxPoolSize value", maxPoolSize);
        }
        return a;
    }

    public synchronized void release(E a) {
        if (busy.remove(a) && available.size() < maxPoolSize) {
            available.add(a);
        }
    }
}
