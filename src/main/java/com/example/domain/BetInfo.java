package com.example.domain;

public class BetInfo {

   private String match;
   private float amount;
   private String email;
   private String selection;

   public void setAmount(float amount) {
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

   public float getAmount() {
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
}
