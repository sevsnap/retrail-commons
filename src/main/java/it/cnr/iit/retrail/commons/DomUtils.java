/*
 * CNR - IIT
 * Coded by: 2014-2015 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author oneadmin
 */
public class DomUtils {

    private static DocumentBuilderFactory dbFactory;

    public static void write(Node n, OutputStream out) throws TransformerException {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(n);

        // Output to console for testing
        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }

    public static String toString(Node n) {
        String xml = null;
        try {

            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(n), new StreamResult(writer));
            xml = writer.toString();
        } catch (TransformerException ex) {
            throw new RuntimeException("Cannot transform to string");
        }
        return xml;
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        if (dbFactory == null) {
            dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
        }
        return dbFactory.newDocumentBuilder();
    }

    public static Document newDocument() throws ParserConfigurationException {
        return getDocumentBuilder().newDocument();
    }

    public static Document read(String data) throws ParserConfigurationException, SAXException, IOException {
        Document doc = getDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(data.getBytes("utf-8"))));
        return doc;
    }

    public static Document read(File inFile) throws ParserConfigurationException, SAXException, IOException {
        Document doc = getDocumentBuilder().parse(inFile);
        return doc;
    }

    public static Document read(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        Document doc = getDocumentBuilder().parse(is);
        return doc;
    }

    public static Element findClosestParentWithTag(String theTag, Node startingFromThis) {
        Element rv = null;
        if (startingFromThis == null) {
            rv = null;
        } else if (startingFromThis instanceof Element && ((Element) startingFromThis).getTagName().equals(theTag)) {
            rv = (Element) startingFromThis;
        } else {
            rv = findClosestParentWithTag(theTag, startingFromThis.getParentNode());
        }
        return rv;
    }

    public static List<Element> findDirectChildrenWithTagNS(String ns, String theTag, Node startingFromThis) {
        List<Element> al = new ArrayList<>();
        NodeList nl = startingFromThis.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element e = (Element) n;
                if(e.getNamespaceURI().equals(ns) && e.getLocalName().equals(theTag))
                    al.add((Element) n);
            }
        }
        return al;
    }
    
    private static final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
            @Override
            public Object convert(String value, Class clazz) {
                  if (clazz.isEnum()){
                       return Enum.valueOf(clazz, value);
                  }else{
                       return super.convert(value, clazz);
                  }
           }
        });
    
    public static void setPropertyOnObjectNS(String ns, String theTag, Node startingFromThis, Object o) {
        List<Element> pl = findDirectChildrenWithTagNS(ns, theTag, startingFromThis);
        for (Element propertyElement : pl) {
            String propertyName = propertyElement.getAttributeNS(null, "name");
            String propertyValue = propertyElement.getTextContent();
            try {
                beanUtilsBean.setProperty(o, propertyName, propertyValue);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("cannot set property " + propertyName + " for " + o + ": " + e);
            }
        }
    }

}
