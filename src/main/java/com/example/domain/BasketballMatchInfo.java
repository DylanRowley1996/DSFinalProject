package com.example.domain;

/*
@author Qinyuan Zhang
@date 03/01/2019
*/
public class BasketballMatchInfo {
   private String homeTeam;
   private String visitingTeam;
   private double homeTeamWinProb;
   private double visitingTeamWinProb;

   public BasketballMatchInfo(String homeTeam, String visitingTeam, double homeTeamWinProb, double visitingTeamWinProb) {
      this.homeTeam = homeTeam;
      this.visitingTeam = visitingTeam;
      this.homeTeamWinProb = homeTeamWinProb;
      this.visitingTeamWinProb = visitingTeamWinProb;
   }

   public void setHomeTeam(String homeTeam) {
      this.homeTeam = homeTeam;
   }

   public void setVisitingTeam(String visitingTeam) {
      this.visitingTeam = visitingTeam;
   }

   public void setHomeTeamWinProb(double homeTeamWinProb) {
      this.homeTeamWinProb = homeTeamWinProb;
   }

   public void setVisitingTeamWinProb(double visitingTeamWinProb) {
      this.visitingTeamWinProb = visitingTeamWinProb;
   }

   public String getHomeTeam() {
      return homeTeam;
   }

   public String getVisitingTeam() {
      return visitingTeam;
   }

   public double getHomeTeamWinProb() {
      return homeTeamWinProb;
   }

   public double getVisitingTeamWinProb() {
      return visitingTeamWinProb;
   }

}
