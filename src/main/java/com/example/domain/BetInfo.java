package com.example.domain;

public class BetInfo {

   private float amount;
   private String acountEmail;

   public BetInfo(float amount, String acountEmail) {
      this.amount = amount;
      this.acountEmail = acountEmail;
   }

   public void setAmount(float amount) {
      this.amount = amount;
   }

   public void setAcountEmail(String acountEmail) {
      this.acountEmail = acountEmail;
   }

   public float getAmount() {
      return amount;
   }

   public String getAcountEmail() {
      return acountEmail;
   }

}
