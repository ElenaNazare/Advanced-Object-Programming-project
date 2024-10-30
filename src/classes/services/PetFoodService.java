package classes.services;

import classes.Brand;
import others.DatabaseConfiguration;
import classes.PetFood;
import others.AuditHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PetFoodService {
    // Singleton instance
    private static PetFoodService instance;
    // Database connection
    private Connection connection;
    // Audit helper
    private AuditHelper auditHelper;

    // Private constructor to prevent instantiation
    private PetFoodService() throws IOException {
        this.connection = DatabaseConfiguration.getDatabaseConnection();
        this.auditHelper = AuditHelper.getInstance();
    }

    // Public method to provide access to the instance
    public static synchronized PetFoodService getInstance() throws IOException {
        if (instance == null) {
            instance = new PetFoodService();
        }
        return instance;
    }

    public void createPetFood(String productID, String nameOfTheProduct, String description, double price,
                              boolean hasCoupon, double couponPercentageValue, String expirationDate, String brandName,
                              List<String> ingredientNames) {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not initialized.");
        }

        String insertPetFoodSql = "INSERT INTO PetFood(productID, nameOfTheProduct, description, price, hasCoupon, " +
                "couponPercentageValue, expirationDate, brandName) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertPetFoodSql);
            preparedStatement.setString(1, productID);
            preparedStatement.setString(2, nameOfTheProduct);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            preparedStatement.setBoolean(5, hasCoupon);
            preparedStatement.setDouble(6, couponPercentageValue);
            preparedStatement.setString(7, expirationDate);
            preparedStatement.setString(8, brandName);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Created PetFood: " + nameOfTheProduct);

            // Create and associate ingredients
            createIngredientList(productID, ingredientNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePetFoodById(String productID) {
        String deletePetFoodSql = "DELETE FROM PetFood WHERE productID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deletePetFoodSql);
            preparedStatement.setString(1, productID);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                auditHelper.addAction("Deleted PetFood with ID: " + productID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePetFoodByName(String nameOfTheProduct) {
        String deletePetFoodSql = "DELETE FROM PetFood WHERE nameOfTheProduct = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deletePetFoodSql);
            preparedStatement.setString(1, nameOfTheProduct);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                auditHelper.addAction("Deleted PetFood with name: " + nameOfTheProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PetFood getPetFoodByName(String nameOfTheProduct) {
        String selectSql = "SELECT * FROM PetFood WHERE nameOfTheProduct=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, nameOfTheProduct);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToPetFood(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PetFood getPetFoodById(String productID) {
        String selectSql = "SELECT * FROM PetFood WHERE productID=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, productID);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToPetFood(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createIngredientList(String productID, List<String> ingredientNames) {
        String insertIngredientSql = "INSERT INTO IngredientList(ingredientName, productID) VALUES(?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertIngredientSql);
            for (String ingredientName : ingredientNames) {
                // Check if the ingredient already exists
                if (!ingredientExists(ingredientName)) {
                    createIngredient(ingredientName);
                }
                // Associate ingredient with the product
                preparedStatement.setString(1, ingredientName);
                preparedStatement.setString(2, productID);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            auditHelper.addAction("Associated ingredients with PetFood: " + productID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean ingredientExists(String ingredientName) throws SQLException {
        String selectSql = "SELECT name FROM Ingredient WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        preparedStatement.setString(1, ingredientName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private void createIngredient(String ingredientName) throws SQLException {
        String insertIngredientSql = "INSERT INTO Ingredient(name) VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertIngredientSql);
        preparedStatement.setString(1, ingredientName);
        preparedStatement.executeUpdate();
        auditHelper.addAction("Created new ingredient: " + ingredientName);
    }

    private Set<String> getIngredientsByProductID(String productID) throws SQLException {
        Set<String> ingredients = new HashSet<>();
        String selectSql = "SELECT ingredientName FROM IngredientList WHERE productID=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setString(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ingredientName = resultSet.getString("ingredientName");
                ingredients.add(ingredientName);
            }
        }
        return ingredients;
    }

    public void updatePetFood(String productID, String nameOfTheProduct, String description, Double price,
                              Boolean hasCoupon, Double couponPercentageValue, String expirationDate, String brandName,
                              List<String> ingredientNames) {
        // Build the update SQL statement
        StringBuilder updatePetFoodSql = new StringBuilder("UPDATE PetFood SET ");
        List<Object> params = new ArrayList<>();

        if (nameOfTheProduct != null) {
            updatePetFoodSql.append("nameOfTheProduct=?, ");
            params.add(nameOfTheProduct);
        }
        if (description != null) {
            updatePetFoodSql.append("description=?, ");
            params.add(description);
        }
        if (price != null) {
            updatePetFoodSql.append("price=?, ");
            params.add(price);
        }
        if (hasCoupon != null) {
            updatePetFoodSql.append("hasCoupon=?, ");
            params.add(hasCoupon);
        }
        if (couponPercentageValue != null) {
            updatePetFoodSql.append("couponPercentageValue=?, ");
            params.add(couponPercentageValue);
        }
        if (expirationDate != null) {
            updatePetFoodSql.append("expirationDate=?, ");
            params.add(expirationDate);
        }
        if (brandName != null) {
            updatePetFoodSql.append("brandName=?, ");
            params.add(brandName);
        }

        // Remove the trailing comma and space
        updatePetFoodSql.deleteCharAt(updatePetFoodSql.length() - 1);
        updatePetFoodSql.deleteCharAt(updatePetFoodSql.length() - 1);

        // Add the WHERE clause for the productID
        updatePetFoodSql.append(" WHERE productID=?");
        params.add(productID);

        // Execute the update
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updatePetFoodSql.toString());
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            preparedStatement.executeUpdate();
            auditHelper.addAction("Updated PetFood with ID: " + productID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private PetFood mapToPetFood(ResultSet resultSet) throws SQLException {
        try {
            if (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String nameOfTheProduct = resultSet.getString("nameOfTheProduct");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                Boolean hasCoupon = resultSet.getBoolean("hasCoupon");
                double couponPercentageValue = resultSet.getDouble("couponPercentageValue");
                String expirationDate = resultSet.getString("expirationDate");
                String brandName = resultSet.getString("brandName");

                BrandService brandService = BrandService.getInstance();
                Brand brand = brandService.getBrandByName(brandName);

                Set<String> ingredients = getIngredientsByProductID(productID);

                return new PetFood(nameOfTheProduct, description, productID, price,
                        hasCoupon, couponPercentageValue, brand, ingredients, expirationDate);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
