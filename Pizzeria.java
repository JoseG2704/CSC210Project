package com.example.pizzaproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.HashMap;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;


public class Pizzeria extends Application {
    Stage stage1;
    TextField textField1; // Username input field
    TextField textField2; // Password input field

    @Override
    public void start(Stage primaryStage) {
        // Create UI elements for login screen
        Text text1 = new Text("Username");
        Text text2 = new Text("Password");

        textField1 = new TextField();
        textField2 = new TextField();

        Button button1 = new Button("Login");
        Button button2 = new Button("Clear");

        // Set login button handler
        button1.setOnAction(new CalButtonHandler());

        // Layout for login screen
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        // Add UI elements to login grid
        gridPane.add(text1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textField2, 1, 1);
        gridPane.add(button1, 1, 2);
        gridPane.add(button2, 1, 3);

        // Create and show login scene
        Scene s1 = new Scene(gridPane);
        primaryStage.setScene(s1);
        primaryStage.setTitle("Pizzeria login");
        primaryStage.show();
    }

    class CalButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Validate login credentials
            if (textField1.getText().equals("Baskin") && textField2.getText().equals("100 Chambers")) {
                displayMenu(); // Show pizza menu if login is correct
            } else {
                // Show error alert if login fails
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid credentials!");
                alert.setContentText("Please try again with the correct username and password.");
                alert.showAndWait();
            }
        }

        // Displays the pizza ordering interface
        public void displayMenu() {
            GridPane gridPane2 = new GridPane();
            gridPane2.setMinSize(600, 400);
            gridPane2.setPadding(new Insets(10));
            gridPane2.setVgap(10);
            gridPane2.setHgap(10);
            gridPane2.setAlignment(Pos.CENTER);
            gridPane2.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW,null,null)));

            // Load pizza data
            PizzaStore store = new PizzaStore();
            String[] pizzaNames = store.getItemNames();
            HashMap<String, Integer> quantities = new HashMap<>(); // Tracks quantity per pizza
            HashMap<String, Text> quantityTextFields = new HashMap<>(); // UI fields for quantities

            // Texts for subtotal, tax, total
            Text subtotalText = new Text("Subtotal: $0.00");
            Text taxText = new Text("Tax (8%): $0.00");
            Text totalText = new Text("Total: $0.00");

            // Add table headers
            gridPane2.add(new Text("Item"), 1, 0);
            gridPane2.add(new Text("Pizza"), 2, 0);
            gridPane2.add(new Text("Price"), 3, 0);
            gridPane2.add(new Text("Quantity"), 4, 0);

            // Loop to display each pizza item
            for (int i = 0; i < pizzaNames.length; i++) {
                String pizza = pizzaNames[i];
                double price = store.getPrices(pizza);

                // Display initial quantity as 0
                Text quantityText = new Text("0");
                quantities.put(pizza, 0);
                quantityTextFields.put(pizza, quantityText);

                // "+" and "-" buttons to modify quantity
                Button plus = new Button("+");
                Button minus = new Button("-");

                int row = i + 1;

                // "+" button increases quantity
                plus.setOnAction(e -> {
                    int current = Integer.parseInt(quantityText.getText());
                    quantityText.setText(String.valueOf(current + 1));
                    quantities.put(pizza, current + 1);
                    updateTotals(quantities, store, subtotalText, taxText, totalText);
                });

                // "-" button decreases quantity
                minus.setOnAction(e -> {
                    int current = Integer.parseInt(quantityText.getText());
                    if (current > 0) {
                        quantityText.setText(String.valueOf(current - 1));
                        quantities.put(pizza, current - 1);
                        updateTotals(quantities, store, subtotalText, taxText, totalText);
                    }
                });

                // Add pizza row to grid
                gridPane2.add(new Text(String.valueOf(i + 1)), 1, row);
                gridPane2.add(new Text(pizza), 2, row);
                gridPane2.add(new Text("$" + String.format("%.2f", price)), 3, row);
                gridPane2.add(quantityText, 4, row);
                gridPane2.add(plus, 5, row);
                gridPane2.add(minus, 6, row);
            }

            // Add total labels
            int footerRow = pizzaNames.length + 2;
            gridPane2.add(subtotalText, 4, footerRow);
            gridPane2.add(taxText, 4, footerRow + 1);
            gridPane2.add(totalText, 4, footerRow + 2);

            // Clear button resets all selections
            Button clearBtn = new Button("Clear");
            clearBtn.setOnAction(e -> {
                for (String pizza : pizzaNames) {
                    quantities.put(pizza, 0);
                    quantityTextFields.get(pizza).setText("0");
                }
                updateTotals(quantities, store, subtotalText, taxText, totalText);
            });

            // Purchase button displays order summary in an alert
            Button purchaseBtn = new Button("Purchase");
            purchaseBtn.setOnAction(e -> {
                StringBuilder summary = new StringBuilder("Order Summary:\n\n");
                double subtotal = 0.0;

                // Build summary of each ordered item
                for (String pizza : pizzaNames) {
                    int qty = quantities.get(pizza);
                    if (qty > 0) {
                        double itemTotal = store.getPrices(pizza) * qty;
                        subtotal += itemTotal;
                        summary.append(pizza)
                                .append(" x")
                                .append(qty)
                                .append(" = $")
                                .append(String.format("%.2f", itemTotal))
                                .append("\n");
                    }
                }

                // Calculate final totals
                double tax = subtotal * 0.08;
                double total = subtotal + tax;

                // Append totals to summary
                summary.append("\nSubtotal: $").append(String.format("%.2f", subtotal));
                summary.append("\nTax (8%): $").append(String.format("%.2f", tax));
                summary.append("\nTotal: $").append(String.format("%.2f", total));
                summary.append("\n\nThank you for your purchase!");

                // Show confirmation alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Order Confirmation");
                alert.setHeaderText("Purchase Successful");
                alert.setContentText(summary.toString());
                alert.showAndWait();
            });

            // Add control buttons to layout
            gridPane2.add(clearBtn, 4, footerRow + 3);
            gridPane2.add(purchaseBtn, 5, footerRow + 3);

            //importing images

            String[] images = {"/images/PepperoniPizza.jpg","/images/CheesePizza.jpg", "/images/Chicken Buffalo Pizza.jpg","/images/Hawaiian Pizza.jpg"};
            for(int i = 0; i < images.length; i++) {
                //try to load the images
                try{
                    Image image = new Image(getClass().getResource(images[i]).toExternalForm());
                    ImageView imageView = new ImageView(image);
                    gridPane2.add(imageView, 0, i + 1);
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(60);
                }catch (Exception e) { //handling the exceptions that may be found if image isn't loaded
                    System.out.println(e);
                }
            }



            // Create and display the pizza ordering scene
            Scene scene1 = new Scene(gridPane2);
            stage1 = new Stage();
            stage1.setScene(scene1);
            stage1.setTitle("Pizza Menu");
            stage1.show();
        }

        // Calculates and updates totals
        public void updateTotals(HashMap<String, Integer> quantities, PizzaStore store,
                                 Text subtotalText, Text taxText, Text totalText) {
            double subtotal = 0.0;
            for (String pizza : quantities.keySet()) {
                subtotal += store.getPrices(pizza) * quantities.get(pizza);
            }
            double tax = subtotal * 0.08;
            double total = subtotal + tax;

            // Update display
            subtotalText.setText("Subtotal: $" + String.format("%.2f", subtotal));
            taxText.setText("Tax (8%): $" + String.format("%.2f", tax));
            totalText.setText("Total: $" + String.format("%.2f", total));
        }

        // Application entry point
        public static void main(String args[]) {
            launch(args);
        }
    }
}


