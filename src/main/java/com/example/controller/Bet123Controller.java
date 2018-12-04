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
import java.util.List;

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
      Destination destination = new ActiveMQQueue("events.eventOrganiser");
      b1bs.sendToGetEvents(destination, true);
      return "bet123";
   }

   /*
   The GET method leads to the html file
    */
   @RequestMapping(value = "/bet-in-bet123", method = RequestMethod.GET)
   public String betinbet123get(Model model, HttpSession session) {
      model.addAttribute("bookie", "Bet123");
      List<String> eventsList = b1bs.getEventsList();
      System.out.println("Controller get list: " + eventsList);
      session.setAttribute("list", eventsList);
      return "BetNow";
   }
}
