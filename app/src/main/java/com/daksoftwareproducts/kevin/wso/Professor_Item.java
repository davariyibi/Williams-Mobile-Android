package com.daksoftwareproducts.kevin.wso;

/**
 * Created by Kevin on 8/9/2017.
 */

public class Professor_Item {

    private String name;
    private String id;

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public String getString() {
        return name + ":\n" + id;
    }

}
