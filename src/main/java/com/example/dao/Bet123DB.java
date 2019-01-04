package com.example.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
public class Bet123DB {
   // This method is used to keep the connection with the database
   public DBCollection getTable () {
      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("bet123db");
      DBCollection table = db.getCollection("user");
      return table;
   }
   // This method is used to keep the connection with the database
   public DBCollection getBetsTable() {
      MongoClient mongo = new MongoClient("localhost", 27017);
      DB db = mongo.getDB("bet123db");
      DBCollection table = db.getCollection("bet");
      return table;
   }
}
