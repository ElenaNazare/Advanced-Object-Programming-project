package classes;

import java.util.Objects;

public class Fish extends Animal{
    private String typeOfWater;
    private String color;
    private String tankRequirements;

    public Fish() {
    }

    public Fish(String name, int animalID, String species, int age, int weight, String weightMeasurement) {
        super(name,animalID, species, age, weight, weightMeasurement);
    }

    public Fish(String name, int animalID, String species, int age, int weight, String weightMeasurement,
                String typeOfWater, String color, String tankRequirements) {
        super(name, animalID, species, age, weight, weightMeasurement);
        this.typeOfWater = typeOfWater;
        this.color = color;
        this.tankRequirements = tankRequirements;
    }

    public String getTypeOfWater() {
        return typeOfWater;
    }

    public void setTypeOfWater(String typeOfWater) {
        this.typeOfWater = typeOfWater;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTankRequirements() {
        return tankRequirements;
    }

    public void setTankRequirements(String tankRequirements) {
        this.tankRequirements = tankRequirements;
    }

    @Override
    public void feedPet() {
        System.out.println("Glob glob :D");
    }

    @Override
    public String toString() {
        return "Fish{" +
                "name='" + this.getName() + '\'' +
                ", id='" + this.getAnimalID() + '\'' +
                ", species='" + this.getSpecies() + '\'' +
                ", age=" + this.getAge() + '\'' +
                ", weight=" + this.getWeight() + '\'' +
                ", weightMeasurement=" + this.getWeightMeasurement() + '\'' +
                ", typeOfWater='" + typeOfWater + '\'' +
                ", color='" + color + '\'' +
                ", tankRequirements='" + tankRequirements + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Fish fish = (Fish) o;
        return Objects.equals(typeOfWater, fish.typeOfWater) && Objects.equals(color, fish.color) && Objects.equals(tankRequirements, fish.tankRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeOfWater, color, tankRequirements);
    }
}
