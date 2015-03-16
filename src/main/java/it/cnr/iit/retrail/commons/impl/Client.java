/*
 * CNR - IIT
 * Coded by: 2014-2015 Enrico "KMcC;) Carniani
 * TODO: Use XmlRpcLiteHttp14TransportFactory
 */

package it.cnr.iit.retrail.commons.impl;

import it.cnr.iit.retrail.commons.RecorderInterface;
import it.cnr.iit.retrail.commons.HttpsTrustManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
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

public class Client extends XmlRpcClient implements RecorderInterface {
    protected final Logger log;
    FileWriter out;
    private long millis;
    protected final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

    static private final String openTag = "<retrailRecorder>";
    static private final String closeTag = "</retrailRecorder>";

    private class MessageLoggingTransport extends XmlRpcSunHttpTransport {
 
        public MessageLoggingTransport(final XmlRpcClient pClient) {
            super(pClient);
        }

        @Override
        protected void writeRequest(final XmlRpcStreamTransport.ReqWriter pWriter) throws IOException, XmlRpcException, SAXException {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pWriter.write(baos);
            long delta = System.currentTimeMillis() - millis;
            String req = "<record ms=\""+delta+"\">\n"+baos.toString().replaceFirst("<\\?.*?\\?>", "")+"\n";
            try {
                out.append(req);
            }
            catch(IOException e) {
                log.error("while appending to log file: {}", e);
            }
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
                out.append(req);
                out.append("\n</record>\n");
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
    
    @Override
    public synchronized void startRecording(File outputFile) throws Exception {
        if(out != null)
            throw new RuntimeException("already recording session");
        log.info("switching log recorder ON. Logging to {}", outputFile.getAbsolutePath());
        out = new FileWriter(outputFile, false);
        out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+openTag+"\n");
        millis = System.currentTimeMillis();
        setTransportFactory(new TransportFactory(this));
    }
    
    @Override
    public void continueRecording(File target, long millis) throws Exception {
        log.debug("called for {}", target.getAbsolutePath());
        this.millis = millis;
        if(out == null) {
            if (target.exists()) {
                log.debug("append data logging to {}", target.getAbsolutePath());
                RandomAccessFile file = new RandomAccessFile(target, "rwd");
                long newPos = target.length()-closeTag.length();
                if(newPos >= 0) {
                    file.seek(newPos); // Set the pointer to closeTag
                    if(file.readLine().equals(closeTag)) {               
                        file.setLength(newPos);
                        log.info("removing trailing {}", closeTag);
                    }
                }
                file.close();
                log.debug("opening file {} in append mode", target.getAbsolutePath());
                out = new FileWriter(target, true);
                assert(out != null);
                setTransportFactory(new TransportFactory(this));
            } else {
                log.debug("target {} does not exist, creating new file", target.getAbsolutePath());
                startRecording(target);
            }
        }
    }

    @Override
    public boolean isRecording() {
        return out != null;
    }
    
    @Override
    public synchronized void stopRecording() {
        if(out != null) {
            log.info("switching log recorder OFF.");
            try {
                out.append(closeTag);
                out.close();
            } catch (IOException ex) {
                log.error("IO exception: {}", ex.getMessage());
            }
            out = null;
        }
        setTransportFactory(new XmlRpcSunHttpTransportFactory(this));
    } 
  
    @Override
    public SSLContext trustAllPeers() throws Exception {
        log.warn("creating fake trust context (it does not validate certificate chains)");
        // Install the all-trusting trust manager
        SSLContext contextForUntrusted = SSLContext.getInstance("SSL");
        contextForUntrusted.init(null, HttpsTrustManager.getTrustManagers(), new java.security.SecureRandom());
        // FIXME make it local!
        // Since we're using the XmlRpcSunHttpTransportFactory, that in turn
        // uses the java URL HTTP connection, we can simply set the defaults.
        HttpsURLConnection.setDefaultSSLSocketFactory(contextForUntrusted.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(HttpsTrustManager.getHostnameVerifier());
        return contextForUntrusted;
    }
    
    public long getMillis() {
        return millis;
    }

    public double getConnectionTimeout() {
        return config.getConnectionTimeout()/1000.0;
    }

    public void setConnectionTimeout(double connectionTimeout) {
        log.info("setting connection timeout={}s", connectionTimeout);
        config.setConnectionTimeout((int)(connectionTimeout*1000));
        setConfig(config);
    }

    public double getReplyTimeout() {
        return config.getReplyTimeout()/1000.0;
    }

    public void setReplyTimeout(double replyTimeout) {
        log.info("setting reply timeout={}s", replyTimeout);
        config.setReplyTimeout((int)(replyTimeout*1000));
        setConfig(config);
    }

    public void setServerURL(URL url) {
        config.setServerURL(url);
        setConfig(config);
    }
    
    public Client(URL serverUrl) throws Exception {
        super();
        log = LoggerFactory.getLogger(getClass());
        if(serverUrl != null)
            config.setServerURL(serverUrl);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(2 * 1000);
        config.setReplyTimeout(8 * 1000);   
        setConfig(config);
        stopRecording();
    }
}
