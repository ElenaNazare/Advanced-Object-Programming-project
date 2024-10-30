package classes.otherClasses;

import classes.Client;
import classes.Product;

import java.util.HashMap;
import java.util.Map;

//class Manager is a service class for all the application clients
//tried to make it a Singleton
public class Manager {
    private static Manager instance = null;
    //TO FIX: inconsistency, ID for clients is integer, but ID for products is String
    private Map<Integer, Client> clients;
    private Map<String, Product> availableProducts;
    //I want a map with all available products as well
    //I want the products and clients to be added in these lists automatically, from their respective constructors
    private Manager() {
        clients = new HashMap<>();
        availableProducts = new HashMap<>();
    }

    public static Manager getInstance() {
        if (instance == null) {
            synchronized (Manager.class) { //using synchronized so that only one thread can access this section at a time
                if (instance == null) {
                    instance = new Manager();
                }
            }
        }
        return instance;
    }

    public void addClient(Client client) {
        clients.put(client.getClientId(), client);
    }

    public void removeClient(int clientId) {
        clients.remove(clientId);
    }

    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    public Map<Integer, Client> getAllClients() {
        return clients;
    }

    public int getNumberOfClients(){
        return clients.size();
    }

    public void addProduct(Product product) {
        availableProducts.put(product.getProductID(), product);
    }

    public void removeProduct(String productId) { //need to find a way to delete all instances of the product as well
        availableProducts.remove(productId);
    }
    public Product getProduct(String productId){    //to check if it works for derived classes
        return availableProducts.get(productId);
    }
    public Map<String, Product> getAllAvailableProducts(){
        return availableProducts;
    }
}
