package com.example.ecommerce;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
    List<Product> productList;
    DatabaseConnection databaseConnection;

    public ProductList(){
        new ProductList("select * from products");
    }

    public ProductList(String query) {
        productList = new ArrayList<>();
        databaseConnection = new DatabaseConnection();
        ResultSet rs = databaseConnection.getQueryTable(query);

        try {
            while (rs != null && rs.next()) {
                productList.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")));
                System.out.println(productList.get(productList.size()-1).getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public List<Product> getProductList() {
        return productList;
    }
}
