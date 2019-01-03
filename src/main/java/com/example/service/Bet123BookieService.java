package com.example.service;

import com.example.dao.Bet123DB;
import com.example.dao.EventOrganiserDB;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
@Service
public class Bet123BookieService {

   // Create a string to store user's email
   private String UserEmail = null;

   // The jms listener to get the email from the central bookie
   @JmsListener(destination = "email.bookies")
   public void receiveEmail(String email) {
      UserEmail = email;
      System.out.println("Consumer get email: " + UserEmail);
   }

   // Create the connection with the database
   private Bet123DB b1d = new Bet123DB();
   private EventOrganiserDB eoDB = new EventOrganiserDB();

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
   // Create a string list to store the events
   private Map<String, String> matchesList = new HashMap<String, String>();
   // Create the jms template
   @Resource
   private JmsMessagingTemplate jmsTemplate;

   public void sendToGetEvents(Destination destination, Boolean getMatches, String sport) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("getMatches", getMatches);
      jsonObject.addProperty("sport", sport);

      jmsTemplate.convertAndSend(destination, jsonObject);
   }

   // JMSListener for "matches.bookies" though no message is ever sent to that queue?
   @JmsListener(destination = "matches.bookies")
   public void getEvents(String matchesJson) {
      // System.out.println("Bet123 Service get Map: " + matches);
      Gson gson = new Gson();
      matchesList = gson.fromJson(matchesJson, new TypeToken<Map<String, String>>(){}.getType());
   }

   // TODO Maybe change return type to List?
   public Map<String, String> getEventsList(String sport) {
      switch (sport){
         case "football":
            DBCursor cursor = eoDB.getTable(sport).find();
            while(cursor.hasNext()) {
               String league = (String) cursor.next().get("League");
               if(!(matchesList.containsKey(league)))
                  matchesList.put(league, "Football");
            }
            matchesList = new TreeMap<String, String>(matchesList);
            break;

            default:
               System.out.println("Invalid sport");
      }
      return matchesList;
   }
}
