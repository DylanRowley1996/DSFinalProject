package com.example.domain;

public class BetInfo {

   private String match;
   private double amount;
   private String email;
   private String selection;
   private double odd;

   public void setAmount(double amount) {
      this.amount = amount;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setMatch(String match) {
      this.match = match;
   }

   public void setSelection(String selection) {
      this.selection = selection;
   }

   public void setOdd(double odd) { this.odd = odd; }

   public double getAmount() {
      return amount;
   }

   public String getEmail() {
      return email;
   }

   public String getMatch() {
      return match;
   }

   public String getSelection() {
      return selection;
   }

   public double getOdd() { return odd; }
}
