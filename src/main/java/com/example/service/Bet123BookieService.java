package com.example.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
@Service
public class Bet123BookieService {
   @JmsListener(destination = "in.bet123")
   @SendTo("out.bet123")
   public String receiveQueue(String email) {
      System.out.println("Consumer get email: " + email);
      return "return message: " + email;
   }
}
