package com.example.service;

import com.example.dao.EventOrganiserDB;
import com.example.domain.FootballMatchInfo;
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
   public String receiveAndSendBackEvents(String event) {
      if (event.equals("football")) {
         System.out.println("Bookies want to get football footballMatches from Event Organiser");
         // Create a list first
         List<FootballMatchInfo> footballMatches = new ArrayList<>();
         /**** Find all the events and store in a list****/
         DBCursor cursor = eod.getFootballTable().find();
         FootballMatchInfo footballMatchInfo;
         // We only want 10 matches now
         int i = 0;
         for (DBObject object : cursor) {
            footballMatchInfo = new FootballMatchInfo();
            footballMatchInfo.setLeague((String)object.get("League"));
            footballMatchInfo.setHomeTeam((String)object.get("team1"));
            footballMatchInfo.setVisitingTeam((String)object.get("team2"));
            footballMatchInfo.setHomeTeamWinProb(Double.valueOf(object.get("prob1Win").toString()));
            footballMatchInfo.setVisitingTeamWinProb(Double.valueOf(object.get("prob2Win").toString()));
            footballMatchInfo.setDrawProb(Double.valueOf(object.get("probTie").toString()));
            footballMatches.add(footballMatchInfo);
            i++;
            if (i == 10)    break;
         }
         System.out.println("Event Organiser send list: " + footballMatches.get(0).getLeague());
         Gson gson = new Gson();
         return gson.toJson(footballMatches);
      }
      return null;
   }
}
