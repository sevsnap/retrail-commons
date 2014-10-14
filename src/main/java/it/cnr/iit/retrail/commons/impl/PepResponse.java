/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    protected Collection<String> obligations = new ArrayList<>();

    public PepResponse(Document doc) {
        setResponse(doc);
    }

    public final void setResponse(Document doc) {
        element = (Element) doc.getElementsByTagName("Response").item(0);
        String decisionString = element.getElementsByTagName("Decision").item(0).getTextContent();
        setDecision(DecisionEnum.valueOf(decisionString));
        NodeList statusMessages = element.getElementsByTagName("StatusMessage");
        if (statusMessages.getLength() > 0) 
            setMessage(statusMessages.item(0).getTextContent());
        obligations.clear();
        NodeList o = element.getElementsByTagName("Obligation");
        for(int i = o.getLength(); i-- > 0;)
            obligations.add(((Element)(o.item(i))).getAttribute("ObligationId"));
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

    public Collection<String> getObligations() {
        return obligations;
    }
    
    public void setObligations(Collection<String> obligations) {
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
