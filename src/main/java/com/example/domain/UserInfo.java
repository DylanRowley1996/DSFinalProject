package com.example.domain;

public class UserInfo {

   private String username;
   private int age;
   private float balance;
   private String password;
   private String email;

   public void setUsername(String username) {
      this.username = username;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public void setBalance(float balance) {
      this.balance = balance;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   public String getUsername() {
      return username;
   }

   public int getAge() {
      return age;
   }

   public float getBalance() {
      return balance;
   }

   public float changeBalance (float a) {
      this.balance += a;
      return balance;
   }
}
