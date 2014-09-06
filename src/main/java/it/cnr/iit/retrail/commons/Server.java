/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.cnr.iit.retrail.commons;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;


public class Server {
    protected final org.apache.xmlrpc.server.XmlRpcServer server;
  
    /**
     *
     * @param myUrl
     * @param APIClass
     * @throws java.net.UnknownHostException
     * @throws org.apache.xmlrpc.XmlRpcException
     */
    public Server(URL myUrl, Class APIClass) throws UnknownHostException, XmlRpcException, IOException {  
        // start server 
        InetAddress address = java.net.InetAddress.getByName(myUrl.getHost());
        int port = myUrl.getPort();
        if(port == -1)
            port = myUrl.getDefaultPort();
        if(port == -1)
            port = 80;
        WebServer webServer = new WebServer(port, address);
        server = webServer.getXmlRpcServer();
        PropertyHandlerMapping phm;
        phm = new PropertyHandlerMapping();
        phm.addHandler(getClass().getSimpleName(), APIClass);
        server.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig
                = (XmlRpcServerConfigImpl) server.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        webServer.start();
    }
    
}
