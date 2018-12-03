package com.example.domain;

public class BetInfo {

   private String event;
   private float amount;
   private String acountEmail;


   public void setAmount(float amount) {
      this.amount = amount;
   }

   public void setAcountEmail(String acountEmail) {
      this.acountEmail = acountEmail;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   public float getAmount() {
      return amount;
   }

   public String getAcountEmail() {
      return acountEmail;
   }

   public String getEvent() {
      return event;
   }
}
