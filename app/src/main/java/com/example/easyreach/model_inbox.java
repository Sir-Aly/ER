package com.example.easyreach;



public class model_inbox {
    String message, from;

    public model_inbox(){
    }
    public model_inbox(String message, String from){
        this.message = message;
        this.from = from;
    }
    public String getmessage() {
        return message;
    }
    public String getfrom(){
        return from;

    }
    public String setmessage(){
        return message;
    }
    public String setFrom(){
        return from;

    }

}

