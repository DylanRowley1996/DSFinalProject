package com.example.controller;

import com.example.service.Bet123BookieService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jms.Destination;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
      List<String> currentMatchesList = new ArrayList<>();
      for (Map.Entry<String, String> vo : matchesList.entrySet()) {
         if (vo.getValue().equals("Football")) {
            String matchInfo = vo.getKey();
            String[] matchInfoDetails = matchInfo.split(",");
            currentMatchesList.add("[" + matchInfoDetails[0] + "] "
                                 + matchInfoDetails[1] + " VS "
                                 + matchInfoDetails[2]);
         }
      }
      System.out.println("Controller get list: " + currentMatchesList);
      model.addAttribute("matchesList", currentMatchesList);
      return "BetNow";
   }
}
