package com.example.flixtyle;

public class cards {

    private String userId;
    private String name;
    public cards (String userId, String name){
        this.userId=userId;
        this.name=name;

    }

    public String getUserId(){
        return userId;


    }
    public void setUserId(String userID){
        this.userId=userID;

    }

    public String getName(){
        return name;


    }
    public void setName(String Name){
        this.name=Name;

    }
}
