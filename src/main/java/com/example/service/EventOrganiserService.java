package com.example.service;

import com.example.dao.EventOrganiserDB;
import com.example.domain.EventInfo;
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
   public List<EventInfo> receiveAndSendBackEvents(Boolean getEvents) {
      if (getEvents) {
         System.out.println("Bookies want to get events from Event Organiser");
      }
      // Create a list first
      List<EventInfo> events = new ArrayList<>();
      // Create a new event info
      EventInfo eventInfo = new EventInfo();
      /**** Find all the events and store in a list****/
      DBCursor cursor = eod.getTable().find();
      int id = 0;
      for (DBObject object : cursor) {
         eventInfo.setEventName((String)object.get("name"));
         eventInfo.setId(id);
         id ++;
         events.add(eventInfo);
      }
      return events;
   }
}
