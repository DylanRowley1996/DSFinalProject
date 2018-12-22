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
   // A basic method to generate odds
   public List<Double> generateFootballOdds(List<Double> probabilities) {
      // The odds generating method we use is simple
      // Use 1 divide the win rate, then times 0.9
      List<Double> odds = new ArrayList<>();
      for (Double probability : probabilities) {
         odds.add((1 / probability) * 0.9);
      }
      return odds;
   }
}
