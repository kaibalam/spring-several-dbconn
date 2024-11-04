package com.claro.proceso.migracion.pospago.api.config;

import com.claro.proceso.migracion.pospago.api.utils.KflConstants;
import lombok.extern.slf4j.Slf4j;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@Slf4j
public class SoapClient {

    public static String convertStringToString(InputStream is){
        Scanner sc = new Scanner(is).useDelimiter("\\A");
        return sc.hasNext()? sc.next() : "";
    }

    public String consumeTheService(String SOAPXML , String APINAME, String serviceUrl){
        log.info("APINAME {}",APINAME);
        String result = null;
        try {
            URL url = new URL(serviceUrl);
            log.info(url.toString());
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", KflConstants.CONTENT_TYPE_TEXT_XML);
            conn.setRequestProperty(APINAME, serviceUrl + KflConstants.DELETE_SUBSCRIBER_URL);
            log.info("Sending the envelop to server");

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(SOAPXML.getBytes());
            outputStream.close();

            log.info("Reading the Response");
            InputStream is = conn.getInputStream();
            result = convertStringToString(is);
            is.close();

            InputStream inputStream = new ByteArrayInputStream(result.getBytes());
            SOAPMessage responseSOAP = MessageFactory.newInstance().createMessage(null,inputStream);

            log.info("Result SOAP: {}",responseSOAP.toString());
            log.info("Result String: {}", result);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return e.getMessage();

        }
    }



}
