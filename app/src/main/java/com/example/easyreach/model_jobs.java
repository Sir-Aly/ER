package com.example.easyreach;



public class model_jobs {
    String jDescription, jLocation,jRequirements,jSalary,jTitle, jobImage, pUid;

    public model_jobs(){
    }
    public model_jobs(String jDescription, String jLocation,String jRequirements, String jSalary, String jTitle, String jobImage,String pUid){
        this.jDescription = jDescription;
        this.jLocation = jLocation;
        this.jRequirements = jRequirements;
        this.jSalary = jSalary;
        this.jTitle = jTitle;
        this.jobImage = jobImage;
        this.pUid = pUid;
    }
    public String getjDescription() {
        return jDescription;
    }
    public String getjRequirements(){
        return jRequirements;
//hi
    }
//bot
    public String getjSalary(){
        return jSalary;

    }

    public String getjTitle(){
        return jTitle;

    }

    public String getjLocation(){
        return jLocation;

    }
    //// setters



    public String setjDescription(){
        return jDescription;
    }
    public String setjLocation(){
        return jLocation;
    }

    public String setjjRequirements(){
        return jLocation;
    }

    public String setjSalary(){
        return jLocation;
    }

    public String setjTitle(){
        return jLocation;
    }
    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }
}

