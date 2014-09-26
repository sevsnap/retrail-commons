/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */

package it.cnr.iit.retrail.commons;

import java.util.ArrayList;
import java.util.Arrays;
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
public class PepAccessRequest extends ArrayList<PepRequestAttribute> {
    // Attributes may be overwritten (they are simply replaced).
    protected static final Logger log = LoggerFactory.getLogger(PepAccessRequest.class);
    
    public static PepAccessRequest newInstance(String subject, String action, String resourceUrl, String issuer) {
        PepAccessRequest request = new PepAccessRequest();
        PepRequestAttribute attribute;
        attribute = PepRequestAttribute.newEmailSubject(subject, issuer);
        request.add(attribute);
        attribute = PepRequestAttribute.newAction(action, issuer);
        request.add(attribute);
        attribute = PepRequestAttribute.newResource(resourceUrl, issuer);
        request.add(attribute);
        return request;
    }

    public PepAccessRequest() {
        super();
    }

    public PepAccessRequest(Document doc) {
        super();
        Element req;
        req = (Element) doc.getElementsByTagName(PolicyConstants.Request.REQUEST_ELEMENT).item(0);
        NodeList children = req.getElementsByTagName(PolicyConstants.ATTRIBUTE);
        for (int i = 0; i < children.getLength(); i++) {
            Element e = (Element) children.item(i);
            PepRequestAttribute a = new PepRequestAttribute(e);
            add(a);
        }
    }

    @Override
    public boolean add(PepRequestAttribute attribute) {
        // TODO inefficient implementation
        for(PepRequestAttribute a: this) {
            if(a.category.equals(attribute.category) && a.id.equals(attribute.id)) {
                log.debug("already present, removing "+attribute);
                super.remove(a);
                break;
            }
        }
        log.debug("adding "+attribute);
        return super.add(attribute);
    }
    
    @Override
    public boolean remove(Object attribute) {
        throw new UnsupportedOperationException("attribute removal is not allowed");
    }
    
    public Map<String, List<PepRequestAttribute>> getCategories() {
        // Since XACML 3.0 organizes categories in <Attributes> blocks, 
        // we group the simple list of attributes in a map by category.
        Map<String, List<PepRequestAttribute>> categories = new HashMap<>();
        for (PepRequestAttribute entry : this) {
            List<PepRequestAttribute> categoryList = categories.get(entry.category);
            if (categoryList == null) {
                categoryList = new ArrayList<>();
                categories.put(entry.category, categoryList);
            }
            categoryList.add(entry);
        }
        return categories;
    }
    
    public Element toElement() throws Exception {
        // Since XACML 3.0 organizes categories in <Attributes> blocks, 
        // we group the simple list of attributes in a map by category.
        Map<String, List<PepRequestAttribute>> categories = getCategories();
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
            for (PepRequestAttribute entry : categories.get(category)) {
                AttributeElementDTO elementDTO = new AttributeElementDTO();
                elementDTO.setAttributeId(entry.id);
                elementDTO.setDataType(entry.type);
                elementDTO.setIncludeInResult(false);
                elementDTO.setIssuer(entry.issuer);
                elementDTO.setAttributeValues(Arrays.asList(entry.value));
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
