package com.example;

import java.util.List;

public class Product {
    private String name; 
    //private double price; not available in JSON file
    private List<Article> containArticles; 

    public Product(String name, List<Article> containArticles){
        this.name = name;
        this.containArticles = containArticles;
    }

    //Getters & Setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setContainArticles(List<Article> containArticles) {
        this.containArticles = containArticles;
    }

    public List<Article> getContainArticles(){
        return containArticles; 
    }

    // Override the toString() method
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product{name='").append(name).append('\'');
        sb.append(", containArticles=");
        if (containArticles != null) {
            sb.append('[');
            for (int i = 0; i < containArticles.size(); i++) {
                sb.append(containArticles.get(i).toString());
                if (i < containArticles.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(']');
        } else {
            sb.append("null");
        }
        sb.append('}');
        return sb.toString();
    }
}
