package com.example.controller;

import com.example.domain.UserInfo;
import com.example.service.Bet123BookieService;
import com.example.service.CentralBookieService;
import com.example.domain.AuthInfo;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
The bookie controller.
@author Qinyuan Zhang
@date 21/11/2018
 */
@Controller
public class BookieController {

   @Autowired
   private CentralBookieService cbs;
   @Autowired
   private Bet123BookieService b1bs;

   /*
   Redirect to the index
    */
   @RequestMapping()
   public String index(HttpServletResponse response) {
      return response.encodeRedirectURL("/index");
   }

   /*
   The real index page
    */
   @RequestMapping("/index")
   public String home(Model model) {
      return "index";
   }

   /*
   The GET method leads to the html file
    */
   @RequestMapping(value = "/register", method = RequestMethod.GET)
   public String registerGet() {
      return "register";
   }

   /*
   The POST method to register user
    */
   @RequestMapping(value = "/register", method = RequestMethod.POST)
   public String registerPost(Model model,
                              // Connect this to the "user" in register html file
                              @ModelAttribute(value = "user") UserInfo userInfo,
                              HttpServletResponse response) {
      String info = "Sorry the email you entered has been registered. Please try again.";
      if (!cbs.ifExist(userInfo)) {
         info = cbs.registryUser(userInfo);
         // Generate user in bet123's database
         b1bs.createUser(userInfo.getEmail());
      }
      // Return the username to the index page
      model.addAttribute("result", info);
      return response.encodeRedirectURL("/index");
   }

   /*
   The GET for the login
    */
   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public String loginGet() {
      return "login";
   }

   /*
   The POST for the login where the actual service is called
    */
   @RequestMapping(value = "/login", method = RequestMethod.POST)
   public String loginPost(Model model,
                           @ModelAttribute(value = "authinfo") AuthInfo authInfo,
                           HttpServletResponse response,
                           HttpSession session) {
      System.out.println(authInfo.getEmail() + authInfo.getPassword());
      Boolean result = cbs.login(authInfo);
      // If match the database, add the session
      if (result) {
         session.setAttribute("Auth", authInfo);
      }
      model.addAttribute("result", cbs.getUsername(authInfo));
      return response.encodeRedirectURL("/index");
   }

   /*
   The GET method for logout
    */
   @RequestMapping(value = "/logout", method = RequestMethod.GET)
   public String loginOut(HttpSession session) {
      //Delete the Auth in the session
      session.removeAttribute("Auth");
      return "index";
   }

   /*
   The GET method to check certain bookie
    */
   @RequestMapping(value = "/bet123", method = RequestMethod.GET)
   public String checkBookie(Model model, HttpSession session) throws InterruptedException {
      Destination destination = new ActiveMQQueue("in.bet123");
      AuthInfo authInfo = (AuthInfo) session.getAttribute("Auth");
      cbs.sendMessage(destination, authInfo.getEmail());
      model.addAttribute("result", cbs.getUsername(authInfo));
      model.addAttribute("balance", b1bs.getBalance(authInfo.getEmail()));
      return "bet123";
   }
}
