/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.cnr.iit.retrail.commons;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    
    private String uuid, customId;
    private URL uconUrl;
    
    public enum Status {
        UNKNOWN, TRY, ONGOING, REVOKED, DELETED
    }
    
    private Status status = Status.UNKNOWN;

    public PepSession(DecisionEnum decisionEnum, String statusMessage) throws ParserConfigurationException, SAXException, IOException {   
        super(DomUtils.read("<Response><Result><Decision>"+decisionEnum.name()+"</Decision><StatusMessage>"+statusMessage+"</StatusMessage></Result></Response>"));
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public URL getUconUrl() {
        return uconUrl;
    }

    public void setUconUrl(URL uconUrl) {
        this.uconUrl = uconUrl;
    }

    public PepSession(Document doc) throws MalformedURLException {
        super(doc);
        Element session = (Element) element.getElementsByTagName("Session").item(0);
        if (session != null) {
            this.uuid = session.getAttributeNS(null, "uuid");
            this.customId = session.getAttributeNS(null, "customId");
            this.status = Status.valueOf(session.getAttributeNS(null, "status"));
            String urlString = session.getAttributeNS(null, "uconUrl");
            this.uconUrl = urlString == null? null : new URL(urlString);
        } 
    }

    public void addSessionElement(String uuid, String customId, Status status, URL url) {
        Element session = element.getOwnerDocument().createElementNS(null, "Session");
        session.setAttributeNS(null, "uuid", uuid);
        session.setAttributeNS(null, "customId", customId);
        session.setAttributeNS(null, "uconUrl", url.toString());
        session.setAttributeNS(null, "status", status.toString());
        this.uuid = uuid;
        this.customId = customId;
        this.status = status;
        this.uconUrl = url;
        element.appendChild(session);
    }
    
    @Override
    public String toString() {
        return "PepSession [uuid="+uuid+", customId="+customId+", decision="+decision+", message="+message+", uconUrl="+uconUrl+"]";
    }
    
    @Override
    public int hashCode() {
        int hash = uuid.hashCode();
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
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return true;
    }

}
