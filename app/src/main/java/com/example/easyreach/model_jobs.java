package com.example.easyreach;



public class model_jobs {
    String jDescription, jLocation,jRequirements,jSalary,jTitle;

    public model_jobs(){
    }
    public model_jobs(String jDescription, String jLocation,String jRequirements, String jSalary, String jTitle){
        this.jDescription = jDescription;
        this.jLocation = jLocation;
        this.jRequirements = jRequirements;
        this.jSalary = jSalary;
        this.jTitle = jTitle;
    }
    public String getjDescription() {
        return jDescription;
    }
    public String getjRequirements(){
        return jRequirements;

    }

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

}

