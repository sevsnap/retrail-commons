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
import org.wso2.balana.XACMLConstants;
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
    private long ms;

    public PepSession() throws Exception {
        super(DomUtils.read("<Response xmlns=\""+XACMLConstants.XACML_3_0_IDENTIFIER+"\"><Result><Decision>" + DecisionEnum.Permit.name() + "</Decision><StatusMessage></StatusMessage></Result></Response>"));
    }
    
    public PepSession(DecisionEnum decisionEnum, String statusMessage) throws ParserConfigurationException, SAXException, IOException {
        super(DomUtils.read("<Response xmlns=\""+XACMLConstants.XACML_3_0_IDENTIFIER+"\"><Result><Decision>" + decisionEnum.name() + "</Decision><StatusMessage>" + statusMessage + "</StatusMessage></Result></Response>"));
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

    @Override
    public long getMs() {
        return ms;
    }

    @Override
    public void setMs(long ms) {
        this.ms = ms;
    }

    public PepSession(Document doc) throws Exception {
        super(doc);
        copy();
    }

    public PepSession(Element e) throws Exception {
        super(e);
        copy();
    }

    private void copy() throws Exception {
        Element session = (Element) element.getElementsByTagNameNS("*", "Session").item(0);
        if (session != null) {
            setUuid(session.getAttributeNS(null, "uuid"));
            setCustomId(session.getAttributeNS(null, "customId"));
            setStatus(Status.valueOf(session.getAttributeNS(null, "status")));
            String urlString = session.getAttributeNS(null, "uconUrl");
            setUconUrl(urlString.length() == 0 ? null : new URL(urlString));
            String msString = session.getAttributeNS(null, "ms");
            setMs(msString.length() > 0? Long.parseLong(msString) : 0);
        } else {
            setUuid(null);
            setCustomId(null);
            setStatus(Status.UNKNOWN);
            setUconUrl(null);            
            setMs(0);
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
        if (getMs() != 0) {
            session.setAttributeNS(null, "ms", ""+getMs());
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
        if (getMs() != 0) {
            s += ", ms=" + getMs();
        }
        s += "]";
        return s;
    }

}
