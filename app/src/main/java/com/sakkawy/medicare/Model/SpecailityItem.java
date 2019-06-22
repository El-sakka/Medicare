package com.sakkawy.medicare.Model;

public class SpecailityItem {
    private String name;
    private String id;
    private int icon;


    public SpecailityItem(int icon,String name,String id){
        this.name = name;
        this.id = id;
        this.icon = icon;
    }


    public SpecailityItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
