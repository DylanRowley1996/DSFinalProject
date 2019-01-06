package com.example.service;

import com.example.dao.BookieDB;
import com.example.dao.CentralBookieDB;
import com.example.domain.*;
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
import java.util.regex.Pattern;

@Service
public class CentralBookieService implements BookieService {

   private CentralBookieDB cbd = new CentralBookieDB();
   private BookieDB bDB = new BookieDB();

   // Create a connection to the DB.
   private MongoClient mongo = new MongoClient("localhost", 27017);
   // Create a string to store user's email
   private String userEmail = null;

   // The jms listener to get the email from the central bookie
   @JmsListener(destination = "email.bookies")
   public void receiveEmail(String email) {
      userEmail = email;
      System.out.println("Consumer get email: " + userEmail);
   }

   /*
   This is used to define whether the client have correct email and password
    */
   @Override
   public boolean login(AuthInfo authInfo) {
      /**** Find whether the database has same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", authInfo.getEmail());
      searchQuery.append("password", authInfo.getPassword());
      DBCursor cursor = cbd.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         return true;
      }
      return false;
   }

   /*
   This is a method to get the username by using email
    */
   @Override
   public String getUsername(AuthInfo authInfo){

      /**** Find the database with the same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", authInfo.getEmail());
      DBCursor cursor = cbd.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         return (String) cursor.next().get("username");
      }
      return null;
   }

   /*
   This is used to register and put user info into the database
    */
   @Override
   public String registerUser(UserInfo userInfo){
      String emailPattrn = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
      if (!Pattern.matches(emailPattrn, userInfo.getEmail())) {
         return "Wrong email format. Please try again!";
      }
      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("username", userInfo.getUsername());
      document.put("age", userInfo.getAge());
      document.put("email", userInfo.getEmail());
      document.put("password", userInfo.getPassword());
      document.put("balance", 0);
      cbd.getTable().insert(document);
      return "Successfully registered, please login!";
   }

   /*
   This is used to determine whether there are existing email in the database
    */
   @Override
   public boolean ifExist(UserInfo userInfo) {
      /**** Find whether there are existing users****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", userInfo.getEmail());
      DBCursor cursor = cbd.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         return true;
      }
      return false;
   }

//   @Override
//   public boolean bet(BetInfo betInfo) {
//      /**** Update ****/
//      // search document where name="mkyong" and update it with new values
//      BasicDBObject query = new BasicDBObject();
//      query.put("email", betInfo.getEmail());
//      DBCursor cursor = cbd.getTable().find(query);
//
//      while (cursor.hasNext()) {
//         int a = (int)cursor.next().get("balance");
//
//         // Generate a random number from (-50,50]
//         // TODO: Need to change to a real betting result later
//         final long l = System.currentTimeMillis();
//         final int i = (int)( l % 100 ) - 50;
//         a += i;
//
//         BasicDBObject newDocument = new BasicDBObject();
//         newDocument.put("balance", a);
//
//         BasicDBObject updateObj = new BasicDBObject();
//         updateObj.put("$set", newDocument);
//
//         cbd.getTable().update(query, updateObj);
//         return true;
//      }
//      return false;
//   }

   // Get the user's balance
   public double getBalance(String bookie) {
      System.out.println("Getting balance... ...");
      /**** Find whether the database has same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", userEmail);
      DBCursor cursor = bDB.getUserTable(bookie).find(searchQuery);

      while (cursor.hasNext()) {
         return Double.valueOf(cursor.next().get("balance").toString());
      }

      // If there are no users in this bookie's database, create a new user here
      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("email", userEmail);
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
      document.put("email", userEmail);
      document.put("selection", betInfo.getSelection());
      document.put("odd", betInfo.getOdd());
      bDB.getBetsTable(bookie).insert(document);

      return getBetInfoList(bookie);
   }

   public List<BetInfo> getBetInfoList(String bookie) {
      /**** Find whether the rest bets and return****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", userEmail);
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

   // Send message
   public void sendEmail(Destination destination, String email) {
      jmsTemplate.convertAndSend(destination, email);
   }

   // Get return message
   @JmsListener(destination = "out.bet123")
   public void consumerMessage(String text) {
      //TODO: return message from certain bookie
   }

}

