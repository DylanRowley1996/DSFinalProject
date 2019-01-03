package com.example.service;

import com.example.dao.Bet123DB;
import com.example.domain.BasketballMatchInfo;
import com.example.domain.BetInfo;
import com.example.domain.FootballMatchInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

   // The send message to get matches
   // Create a string list to store the events
   private List<FootballMatchInfo> footballMatchesList = null;
   private List<BasketballMatchInfo> basketballMatchesList = null;
   // Create the jms template
   @Resource
   private JmsMessagingTemplate jmsTemplate;

   public void sendToGetEvents(Destination destination, String event) {
      jmsTemplate.convertAndSend(destination, event);
   }

   @JmsListener(destination = "footballMatches.bookies")
   public void getFootballMatches(String matchesJson) {
      // System.out.println("Bet123 Service get Map: " + matches);
      Gson gson = new Gson();
      footballMatchesList = gson.fromJson(matchesJson, new TypeToken<List<FootballMatchInfo>>(){}.getType());
      System.out.println("Bet123Service get info: " + footballMatchesList.get(0).getHomeTeam());
   }

   public List<FootballMatchInfo> getFootballMatchesList() {
      return footballMatchesList;
   }

   @JmsListener(destination = "basketballMatches.bookies")
   public void getBasketballMatches(String matchesJson) {
      Gson gson = new Gson();
      basketballMatchesList = gson.fromJson(matchesJson, new TypeToken<List<BasketballMatchInfo>>(){}.getType());
      System.out.println("Bet123Service get info: " + basketballMatchesList.get(0).getHomeTeam());
   }

   public List<BasketballMatchInfo> getBasketballMatchesList() {
      return basketballMatchesList;
   }

   public List<BetInfo> placeOrder(BetInfo betInfo) {
      // Create a document to store the order
      BasicDBObject document = new BasicDBObject();
      document.put("match", betInfo.getMatch());
      document.put("amount", betInfo.getAmount());
      document.put("email", betInfo.getEmail());
      document.put("selection", betInfo.getSelection());
      document.put("odd", betInfo.getOdd());
      b1d.getOrderTable().insert(document);

      /**** Find whether the rest orders and return****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", betInfo.getEmail());
      DBCursor cursor = b1d.getOrderTable().find(searchQuery);

      List<BetInfo> betInfoList = new ArrayList<>();
      while (cursor.hasNext()) {
         Object c = cursor.next();
         BetInfo newBetInfo = new BetInfo();
         newBetInfo.setMatch((String) ((DBObject) c).get("match"));
         newBetInfo.setAmount((Double) ((DBObject) c).get("amount"));
         newBetInfo.setSelection((String) ((DBObject) c).get("selection"));
         newBetInfo.setEmail((String) ((DBObject) c).get("email"));
         newBetInfo.setOdd((Double) ((DBObject) c).get("odd"));
         betInfoList.add(newBetInfo);
      }

      return betInfoList;
   }
}
