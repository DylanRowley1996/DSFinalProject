package com.example.dbsetup;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class PopulateDB {

    private MongoClient mongo;

    public PopulateDB(String hostname, int port){
        mongo = new MongoClient(hostname, port);
    }

    public void populateFootballTeams(String hostname, int port){
        DB db = this.mongo.getDB("eventOrganiserDB");
        db.createCollection( "Football Teams", new BasicDBObject());


    }


}

