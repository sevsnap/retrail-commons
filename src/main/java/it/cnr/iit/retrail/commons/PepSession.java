/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
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

        UNKNOWN, TRY, ONGOING, REVOKED, DELETED, REJECTED
    }

    private Status status = Status.UNKNOWN;

    public PepSession(DecisionEnum decisionEnum, String statusMessage) throws ParserConfigurationException, SAXException, IOException {
        super(DomUtils.read("<Response><Result><Decision>" + decisionEnum.name() + "</Decision><StatusMessage>" + statusMessage + "</StatusMessage></Result></Response>"));
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
            this.uconUrl = urlString.length() == 0 ? null : new URL(urlString);
        }
    }

    @Override
    public Element toXacml3Element() {
        Element root = super.toXacml3Element();
        Element session = element.getOwnerDocument().createElementNS(null, "Session");
        if (uuid != null) {
            session.setAttributeNS(null, "uuid", uuid);
        }
        if (customId != null) {
            session.setAttributeNS(null, "customId", customId);
        }
        if (uconUrl != null) {
            session.setAttributeNS(null, "uconUrl", uconUrl.toString());
        }
        if (status != null) {
            session.setAttributeNS(null, "status", status.name());
        }
        if (session.hasAttributes()) {
            root.appendChild(session);
        }
        return root;
    }

    @Override
    public String toString() {
        String s = "PepSession [uuid=" + uuid;
        if (customId != null && !customId.equals(uuid)) {
            s += ", customId=" + customId;
        }
        s += ", decision=" + decision;
        if (message != null && message.length() > 0) {
            s += ", message=" + message;
        }
        s += ", status=" + status;
        if (uconUrl != null) {
            s += ", uconUrl=" + uconUrl;
        }
        s += "]";
        return s;
    }

}
