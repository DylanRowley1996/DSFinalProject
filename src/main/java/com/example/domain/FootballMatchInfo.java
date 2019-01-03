package com.example.domain;

/*
@author Qinyuan Zhang
@date 03/01/2019
*/
public class FootballMatchInfo {
   private String league;
   private String homeTeam;
   private String visitingTeam;
   private double homeTeamWinProb;
   private double visitingTeamWinProb;
   private double drawProb;

   public void setLeague(String league) {
      this.league = league;
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

   public void setDrawProb(double drawProb) {
      this.drawProb = drawProb;
   }

   public String getLeague() {
      return league;
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

   public double getDrawProb() {
      return drawProb;
   }
}
