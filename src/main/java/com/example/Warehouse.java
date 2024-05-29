package com.example;

import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Warehouse {
    //List that holds all articles in inventory.
    static List<Article> inventoryArticles = new ArrayList<>();
    //List that holds all products and what articles they need. 
    static List<Product> allProducts = new ArrayList<>();
    
    public static void main(String [] args) throws IOException, ParseException{
        loadInventoryFile("src/main/resources/inventory.json");
        loadProductsFile("src/main/resources/products.json");

        //printInventory();
        //printProducts();

        Map<String, Integer> inventoryMap = new HashMap<>();
        for (Article item : inventoryArticles) {
            inventoryMap.put(item.getArtId(), item.getStock());
        }

        calculateProductQuantities(allProducts, inventoryMap);

        sellProduct("Dining Chair", allProducts, inventoryMap);
        sellProduct("Dining Chair", allProducts, inventoryMap);
        sellProduct("Dinning Table", allProducts, inventoryMap);

        calculateProductQuantities(allProducts, inventoryMap);
    }


    public static void loadInventoryFile(String filePath) throws IOException, ParseException{
        //Parse the entire json file.       
        JSONParser jsonParser = new JSONParser();
        //Loading json file.
        FileReader reader = new FileReader(filePath);
        //Parse the data from reader to an object. 
        Object obj = jsonParser.parse(reader);
        //Convert java object to json object.
        JSONObject jsonObject = (JSONObject) obj;
        //Extract array under "inventory" (array)
        JSONArray jsonArray = (JSONArray)jsonObject.get("inventory");

        //Get the elements.
        for(int i = 0; i < jsonArray.size(); i++)
        {
            //Article under "inventory".
            JSONObject inventoryObject = (JSONObject) jsonArray.get(i); 

            //Extract art_id, name and stock.
            String art_id = (String) inventoryObject.get("art_id");
            String name = (String) inventoryObject.get("name");
            String stock = (String) inventoryObject.get("stock");

            //Create article in software with extracted data.
            Article article = new Article(art_id, name, Integer.valueOf(stock));
            //Add article to articles list. 
            inventoryArticles.add(article);
        }
    }

    public static void loadProductsFile(String filePath) throws IOException, ParseException{
        //Parse entire json file.
        JSONParser jsonParser = new JSONParser();
        //Load json file.
        FileReader reader = new FileReader(filePath);
        //Parse data from reader to object.
        Object obj = jsonParser.parse(reader);
        //Convert java object to json object. 
        JSONObject jsonObject = (JSONObject) obj;
        //Extract array under "products".
        JSONArray array = (JSONArray) jsonObject.get("products");
        
        //Loop through products
        for(int i = 0; i < array.size(); i++){
            //Product under "products"
            JSONObject products = (JSONObject) array.get(i);
            //Array within each product that names needed articles to build the product.
            JSONArray secondArray = (JSONArray) products.get("contain_articles");
            //Extract product name
            String name = (String) products.get("name");

            //Temp list of articles needed to build the product. 
            List<Article> tempList = new ArrayList<>();
            
            //Loop through products' "contain_articles".
            for(int j = 0; j < secondArray.size(); j++){              
                //Articles inside "contain_articles"  
                JSONObject articles = (JSONObject) secondArray.get(j);
                String art_id = (String) articles.get("art_id");
                String amount_of = (String) articles.get("amount_of");

                //Article needed to build the product
                Article tempArticle = new Article(art_id, null, Integer.valueOf(amount_of));
                
                //Add article to list of articles needed to build the product
                tempList.add(tempArticle);
            }
            //Add product to the software with its name and list of articles needed from inventory. 
            Product product = new Product(name, tempList);
            //Add product to list of products. 
            allProducts.add(product);
        }
    }

    //Print inventory 
    public static void printInventory(){
        for(int i = 0; i < inventoryArticles.size(); i++){
            System.out.println(inventoryArticles.get(i));
        }
    }

    //Print products
    public static void printProducts(){
        for(int i = 0; i < allProducts.size(); i++){
            System.out.println(allProducts.get(i));
        }
    }   


    
    /**
     * Get all products and quantity of each that is an available with the current inventory
     * 
     * @param products      List of products to be calculated.
     * @param inventoryMap  Map of article IDs to their available stock.
     */
    static void calculateProductQuantities(List<Product> products, Map<String, Integer> inventoryMap) {
        for (Product product : products) {
            //Initialize a large number
            int maxQuantity = Integer.MAX_VALUE;
            for (Article article : product.getContainArticles()) {
                int stock = inventoryMap.getOrDefault(article.getArtId(), 0);
                // Calculate the maximum quantity for this product based on current article stock
                maxQuantity = Math.min(maxQuantity, stock / article.getStock());
            }
            System.out.println(product.getName() + ": " + maxQuantity);
        }
    }

    /**
     * Remove(Sell) a product and update the inventory accordingly
     * If there is not enough inventory, print a message indicating the sale cannot be completed.
     * @param productName   Name of the product to be sold.
     * @param products      List of products.
     * @param inventoryMap  Map of article IDs to their available stock.
     */
    static void sellProduct(String productName, List<Product> products, Map<String, Integer> inventoryMap) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                boolean canSell = true;
                //Check if there's enough in inventory to sell product
                for (Article article : product.getContainArticles()) {
                    int stock = inventoryMap.getOrDefault(article.getArtId(), 0);
                    if (stock < article.getStock()) {
                        //Not enough in inventory
                        canSell = false;
                        break;
                    }
                }
                if (canSell) {
                    //Reduce inventory for every article
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