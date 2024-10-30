package classes;

import java.util.Objects;

public class Toy extends Product{
    private String material;
    private Dimensions dimensions;

    public Toy() {
    }

    public Toy(String nameOfTheProduct, String description, String productID, double price,
               Boolean hasCoupon, double couponPercentageValue, Brand brand, String material) {
        super(nameOfTheProduct, description, productID, price, hasCoupon, couponPercentageValue, brand);
        this.material = material;
    }

    public Toy(String nameOfTheProduct, String description, String productID, double price,
               Boolean hasCoupon, double couponPercentageValue, Brand brand, String material, Dimensions dimensions) {
        super(nameOfTheProduct, description, productID, price, hasCoupon, couponPercentageValue, brand);
        this.material = material;
        this.dimensions = dimensions;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "nameOfTheProduct='" + this.getNameOfTheProduct() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", productID='" + this.getProductID() + '\'' +
                ", price=" + this.getPrice() + '\'' +
                ", hasCoupon=" + this.getHasCoupon().toString() + '\'' +
                ", couponPercentageValue=" + this.getCouponPercentageValue() + '\'' +
                ", brand=" + this.getBrand().getName() + '\'' +
                ", material='" + material + '\'' +
                ", dimensions=" + dimensions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Toy toy = (Toy) o;
        return Objects.equals(material, toy.material) && Objects.equals(dimensions, toy.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), material, dimensions);
    }
}
