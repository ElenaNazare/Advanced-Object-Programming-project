package classes;

import classes.otherClasses.Manager;

import java.util.Objects;


//IDEA : when a product is created it should automatically go to the Manager list of Products, so I can have a list of all available products
public abstract class Product {
    private String description;
    private String nameOfTheProduct;
    private String productID;
    private double price;
    private Boolean hasCoupon;
    private double couponPercentageValue; //ex: 10%
    private Brand brand;

    //I shouldn't be able to make an empty product, since it doesn't have an ID, and therefore I cannot add it to the allAvailableProducts
    //,but I get errors if I remove the default constructor - to fix later
    public Product() {
    }

    public Product(String nameOfTheProduct, String description, String productID, double price,
                   Boolean hasCoupon, double couponPercentageValue, Brand brand) {
        this.description = description;
        this.nameOfTheProduct = nameOfTheProduct;
        this.productID = productID;
        this.price = price;
        this.hasCoupon = hasCoupon;
        this.couponPercentageValue = couponPercentageValue;
        this.brand = brand;

        Manager manager = Manager.getInstance();
        manager.addProduct(this);
    }

    //private Category animalCategory;
    //category - idea for implementation in the future
    //would make it a separate class and count how many products of a certain category exist

    public void applyCoupon(){
        double value = (this.price * this.couponPercentageValue) / 100;
        this.price = this.price - value;
        this.hasCoupon = true;
    }

    public void addCoupon(int value){
        if(this.hasCoupon)
            System.out.println("A coupon has already been applied to this product");
        else{
            this.couponPercentageValue = value;
            this.applyCoupon();
        }
    }

    public double getPriceReductionAmmount(){
        if(!this.hasCoupon) {
            System.out.println("A coupon has not been applied.");
            return 0;
        }
        double oldPrice = this.price / (1 - (this.couponPercentageValue / 100));
        return oldPrice - this.price;
    }

    public void removeCoupon(){
        this.price += getPriceReductionAmmount();
        this.couponPercentageValue = 0;
        this.hasCoupon = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameOfTheProduct() {
        return nameOfTheProduct;
    }

    public void setNameOfTheProduct(String nameOfTheProduct) {
        this.nameOfTheProduct = nameOfTheProduct;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Brand getBrand() {
        return brand;
    }

    public Boolean getHasCoupon() {
        return hasCoupon;
    }

    public double getCouponPercentageValue() {
        return couponPercentageValue;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && Double.compare(couponPercentageValue, product.couponPercentageValue) == 0
                && Objects.equals(description, product.description) && Objects.equals(nameOfTheProduct, product.nameOfTheProduct)
                && Objects.equals(productID, product.productID) && Objects.equals(hasCoupon, product.hasCoupon)
                && Objects.equals(brand, product.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, nameOfTheProduct, productID, price, hasCoupon, couponPercentageValue, brand);
    }
}
