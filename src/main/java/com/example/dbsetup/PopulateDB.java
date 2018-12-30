package com.example.dbsetup;

import com.mongodb.*;
import org.bson.types.ObjectId;
import java.io.*;


public class PopulateDB {

    // Create client and get db to work with
    private MongoClient mongo = new MongoClient("localhost", 27017);
    private DB db = this.mongo.getDB("eventOrganiserDB");

    // Paths to files that contain the data which needs to be entered into the DB.
    private File footballDataFile = new File("src/main/resources/odds/FootBallData.csv");
    private File horseRacingDataFile = new File("src/main/resources/odds/horse_racing_odds.csv");
    private File nbaDataFile = new File("src/main/resources/odds/nba_data.csv");




    public PopulateDB(){}

    public void populateDatabase(){
        populateFootballTable();
        populateHorseRacingTable();
        populateBasketballTable();
    }

    private void populateFootballTable(){

        //Get the collection to work with.
        DBCollection footballMatches = db.getCollection("FootballMatches");

        try {
            BufferedReader br = new BufferedReader(new FileReader(footballDataFile));

            String st;
            while ((st = br.readLine()) != null) {

                //Split line into its fields.
                String[] fields = st.split(",");

                //Create a DBObject with the obtained fields.
                DBObject footballMatch = new BasicDBObject("_id", new ObjectId())
                        .append("League", fields[0])
                        .append("team1", fields[1])
                        .append("team2", fields[2])
                        .append("prob1Win", fields[3])
                        .append("prob2Win", fields[4])
                        .append("probTie", fields[5]);

                // Insert football match into table.
                footballMatches.insert(footballMatch);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void populateHorseRacingTable(){

        //Get the collection to work with.
        DBCollection horseRaces = db.getCollection("HorseRaces");

        try {
            BufferedReader br = new BufferedReader(new FileReader(horseRacingDataFile));

            String st;
            while ((st = br.readLine()) != null) {

                //Split line into its fields.
                String[] fields = st.split(",");

                //Create a DBObject with the obtained fields.
                DBObject horse = new BasicDBObject("_id", new ObjectId())
                        .append("horseName", fields[0])
                        .append("probWin", fields[1]);

                // Insert football match into table.
                horseRaces.insert(horse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void populateBasketballTable(){

        //Get the collection to work with.
        DBCollection basketballTeams = db.getCollection("BasketBall");

        try {
            BufferedReader br = new BufferedReader(new FileReader(nbaDataFile));

            String st;
            while ((st = br.readLine()) != null) {

                //Split line into its fields.
                String[] fields = st.split(",");

                //Create a DBObject with the obtained fields.
                DBObject basketballMatch = new BasicDBObject("_id", new ObjectId())
                        .append("team1", fields[0])
                        .append("team2", fields[1])
                        .append("prob1Win", fields[2])
                        .append("prob2Win", fields[3]);

                // Insert football match into table.
                basketballTeams.insert(basketballMatch);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

