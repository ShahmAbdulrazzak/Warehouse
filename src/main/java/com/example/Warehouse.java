package com.example;

import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Warehouse {
    //TODO: Change names
    /*
     * Why seperate JSON files from java classes? Simplicity reasons.
     * 
     * Use maps for efficiency (future addition to code)
     * 
     * 
     * 
     * CON: Seperate classes 
     * 
     * 
     */

    static List<Article> inventoryArticles = new ArrayList<>();
    static List<Product> allProducts = new ArrayList<>();
    
    public static void main(String [] args) throws IOException, ParseException{
        readFromInventoryFile("src/main/resources/inventory.json");
        readFromProductsFile("src/main/resources/products.json");

        printInventory();
        //printProducts();
        
        //printProductQuantity();

        Map<String, Integer> inventoryMap = new HashMap<>();
        for (Article item : inventoryArticles) {
            inventoryMap.put(item.getArtId(), item.getStock());
        }

        calculateProductQuantities(allProducts, inventoryMap);

        //sellProduct("Dinning Table", allProducts, inventoryMap);

        sellProduct("Dining Chair", allProducts, inventoryMap);
        sellProduct("Dining Chair", allProducts, inventoryMap);
        sellProduct("Dinning Table", allProducts, inventoryMap);

        calculateProductQuantities(allProducts, inventoryMap);
    }

    public static void readFromInventoryFile(String filePath) throws IOException, ParseException{
        //TODO Learn what these do 
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filePath);
        Object obj = jsonParser.parse(reader);
        JSONObject empjsonobj = (JSONObject) obj;
        JSONArray array = (JSONArray)empjsonobj.get("inventory");

        for(int i = 0; i < array.size(); i++)
        {
            //Get the 4 elements we have
            JSONObject inventories = (JSONObject) array.get(i); 
            String art_id = (String) inventories.get("art_id");
            String name = (String) inventories.get("name");
            String stock = (String) inventories.get("stock");

            Article article = new Article(art_id, name, Integer.valueOf(stock));

            inventoryArticles.add(article);
        }
    }

    public static void readFromProductsFile(String filePath) throws IOException, ParseException{
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filePath);
        Object obj = jsonParser.parse(reader);
        JSONObject empjsonobj = (JSONObject) obj;
        JSONArray array = (JSONArray) empjsonobj.get("products");
        
        for(int i = 0; i < array.size(); i++){
            JSONObject products = (JSONObject) array.get(i);
            JSONArray secondArray = (JSONArray) products.get("contain_articles");
            
            String name = (String) products.get("name");

            List<Article> tempList = new ArrayList<>();
            
            for(int j = 0; j < secondArray.size(); j++){                
                JSONObject articles = (JSONObject) secondArray.get(j);
                String art_id = (String) articles.get("art_id");
                String amount_of = (String) articles.get("amount_of");

                Article tempArticle = new Article(art_id, null, Integer.valueOf(amount_of));
                
                tempList.add(tempArticle);
            }
            Product product = new Product(name, tempList);
            allProducts.add(product);
        }
    }

    public static void printInventory(){
        for(int i = 0; i < inventoryArticles.size(); i++){
            System.out.println(inventoryArticles.get(i));
        }
    }

    public static void printProducts(){
        for(int i = 0; i < allProducts.size(); i++){
            System.out.println(allProducts.get(i));
        }
    }   


    static void calculateProductQuantities(List<Product> products, Map<String, Integer> inventoryMap) {
        for (Product product : products) {
            int maxQuantity = Integer.MAX_VALUE;
            for (Article article : product.getContainArticles()) {
                int stock = inventoryMap.getOrDefault(article.getArtId(), 0);
                maxQuantity = Math.min(maxQuantity, stock / article.getStock());
            }
            System.out.println(product.getName() + ": " + maxQuantity);
        }
    }

    //TODO Go through explanation of what it does. 
    static void sellProduct(String productName, List<Product> products, Map<String, Integer> inventoryMap) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                boolean canSell = true;
                for (Article article : product.getContainArticles()) {
                    int stock = inventoryMap.getOrDefault(article.getArtId(), 0);
                    if (stock < article.getStock()) {
                        canSell = false;
                        break;
                    }
                }
                if (canSell) {
                    for (Article article : product.getContainArticles()) {
                        int stock = inventoryMap.get(article.getArtId());
                        inventoryMap.put(article.getArtId(), stock - article.getStock());
                    }
                    System.out.println("Sold one " + product.getName());
                } else {
                    System.out.println("Not enough inventory to sell one " + product.getName());
                }
                return;
            }
        }
        System.out.println("Product " + productName + " not found");
    }

}