package com.example.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/*
@author Qinyuan Zhang
@date 04/12/2018
*/
public class EventOrganiserDB {
   // This method is used to keep the connection with the database
   public DBCollection getTable (String sport) {
      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("eventOrganiserDB");
      DBCollection table = null;

      switch(sport) {
         case "football":
            table = db.getCollection("FootballMatches");
            break;
         case "basketball":
            table = db.getCollection("BasketBall");
            break;
         case "horseracing":
            table = db.getCollection("HorseRaces");
            break;
      }

      return table;
   }
}