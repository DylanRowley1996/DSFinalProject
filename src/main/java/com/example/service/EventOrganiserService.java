package com.example.service;

import com.example.dao.EventOrganiserDB;
import com.google.gson.Gson;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
@author Qinyuan Zhang
@date 04/12/2018
*/
@Service
public class EventOrganiserService {

   // Create the connection with the database
   private EventOrganiserDB eod = new EventOrganiserDB();

   // Get info from bookie companies amd send back all the matches' info
   @JmsListener(destination = "matches.eventOrganiser")
   @SendTo("matches.bookies")
   public String receiveAndSendBackEvents(Boolean getMatches) {
      if (getMatches) {
         System.out.println("Bookies want to get matches from Event Organiser");
      }
      // Create a list first
      Map<String, String> matches = new HashMap<>();
      /**** Find all the events and store in a list****/
      DBCursor cursor = eod.getTable().find();
      for (DBObject object : cursor) {
         matches.put((String)object.get("info"), (String)object.get("event"));
      }
      // System.out.println("Event Organiser send map: " + matches);
      Gson gson = new Gson();
      String matchesJson = gson.toJson(matches);
      return matchesJson;
   }
}
