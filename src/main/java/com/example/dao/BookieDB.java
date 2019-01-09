package com.example.dao;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/*
@author Qinyuan Zhang
@date 03/12/2018
*/
public class BookieDB {
   // This method is used to keep the connection with the database
   public DBCollection getUserTable(String bookie) {
      MongoClient mongo = new MongoClient("main-db", 27017);
      DB db = mongo.getDB(bookie+"DB");
      DBCollection table = db.getCollection("user");
      return table;
   }
   // This method is used to keep the connection with the database
   public DBCollection getBetsTable(String bookie) {
      MongoClient mongo = new MongoClient("main-db", 27017);
      DB db = mongo.getDB(bookie+"DB");
      DBCollection table = db.getCollection("bet");
      return table;
   }
}
