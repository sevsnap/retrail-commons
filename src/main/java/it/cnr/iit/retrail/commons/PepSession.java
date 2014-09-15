/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.cnr.iit.retrail.commons;

import java.io.IOException;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author oneadmin
 */
public class PepSession extends PepAccessResponse {
    
    private String id, cookie;
    
    public enum Status {
        TRY, ONGOING, REVOKED
    }
    
    Status status = Status.TRY;

    public PepSession(DecisionEnum decisionEnum, String statusMessage) throws ParserConfigurationException, SAXException, IOException {   
        super(DomUtils.read("<Response><Result><Decision>"+decisionEnum.name()+"</Decision><StatusMessage>"+statusMessage+"</StatusMessage></Result></Response>"));
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PepSession(Document doc) {
        super(doc);
        Element session = (Element) element.getElementsByTagName("Session").item(0);
        if (session != null) {
            this.id = session.getAttributeNS(null, "id");
            this.cookie = session.getAttributeNS(null, "cookie");
        } 
    }

    public void addSession(String id, String cookie, Status status) {
        Element session = element.getOwnerDocument().createElementNS(null, "Session");
        session.setAttributeNS(null, "id", id);
        session.setAttributeNS(null, "cookie", cookie);
        session.setAttributeNS(null, "status", status.toString());
        this.id = id;
        this.cookie = cookie;
        this.status = status;
        element.appendChild(session);
    }
    
    @Override
    public String toString() {
        return "PepSession [id="+id+", cookie="+cookie+", decision="+decision+", message="+message+"]";
    }
    
    @Override
    public int hashCode() {
        int hash = id.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PepSession other = (PepSession) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
