package com.claro.proceso.migracion.pospago.api.services.marshalling;

import com.claro.proceso.migracion.pospago.api.wsdl.CustomDeleteResponse;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@Service
public class XMLService {

    public CustomDeleteResponse unmarshallXMLToDeleteResponse(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CustomDeleteResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(xml);
        return  (CustomDeleteResponse) unmarshaller.unmarshal(reader);
    }
}
