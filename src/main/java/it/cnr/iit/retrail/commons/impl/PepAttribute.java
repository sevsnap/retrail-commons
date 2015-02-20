/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons.impl;

import it.cnr.iit.retrail.commons.PepAttributeInterface;
import java.util.Date;
import org.w3c.dom.Element;
import org.wso2.balana.utils.Constants.PolicyConstants;

/**
 *
 * @author oneadmin
 */
public class PepAttribute implements PepAttributeInterface {

    private final String id, type, issuer, category, factory;
    private String value;
    private PepAttribute parent;
    private Date expires;
    
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
        static final public String BOOLEAN = PolicyConstants.DataType.BOOLEAN;
    }
    
    public static PepAttribute newSubject(String subject, String issuer) {
        return new PepAttribute(IDS.SUBJECT, 
                                       DATATYPES.STRING, 
                                       subject, 
                                       issuer,
                                       PepAttribute.CATEGORIES.SUBJECT);
    }
    
    public static PepAttribute newEmailSubject(String email, String issuer) {
        return new PepAttribute(IDS.SUBJECT, 
                                       DATATYPES.EMAIL, 
                                       email, 
                                       issuer,
                                       PepAttribute.CATEGORIES.SUBJECT);
    }
    
    public static PepAttribute newAction(String action, String issuer) {
        return new PepAttribute(IDS.ACTION, 
                                       DATATYPES.STRING, 
                                       action, 
                                       issuer,
                                       PepAttribute.CATEGORIES.ACTION);
    }
    
    public static PepAttribute newResource(String resource, String issuer) {
        return new PepAttribute(IDS.RESOURCE, 
                                       DATATYPES.URI, 
                                       resource, 
                                       issuer,
                                       PepAttribute.CATEGORIES.RESOURCE);
    }
    
    public PepAttribute(String id, String type, String value, String issuer, String category) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.issuer = issuer;
        this.category = category;
        this.factory = null;
        this.parent = null;
    }
    
    public PepAttribute(String id, String type, String value, String issuer, String category, String factory) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.issuer = issuer;
        this.category = category;
        this.factory = factory;
        this.parent = null;
    }

    public PepAttribute(Element attributeValueElement) {
        // XACML 3.0 format
        assert(attributeValueElement.getTagName().equals("AttributeValue"));
        Element attributeElement = (Element) attributeValueElement.getParentNode();
        Element rootElement = (Element) attributeElement.getParentNode();
        category = rootElement.getAttribute(PolicyConstants.CATEGORY);
        id = attributeElement.getAttribute(PolicyConstants.ATTRIBUTE_ID);
        issuer = attributeElement.getAttribute(PolicyConstants.ISSUER);
        type = attributeValueElement.getAttribute(PolicyConstants.DATA_TYPE);
        value = attributeValueElement.getTextContent();
        factory = null;
        parent = null;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public PepAttributeInterface getParent() {
        return parent;
    }

    public void setParent(PepAttributeInterface parent) {
        this.parent = (PepAttribute) parent;
    }

    @Override
    public Date getExpires() {
        return expires;
    }

    @Override
    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getFactory() {
        return factory;
    }
    
    @Override
    public String toString() {
        return "PepRequestAttribute [id="+id+", value="+value+", factory="+factory+"]";
    }

}
