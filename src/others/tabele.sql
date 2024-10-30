CREATE TABLE `Brand` (
                         `name` varchar(30) NOT NULL,
                         `factoryAddress` varchar(70) default null,
                         `contactNumber` varchar(15) default null,
                         `country` varchar(30) default null,
                         primary key (`name`)
);

CREATE TABLE `Ingredient` (
                              `name` varchar(30) NOT NULL,
                              primary key (`name`)
);

CREATE TABLE `IngredientList` (
                                  `ingredientName` varchar(30) NOT NULL,
                                  `productID` varchar(15) NOT NULL,
                                  key `ingredientName` (`ingredientName`),
                                  key `productID` (`productID`),
                                  constraint `ingr_fk1` foreign key (`ingredientName`) references `Ingredient` (`name`) on delete cascade,
                                  constraint `ingr_fk2` foreign key (`productID`) references `PetFood` (`productID`) on delete cascade,
                                  primary key (`ingredientName`,`productID`)
);

CREATE TABLE `PetFood` (
                           `productID` varchar(15) NOT NULL,
                           `nameOfTheProduct` varchar(30) default null,
                           `description` varchar(100) default null,
                           `price` double default null,
                           `hasCoupon` boolean default false,
                           `couponPercentageValue` double default null,
    -- lista de ingrediente
                           `expirationDate` varchar(30) default null,
                           primary key (`productID`),
                           `brandName` varchar(30) default null,
                           key `brandName` (`brandName`),
                           constraint `petF_fk` foreign key (`brandName`) references `Brand`(`name`) on delete cascade
);

CREATE TABLE `Fish` (
                        `animalID` int NOT NULL auto_increment,
                        `name` varchar(30) default null,
                        `species` varchar(30) default null,
                        `age` int default null,
                        `weight` int default null,
                        `weightMeasurement` varchar(30) default "grams",
                        `typeOfWater` varchar(30) default null,
                        `color` varchar(30) default null,
                        `tankRequirements` varchar(50) default null,
                        primary key (`animalID`)
);

CREATE TABLE `ShoppingCart` (
                                `cartCode` int NOT NULL auto_increment,
                                `totalPrice` double default null,
                                `voucherPercentageValue` double default null,
                                `hasVoucher` boolean default false,
                                `transactionWasFinalised` boolean default false,
    -- lista de produse
                                primary key (`cartCode`)
);


-- NEED TO CHANGE ANIMAL ID, I NEED TO CREATE A PET THAT CONTAINS AN ANIMAL BUT EXTENDS PRODUCT

CREATE TABLE `ProductList` (
                               `cartCode` int NOT NULL,
    -- `animalID` int unique,
                               `foodID` varchar(15) unique,
    -- PRIMARY KEY (`cartCode`, `animalID`, `foodID`),
                               PRIMARY KEY (`cartCode`,`foodID`),
    -- KEY `idx_animalID` (`animalID`),
                               KEY `idx_foodID` (`foodID`),
                               KEY `idx_cartCode` (`cartCode`),
    -- CONSTRAINT `prod_list_fk1` FOREIGN KEY (`animalID`) REFERENCES `Fish` (`animalID`) ON DELETE CASCADE,
                               CONSTRAINT `prod_list_fk2` FOREIGN KEY (`foodID`) REFERENCES `PetFood` (`productID`) ON DELETE CASCADE,
                               CONSTRAINT `prod_list_fk3` FOREIGN KEY (`cartCode`) REFERENCES `ShoppingCart` (`cartCode`) ON DELETE CASCADE
);

CREATE TABLE `Client` (
                          `cleintId` int NOT NULL auto_increment,
                          `firstName` varchar(30) default "guest",
                          `lastName` varchar(30) default null,
                          `email` varchar(30) default null,
                          `phone` varchar(15) default null,
                          `address` VARCHAR(70) default null,
                          `shoppingCartCode` int default null,
                          primary key (`cleintId`),
                          key `shoppingCartCode` (`shoppingCartCode`),
                          CONSTRAINT `client_fk` FOREIGN KEY (`shoppingCartCode`) REFERENCES `ShoppingCart`(`cartCode`) on delete cascade
);