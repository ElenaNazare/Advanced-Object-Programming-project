package classes;

import java.util.Objects;

public class Client {
    private String firstName,lastName;
    private String email;
    private String phone;
    private String address;
    private int clientId; //this client ID should be automatically generated in the future (maybe using a static)
    //shoppingCart
    private ShoppingCart shoppingCart;
    //orderHistory

    public Client(String firstName, String lastName, String email, String phone, int clientId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientId = clientId;
        this.shoppingCart = new ShoppingCart();
    }

    public Client(String firstName, String lastName, String email, String phone, String address, int clientId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.clientId = clientId;
    }

    //FOR TESTING PURPOSES
    //copy constructor, not sure if I want to keep it in the future
    public Client(Client other) {
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.address = other.address;
        this.phone = other.phone;
        this.email = other.email;
        this.clientId = other.clientId;
        //we make sure not to have a reference to the same shopping cart, but to make a new one
        this.shoppingCart = new ShoppingCart(other.shoppingCart);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //ATTENTION : using this setter for ShoppingCart just to be able to show an example in main in a simple way
    //I intend to remove this in the future, since each client creates their own personalized shopping cart after creating an account
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", clientId='" + clientId + '\'' +
                ", email='" + email + '\'' +
                ", phone number='" + phone + '\'' +
                ",\n " + shoppingCart +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId == client.clientId && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(email, client.email) && Objects.equals(phone, client.phone) && Objects.equals(address, client.address) && Objects.equals(shoppingCart, client.shoppingCart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phone, address, clientId, shoppingCart);
    }
}
