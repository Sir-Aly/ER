package com.example.easyreach;



public class model_inbox {
    String message, from,fuid;

    public model_inbox(){
    }
    public model_inbox(String message, String from,String fuid){
        this.message = message;
        this.from = from;
        this.fuid = fuid;

    }
    public String getmessage() {
        return message;
    }
    public String getfrom(){
        return from;

    }
    public String getfuid(){
        return fuid;
    }
    public String setmessage(){
        return message;
    }
    public String setFrom(){
        return from;
    }
    public String setuid(){
        return fuid;
    }

}

