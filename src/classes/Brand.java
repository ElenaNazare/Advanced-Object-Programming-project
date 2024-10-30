package classes;

import java.util.Objects;

public class Brand {
    private String name;
    private String factoryAddress;
    private String contactNumber;
    private String country;

    public Brand() {
    }
    public Brand(String name) {
        this.name = name;
    }
    public Brand(String name, String factoryAddress, String contactNumber, String country) {
        this.name = name;
        this.factoryAddress = factoryAddress;
        this.contactNumber = contactNumber;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactoryAddress() {
        return factoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        this.factoryAddress = factoryAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", factoryAddress='" + factoryAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(name, brand.name) && Objects.equals(factoryAddress, brand.factoryAddress) && Objects.equals(contactNumber, brand.contactNumber) && Objects.equals(country, brand.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, factoryAddress, contactNumber, country);
    }
}
