/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.xmlrpc.webserver.WebServer;

/**
 *
 * @author oneadmin
 */
public class HttpsWebServer extends WebServer {

    public HttpsWebServer(int pPort) {
        super(pPort);
    }
    
    public HttpsWebServer(int pPort, InetAddress inetAddress) {
        super(pPort, inetAddress);
    }
    
    @Override
    protected ServerSocket createServerSocket(int port, int backlog, InetAddress addr) throws IOException {
        ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
        ServerSocket ssocket = ssocketFactory.createServerSocket(port, backlog, addr);
        return ssocket;
    }
}
