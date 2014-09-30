/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Server implements Runnable {
    public static int watchdogPeriod = 15;
    private Thread watchdogThread;
    
    public final URL myUrl;
    private final WebServer webServer;
    protected static final Logger log = LoggerFactory.getLogger(Server.class);

    /**
     *
     * @param myUrl
     * @param APIClass
     * @throws java.net.UnknownHostException
     * @throws org.apache.xmlrpc.XmlRpcException
     */
    public Server(URL myUrl, Class APIClass) throws UnknownHostException, XmlRpcException {
        this.myUrl = myUrl;
        this.webServer = createWebServer(myUrl, APIClass, getClass().getSimpleName());
    }
    
    public Server(URL myUrl, Class APIClass, String namespace) throws UnknownHostException, XmlRpcException {
        this.myUrl = myUrl;
        webServer = createWebServer(myUrl, APIClass, namespace);
    }
    
    private WebServer createWebServer(URL myUrl, Class APIClass, String namespace) throws UnknownHostException, XmlRpcException {
        InetAddress address = java.net.InetAddress.getByName(myUrl.getHost());
        int port = myUrl.getPort();
        if(port == -1)
            port = myUrl.getDefaultPort();
        if(port == -1)
            port = 80;
        WebServer wServer = new WebServer(port, address);
        XmlRpcServer server = wServer.getXmlRpcServer();
        PropertyHandlerMapping phm;
        phm = new PropertyHandlerMapping();
        log.info("class: "+namespace+", api: "+APIClass);
        phm.addHandler(namespace, APIClass);
        server.setHandlerMapping(phm);

        XmlRpcServerConfigImpl serverConfig
                = (XmlRpcServerConfigImpl) server.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);
        log.info("available xmlrpc methods:");
        for(String method: phm.getListMethods()) 
            log.info(method);
        return wServer;
    }
    
    public void init() throws Exception  {
        // start server 
        webServer.start();
        // start heartbeat
        watchdogThread = new Thread(this);
        watchdogThread.setName(getClass().getSimpleName()+".watchdog");
        watchdogThread.start();
    }
    
    public void forever() {
        try {
            watchdogThread.join();
        } catch (InterruptedException ex) {
            log.warn("exiting");
        }
    }
    
    public void term() throws InterruptedException {
        webServer.shutdown();
        watchdogThread.interrupt();
        watchdogThread.join();
    }

    protected void watchdog() throws InterruptedException {
        log.info("Server.heartbeat(): idle call");
    }
    
    @Override
    public void run() {
        // heartbeat
        while (true) {
            try {
                watchdog();
                Thread.sleep(watchdogPeriod * 1000);
            } catch (InterruptedException ex) {
                log.warn("{} interrupted -- exiting", Thread.currentThread());
                return;
            }
        }
    }
    
}
