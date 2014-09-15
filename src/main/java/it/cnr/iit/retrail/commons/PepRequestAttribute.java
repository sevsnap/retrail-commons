/*
 */
package it.cnr.iit.retrail.commons;

import java.util.Date;
import org.w3c.dom.Element;
import org.wso2.balana.utils.Constants.PolicyConstants;

/**
 *
 * @author oneadmin
 */
public class PepRequestAttribute {

    public final String id, type, value, issuer, category;
    public Date expires;
    public String factory;
    
    public static class CATEGORIES {
        static final public String SUBJECT = PolicyConstants.SUBJECT_CATEGORY_URI;
        static final public String ACTION = PolicyConstants.ACTION_CATEGORY_URI;
        static final public String RESOURCE = PolicyConstants.RESOURCE_CATEGORY_URI;
    };
    
    public static class IDS {
        static final public String SUBJECT = PolicyConstants.SUBJECT_ID_DEFAULT;
        static final public String ACTION = PolicyConstants.ACTION_ID;
        static final public String RESOURCE = PolicyConstants.RESOURCE_ID;
    };
    
    public static class DATATYPES {
        static final public String STRING = PolicyConstants.DataType.STRING;
        //static final public String URI = PolicyConstants.DataType.ANY_URI;
        static final public String URI = PolicyConstants.DataType.STRING;   //FIXME
        //static final public String EMAIL = PolicyConstants.DataType.RFC;
        static final public String EMAIL = PolicyConstants.DataType.STRING;   //FIXME
    }
    
    public static PepRequestAttribute newSubject(String subject, String issuer) {
        return new PepRequestAttribute(IDS.SUBJECT, 
                                       DATATYPES.STRING, 
                                       subject, 
                                       issuer,
                                       PepRequestAttribute.CATEGORIES.SUBJECT);
    }
    
    public static PepRequestAttribute newEmailSubject(String email, String issuer) {
        return new PepRequestAttribute(IDS.SUBJECT, 
                                       DATATYPES.EMAIL, 
                                       email, 
                                       issuer,
                                       PepRequestAttribute.CATEGORIES.SUBJECT);
    }
    
    public static PepRequestAttribute newAction(String action, String issuer) {
        return new PepRequestAttribute(IDS.ACTION, 
                                       DATATYPES.STRING, 
                                       action, 
                                       issuer,
                                       PepRequestAttribute.CATEGORIES.ACTION);
    }
    
    public static PepRequestAttribute newResource(String resource, String issuer) {
        return new PepRequestAttribute(IDS.RESOURCE, 
                                       DATATYPES.URI, 
                                       resource, 
                                       issuer,
                                       PepRequestAttribute.CATEGORIES.RESOURCE);
    }
    
    public PepRequestAttribute(String id, String type, String value, String issuer, String category) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.issuer = issuer;
        this.category = category;
    }

    public PepRequestAttribute(Element attributeElement) {
        // XACML 3.0 format
        Element rootElement = (Element) attributeElement.getParentNode();
        category = rootElement.getAttribute(PolicyConstants.CATEGORY);
        id = attributeElement.getAttribute(PolicyConstants.ATTRIBUTE_ID);
        issuer = attributeElement.getAttribute(PolicyConstants.ISSUER);
        Element attributeValue = (Element) attributeElement.getElementsByTagName(PolicyConstants.ATTRIBUTE_VALUE).item(0);
        type = attributeValue.getAttribute(PolicyConstants.DATA_TYPE);
        value = attributeValue.getTextContent();
    }

    public String toString() {
        return "PepRequestAttribute [id="+id+", value="+value+"]";
    }

}
