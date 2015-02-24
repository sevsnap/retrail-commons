/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.balana.utils.Constants.PolicyConstants;

/**
 *
 * @author oneadmin
 */
public class PepResponse {

    private static final Logger log = LoggerFactory.getLogger(PepResponse.class);
    protected Element element;

    public enum DecisionEnum {
        Indeterminate, NotApplicable, Deny, Permit
    };

    protected DecisionEnum decision = DecisionEnum.Indeterminate;
    protected String message = "";
    protected List<String> obligations = new ArrayList<>();

    public PepResponse(Document doc) {
        setResponse(doc);
    }

    public PepResponse(Element e) {
        setResponse(e);
    }
    public final void setResponse(Document doc) {
        NodeList nl = doc.getElementsByTagNameNS("*", "Response");
        // NOTE: if the Response has no namespace declared, the following
        // assertion will fail.
        // Example: <Response xmlns="...."> is ok, but <Response> is not.
        assert(nl.getLength() == 1);
        setResponse((Element) nl.item(0));
    }
    
    public final void setResponse(Element e) {
        element = e;
        assert(e != null);
        NodeList nl = element.getElementsByTagNameNS("*", "Decision");
        assert(nl.getLength() == 1);
        String decisionString = nl.item(0).getTextContent();
        setDecision(DecisionEnum.valueOf(decisionString));
        NodeList statusMessages = element.getElementsByTagNameNS("*", "StatusMessage");
        if (statusMessages.getLength() > 0) 
            setMessage(statusMessages.item(0).getTextContent());
        obligations.clear();
        NodeList o = element.getElementsByTagNameNS("*", "Obligation");
        for(int i = o.getLength(); i-- > 0;) {
            // FIXME getAttributeNS("*", PolicyConstants.OBLIGATION_ID) not working
            String value = ((Element)o.item(i)).getAttributeNS(null, PolicyConstants.OBLIGATION_ID);
            assert(value.length() > 0);
            obligations.add(value);
        }
    }
    
    public DecisionEnum getDecision() {
        return decision;
    }

    public void setDecision(DecisionEnum decision) {
        this.decision = decision;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getObligations() {
        return obligations;
    }
    
    public void setObligations(List<String> obligations) {
        this.obligations = obligations;
    }
    
    public Element toXacml3Element() {
        return element;
    }
    
    @Override
    public String toString() {
        String s = getClass().getSimpleName()+" [decision=" + getDecision();
        if (getMessage() != null && getMessage().length() > 0) {
            s += ", message=" + getMessage();
        }
        s += "]";
        return s;
    }
}
