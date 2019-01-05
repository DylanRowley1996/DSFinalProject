package com.example.service;

import com.example.dao.BookieDB;
import com.example.domain.AuthInfo;
import com.example.domain.BasketballMatchInfo;
import com.example.domain.BetInfo;
import com.example.domain.FootballMatchInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.ArrayList;
import java.util.List;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
@Service
public class GeneralBookieService {

   // Create a connection to the DB.
   private MongoClient mongo = new MongoClient("localhost", 27017);

   // Create a string to store user's email
   private String UserEmail = null;

   // The jms listener to get the email from the central bookie
   @JmsListener(destination = "email.bookies")
   public void receiveEmail(String email) {
      UserEmail = email;
      System.out.println("Consumer get email: " + UserEmail);
   }

   // Create the connection with the database
   private BookieDB bDB = new BookieDB();

   // Get the user's balance
   public double getBalance(String bookie) {
      System.out.println("Getting balance... ...");
      /**** Find whether the database has same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", UserEmail);
      DBCursor cursor = bDB.getUserTable(bookie).find(searchQuery);

      while (cursor.hasNext()) {
         return Double.valueOf(cursor.next().get("balance").toString());
      }

      // If there are no users in this bookie's database, create a new user here
      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("email", UserEmail);
      document.put("balance", 0.0);
      bDB.getUserTable(bookie).insert(document);
      return 0.0;
   }

   // Update the users balance with a given bookie.
   public void updateBalance(AuthInfo authInfo, String bookie, double amount) {

      // Calculate the new balance
      double newBalance = getBalance(bookie)+amount;

      //Get the correct DB
      DB db = this.mongo.getDB(bookie+"DB");
      DBCollection userCollection = db.getCollection("user");

      // Identify the document we want to find.
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.put("email", authInfo.getEmail());

      // Define that we want to update the balance.
      BasicDBObject updateQuery = new BasicDBObject();
      updateQuery.append("$set", new BasicDBObject("balance", newBalance));

      // Finally, update the DB
      userCollection.update(searchQuery, updateQuery);

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
      Gson gson = new Gson();
      footballMatchesList = gson.fromJson(matchesJson, new TypeToken<List<FootballMatchInfo>>(){}.getType());
      System.out.println("BookieService get info: " + footballMatchesList.get(0).getHomeTeam());
   }


   public List<FootballMatchInfo> getFootballMatchesList() {
      return footballMatchesList;
   }

   @JmsListener(destination = "basketballMatches.bookies")
   public void getBasketballMatches(String matchesJson) {
      Gson gson = new Gson();
      basketballMatchesList = gson.fromJson(matchesJson, new TypeToken<List<BasketballMatchInfo>>(){}.getType());
      System.out.println("BookieService get info: " + basketballMatchesList.get(0).getHomeTeam());
   }

   public List<BasketballMatchInfo> getBasketballMatchesList() {
      return basketballMatchesList;
   }

   public List<BetInfo> placeBet(BetInfo betInfo, String bookie) {
      // Create a document to store the bet
      BasicDBObject document = new BasicDBObject();
      document.put("match", betInfo.getMatch());
      document.put("amount", betInfo.getAmount());
      document.put("email", betInfo.getEmail());
      document.put("selection", betInfo.getSelection());
      document.put("odd", betInfo.getOdd());
      bDB.getBetsTable(bookie).insert(document);

      /**** Find whether the rest bets and return****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", betInfo.getEmail());
      DBCursor cursor = bDB.getBetsTable(bookie).find(searchQuery);

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
