package com.example.ecommerce;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> contents;
    double total;

    Cart(){
        this.contents = new ArrayList<>();
        this.total = 0;

    }
    public List<Product> getCartContents(){
        return this.contents;
    }
    public void clearCart(){
        this.contents.clear();
    }
    public void add(Product product){
        this.contents.add(product);
        this.total+= product.getPrice();

    }
    public void remove(Product product){
        this.contents.remove(product);
    }

    public boolean checkout(int user_id){

        DatabaseConnection dbconn = new DatabaseConnection();


        // create a new order
        try {
            BigInteger oid = dbconn.updateTable("insert into orders (user_id, order_total) values( " + user_id + "," + total + " )",true);
//            ResultSet resultSet = dbconn.getQueryTable("SELECT LAST_INSERT_ID();");
//            int oid=-1;
//            if(resultSet!=null && resultSet.next())  oid = resultSet.getInt("LAST_INSERT_ID()");
            System.out.println(oid);
            for(Product product: this.getCartContents()) {
                System.out.println(product.getId());
                dbconn.updateTable("insert into order_items ( order_id , product_id) values ( " + oid + " , " + product.getId() + " )");

            }
            clearCart();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }
}
