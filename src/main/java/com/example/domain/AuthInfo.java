package com.example.domain;

import java.util.Random;

public class AuthInfo {

   private String email;
   private String password;
   private String uniqueID;

   public void setEmail(String email) {
      this.email = email;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   /*
         Use to generate random uniqueID
          */
   public static String getRandomString(int length) {
      String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      Random random = new Random();
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < length; ++i) {
         //int number = random.nextInt(62);// [0,62)
         sb.append(str.charAt(random.nextInt(62)));
      }
      return sb.toString();
   }
}
