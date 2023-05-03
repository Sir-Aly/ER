package com.example.easyreach;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.google.firebase.firestore.Exclude;

public class Add {
        private String documentId;
        private String Name;
        private String Skills;
        private String Location;



        public Add() {
            //public no-arg constructor needed
        }

        @Exclude
        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public Add(String Name, String Skills,String Location) {
            this.Name =Name;
            this.Skills = Skills;
            this.Location=Location;

        }

        public String getSkills() {
            return Skills;
        }



        public String getName() {
            return Name;
        }


        public String getLocation(){

            return Location;
        }

    public void getFirstName(String FirstName) {
        this.Name = FirstName;
    }

    }

