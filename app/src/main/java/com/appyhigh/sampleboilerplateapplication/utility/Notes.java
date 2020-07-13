package com.appyhigh.sampleboilerplateapplication.utility;

public class Notes {

    private String title;
    private String description;

    public Notes(){

    }

    public Notes(String title , String description){
       this.title = title;
       this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
