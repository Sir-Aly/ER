package com.example.easyreach;

public class JobOffer {
    private String jTitle;
    private String jRequirements;
    private String jLocation;
    private String JobImage;
    private String pEmail;
    private String pUid;


    public JobOffer() {
        // Default constructor required for Firebase
    }
    public JobOffer(String jTitle, String jRequirements, String jLocation, String JobImage, String pEmail, String pUid) {
        this.jTitle = jTitle;
        this.jRequirements = jRequirements;
        this.jLocation = jLocation;
        this.JobImage = JobImage;
        this.pEmail = pEmail;
        this.pUid = pUid;
    }

    public String getjTitle() {
        return jTitle;
    }

    public void setjTitle(String jTitle) {
        this.jTitle = jTitle;
    }

    public String getjRequirements() {
        return jRequirements;
    }

    public void setjRequirements(String jRequirements) {
        this.jRequirements = jRequirements;
    }

    public String getjLocation() {
        return jLocation;
    }

    public void setjLocation(String jLocation) {
        this.jLocation = jLocation;
    }

    public String getJobImage() {
        return JobImage;
    }

    public void setJobImage(String jobImage) {
        this.JobImage = jobImage;
    }

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

}
