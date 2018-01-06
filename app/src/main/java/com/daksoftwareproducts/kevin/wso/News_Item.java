package com.daksoftwareproducts.kevin.wso;

/**
 * Created by Kevin on 6/18/2017.
 */

public class News_Item {
    private String title;
    private String description;

    public String getTitle(){
        return title;
    }

    public void setTitle( String title ){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription( String description){
        this.description = description;
    }

    public String getString() {
        return title + ":\n" + description;
    }
}
