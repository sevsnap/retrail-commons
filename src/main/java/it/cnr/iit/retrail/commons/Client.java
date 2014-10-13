/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons;

import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class Client extends XmlRpcClient {

    public Client(URL serverUrl) {
        super();

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(serverUrl);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);
        // use Commons HttpClient as transport
        setTransportFactory( new XmlRpcCommonsTransportFactory(this));
        // set configuration
        setConfig(config);
    }

}
