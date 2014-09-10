/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.cnr.iit.retrail.commons;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author oneadmin
 */
public class PepSession extends PepAccessResponse {
    
    private String id, cookie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public PepSession(Document doc) {
        super(doc);
        Element session = (Element) element.getElementsByTagName("Session").item(0);
        if (session != null) {
            this.id = session.getAttributeNS(null, "id");
            this.cookie = session.getAttributeNS(null, "cookie");
        } 
    }

    public void addSession(String id, String cookie) {
        Element session = element.getOwnerDocument().createElementNS(null, "Session");
        session.setAttributeNS(null, "id", id);
        session.setAttributeNS(null, "cookie", cookie);
        this.id = id;
        this.cookie = cookie;
        element.appendChild(session);
        System.out.println("PepAccessResponse.addSessionInfo:");
        DomUtils.write(element);
    }
    
    @Override
    public String toString() {
        return "PepSession [id="+id+", cookie="+cookie+"]";
    }
}
