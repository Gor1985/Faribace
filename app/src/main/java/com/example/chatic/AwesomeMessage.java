package com.example.chatic;

public class AwesomeMessage {
   private String text;
   private String name;
   private String ImageUrl;
   private String sender;// отправитель дя отдельного окна с пользователем
   private String recepient;// получатель
 private boolean isMane;

    public AwesomeMessage() {
    }

    public AwesomeMessage(String text, String name, String imageUrl, String sender, String recepient,boolean isMane) {
        this.text = text;
        this.name = name;
        ImageUrl = imageUrl;
        this.sender = sender;
        this.recepient = recepient;
        this.isMane=isMane;
    }





    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isMane() {
        return isMane;
    }

    public void setMane(boolean mane) {
        isMane = mane;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
