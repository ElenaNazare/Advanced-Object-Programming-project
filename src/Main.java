import classes.Brand;
import classes.Fish;
import classes.PetFood;
import classes.ShoppingCart;
import classes.services.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            //Main.manualTests();
            Main.menu();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void menu() throws IOException {
        BrandService brandService = BrandService.getInstance();
        ClientService clientService = ClientService.getInstance();
        FishService fishService = FishService.getInstance();
        PetFoodService petFoodService = PetFoodService.getInstance();
        ShoppingCartService shoppingCartService = ShoppingCartService.getInstance();

        String option;
        Scanner scan = new Scanner(System.in);

        System.out.println("Main Application: \n(pentru iesirea din program tastati 'stop')\n");

        do {
            System.out.println("\nAlegeti: \n'1': Brands; \n'2': Clients; \n'3': Fish; \n'4': Pet Food; \n'5': Shopping Cart;");
            option = scan.nextLine().trim();
            System.out.println("Ai introdus: " + option);

            if (option.equals("stop")) {
                System.out.println("Exit");
            } else {
                int choice = Integer.parseInt(option);
                switch (choice) {
                    case 1:
                        handleBrandOptions(brandService, scan);
                        break;
                    case 2:
                        //handleClientOptions(clientService, scan);
                        break;
                    case 3:
                        //handleFishOptions(fishService, scan);
                        break;
                    case 4:
                        //handlePetFoodOptions(petFoodService, scan);
                        break;
                    case 5:
                        //handleShoppingCartOptions(shoppingCartService, scan);
                        break;
                    default:
                        System.out.println("Optiune invalida. Va rugam sa incercati din nou.");
                }
            }
        } while (!option.equals("stop"));


        scan.close();
    }

    private static void handleBrandOptions(BrandService brandService, Scanner scan) {
        String option;
        do {
            System.out.println("\nOptiuni pentru Brand: \n'1': Adaugare Brand; \n'2': Afisare Brand dupa nume; " +
                    "\n'3': Afisarea tuturor Brand-urilor; \n'4': Modificare Brand (adresa); \n'5': Stergere Brand;");
            option = scan.nextLine().trim();

            if (option.equals("stop")) {
                System.out.println("Exit");
            } else if (Integer.parseInt(option) == 1) {
                System.out.println("Nume Brand: ");
                String name = scan.nextLine();
                System.out.println("Adresa: ");
                String factoryAddress = scan.nextLine();
                System.out.println("Nr. de contact: ");
                String contactNumber = scan.nextLine();
                System.out.println("Tara: ");
                String country = scan.nextLine();
                brandService.insertBrand(name,factoryAddress,contactNumber,country);
            } else if (Integer.parseInt(option) == 2) {
                System.out.println("Nume Brand: ");
                String name = scan.nextLine();
                System.out.println(brandService.getBrandByName(name));
            } else if (Integer.parseInt(option) == 3) {
                List<Brand> brands = brandService.getAllBrands();
                for (Brand brand : brands) {
                    System.out.println(brand);
                }
            } else if (Integer.parseInt(option) == 4) {
                System.out.println("Numele Brand-ului ce se doreste modificat: ");
                String name = scan.nextLine();
                System.out.println("Noua adresa: ");
                String factoryAddress = scan.nextLine();
                brandService.updateBrandFactoryAddress(name,factoryAddress);
            } else if (Integer.parseInt(option) == 5) {
                System.out.println("Numele Brand-ului ce se doreste a fi sters: ");
                String name = scan.nextLine();
                brandService.deleteBrandByName(name);
            }
        } while (!option.equals("stop"));
    }

    public static void manualTests() throws IOException {
        try {
            BrandService brandService = BrandService.getInstance();

//            // Insert a new brand
//            brandService.insertBrand("BrandA", "123 Factory St", "1234567890", "USA");

            // Retrieve a brand
            Brand brand = brandService.getBrandByName("BrandA");
            if (brand != null) {
                System.out.println("Retrieved Brand: " + brand.getName() + ", " + brand.getFactoryAddress() + ", " + brand.getContactNumber() + ", " + brand.getCountry());
            }

//            // Update the factory address of a brand
//            brandService.updateBrandFactoryAddress("BrandA", "456 New Factory St");

            // Retrieve the updated brand
            brand = brandService.getBrandByName("BrandA");
            if (brand != null) {
                System.out.println("Updated Brand: " + brand.getName() + ", " + brand.getFactoryAddress() + ", " + brand.getContactNumber() + ", " + brand.getCountry());
            }

//            // Delete a brand
//            brandService.deleteBrandByName("BrandA");
//
//            // Try to retrieve the deleted brand
//            brand = brandService.getBrandByName("BrandA");
//            if (brand == null) {
//                System.out.println("BrandA has been deleted successfully.");
//            }

            PetFoodService petFoodService = PetFoodService.getInstance();

//            // Test Case 1: Create PetFood with Existing Ingredients
//            petFoodService.createPetFood("PF001", "Premium Dog Food", "High-quality dog food", 15.99,
//                    false, 0.0, "2024-12-31", "BrandA", Arrays.asList("Chicken", "Rice", "Vegetables"));
//
//            // Test Case 2: Create PetFood with New Ingredient
//            petFoodService.createPetFood("PF002", "Healthy Cat Food", "Nutritious cat food", 12.99,
//                    false, 0.0, "2024-12-31", "BrandA", Arrays.asList("Salmon", "Potato", "Spinach"));
//
//            // Test Case 3: Create PetFood with Duplicate Ingredient
//            petFoodService.createPetFood("PF003", "Deluxe Dog Food", "Gourmet dog food", 19.99,
//                    false, 0.0, "2024-12-31", "BrandA", Arrays.asList("Chicken", "Rice", "Vegetables"));

            petFoodService.deletePetFoodById("PF001");
            PetFood petFood = petFoodService.getPetFoodById("PF001");
            if (petFood != null) {
                System.out.println("PetFood with ID 'PF001' deleted successfully.");
            }

//            petFoodService.deletePetFoodByName("Deluxe Dog Food");
//            petFood = petFoodService.getPetFoodByName("Deluxe Dog Food");
//            if (petFood != null) {
//                System.out.println("PetFood with name \"Deluxe Dog Food\" deleted successfully.");
//            }

            petFood = petFoodService.getPetFoodById("PF002");
            if (petFood != null) {
                System.out.println("PetFood ID: " + petFood.getProductID());
                System.out.println("Name: " + petFood.getNameOfTheProduct());
                // Display other details as needed
            } else {
                System.out.println("PetFood with ID 'PF002' not found.");
            }

            //Update PetFood Name
            petFoodService.updatePetFood("PF001", "New Name", null, null, null,
                    null, null, null, null);

            //Update PetFood Price and Expiration Date
            petFoodService.updatePetFood("PF002", null, null, 25.99, null,
                    null, "2024-12-31", null, null);

            //Update PetFood Brand Name and Has Coupon
            petFoodService.updatePetFood("PF003", null, null, null, true,
                    null, null, null, null);

            //Update PetFood Description, Coupon Percentage Value, and Ingredients
            petFoodService.updatePetFood("PF004", null, "New Description", null, null,
                    10.0, null, null, Arrays.asList("Ingredient1", "Ingredient2"));

            FishService fishService = FishService.getInstance();
            //Create a new fish
            fishService.createFish("Goldfish", "Carassius auratus", 2, 5,
                    "grams", "Freshwater", "Gold", "Requires clean water");

            //Update fish details
            fishService.updateFish(1, "Goldfish", "Carassius auratus", 3, 7,
                    "grams", "Freshwater", "Golden", "Requires clean water");

            //Get fish by ID
            Fish fish = fishService.getFishById(1);
            if (fish != null) {
                System.out.println("Fish ID: " + fish.getAnimalID());
                System.out.println("Name: " + fish.getName());
                System.out.println("Species: " + fish.getSpecies());
                System.out.println("Age: " + fish.getAge());
                System.out.println("Weight: " + fish.getWeight() + " " + fish.getWeightMeasurement());
                System.out.println("Type of Water: " + fish.getTypeOfWater());
                System.out.println("Color: " + fish.getColor());
                System.out.println("Tank Requirements: " + fish.getTankRequirements());
            } else {
                System.out.println("Fish not found.");
            }

            //Delete fish by ID
            fishService.deleteFishById(1);
            System.out.println("Fish with ID 1 deleted successfully.");

            // SHOPPING CART

            ShoppingCartService shoppingCartService = ShoppingCartService.getInstance();

//            // Create a new shopping cart
//            shoppingCartService.createShoppingCart(100.0, null, false, false);

            // Get the created shopping cart by code
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByCode(1);
            System.out.println("Shopping Cart Retrieved: " + shoppingCart);

            // Add products to the shopping cart
            shoppingCartService.addProductsToCart(3, Arrays.asList("PF002", "PF003"));
            shoppingCart = shoppingCartService.getShoppingCartByCode(3);
            System.out.println("Shopping Cart After Adding Products: " + shoppingCart);

            // Update the shopping cart
            shoppingCartService.updateShoppingCart(3, 12.0, true);
            shoppingCart = shoppingCartService.getShoppingCartByCode(3);
            System.out.println("Shopping Cart After Update: " + shoppingCart);

//            // Delete the shopping cart
//            shoppingCartService.deleteShoppingCartByCode(1);
//            shoppingCart = shoppingCartService.getShoppingCartByCode(1);

            //testClientService();

            // CLIENT
            ClientService clientService = ClientService.getInstance();

//            // Insert a new client
//            clientService.insertClient("John", "Doe", "john.doe@example.com", "123456789", "123 Main St", 2);

            // Retrieve the inserted client by id
            int clientId = 9;
            System.out.println("Retrieving client with ID " + clientId + ":");
            System.out.println(clientService.getClientById(clientId));

            // Update the address of the client
            String newAddress = "456 Elm St";
            System.out.println("Updating client address to " + newAddress + ":");
            clientService.updateClientAddress(clientId, newAddress);
            System.out.println(clientService.getClientById(clientId));

//            // Delete the client
//            System.out.println("Deleting client with ID " + clientId + ":");
//            clientService.deleteClientById(clientId);
//            System.out.println(clientService.getClientById(clientId));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // OLD TESTS: BEFORE IMPLEMENTING SERVICES

//    public static void main(String[] args) {
//
//        //testing Brand
//        Brand RoyalCanin = new Brand("RoyalCanin");
//        Brand PetKult = new Brand("PetKult", "Bucharest, Str.Florea, Nr.1",
//                "0776406782", "Romania");
//        Brand Padovan = new Brand("Padovan");
//        System.out.println(RoyalCanin);
//
//        //testing Category
//        Category categorie1 = new Category("pisici", "produse pentru feline");
//        Category categorie2 = new Category("pesti");
//
//        //testing PetFood
//        Product hranaPesti = new PetFood("BetaFish Food","Food for Betas",
//                "1145", 12.40d ,false,0,Padovan,null, "3 February 2025" );
//        ((PetFood) hranaPesti).addIngredient("ceva");
//        ((PetFood) hranaPesti).addIngredient("ceva2");
//        System.out.println(hranaPesti);
//
//        Animal fish = new Fish("Nemo","clownfish",1,12,"grams",
//                "saltwater","orange with black stripes","minimum 30 liters");
//        System.out.println(fish);
//
//        Dimensions dimensions = new Dimensions(12,5,3);
//
//        Product jucarie = new Toy("Perch", "Perch for small to medium sized birds.",
//                "3346",11.34d,false,0,Padovan,"fiber",dimensions);
//        System.out.println(jucarie);
//
//        //testing ShoppingCart
//        List<Product> listP = new ArrayList<Product>();
//        listP.add(hranaPesti);
//        listP.add(jucarie);
//
//        ShoppingCart shoppingCart = new ShoppingCart(0,0,false, listP);
//        System.out.println(shoppingCart);
//        shoppingCart.verifyCartPrice();
//        shoppingCart.addProduct(jucarie);
//        shoppingCart.sortProductsByPrice();
//        System.out.println(shoppingCart);
//
//        //testing Client
//        System.out.println("------------Testing Client");
//        Client client1 = new Client("Popescu","Marian",76234);
//        System.out.println(client1);
//        client1.setShoppingCart(shoppingCart);//just for example purposes
//        System.out.println(client1);
//
//        //testing Manager, also known as ServiceClient class
//        System.out.println("------------Testing Manager");
//        Manager m1 = Manager.getInstance();
//        m1.addClient(client1);
//        System.out.println(m1.getNumberOfClients());
//        System.out.println(m1.getAllClients());
//
//        //since it's a map, it won't have duplicated clients
//        Client client2 = new Client(client1);
//        client2.setClientId(46782);
//        m1.addClient(client2);
//        System.out.println(m1.getAllClients());
//
//        System.out.println("------------Testing Functionalities");
//        //price reduction using a coupon
//        System.out.println(hranaPesti);
//        hranaPesti.addCoupon(10);
//        System.out.println(hranaPesti);
//        //seeing the applied discount + restoring old prince and removing coupon
//        System.out.println(hranaPesti.getPriceReductionAmmount());
//        hranaPesti.removeCoupon();
//        System.out.println(hranaPesti.getPrice());
//        System.out.println(hranaPesti.getPriceReductionAmmount());
//
//        Manager manager = Manager.getInstance();
//        System.out.println(manager.getAllAvailableProducts());
//    }
}