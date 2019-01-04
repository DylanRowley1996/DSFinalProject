package com.example.service;

import com.example.dao.EventOrganiserDB;
import com.example.domain.BasketballMatchInfo;
import com.example.domain.FootballMatchInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

   // Get info from bookie companies amd send back all the matches' info
   @JmsListener(destination = "footballMatches.eventOrganiser")
   @SendTo("footballMatches.bookies")
   public String receiveAndSendBackFootballMatches(String event) {
      Gson gson = new Gson();
      FootballMatchInfo footballMatchInfo;
      DBCursor cursor;
      if (event.equals("football")) {
         System.out.println("Bookies want to get all football Matches from Event Organiser");
         // Create a list first
         List<FootballMatchInfo> matches = new ArrayList<>();
         /**** Find all the events and store in a list****/
         cursor = eod.getTable("football").find();
         // We only want 10 matches now
         int i = 0;
         for (DBObject object : cursor) {
            footballMatchInfo = new FootballMatchInfo((String) object.get("League"), (String) object.get("team1"),
                                                      (String) object.get("team2"), Double.valueOf(object.get("prob1Win").toString()),
                                                      Double.valueOf(object.get("prob2Win").toString()),
                                                      Double.valueOf(object.get("probTie").toString()));
            matches.add(footballMatchInfo);
            i++;
            if (i == 10) return gson.toJson(matches);
         }

      }
      return null;
   }

   @JmsListener(destination = "basketballMatches.eventOrganiser")
   @SendTo("basketballMatches.bookies")
   public String receiveAndSendBackBasketballMatches(String event) {
      Gson gson = new Gson();
      BasketballMatchInfo basketballMatchInfo;
      DBCursor cursor;

      // Create a list first
      List<BasketballMatchInfo> matches = new ArrayList<>();
      /**** Find all the events and store in a list****/
      cursor = eod.getTable("basketball").find();
      // We only want 10 matches now
      int i = 0;
      for (DBObject object : cursor) {
         basketballMatchInfo = new BasketballMatchInfo((String) object.get("team1"), (String) object.get("team2"),
                 Double.valueOf(object.get("prob1Win").toString()), Double.valueOf(object.get("prob2Win").toString()));
         matches.add(basketballMatchInfo);
         i++;
         if (i == 10) break;
      }
      return gson.toJson(matches);
   }
}
