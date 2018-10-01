package com.h8.nh.nhoodlocationsvc.services;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class LocationEntryReceiver {

    @JmsListener(destination = "location.inbound", containerFactory = "myFactory")
    public void receiveMessage(LocationEntry l) {
        System.out.println("Received <" + l + ">");
    }
}
