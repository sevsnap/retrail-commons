/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.Serializable;
import java.util.Collection;
import org.w3c.dom.Element;

/**
 *
 * @author oneadmin
 */
public interface PepRequestInterface extends Collection<PepAttributeInterface>, Serializable {

    PepAttributeInterface getAttribute(String category, String id);

    Collection<PepAttributeInterface> getCategory(String category);
    
    boolean replace(PepAttributeInterface attribute);
    
    Element toElement() throws Exception;
    
}
