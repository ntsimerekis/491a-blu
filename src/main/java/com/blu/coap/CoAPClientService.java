package com.blu.coap;

import org.eclipse.californium.elements.exception.ConnectorException;
import org.springframework.stereotype.Service;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;

import java.io.IOException;

@Service
public class CoAPClientService {

    private final CoapClient coapClient;

    public CoAPClientService() {
        coapClient = new CoapClient();
    }

    public CoapResponse get(String uri) throws ConnectorException, IOException {
        coapClient.setURI(uri);
        return coapClient.get();
    }

}
