package com.example.service;

import com.example.dao.Bet123DB;
import com.example.domain.EventInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.List;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
@Service
public class Bet123BookieService {

   // Create a string to store user's email
   private String UserEmail = null;
   // Create a string list to store the events
   private List<EventInfo> eventsList = null;

   // The jms listener to get the email from the central bookie
   @JmsListener(destination = "email.bookies")
   public void receiveEmail(String email) {
      UserEmail = email;
      System.out.println("Consumer get email: " + UserEmail);
   }

   // Create the connection with the database
   private Bet123DB b1d = new Bet123DB();

   // Get the user's balance
   public double getBalance() {
      System.out.println("Getting balance... ...");
      /**** Find whether the database has same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", UserEmail);
      DBCursor cursor = b1d.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         return Double.valueOf(cursor.next().get("balance").toString());
      }

      // If there are no users in this bookie's database, create a new user here
      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("email", UserEmail);
      document.put("balance", 0.0);
      b1d.getTable().insert(document);
      return 0.0;
   }

   // The send message to get events
   // Create the jms template
   @Resource
   private JmsMessagingTemplate jmsTemplate;

   public void sendToGetEvents(Destination destination, Boolean getEvents) {
      jmsTemplate.convertAndSend(destination, getEvents);
   }

   @JmsListener(destination = "events.bookies")
   public void getEvents(List<EventInfo> events) {
      System.out.println("Get the events: " + events);
      eventsList = events;
   }

   public List<EventInfo> getEventsList() {
      return eventsList;
   }
}
