package classes.services;

import others.DatabaseConfiguration;
import classes.Fish;
import others.AuditHelper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FishService {
    // Singleton instance
    private static FishService instance;
    // Database connection
    private Connection connection;
    // Audit helper
    private AuditHelper auditHelper;

    // Private constructor to prevent instantiation
    private FishService() throws IOException {
        this.connection = DatabaseConfiguration.getDatabaseConnection();
        this.auditHelper = AuditHelper.getInstance();
    }

    // Public method to provide access to the instance
    public static synchronized FishService getInstance() throws IOException {
        if (instance == null) {
            instance = new FishService();
        }
        return instance;
    }

    public void createFish(String name, String species, Integer age, Integer weight,
                           String weightMeasurement, String typeOfWater, String color, String tankRequirements) {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not initialized.");
        }

        String insertFishSql = "INSERT INTO Fish(name, species, age, weight, weightMeasurement, " +
                "typeOfWater, color, tankRequirements) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertFishSql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, species);
            preparedStatement.setInt(3, age);
            preparedStatement.setInt(4, weight);
            preparedStatement.setString(5, weightMeasurement);
            preparedStatement.setString(6, typeOfWater);
            preparedStatement.setString(7, color);
            preparedStatement.setString(8, tankRequirements);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Created Fish: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFishById(int animalID) {
        String deleteFishSql = "DELETE FROM Fish WHERE animalID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteFishSql);
            preparedStatement.setInt(1, animalID);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                auditHelper.addAction("Deleted Fish with ID: " + animalID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFish(int animalID, String name, String species, Integer age, Integer weight,
                           String weightMeasurement, String typeOfWater, String color, String tankRequirements) {
        // Build the update SQL statement
        StringBuilder updateFishSql = new StringBuilder("UPDATE Fish SET ");
        List<Object> params = new ArrayList<>();

        if (name != null) {
            updateFishSql.append("name=?, ");
            params.add(name);
        }
        if (species != null) {
            updateFishSql.append("species=?, ");
            params.add(species);
        }
        if (age != null) {
            updateFishSql.append("age=?, ");
            params.add(age);
        }
        if (weight != null) {
            updateFishSql.append("weight=?, ");
            params.add(weight);
        }
        if (weightMeasurement != null) {
            updateFishSql.append("weightMeasurement=?, ");
            params.add(weightMeasurement);
        }
        if (typeOfWater != null) {
            updateFishSql.append("typeOfWater=?, ");
            params.add(typeOfWater);
        }
        if (color != null) {
            updateFishSql.append("color=?, ");
            params.add(color);
        }
        if (tankRequirements != null) {
            updateFishSql.append("tankRequirements=?, ");
            params.add(tankRequirements);
        }

        // Remove the trailing comma and space
        updateFishSql.deleteCharAt(updateFishSql.length() - 1);
        updateFishSql.deleteCharAt(updateFishSql.length() - 1);

        // Add the WHERE clause for the animalID
        updateFishSql.append(" WHERE animalID=?");
        params.add(animalID);

        // Execute the update
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateFishSql.toString());
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }
            preparedStatement.executeUpdate();
            auditHelper.addAction("Updated Fish with ID: " + animalID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Fish getFishById(int animalID) {
        String selectSql = "SELECT * FROM Fish WHERE animalID=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, animalID);

            ResultSet resultSet = preparedStatement.executeQuery();
            return mapToFish(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Fish mapToFish(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Fish(resultSet.getString("name"),
                    resultSet.getInt("animalID"),
                    resultSet.getString("species"),
                    resultSet.getInt("age"),
                    resultSet.getInt("weight"),
                    resultSet.getString("weightMeasurement"),
                    resultSet.getString("typeOfWater"),
                    resultSet.getString("color"),
                    resultSet.getString("tankRequirements"));
        }
        return null;
    }
}
