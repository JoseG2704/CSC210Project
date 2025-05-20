package com.example.pizzaproject;

//Jose Garcia & Emily Biel
//CSC210
//Project: Building a Java Application to Manage a Pizza Store


//Class PizzaStore
class PizzaStore {

    //Login & Password information
    private final String[][] loginInfo = {{"Baskin", "100 Chambers"}};
    private final String[] itemNames = {"Pepperoni Pizza", "Cheese Pizza", "Chicken Buffalo Pizza", "Hawaiian Pizza"};

    //Item prices combined with the 8% tax rate
    private final double[] itemPrices = {12.99, 11.99, 13.99, 15.99};
    private final double Tax_Rate = 0.08;

    //Method to return Username
    public String getUsername() {
        return loginInfo[0][0];
    }

    //Method to return Password
    public String getPassword() {
        return loginInfo[0][1];
    }

    //
    public double getPrices(String itemName) {
        for (int i = 0; i < itemNames.length; i++) {
            if (itemNames[i].equalsIgnoreCase(itemName)) {
                return itemPrices[i];
            }
        }
        return -1; //If the item is not found
    }

    public String[] getItemNames() {
        return itemNames;
    }

    public double calculateCost(String[] selectedItems) {
        double total = 0.0;
        for (String item: selectedItems) {
            double price = getPrices(item);
            if (price != -1) {
                total += price;
            }
        }
        return total;

    }

    public double calculateTax(double subtotal) {
        return subtotal * Tax_Rate;
    }

    public double calculateTotalCost(String[] selectedItems) {
        double subtotal = calculateCost(selectedItems);
        double tax = calculateTax(subtotal);
        return subtotal + tax;

    }


}