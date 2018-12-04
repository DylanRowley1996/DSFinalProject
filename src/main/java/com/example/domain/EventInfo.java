package com.example.domain;

import java.io.Serializable;

/*
@author Qinyuan Zhang
@date 04/12/2018
*/
public class EventInfo{

   private String eventName;
   private int id;

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getEventName() {
      return eventName;
   }

   public int getId() {
      return id;
   }
}
