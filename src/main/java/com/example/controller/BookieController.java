package com.example.controller;

import com.example.domain.UserInfo;
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
The central bookie controller.
@author Qinyuan Zhang
@date 21/11/2018
 */
@Controller
public class BookieController {

   @Autowired
   private CentralBookieService cbs;

   private AuthInfo authInfo;

   /*
   Redirect to the index
    */
   @RequestMapping()
   public String index(HttpServletResponse response) {
      return response.encodeRedirectURL("index");
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
                              @ModelAttribute UserInfo userInfo,
                              HttpServletResponse response) {
      String info = "Sorry the email you entered has been registered. Please try again.";
      if (!cbs.ifExist(userInfo)) {
         info = cbs.registerUser(userInfo);
      }
      // Return the username to the index page
      model.addAttribute("result", info);
      return response.encodeRedirectURL("index");
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
      // Print out the email and password info
      System.out.println(authInfo.getEmail() + authInfo.getPassword());
      // Use the method in service to login
      Boolean result = cbs.login(authInfo);
      // If match the database, add authInfo into the session
      if (result) {
         session.setAttribute("Auth", authInfo);
         this.authInfo = authInfo;
         model.addAttribute("result", cbs.getUsername(authInfo));
      }

      // Send the email address to all the bookie companies

      /*
      Notice that we need to send out and get info before the next step
      otherwise it will not work
      */
      Destination destination = new ActiveMQQueue("email.bookies");
      cbs.sendEmail(destination, authInfo.getEmail());
      return response.encodeRedirectURL("index");
   }

   /*
   The GET method for logout
    */
   @RequestMapping(value = "/logout", method = RequestMethod.GET)
   public String loginOut(HttpSession session) {
      //Delete the Auth in the session
      session.removeAttribute("Auth");
      this.authInfo = null;
      return "index";
   }

   /*

   */
   @RequestMapping(value="/paymentProcessor", method=RequestMethod.GET)
   public String displayPaymentMethod(Model model, HttpSession session) {
      System.out.println((String) session.getAttribute("bookie"));
      //model.addAttribute("bookie", session.getAttribute("bookie"));
      model.addAttribute("username", cbs.getUsername(this.authInfo));
      session.setAttribute("Auth", this.authInfo);
      return "paymentProcessor";

   }

   @RequestMapping(value="/paymentProcessor", method=RequestMethod.POST)
   public String processPayment(Model model, HttpSession session, @ModelAttribute(value = "amount") double amount) {

      // Update the balance.
      cbs.updateBalance(this.authInfo, session.getAttribute("bookie").toString(), amount);

      //Set-up model for bookie.html page.
      model.addAttribute("bookie", session.getAttribute("bookie"));
      model.addAttribute("balance", cbs.getBalance(session.getAttribute("bookie").toString()));

      return "bookie";

   }



}
