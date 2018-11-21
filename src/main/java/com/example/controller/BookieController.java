package com.example.controller;

import com.example.service.LocalBookieService;
import com.example.domain.AuthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class BookieController {

   @Autowired
   private LocalBookieService lbs;

   @RequestMapping("/")
   public String index() {
      //return response.encodeRedirectURL("/index");
      return "index";
   }

   @RequestMapping("/index")
   public String home(Model model) {
      return "index";
   }

   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public String loginGet() {
      return "login";
   }

   @RequestMapping(value = "/login", method = RequestMethod.POST)
   public String loginPost(Model model,
                           @ModelAttribute(value = "authinfo") AuthInfo authInfo,
                           HttpServletResponse response,
                           HttpSession session) {
      System.out.println(authInfo.getEmail() + authInfo.getPassword());
      Boolean result = lbs.login(authInfo);
      if (result) {
         session.setAttribute("Auth", authInfo);
      }
      model.addAttribute("result", result);
      return response.encodeRedirectURL("/index");
   }
}
