/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author oneadmin
 */
public interface PepSessionInterface extends Serializable {

    /**
     * Get the custom ID for this session. 
     * The default value is the UUID, attributed by the UCon.
     * @return the custom id.
     */
    String getCustomId();

    /**
     * Get the status of the session.
     * @return the status of the session.
     */
    StateType getStateType();
    
    /**
     * Get the state name of the session.
     * @return the state name of the session.
     */
    String getStateName();

    /**
     * Get the url of the UCon service handling this session.
     * @return url of the UCon service.
     */
    URL getUconUrl();

    /**
     * Get the universal identifier of this session.
     * @return the uuid of the session.
     */
    String getUuid();

    /**
     * Set a customId for the session. It must be unique within the Ucon
     * service.
     * @param customId the new customId for this session.
     */
    void setCustomId(String customId);

    /**
     * Set the status of the session.
     * Note: this method should be invoked by the UCon only.
     * @param stateType the new status of the session.
     */
    void setStateType(StateType stateType);

    /**
     * Set the state name of the session.
     * Note: this method should be invoked by the UCon only.
     * @param stateName the state name of the session.
     */
    void setStateName(String stateName);

    /**
     * Set the url of the UCon service.
     * Note: this method should be invoked by the UCon only.
     * @param uconUrl the url of the UCon service.
     */
    void setUconUrl(URL uconUrl);

    /**
     * set a new Uuid for this session.
     * Note: this method must be invoked by the UCon only.
     * 
     * @param uuid the new attributed uuid.
     */
    void setUuid(String uuid);
    
    Collection<String> getObligations();
    
    long getMs();
    void setMs(long ms);
    
    /**
     * LocalInfo is a custom property holding a dictionary intended for 
     * user needs. Values of this property are never used by the framework
     * itself. Information held is never transmitted to/from the UCon.
     * By default the localInfo is an empty map.
     * @param info the map to be set.
     */
    void setLocalInfo(Map<String,Object> info);
    
    /**
     * LocalInfo is a custom property holding a dictionary intended for 
     * user needs. Values are never used by the framework itself and 
     * information held into this container is never transmitted to/from the
     * UCon service.
     * It's empty by default.
     * @return the local custom information dictionary.
     */
    Map<String,Object> getLocalInfo();
}
