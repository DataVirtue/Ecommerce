package com.example.ecommerce;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order  {

   private List<Product> products = new ArrayList<>();

   private String allProductNames;

   static List<Product> getOrderProducts(int oid,DatabaseConnection dbconn){
       List<Product> allOrderedProducts = new ArrayList<>();
       try {
           ResultSet rs = dbconn.getQueryTable("select * from order_items inner join products on order_items.product_id=products.id where order_items.order_id =" + oid);
           while (rs!=null && rs.next()){
               allOrderedProducts.add(new Product(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getInt("quantity"),
                       rs.getDouble("price")
               ));
           }
           return allOrderedProducts;
       }catch (SQLException e){
           e.printStackTrace();
       }
       return null;
   }
    static List<Order> getAllOrder(int user_id){
        List<Order>AllOrders = new ArrayList<>();
        try{
            DatabaseConnection dbconn = new DatabaseConnection();
            ResultSet rs = dbconn.getQueryTable("select * from orders where user_id=" + user_id);

            while(rs!=null && rs.next()){
                AllOrders.add(new Order(
                        rs.getDate("Created_At"),
                        rs.getInt("oid"),
                        rs.getDouble("order_total"),
                        getOrderProducts(rs.getInt("oid"),dbconn)
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return AllOrders;
    }
    private int id;
    private double total;

    private Date date;

//    public Order(long utc, int id, double total){}

    public Order(Date date, int id, double total,List<Product> products){
        this.id = id;
        this.total = total;
        this.date = date;
        this.products = products;

        StringBuilder allProductsNames = new StringBuilder(products.get(0).getName() +"");

        for(int i=1;i<this.products.size();i++) {
            allProductsNames.append(", " + products.get(i).getName());
        }
        this.allProductNames = allProductsNames.toString();



    }
    public String getAllProductNames(){
        return this.allProductNames;
    }
    public Integer getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public double getTotal(){
        return this.total;
    }
    public void setTotal(double total){
        this.total = total;
    }

    public Date getDate(){
        return this.date;
    }

}
