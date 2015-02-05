/*
 * CNR - IIT
 * Coded by: 2015 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.File;
import javax.net.ssl.SSLContext;

/**
 *
 * @author oneadmin
 */
public interface RecorderInterface {

    void startRecording(File outputFile) throws Exception;
    
    void continueRecording(File outputFile) throws Exception;
    
    boolean isRecording();

    void stopRecording();

    SSLContext trustAllPeers() throws Exception;
    
}
