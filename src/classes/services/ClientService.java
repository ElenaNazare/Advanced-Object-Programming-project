package classes.services;

import others.DatabaseConfiguration;
import classes.Client;
import others.AuditHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientService {
    private static ClientService instance;

    private final Connection connection;

    private final AuditHelper auditHelper;

    private ClientService() throws IOException {
        this.connection = DatabaseConfiguration.getDatabaseConnection();
        this.auditHelper = AuditHelper.getInstance();
    }

    public static synchronized ClientService getInstance() throws IOException {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    public void insertClient(String firstName, String lastName, String email, String phone, String address, int shoppingCartCode) {
        String insertClientSql = "INSERT INTO Client(firstName, lastName, email, phone, address, shoppingCartCode) VALUES(?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertClientSql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, address);
            preparedStatement.setInt(6, shoppingCartCode);

            preparedStatement.executeUpdate();
            auditHelper.addAction("Inserted client: " + firstName + " " + lastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Client getClientById(int clientId) {
        String selectSql = "SELECT * FROM Client WHERE clientId=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, clientId);

            ResultSet resultSet = preparedStatement.executeQuery();
            Client client = mapToClient(resultSet);
            if (client != null) {
                auditHelper.addAction("Retrieved client: " + client.getFirstName() + " " + client.getLastName());
            }
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // To implement other updates as well
    public void updateClientAddress(int clientId, String newAddress) {
        String updateAddressSql = "UPDATE Client SET address=? WHERE clientId=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateAddressSql);
            preparedStatement.setString(1, newAddress);
            preparedStatement.setInt(2, clientId);

            preparedStatement.executeUpdate();
            Client client = getClientById(clientId);
            if (client != null) {
                auditHelper.addAction("Updated client address: " + client.getFirstName() + " " + client.getLastName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClientById(int clientId) {
        String deleteSql = "DELETE FROM Client WHERE clientId=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setInt(1, clientId);

            preparedStatement.executeUpdate();
            Client client = getClientById(clientId);
            if (client != null) {
                auditHelper.addAction("Deleted client: " + client.getFirstName() + " " + client.getLastName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Client mapToClient(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Client(resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getInt("clientId"));
        }
        return null;
    }

}
