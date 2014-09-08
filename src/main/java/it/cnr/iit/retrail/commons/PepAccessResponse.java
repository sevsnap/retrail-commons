/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.iit.retrail.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author oneadmin
 */
public class PepAccessResponse {

    private static final Log log = LogFactory.getLog(PepAccessRequest.class);
    private final Element element;

    public String message = "";

    public enum DecisionEnum {
        Indeterminate, NotApplicable, Deny, Permit
    };

    public DecisionEnum decision = DecisionEnum.Indeterminate;

    public PepAccessResponse(Document doc) {
        element = (Element) doc.getElementsByTagName("Response").item(0);
        log.warn("PepAccessResponse creo da doc:" + element);
        DomUtils.write(element);
        String decisionString = element.getElementsByTagName("Decision").item(0).getTextContent();
        decision = DecisionEnum.valueOf(decisionString);
        NodeList statusMessages = element.getElementsByTagName("StatusMessage");
        if (statusMessages.getLength() > 0) {
            message = statusMessages.item(0).getTextContent();
        }
    }

    public Element toElement() {
        return element;
    }
}
