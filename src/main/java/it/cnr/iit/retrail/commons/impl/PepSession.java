/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons.impl;

import it.cnr.iit.retrail.commons.DomUtils;
import it.cnr.iit.retrail.commons.PepSessionInterface;
import it.cnr.iit.retrail.commons.Status;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author oneadmin
 */
public class PepSession extends PepResponse implements PepSessionInterface {

    private String uuid, customId;
    private URL uconUrl;
    private Map<String,Object> localInfo = new HashMap<>();
    private Status status = Status.UNKNOWN;

    public PepSession() throws Exception {
        super(DomUtils.read("<Response><Result><Decision>" + DecisionEnum.Permit.name() + "</Decision><StatusMessage></StatusMessage></Result></Response>"));
    }
    
    public PepSession(DecisionEnum decisionEnum, String statusMessage) throws ParserConfigurationException, SAXException, IOException {
        super(DomUtils.read("<Response><Result><Decision>" + decisionEnum.name() + "</Decision><StatusMessage>" + statusMessage + "</StatusMessage></Result></Response>"));
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public URL getUconUrl() {
        return uconUrl;
    }

    @Override
    public void setUconUrl(URL uconUrl) {
        this.uconUrl = uconUrl;
    }

    public PepSession(Document doc) throws Exception {
        super(doc);
        copy();
    }

    private void copy() throws Exception {
        Element session = (Element) element.getElementsByTagName("Session").item(0);
        if (session != null) {
            setUuid(session.getAttributeNS(null, "uuid"));
            setCustomId(session.getAttributeNS(null, "customId"));
            setStatus(Status.valueOf(session.getAttributeNS(null, "status")));
            String urlString = session.getAttributeNS(null, "uconUrl");
            setUconUrl(urlString.length() == 0 ? null : new URL(urlString));
        } else {
            setUuid(null);
            setCustomId(null);
            setStatus(Status.UNKNOWN);
            setUconUrl(null);            
        }
    }

    @Override
    public Map<String, Object> getLocalInfo() {
        return localInfo;
    }

    @Override
    public void setLocalInfo(Map<String, Object> localInfo) {
        this.localInfo = localInfo;
    }
    
    @Override
    public Element toXacml3Element() {
        Element root = super.toXacml3Element();
        Element session = element.getOwnerDocument().createElementNS(null, "Session");
        if (getUuid() != null) {
            session.setAttributeNS(null, "uuid", getUuid());
        }
        if (getCustomId() != null) {
            session.setAttributeNS(null, "customId", getCustomId());
        }
        if (getUconUrl() != null) {
            session.setAttributeNS(null, "uconUrl", getUconUrl().toString());
        }
        if (getStatus() != null) {
            session.setAttributeNS(null, "status", getStatus().name());
        }
        if (session.hasAttributes()) {
            root.appendChild(session);
        }
        return root;
    }

    @Override
    public String toString() {
        String s = getClass().getSimpleName()+" [uuid=" + getUuid();
        if (getCustomId() != null && !getCustomId().equals(getUuid())) {
            s += ", customId=" + getCustomId();
        }
        s += ", decision=" + getDecision();
        if (getMessage() != null && getMessage().length() > 0) {
            s += ", message=" + getMessage();
        }
        s += ", status=" + getStatus();
        if (getUconUrl() != null) {
            s += ", uconUrl=" + getUconUrl();
        }
        s += "]";
        return s;
    }

}
