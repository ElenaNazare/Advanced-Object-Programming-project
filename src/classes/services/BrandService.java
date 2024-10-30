package classes.services;

import others.DatabaseConfiguration;
import classes.Brand;
import others.AuditHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandService {
    private static BrandService instance;

    private final Connection connection;

    private final AuditHelper auditHelper;

    private BrandService() throws IOException {
        this.connection = DatabaseConfiguration.getDatabaseConnection();
        this.auditHelper = AuditHelper.getInstance();
    }

    public static synchronized BrandService getInstance() throws IOException {
        if (instance == null) {
            instance = new BrandService();
        }
        return instance;
    }

    public void insertBrand(String name, String factoryAddress, String contactNumber, String country) {
        String insertBrandSql = "INSERT INTO Brand(name, factoryAddress, contactNumber, country) VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertBrandSql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, factoryAddress);
            preparedStatement.setString(3, contactNumber);
            preparedStatement.setString(4, country);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Inserted brand: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Brand getBrandByName(String name) {
        String selectSql = "SELECT * FROM Brand WHERE name=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            Brand brand = mapToBrand(resultSet);
            auditHelper.addAction("Retrieved brand: " + name);
            return brand;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBrandFactoryAddress(String name, String factoryAddress) {
        String updateFactoryAddressSql = "UPDATE Brand SET factoryAddress=? WHERE name=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateFactoryAddressSql);
            preparedStatement.setString(1, factoryAddress);
            preparedStatement.setString(2, name);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Updated brand factory address: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBrandByName(String name) {
        String deleteSql = "DELETE FROM Brand WHERE name=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Deleted brand: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Brand> getAllBrands() {
        List<Brand> brands = new ArrayList<>();
        String query = "SELECT * FROM Brand";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Brand brand = new Brand();
                brand.setName(resultSet.getString("name"));
                brand.setFactoryAddress(resultSet.getString("factoryAddress"));
                brand.setContactNumber(resultSet.getString("contactNumber"));
                brand.setCountry(resultSet.getString("country"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brands;
    }

    private Brand mapToBrand(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Brand(resultSet.getString("name"),
                    resultSet.getString("factoryAddress"),
                    resultSet.getString("contactNumber"),
                    resultSet.getString("country"));
        }
        return null;
    }
}
