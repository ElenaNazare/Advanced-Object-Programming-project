package classes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PetFood extends Product {
    private Set<String> ingredients;
    private String expirationDate;

    public PetFood() {
        ingredients = new HashSet<String>();
    }

    public PetFood(String nameOfTheProduct, String description, String productID, double price,
                   Boolean hasCoupon, double couponPercentageValue, Brand brand, Set<String> ingredients, String expirationDate) {
        super(nameOfTheProduct, description, productID, price, hasCoupon, couponPercentageValue, brand);
        if (ingredients == null) {
            this.ingredients = new HashSet<String>();
        } else {
            this.ingredients = ingredients;
        }
        this.expirationDate = expirationDate;
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        String string = String.valueOf(new StringBuilder("PetFood{" +
                "nameOfTheProduct='" + this.getNameOfTheProduct() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", productID='" + this.getProductID() + '\'' +
                ", price=" + this.getPrice() + '\'' +
                ", hasCoupon=" + this.getHasCoupon().toString() + '\'' +
                ", couponPercentageValue=" + this.getCouponPercentageValue() + '\'' +
                ", brand=" + this.getBrand().getName() + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", ingredients="));

        if (this.ingredients == null) {
            string = string.concat(" }");
            return string;
        }

        for (String ingredient : this.ingredients) {
            string = string.concat(" " + ingredient + ",");
        }
        string = string.substring(0, string.length() - 1);
        string = string.concat("}");
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PetFood petFood = (PetFood) o;
        return Objects.equals(ingredients, petFood.ingredients) && Objects.equals(expirationDate, petFood.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ingredients, expirationDate);
    }
}
