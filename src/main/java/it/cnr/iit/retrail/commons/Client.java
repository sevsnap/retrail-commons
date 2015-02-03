/*
 * CNR - IIT
 * Coded by: 2014-2015 Enrico "KMcC;) Carniani
 * TODO: Use XmlRpcLiteHttp14TransportFactory
 */

package it.cnr.iit.retrail.commons;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcStreamTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.common.XmlRpcStreamRequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class Client extends XmlRpcClient {
    protected static final Logger log = LoggerFactory.getLogger(Client.class);
    FileOutputStream out;
    long millis;
    
    private class MessageLoggingTransport extends XmlRpcSunHttpTransport {
 
        public MessageLoggingTransport(final XmlRpcClient pClient) {
            super(pClient);
        }

        @Override
        protected void writeRequest(final XmlRpcStreamTransport.ReqWriter pWriter) throws IOException, XmlRpcException, SAXException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pWriter.write(baos);
            long delta = System.currentTimeMillis() - millis;
            out.write(("<record ms=\""+delta+"\">\n").getBytes());
            String req = baos.toString().replaceFirst("<\\?.*?\\?>", "");
            out.write(req.getBytes());
            out.write(("\n").getBytes());
            super.writeRequest(pWriter);
        }

        @Override
        protected Object readResponse(XmlRpcStreamRequestConfig pConfig, InputStream pStream) throws XmlRpcException {
            final StringBuffer sb = new StringBuffer();
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(pStream));
                String line = reader.readLine();
                while(line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }

            }
            catch(final IOException e) {
                log.error("while reading server response: {}", e);
            }
 
            try {
                String req = sb.toString().replaceFirst("<\\?.*?\\?>", "");
                out.write(req.getBytes());
                out.write("\n</record>\n".getBytes());
            }
            catch(final IOException e) {
                log.error("while writing to local file: {}", e);
            }
            final ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
            return super.readResponse(pConfig, bais);
        }
    }
    
    private class TransportFactory extends XmlRpcSunHttpTransportFactory {
        private XmlRpcTransport singleton;

        public TransportFactory(XmlRpcClient pClient) {
            super(pClient);
        }
       
        @Override
        public XmlRpcTransport getTransport() {
            if(singleton == null) {
                log.info("setting retrail transport hook");
                singleton = new MessageLoggingTransport(Client.this);
            }
            return singleton;
        }    
    }
    
    public synchronized void startRecording(File outputFile) throws Exception {
        stopRecording();
        log.info("switching log recorder ON. Logging to {}", outputFile.getAbsolutePath());
        out = new FileOutputStream(outputFile);
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<retrailRecorder>\n".getBytes());
        millis = System.currentTimeMillis();
        setTransportFactory(new TransportFactory(this));
        
    }

    public synchronized void stopRecording() {
        
        if(out != null) {
            log.info("switching log recorder OFF.");
            try {
                out.write("</retrailRecorder>".getBytes());
                out.close();
            } catch (IOException ex) {
                log.error("IO exception: {}", ex.getMessage());
            }
            out = null;
        }
        millis = 0;
        setTransportFactory(new XmlRpcSunHttpTransportFactory(this));
    } 
  
    public void trustAllServers() throws Exception {
        log.warn("creating fake trust context (it does not validate certificate chains)");
        // Install the all-trusting trust manager
        SSLContext contextForUntrusted = SSLContext.getInstance("SSL");
        contextForUntrusted.init(null, HttpsTrustManager.getTrustManagers(), new java.security.SecureRandom());
        // Since we're using the XmlRpcSunHttpTransportFactory, that in turn
        // uses the java URL HTTP connection, we can simply set the defaults.
        HttpsURLConnection.setDefaultSSLSocketFactory(contextForUntrusted.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(HttpsTrustManager.getHostnameVerifier());
    }
    
    public Client(URL serverUrl) throws Exception {
        super();
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(serverUrl);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);
        
        stopRecording();

        // set configuration
        setConfig(config);
    }

}
