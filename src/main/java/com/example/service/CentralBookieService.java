package com.example.service;

import com.example.dao.CentralBookieDB;
import com.example.domain.AuthInfo;
import com.example.domain.BookieService;
import com.example.domain.UserInfo;
import com.mongodb.*;
import com.example.domain.BetInfo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.regex.Pattern;

@Service
public class CentralBookieService implements BookieService {

   // Create the connection with the database
   private CentralBookieDB cbd = new CentralBookieDB();

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

   @Override
   public boolean bet(BetInfo betInfo) {
      /**** Update ****/
      // search document where name="mkyong" and update it with new values
      BasicDBObject query = new BasicDBObject();
      query.put("email", betInfo.getEmail());
      DBCursor cursor = cbd.getTable().find(query);

      while (cursor.hasNext()) {
         int a = (int)cursor.next().get("balance");

         // Generate a random number from (-50,50]
         // TODO: Need to change to a real betting result later
         final long l = System.currentTimeMillis();
         final int i = (int)( l % 100 ) - 50;
         a += i;

         BasicDBObject newDocument = new BasicDBObject();
         newDocument.put("balance", a);

         BasicDBObject updateObj = new BasicDBObject();
         updateObj.put("$set", newDocument);

         cbd.getTable().update(query, updateObj);
         return true;
      }
      return false;
   }

   @Override
   public int getCurrentBalance(AuthInfo authInfo) {
      /**** Find whether there are existing users****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", authInfo.getEmail());
      DBCursor cursor = cbd.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         int a = (int)cursor.next().get("balance");
         return a;
      }
      return 0;
   }

   // Create the jms template to send info to bookie companies
   @Resource
   private JmsMessagingTemplate jmsTemplate;

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

