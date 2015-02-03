/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import static it.cnr.iit.retrail.commons.Client.log;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.apache.xmlrpc.webserver.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class HttpsWebServer extends WebServer {
    protected static final Logger log = LoggerFactory.getLogger(HttpsWebServer.class);
    protected static SSLContext sslContext;
    
    public HttpsWebServer(int pPort) throws Exception {
        super(pPort);
        sslContext = SSLContext.getDefault();
    }
    
    public HttpsWebServer(int pPort, InetAddress inetAddress) throws Exception {
        super(pPort, inetAddress);
        sslContext = SSLContext.getDefault();
    }
    
    @Override
    protected ServerSocket createServerSocket(int port, int backlog, InetAddress addr) throws IOException {
        log.warn("creating secure server socket at port {}", port);
        assert(sslContext != null);
        ServerSocketFactory ssocketFactory = sslContext.getServerSocketFactory();
        ServerSocket ssocket = ssocketFactory.createServerSocket(port, backlog, addr);
        return ssocket;
    }
   
}
