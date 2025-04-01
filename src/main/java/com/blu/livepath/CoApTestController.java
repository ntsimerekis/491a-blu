package com.blu.livepath;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/test")
public class CoApTestController {

    //For testing a device before officially adding it to
    @GetMapping("/{ipAddress}")
    public boolean test(@PathVariable String ipAddress) throws ConnectorException, IOException {
        CoapClient client = new CoapClient("coap://[" + ipAddress + "]/" + "position");
        return client.get() != null;
    }
}
