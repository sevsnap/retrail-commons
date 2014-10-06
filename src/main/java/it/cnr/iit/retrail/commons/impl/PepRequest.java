/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons.impl;

import it.cnr.iit.retrail.commons.PepAttributeInterface;
import it.cnr.iit.retrail.commons.PepRequestInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.balana.utils.Constants.PolicyConstants;
import org.wso2.balana.utils.PolicyUtils;

import org.wso2.balana.utils.policy.dto.AttributeElementDTO;
import org.wso2.balana.utils.policy.dto.AttributesElementDTO;
import org.wso2.balana.utils.policy.dto.RequestElementDTO;

/**
 *
 * @author oneadmin
 */
public class PepRequest 
extends ArrayList<PepAttributeInterface> 
implements PepRequestInterface {
    // Attributes may be overwritten (they are simply replaced).
    protected static final Logger log = LoggerFactory.getLogger(PepRequest.class);
    private final Map<String, Collection<PepAttributeInterface>> categories;
    
    public static PepRequest newInstance(String subject, String action, String resourceUrl, String issuer) {
        PepRequest request = new PepRequest();
        PepAttribute attribute;
        attribute = PepAttribute.newEmailSubject(subject, issuer);
        request.add(attribute);
        attribute = PepAttribute.newAction(action, issuer);
        request.add(attribute);
        attribute = PepAttribute.newResource(resourceUrl, issuer);
        request.add(attribute);
        return request;
    }

    protected static PepAttributeInterface newAttribute(Element e) {
        return new PepAttribute(e);
    }
    
    public PepRequest() {
        super();
        categories = new HashMap<>();
    }

    public PepRequest(Document doc) {
        super();
        categories = new HashMap<>();
        Element req;
        req = (Element) doc.getElementsByTagName(PolicyConstants.Request.REQUEST_ELEMENT).item(0);
        NodeList children = req.getElementsByTagName(PolicyConstants.ATTRIBUTE);
        for (int i = 0; i < children.getLength(); i++) {
            Element e = (Element) children.item(i);
            PepAttributeInterface a = PepRequest.newAttribute(e);
            add(a);
        }
    }

    @Override
    public final boolean add(PepAttributeInterface attribute) {
        // Since XACML 3.0 organizes categories in <Attributes> blocks, 
        // we group the simple list of attributes in a map by category.

        Collection<PepAttributeInterface> categoryList = categories.get(attribute.getCategory());
        if (categoryList == null) {
            categoryList = new ArrayList<>();
            categories.put(attribute.getCategory(), categoryList);
        }
        for(PepAttributeInterface a: categoryList) {
            if(a.getId().equals(attribute.getId())) {
                log.debug("already present, removing "+attribute);
                categoryList.remove(a);
                super.remove(a);
                break;
            }
        }
        log.debug("adding "+attribute);
        categoryList.add(attribute);
        return super.add(attribute);
    }
    
    @Override
    public boolean remove(Object attribute) {
        throw new UnsupportedOperationException("attribute removal is not allowed");
    }
    
    @Override
    public Collection<PepAttributeInterface> getCategory(String category) {
        return categories.get(category);
    }
    
    @Override
    public PepAttributeInterface getAttribute(String category, String id) {
        Collection<PepAttributeInterface> c = categories.get(category);
        if(c != null)
            for(PepAttributeInterface a: c)
                if(id.equals(a.getId()) && category.equals(a.getCategory()))
                    return a;
        return null;
    }
    
    @Override
    public Element toElement() throws Exception {
        // we now build the request, using balana as much as possible.
        RequestElementDTO requestElementDTO = new RequestElementDTO();
           
        List<AttributesElementDTO> attributesElementDTOs = new ArrayList<>();
        requestElementDTO.setCombinedDecision(false);
        requestElementDTO.setReturnPolicyIdList(false);
        requestElementDTO.setMultipleRequest(false);
        for (String category : categories.keySet()) {
            AttributesElementDTO attributesElementDTO = new AttributesElementDTO();
            List<AttributeElementDTO> elementDTOs = new ArrayList<>();
            attributesElementDTO.setCategory(category);
            for (PepAttributeInterface entry : categories.get(category)) {
                AttributeElementDTO elementDTO = new AttributeElementDTO();
                elementDTO.setAttributeId(entry.getId());
                elementDTO.setDataType(entry.getType());
                elementDTO.setIncludeInResult(false);
                elementDTO.setIssuer(entry.getIssuer());
                elementDTO.setAttributeValues(Arrays.asList(entry.getValue()));
                elementDTOs.add(elementDTO);
            }
            attributesElementDTO.setAttributeElementDTOs(elementDTOs);
            attributesElementDTOs.add(attributesElementDTO);
        }
        requestElementDTO.setAttributesElementDTOs(attributesElementDTOs);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element root = PolicyUtils.createRequestElement(requestElementDTO, doc);
        //doc.setDocumentURI(XACMLConstants.REQUEST_CONTEXT_3_0_IDENTIFIER);
        return root;
    }

}
