package classes;

import java.util.Objects;

public abstract class Animal {
    private String name;//should be in Pet , or Id code

    private int animalID;
    //private final int numberOfLegs
    private String species;

    //should add length and size(of the animal) probably, since it can be useful for any animal

    //age should be in Pet
    private int age; //just years for now, to add a string that tells if it's years or months?
    //or maybe make it a string entirely and a method that returns number of months and years
    private int weight; //could make this and weightMeasurement a string
    //and read only the number, use the idea above
    private String weightMeasurement;

    public Animal() { //keep?
    }

    public Animal(String name, int animalID, String species, int age, int weight,String weightMeasurement) {
        this.name = name;
        this.animalID = animalID;
        this.species = species;
        this.age = age;
        this.weight = weight;
        this.weightMeasurement = weightMeasurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWeightMeasurement() {
        return weightMeasurement;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public void setWeightMeasurement(String weightMeasurement) {
        this.weightMeasurement = weightMeasurement;
    }
    public abstract void feedPet();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return animalID == animal.animalID && age == animal.age && weight == animal.weight && Objects.equals(name, animal.name) && Objects.equals(species, animal.species) && Objects.equals(weightMeasurement, animal.weightMeasurement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, animalID, species, age, weight, weightMeasurement);
    }
}
