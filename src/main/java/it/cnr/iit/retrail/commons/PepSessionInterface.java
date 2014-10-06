/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.Serializable;
import java.net.URL;

/**
 *
 * @author oneadmin
 */
public interface PepSessionInterface extends Serializable {

    String getCustomId();

    Status getStatus();

    URL getUconUrl();

    String getUuid();

    void setCustomId(String customId);

    void setStatus(Status status);

    void setUconUrl(URL uconUrl);

    void setUuid(String uuid);
    
}
