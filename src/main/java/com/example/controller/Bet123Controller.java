package com.example.controller;

import com.example.domain.BetInfo;
import com.example.service.Bet123BookieService;
import com.example.service.OddSpecialistService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jms.Destination;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
@author Qinyuan Zhang
@date 04/12/2018
*/
@Controller
public class Bet123Controller {

   @Autowired
   private Bet123BookieService b1bs;

   @Autowired
   private OddSpecialistService osl;

   /*
   The GET method to check bet123
    */
   @RequestMapping(value = "/bet123", method = RequestMethod.GET)
   public String checkBookie(Model model) {
      model.addAttribute("balance", b1bs.getBalance());
      /*
      Notice that we need to send out and get info before the next step
      otherwise it will not work
      */
      Destination destination = new ActiveMQQueue("matches.eventOrganiser");
      b1bs.sendToGetEvents(destination, true);
      return "bet123";
   }

   /*
   The GET method leads to the html file
   Notice that we want to use one single frontend template for
   all bookie companies and events
    */
   @RequestMapping(value = "/bet-in-bet123/HorseRace", method = RequestMethod.GET)
   public String betinbet123getHorseRace(Model model, HttpSession session) {
      model.addAttribute("result","Horse Race");
      model.addAttribute("bookie", "Bet123");
      Map<String, String> matchesList = b1bs.getEventsList();
      // Create a list to store all the matches info for particular event
      List<String> currentMatchesList = new ArrayList<>();
      for (Map.Entry<String, String> vo : matchesList.entrySet()) {
         if (vo.getValue().equals("HorseRace"))   currentMatchesList.add(vo.getKey());
      }
      System.out.println("Controller get list: " + currentMatchesList);
      session.setAttribute("list", currentMatchesList);
      return "BetNow";
   }

   /*
The GET method leads to the html file
Notice that we want to use one single frontend template for
all bookie companies and events
 */
   @RequestMapping(value = "/bet-in-bet123/Football", method = RequestMethod.GET)
   public String betinbet123getFootball(Model model) {
      model.addAttribute("result","Football");
      model.addAttribute("bookie", "Bet123");
      Map<String, String> matchesList = b1bs.getEventsList();
      // Create a list to store all the matches info for particular event
      Map<String, String> currentMatchesMap = new HashMap<>();
      for (Map.Entry<String, String> vo : matchesList.entrySet()) {
         if (vo.getValue().equals("Football")) {
            String matchInfo = vo.getKey();
            String[] matchInfoDetails = matchInfo.split(",");
            String displayOnHTML = "[" + matchInfoDetails[0] + "] "
                                    + matchInfoDetails[1] + " (h) VS "
                                    + matchInfoDetails[2];
            String hrefInfo = matchInfo.replace(",","&");
            currentMatchesMap.put(displayOnHTML, hrefInfo);
         }
      }
      System.out.println("Controller get info: " + currentMatchesMap);
      model.addAttribute("matchesMap", currentMatchesMap);
      return "BetNow";
   }

   @RequestMapping(value = "/bet-in-bet123/Football/{hrefInfo}", method = RequestMethod.GET)
   public String betinbet123CheckFootballOdds(@PathVariable("hrefInfo") String hrefInfo, Model model, HttpSession session) {
      model.addAttribute("result","Football");
      model.addAttribute("bookie", "Bet123");
      String[] hrefDetails = hrefInfo.split("\\&");
      List<Double> probabilities = new ArrayList<>();
      session.setAttribute("matchInfo", hrefDetails[0] + " " + hrefDetails[1] + " VS " + hrefDetails[2]);
      for (int i = 3 ; i < 6 ; i++) probabilities.add(Double.valueOf(hrefDetails[i]));
      List<Double> odds = osl.generateFootballOdds(probabilities);
      Map<String, Double> displayInfo =  new HashMap<>();
      displayInfo.put(hrefDetails[1] + " (h)", odds.get(0));
      displayInfo.put(hrefDetails[2], odds.get(1));
      displayInfo.put("Draw", odds.get(2));
      model.addAttribute("oddsMap", displayInfo);
      return "CheckOdds";
   }

   @RequestMapping(value = "/bet-in-bet123/Football/placeOrder", method = RequestMethod.POST)
   public String betinbet123PlaceOrderFootball(@ModelAttribute(value = "betinfo") BetInfo betInfo, Model model, HttpSession session) {
      String matchInfo = (String) session.getAttribute("matchInfo");
      if (betInfo.getSelection().equals("null") || matchInfo.equals("null"))   return "error";
      else {
         betInfo.setMatch(matchInfo);
         b1bs.placeOrder(betInfo);
      }
      return "index";
   }
}
