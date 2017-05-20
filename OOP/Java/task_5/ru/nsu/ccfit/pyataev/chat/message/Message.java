package ru.nsu.ccfit.pyataev.chat.message;

import java.io.Serializable;

public class Message implements Serializable{
  private String message;

  public Message(String message){
    this.message = message;
  }

  @Override
  public String toString(){
    return this.message;
  }
}
