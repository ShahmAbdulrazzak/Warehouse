package com.example;

public class Article {
    private String artId;
    private String name;
    private int stock;

    public Article(){

    }

    public Article(String artId, String name, int stock){
        this.artId = artId;
        this.name = name;
        this.stock = stock;
    }

    //Getters & Setters
    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getArtId(){
        return artId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock(){
        return stock; 
    }
    
    // Override the toString() method
    @Override
    public String toString() {
        return "Article{" +
                "artId='" + artId + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
    
}
