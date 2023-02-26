package com.example.ecommerce;

import java.sql.Date;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.text.Text;

import javafx.stage.Stage;



public class Ecommerce extends Application {

    User currentUser;
    BorderPane pane;
    TilePane body;

    Cart cart=null;

    private GridPane createHeader(){
        GridPane header = new GridPane();
        TextField textField = new TextField();
        Button viewCart = new Button("View Cart");

        Button viewOrders = new Button("View Orders");

        viewCart.setOnAction(e->{
            //TODO- add pop up for NULL CART
            this.body.getChildren().clear();
            this.body.getChildren().addAll(createCartPage());
        });

        viewOrders.setOnAction(e->{
            if(this.currentUser==null) {
                Alert alert = new Alert(Alert.AlertType.NONE,"Please login first", ButtonType.OK);
                alert.show();
                return;
            }
            this.body.getChildren().clear();
            this.body.getChildren().addAll(createOrderTable());
        });



        Button search = new Button("search");

        search.setOnAction(e->{
            String searchQuery = textField.getText();
            this.body.getChildren().clear();
            this.body.getChildren().add(createProductDisplay("select * from products where name like " + "'%" + searchQuery + "%'"));

        });

        //container for search button  and text-field
        GridPane searchBar = new GridPane();
        GridPane buttonContainer = new GridPane();
        buttonContainer.add(viewOrders,0,0);
        buttonContainer.add(viewCart,1,0);

        searchBar.add(textField,0,0);
        searchBar.add(search,1,0);

//        header.add(textField,0,0);
//        header.add(search,1,0);
        header.add(searchBar,0,0);
        header.add(buttonContainer,1,0);
        header.setHgap(160);

        GridPane.setMargin(searchBar, new Insets(10.00, 10.00, 10.00, 10.00));
        GridPane.setMargin(buttonContainer, new Insets(10.00, 10.00, 10.00, 10.00));
        GridPane.setMargin(viewOrders, new Insets(0, 5.00, 10.00, 10.00));
        GridPane.setMargin(viewCart, new Insets(0, 5.00, 10.00, 10.00));


        return header;

    }
    private GridPane createCartPage() {

        GridPane mainGrid = new GridPane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(200, 450);

        Button home = new Button("HomePage");
        home.setOnAction(e->{
            this.body.getChildren().clear();
            this.pane.setCenter(createBody()); // reset body

        });

        if(cart==null || cart.getCartContents().isEmpty()){

            GridPane emptyCartPage = new GridPane();
            Text heading = new Text("Your Cart Is Currently Empty");
            heading.setFont(new Font("Verdana", 20));
            emptyCartPage.add(heading,0,0);
            emptyCartPage.add(home,0,1);
            GridPane.setHalignment(home, HPos.CENTER);
            emptyCartPage.setVgap(10.00);
//            scrollPane.setContent(emptyCartPage);

            return emptyCartPage;

        }

        List<Product> productList = this.cart.getCartContents();
        Text heading = new Text("Products Currently in the cart");
        heading.setFont(new Font("Verdana", 20));
        mainGrid.add(heading,0,0);


        GridPane productGird = new GridPane(); // grid of all products


        for (int i=0;i<productList.size();i++) {
            // text nodes for attributes
            Product product = productList.get(i);

            Text name = new Text(product.getName());
            Text price = new Text("₹ " + product.getPrice());
            name.setFont(new Font("Verdana", 20));
            price.setFont(new Font("Verdana Italics", 15));

            ImageView imageView = new ImageView(new Image("/laptop2.jpg"));
            imageView.setFitWidth(120.00);
            imageView.setFitHeight(120.00);

            Button removeButton = new Button("Remove");

            removeButton.setOnAction(e->{
                this.cart.remove(product);
                this.body.getChildren().clear();
                this.body.getChildren().add(createCartPage());

            });

            GridPane productInfo = new GridPane(); // container for image and details grid
            productInfo.add(imageView, 0, 1);

            GridPane productDetails = new GridPane(); // details grid
            productDetails.add(name,1,0);
            productDetails.add(price,1,1);
            productDetails.add(removeButton,1,3);
            productDetails.setVgap(5.00);

            productInfo.add(productDetails,1,1);

            // set margins
            productInfo.setVgap(10.0);
            productInfo.setHgap(10.00);



            productGird.add(productInfo,0,i+1);
            GridPane.setMargin(productInfo, new Insets(10.00, 10.00, 10.00, 10.00));
        }
        Button checkout = new Button("Checkout");

        checkout.setOnAction(e->{
            boolean success =  this.cart.checkout(currentUser.id);


            if(success) {
                // create a alert
                Alert a = new Alert(Alert.AlertType.NONE, "Order Placed Successfully", ButtonType.CLOSE);
                a.show();
                this.body.getChildren().clear();
                this.body.getChildren().addAll(createCartPage());
            }

        });

        GridPane.setHalignment(checkout, HPos.LEFT);
        GridPane.setHalignment(home, HPos.LEFT);


        // add buttons
        mainGrid.add(checkout,0,2);
        mainGrid.add(home,1,2);


//        GridPane.setMargin(checkout, new Insets(10.00, 10.00, 10.00, 10.00));


        // wrap product grid in a scrollpane to allow scrolling

        scrollPane.setContent(productGird);
        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainGrid.add(scrollPane,0,1);

        return mainGrid;
    }
    private GridPane createSignUpForm(){

        GridPane signUpForm = new GridPane();

        //username
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter New UserName");

        //email
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Your Email Id");

        //password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter New Password");


        Button signUpButton = new Button("Sign Up");
        signUpButton.setOnAction(e->{
            this.currentUser = HandleAuthentication.signUp(userNameField.getText(),passwordField.getText(),emailField.getText());
            this.pane.setCenter(createBody());
        });
        signUpButton.setAlignment(Pos.CENTER);

        // Login form link
        Text loginFormLink = new Text();
        loginFormLink.setText("Click Here to Login");
        loginFormLink.setFont( new Font("Verdana", 10));
        loginFormLink.setFill(Color.BLUE);
        loginFormLink.setUnderline(true);


        loginFormLink.setOnMouseClicked(e->{ //  event handler to render the login form
            if(e.getSource()==loginFormLink)
                this.body.getChildren().clear();
            this.body.getChildren().add(createloginForm());
        });

        //adding all the fields to the grid
        signUpForm.add(userNameField,0,1);
        signUpForm.add(emailField,0,2);

        signUpForm.add(passwordField,0,3);
        signUpForm.add(signUpButton,0,4);

        signUpForm.add(loginFormLink,0,5);

        signUpForm.setAlignment(Pos.CENTER);
        signUpForm.setVgap(10);

        GridPane.setHalignment(signUpButton, HPos.CENTER);
        GridPane.setHalignment(loginFormLink, HPos.CENTER);

        return signUpForm;
    }
    private GridPane createloginForm(){

        GridPane loginForm = new GridPane();

        //username
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter UserName");

        //password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        // login button
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e->{
            this.currentUser =  HandleAuthentication.authenticate(userNameField.getText(),passwordField.getText());
            System.out.println(currentUser);
            this.pane.setCenter(createBody());

        });
        loginButton.setAlignment(Pos.CENTER);

        // signup form link
        Text signUpFormLink = new Text();
        signUpFormLink.setText("Click here to Sign UP");
        signUpFormLink.setFont( new Font("Verdana", 10));
        signUpFormLink.setFill(Color.BLUE);
        signUpFormLink.setUnderline(true);


        signUpFormLink.setOnMouseClicked(e->{ // signup event handler to render the signup form
            if(e.getSource()==signUpFormLink)
                this.body.getChildren().clear();
            this.body.getChildren().add(createSignUpForm());
        });



        loginForm.add(userNameField,0,1);
        loginForm.add(passwordField,0,2);
        loginForm.add(loginButton,0,3);
        loginForm.add(signUpFormLink,0,4);



        loginForm.setVgap(10);

        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(signUpFormLink,HPos.CENTER);
        loginForm.setAlignment(Pos.CENTER);




        return loginForm;
    }

    private TableView<Order> createOrderTable(){
        TableView<Order> ordersTable = new TableView<>();
//
//        ordersTable.prefWidth(800.00);
//        ordersTable.prefHeight(800.00);

        ordersTable.setPrefSize( 500, 450 );

        TableColumn<Order, Date> dateColumn  = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Order,Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order,String> numProductsColumn = new TableColumn<>("Products");
        numProductsColumn.setCellValueFactory(new PropertyValueFactory<>("allProductNames"));

        TableColumn<Order,Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));


        ordersTable.getColumns().add(dateColumn);
        ordersTable.getColumns().add(idColumn);
        ordersTable.getColumns().add(numProductsColumn);
        ordersTable.getColumns().add(totalColumn);

        List<Order> allOrders = Order.getAllOrder(this.currentUser.id);



        for(Order order: allOrders)  ordersTable.getItems().add(order); // add all orders to table


        return ordersTable;
    }
    private Pane createBody(){
         this.body = new TilePane();

        if(currentUser==null) body.getChildren().add(createloginForm());
        else body.getChildren().add(createProductDisplay());
//        this.body.getChildren().add(createOrderTable());
        body.setAlignment(Pos.CENTER);
        return body;
    }

    private TilePane createProductDisplay(){
        return createProductDisplay("select * from products");
    }
    private TilePane createProductDisplay(String query){
        TilePane productGird = new TilePane();
        ProductList productList = new ProductList(query);
        for(Product product: productList.getProductList()){
            // text nodes for attributes
            Text name = new Text(product.getName());
            Text price = new Text("₹ "+product.getPrice());
            name.setFont(new Font("Verdana",20));
            price.setFont(new Font("Verdana Italics",15));

            ImageView imageView = new ImageView(new Image("/laptop2.jpg"));
            imageView.setFitWidth(120.00);
            imageView.setFitHeight(120.00);

            Button addToCart = new Button("Add To Cart");

            addToCart.setOnAction(e->{
                if(cart == null) this.cart = new Cart();
                this.cart.add(product);
                System.out.println(product.getId());
                Alert alert = new Alert(Alert.AlertType.NONE, "Item added to Cart", ButtonType.OK);
                alert.show();
            });


            GridPane productInfo = new GridPane();
            productInfo.add(imageView,1,1);
            productInfo.add(name,1,2);
            productInfo.add(price,1,3);
            productInfo.add(addToCart,1,4);

            GridPane.setHalignment(addToCart,HPos.CENTER);
            GridPane.setHalignment(price,HPos.CENTER);
            productInfo.setVgap(10.0);


            productGird.getChildren().add(productInfo);
            TilePane.setMargin(productInfo,new Insets(10.00,10.00,10.00,10.00));
        }
        productGird.setPrefColumns(3);
//        productGird.setAlignment(Pos.CENTER);


        return productGird;


    }
    private BorderPane createContent(){
        BorderPane pane = new BorderPane();

        GridPane header = createHeader();
        pane.setTop(header);
        pane.setCenter(createBody());
        this.pane = pane;
        return pane;
    }
    @Override
    public void start(Stage stage) {


        Scene scene = new Scene(createContent(),600,600);
        stage.setTitle("Electronics For Sale");
        stage.setScene(scene);
//        stage.setResizable(false);
        stage.show();
        System.out.println(this.currentUser);
    }

    public static void main(String[] args) {
        launch();
    }
}