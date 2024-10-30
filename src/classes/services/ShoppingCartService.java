package classes.services;

import classes.Product;
import classes.PetFood;
import others.DatabaseConfiguration;
import classes.ShoppingCart;
import others.AuditHelper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartService {
    // Singleton instance
    private static ShoppingCartService instance;
    // Database connection
    private Connection connection;
    // Audit helper
    private AuditHelper auditHelper;

    // Private constructor to prevent instantiation
    private ShoppingCartService() throws IOException {
        this.connection = DatabaseConfiguration.getDatabaseConnection();
        this.auditHelper = AuditHelper.getInstance();
    }

    // Public method to provide access to the instance
    public static synchronized ShoppingCartService getInstance() throws IOException {
        if (instance == null) {
            instance = new ShoppingCartService();
        }
        return instance;
    }

    public void createShoppingCart(double totalPrice, Double voucherPercentageValue, boolean hasVoucher,
                                   boolean transactionWasFinalised) {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not initialized.");
        }

        String insertShoppingCartSql = "INSERT INTO ShoppingCart(totalPrice, voucherPercentageValue, " +
                "hasVoucher, transactionWasFinalised) VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertShoppingCartSql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, totalPrice);
            preparedStatement.setObject(2, voucherPercentageValue);
            preparedStatement.setBoolean(3, hasVoucher);
            preparedStatement.setBoolean(4, transactionWasFinalised);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int cartCode = generatedKeys.getInt(1);
                auditHelper.addAction("Created ShoppingCart with Code: " + cartCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShoppingCartByCode(int cartCode) {
        String deleteShoppingCartSql = "DELETE FROM ShoppingCart WHERE cartCode = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteShoppingCartSql);
            preparedStatement.setInt(1, cartCode);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                auditHelper.addAction("Deleted ShoppingCart with Code: " + cartCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProductsByShoppingCartCode(int cartCode) {
        List<Product> products = new ArrayList<>();
        String selectSql = "SELECT pf.productID FROM PetFood pf " +
                "INNER JOIN ProductList pl ON pf.productID = pl.foodID " +
                "WHERE pl.cartCode = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, cartCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            PetFoodService petFoodService = PetFoodService.getInstance();

            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                Product petFood = petFoodService.getPetFoodById(productID);
                if (petFood != null) {
                    products.add(petFood);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    public void updateShoppingCart(int cartCode, Double voucherPercentageValue, boolean hasVoucher) {
        String updateShoppingCartSql = "UPDATE ShoppingCart SET voucherPercentageValue=?, hasVoucher=? WHERE cartCode=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateShoppingCartSql);
            preparedStatement.setObject(1, voucherPercentageValue);
            preparedStatement.setBoolean(2, hasVoucher);
            preparedStatement.setInt(3, cartCode);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Updated ShoppingCart with Code: " + cartCode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ShoppingCart getShoppingCartByCode(int cartCode) {
        String selectSql = "SELECT * FROM ShoppingCart WHERE cartCode=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, cartCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToShoppingCart(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ShoppingCart mapToShoppingCart(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int cartCode = resultSet.getInt("cartCode");
            double totalPrice = resultSet.getDouble("totalPrice");
            double voucherPercentageValue = resultSet.getDouble("voucherPercentageValue");
            boolean hasVoucher = resultSet.getBoolean("hasVoucher");
            boolean transactionWasFinalised = resultSet.getBoolean("transactionWasFinalised");

            List<Product> products = getProductsByShoppingCartCode(cartCode);

            return new ShoppingCart(totalPrice, cartCode, voucherPercentageValue, hasVoucher, products, transactionWasFinalised);
        }
        return null;
    }

////
//    public ShoppingCart addProductsToCart(ShoppingCart cart, List<String> productIDs) {
//        // Check if the cart exists
//        if (cart == null) {
//            System.out.println("Shopping cart is null. Cannot add products.");
//            return null;
//        }
//
//        // Initialize the product list if it's null
//        if (cart.getProducts() == null) {
//            cart.setProducts(new ArrayList<>());
//        }
//
//        // Get the PetFoodService instance
//        PetFoodService petFoodService = null;
//        try {
//            petFoodService = PetFoodService.getInstance();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null; // Failed to get PetFoodService instance, return null
//        }
//
//        // Iterate through the list of product IDs
//        for (String productID : productIDs) {
//            // Check if the product is already in the cart
//            if (containsProduct(cart, productID)) {
//                System.out.println("Product with ID " + productID + " is already in the shopping cart. Skipping...");
//                continue;
//            }
//
//            // Get the pet food from the PetFoodService using its ID
//            Product petFood = petFoodService.getPetFoodById(productID);
//            if (petFood == null) {
//                System.out.println("Pet food with ID " + productID + " does not exist in the database. Skipping...");
//                continue;
//            }
//
//            // Add the pet food to the shopping cart
//            cart.addProduct(petFood);
//
//            // Insert a record in the ProductList table
//            insertProductListEntry(cart.getCartCode(), productID); // Assuming getCode() returns the cart code
//        }
//
//        // Update the total price of the shopping cart
//        cart.verifyCartPrice();
//
//        // Return the updated shopping cart
//        return cart;
//    }

    public void addProductsToCart(int cartCode, List<String> productIDs) {
        // Check if the cart exists
        ShoppingCart cart = getShoppingCartByCode(cartCode);
        if (cart == null) {
            System.out.println("Shopping cart with code " + cartCode + " does not exist.");
            return;
        }

        // Initialize the product list if it's null
        if (cart.getProducts() == null) {
            cart.setProducts(new ArrayList<>());
        }

        // Get the PetFoodService instance
        PetFoodService petFoodService = null;
        try {
            petFoodService = PetFoodService.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            return; // Failed to get PetFoodService instance, return without adding products
        }

        // Iterate through the list of product IDs
        for (String productID : productIDs) {
            // Check if the product is already in the cart
            if (containsProduct(cart, productID)) {
                System.out.println("Product with ID " + productID + " is already in the shopping cart. Skipping...");
                continue;
            }

            // Get the pet food from the PetFoodService using its ID
            Product petFood = petFoodService.getPetFoodById(productID);
            if (petFood == null) {
                System.out.println("Pet food with ID " + productID + " does not exist in the database. Skipping...");
                continue;
            }

            // Add the pet food to the shopping cart
            cart.addProduct(petFood);

            // Insert a record in the ProductList table
            insertProductListEntry(cartCode, productID); // Assuming getCode() returns the cart code
        }

        // Update the total price of the shopping cart
        cart.verifyCartPrice();
    }


    public boolean containsProduct(ShoppingCart cart, String productID) {
        List<Product> products = cart.getProducts();
        if (products == null) {
            return false;
        }
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                return true;
            }
        }
        return false;
    }

    public void insertProductListEntry(int cartCode, String productID) {
        String insertSql = "INSERT INTO ProductList (cartCode, foodID) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);

            preparedStatement.setInt(1, cartCode);
            preparedStatement.setString(2, productID);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Product with ID " + productID + " successfully added to cart with code " + cartCode);
            } else {
                System.out.println("Failed to add product with ID " + productID + " to cart with code " + cartCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occurred while inserting product into ProductList.");
        }
    }


    //work in progress

//    public void deleteProductFromCart(int cartCode, String identifier) {
//        // Check if the cart exists
//        ShoppingCart cart = getShoppingCartByCode(cartCode);
//        if (cart == null) {
//            System.out.println("Shopping cart with code " + cartCode + " does not exist.");
//            return;
//        }
//
//        // Search for the product in the cart by name or ID
//        PetFoodService productService = PetProductService.getInstance();
//        PetFood productToRemove = productService.getProductByNameOrId(identifier);
//
//        // Remove the product if found
//        if (productToRemove != null && cart.getProducts().contains(productToRemove)) {
//            cart.getProducts().remove(productToRemove);
//            System.out.println("Pet food '" + identifier + "' removed from the shopping cart.");
//            // Also delete the corresponding entry from the ProductList table
//            deleteProductListEntry(cartCode, productToRemove.getAnimalID(), productToRemove.getProductID());
//            // Update the total price of the shopping cart
//            cart.verifyCartPrice();
//        } else {
//            System.out.println("Pet food '" + identifier + "' not found in the shopping cart.");
//        }
//    }

    private void deleteProductListEntry(int cartCode, Integer animalID, String productID) {
        String deleteProductListSql = "DELETE FROM ProductList WHERE cartCode = ? AND animalID = ? AND foodID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteProductListSql);
            preparedStatement.setInt(1, cartCode);
            if (animalID != null) {
                preparedStatement.setInt(2, animalID);
            } else {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(3, productID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
