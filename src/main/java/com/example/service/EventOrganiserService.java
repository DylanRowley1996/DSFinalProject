package com.example.service;

import com.example.dao.EventOrganiserDB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
@author Qinyuan Zhang
@date 04/12/2018
*/
@Service
public class EventOrganiserService {

   // Create the connection with the database
   private EventOrganiserDB eod = new EventOrganiserDB();

   // Get info from bookie companies amd send back all the events' info
   @JmsListener(destination = "events.eventOrganiser")
   @SendTo("events.bookies")
   public List<String> receiveAndSendBackEvents(Boolean getEvents) {
      if (getEvents) {
         System.out.println("Bookies want to get events from Event Organiser");
      }
      // Create a list first
      List<String> events = new ArrayList<>();
      /**** Find all the events and store in a list****/
      DBCursor cursor = eod.getTable().find();
      for (DBObject object : cursor) {
         events.add((String)object.get("name"));
      }
      return events;
   }
}
