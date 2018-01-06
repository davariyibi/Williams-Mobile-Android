package com.daksoftwareproducts.kevin.wso;

/**
 * Created by Kevin on 10/10/2017.
 */

public class Meal_Item {

    private String name;
    private String tags = "None";

    public String getName(){
        return name;
    }
    public void setName( String name ){
        this.name = name;
    }
    public String getTags(){
        return tags;
    }
    public void setTags( String tags ){
        this.tags = tags;
    }

    public String getToString(){
        return "Name: "+getName()+"\n" + "Tags: "+ getTags() + "\n";
    }

}
