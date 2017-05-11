package ru.nsu.ccfit.pyataev.chat.message;

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
