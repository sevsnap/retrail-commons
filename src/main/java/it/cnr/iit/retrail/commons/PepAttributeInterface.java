/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.Serializable;
import java.util.Date;
import org.w3c.dom.Element;

public interface PepAttributeInterface extends Serializable {

    String getCategory();

    Date getExpires();

    String getFactory();

    String getId();

    String getIssuer();

    String getType();

    String getValue();

    void setExpires(Date expires);

    void setValue(String value);
    
}
