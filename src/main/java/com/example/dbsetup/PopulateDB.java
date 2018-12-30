package com.example.dbsetup;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PopulateDB {

    private MongoClient mongo;

    private String footballDataFilePath = "/resources/odds/FootBallData.csv";
    private String horseRacingFilePath = "/resources/odds/horse_racing_odds.csv";
    private String nbaDataFilePath = "/resources/odds/nba_data.csv";

    public PopulateDB(String hostname, int port){ mongo = new MongoClient(hostname, port); }

    public void populateFootballTeams(String hostname, int port){
        DB db = this.mongo.getDB("eventOrganiserDB");
        db.createCollection( "FootballMatches", new BasicDBObject());

        Path pathToFile = Paths.get(footballDataFilePath);

        //Read file
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
                // read the first line from the text file
                String line = br.readLine();

                //Get the collection to work with.
                DBCollection footballmatches = db.getCollection("FootballMatches");

                // loop until all lines are read
                while (line != null) {
                    String[] fields = line.split(",");

                    //Create a DBObject with the obtained fields.
                    DBObject footballMatch = new BasicDBObject("_id", new ObjectId())
                            .append("League", fields[0])
                            .append("team1", fields[1])
                            .append("team2", fields[2])
                            .append("prob1Win", fields[3])
                            .append("prob2Win", fields[4])
                            .append("probTie", fields[5]);
                }



            } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

