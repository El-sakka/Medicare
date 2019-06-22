package com.sakkawy.medicare.Model;

public class ItemHome {
    int iconId;
    String text;

    public ItemHome() {
    }

    public ItemHome(int iconId, String text) {
        this.iconId = iconId;
        this.text = text;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "iconID : "+iconId +" text : "+text;
    }
}
