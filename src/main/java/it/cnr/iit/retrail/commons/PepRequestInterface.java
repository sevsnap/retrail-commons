/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.w3c.dom.Element;

/**
 *
 * @author oneadmin
 */
public interface PepRequestInterface extends Collection<PepAttributeInterface>, Serializable {

    /**
     * Returns the attribute calue instance for the given category and id.
     * This code assumes that they may be only one attribute value; otherwise
     * an assertion error is raised. Use getAttributes() to handle bags.
     * 
     * @param category the category for the attribute value to be retrieved.
     * @param id the uri id for the attribute.
     * @return null if none could be found, the PepAttributeInstance instead.
     */
    PepAttributeInterface getAttribute(String category, String id);
    
    Collection<PepAttributeInterface> getAttributes(String category, String id);

    /**
     * gets back a map whose keys are the attribute ids for the category, and
     * the values are collections of attributes (the bags).
     * @param category the category for the attribute values to be retrieved.
     * @return a map of attribute value bags, grouped by id.
     */
    Map<String, Collection<PepAttributeInterface>> getAttributes(String category);

    Collection<PepAttributeInterface> getCategory(String category);
    
    Collection<String> getCategoryIds();

    boolean replace(PepAttributeInterface attribute);
    
    Element toElement() throws Exception;
    
}
