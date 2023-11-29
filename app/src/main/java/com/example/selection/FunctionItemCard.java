package com.example.selection;

public class FunctionItemCard {
    private String text;
    private int img;
    public FunctionItemCard(String text, int img){
        this.text = text;
        this.img = img;
    }
    public String getText(){
        return text;
    }
    public int getImg(){
        return img;
    }
}
