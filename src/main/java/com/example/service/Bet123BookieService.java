package com.example.service;

import com.example.dao.Bet123DB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
@Service
public class Bet123BookieService {

   // The jms listener
   @JmsListener(destination = "in.bet123")
   @SendTo("out.bet123")
   public String receiveQueue(String email) {
      System.out.println("Consumer get email: " + email);
      return "return message: " + email;
   }

   // Create the connection with the database
   Bet123DB b1d = new Bet123DB();

   // Create user in bet123
   public void createUser(String email) {
      /**** Insert ****/
      // Create a document to store key and value
      BasicDBObject document = new BasicDBObject();
      document.put("username", email);
      document.put("balance", 0);
      b1d.getTable().insert(document);
   }

   // Get the user's balance
   public int getBalance(String email) {
      /**** Find whether the database has same info****/
      BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.append("email", email);
      DBCursor cursor = b1d.getTable().find(searchQuery);

      while (cursor.hasNext()) {
         return (Integer) cursor.next().get("balance");
      }
      return 0;
   }
}
