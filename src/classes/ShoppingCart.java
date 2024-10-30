package classes;

import java.util.*;

//class ShoppingCart is kind of a service class for class Product
public class ShoppingCart {
    private int cartCode;
    private double totalPrice;
    private double voucherPercentageValue;
    private Boolean hasVoucher;
    private List<Product> products;
    private boolean transactionWasFinalised;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public ShoppingCart(double totalPrice, int cartCode, double voucherPercentageValue, Boolean hasVoucher, List<Product> products,
                        boolean transactionWasFinalised) {
        this.totalPrice = totalPrice;
        this.cartCode = cartCode;
        this.voucherPercentageValue = voucherPercentageValue;
        this.hasVoucher = hasVoucher;
        this.transactionWasFinalised = transactionWasFinalised;
        if(products == null) {
            this.products = new ArrayList<>();
        }
        else{
            this.products = products;
        }
        this.products = products;
    }

    //copy constructor
    public ShoppingCart(ShoppingCart other) {
        this.totalPrice = other.totalPrice;
        this.cartCode = other.cartCode;
        this.voucherPercentageValue = other.voucherPercentageValue;
        this.hasVoucher = other.hasVoucher;
        this.products = new ArrayList<>(other.products);
        this.transactionWasFinalised = other.transactionWasFinalised;// Copy the products
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getCartCode() {
        return cartCode;
    }

    public void setCartCode(int cartCode) {
        this.cartCode = cartCode;
    }

    //this should be made private, no need to modify the overall price from outside the ShoppingCart
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getVoucherPercentageValue() {
        return voucherPercentageValue;
    }

    public void setVoucherPercentageValue(double voucherPercentageValue) {
        this.voucherPercentageValue = voucherPercentageValue;
    }

    public Boolean getHasVoucher() {
        return hasVoucher;
    }

    public void setHasVoucher(Boolean hasVoucher) {
        this.hasVoucher = hasVoucher;
    }

    public boolean isTransactionWasFinalised() {
        return transactionWasFinalised;
    }

    public void setTransactionWasFinalised(boolean transactionWasFinalised) {
        this.transactionWasFinalised = transactionWasFinalised;
    }

    //don't use set products (yet?)

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        this.products.add(product);
        this.totalPrice += product.getPrice();
    }

    public void verifyCartPrice(){
        double calculatedPrice = 0;
        for(Product product: products) {
            calculatedPrice += product.getPrice();
        }
        if (totalPrice != calculatedPrice){
            System.out.println("The price has been correctly updated. New price is: " + calculatedPrice);
            this.totalPrice = calculatedPrice;
        }
    }

    public void sortProductsByPrice() {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                // Compare prices of two products
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });
    }

    public void applyVoucher(){
        double value = (this.totalPrice * this.voucherPercentageValue) / 100;
        this.totalPrice = this.totalPrice - value;
        this.hasVoucher = true;
    }

    public void addVoucher(int value){
        if(this.hasVoucher)
            System.out.println("A voucher is already in use for this shopping cart");
        else{
            this.voucherPercentageValue = value;
            this.applyVoucher();
        }
    }

    public double getPriceReductionAmmount(){
        if(!this.hasVoucher) {
            System.out.println("A coupon has not been applied.");
            return 0;
        }
        double oldPrice = this.totalPrice / (1 - (this.voucherPercentageValue / 100));
        return oldPrice - this.totalPrice;
    }

    public void removeVoucher(){
        this.totalPrice += getPriceReductionAmmount();
        this.voucherPercentageValue = 0;
        this.hasVoucher = false;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "totalPrice=" + totalPrice +
                ", transactionWasFinalised=" + transactionWasFinalised +
                ", cartCode=" + cartCode +
                ", voucherPercentageValue=" + voucherPercentageValue +
                ", hasVoucher=" + hasVoucher +
                ",\n products=" + products +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return cartCode == that.cartCode && Double.compare(totalPrice, that.totalPrice) == 0 && Double.compare(voucherPercentageValue, that.voucherPercentageValue) == 0 && transactionWasFinalised == that.transactionWasFinalised && Objects.equals(hasVoucher, that.hasVoucher) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartCode, totalPrice, voucherPercentageValue, hasVoucher, products, transactionWasFinalised);
    }
}
