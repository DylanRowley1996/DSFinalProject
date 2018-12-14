package com.example.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
@author Qinyuan Zhang
@date 14/12/2018
*/
@Service
public class OddSpecialistService {
   // A basic method to generate football odds
   public Map<String, Double> generateFootballOdds(String team1, String team2,
                                   double team1win, double team2win) {
      // The odds generating method we use is simple
      // Use 1 divide the win rate, then times 0.9
      Map<String, Double> odds = new HashMap<>();
      odds.put(team1, (1 / team1win) * 0.9);
      odds.put(team2, (1 / team2win) * 0.9);
      odds.put("draw", (1 / (1 - team1win - team2win) * 0.9));

      return odds;
   }
}
