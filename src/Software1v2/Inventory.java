/**
 * @autho Jessie B. Inventory object Class
 */

package Software1v2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    // List that will store parts and products list
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // add part to allparts list
    public static void addPart(Part newPart) {
        Inventory.allParts.add(newPart);
    }

    // add product to allproducts list
    public static void addProduct(Product newProduct) {
        Inventory.allProducts.add(newProduct);
    }

    // lookup part by ID
    public static Part lookupPart(int partID) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partID) {
                Part searchedPart = allParts.get(i);
                return searchedPart;
            }
        }
        return null;
    }

    // lookup product by ID
    public static Product lookupProduct(int productID) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productID) {
                Product searchedProduct = allProducts.get(i);
                return searchedProduct;
            }
        }
        return null;
    }

    // lookup part by name(partial and full match)
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> currentList = FXCollections.observableArrayList();
        for (int i = 0; i < allParts.size(); i++) {
            // Full and partial string match
            if (allParts.get(i).getName().toLowerCase() == partName.toLowerCase() ||
                    allParts.get(i).getName().toLowerCase().contains(partName.toLowerCase())) {
                currentList.add(allParts.get(i));
            }
        }
        return currentList;
    }

    // lookup product by name(partial and full match)
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> currentList = FXCollections.observableArrayList();
        for (int i = 0; i < allProducts.size(); i++) {
            // Full and partial string match
            if (allProducts.get(i).getName().toLowerCase() == productName ||
                    allProducts.get(i).getName().toLowerCase().contains(productName.toLowerCase())) {
                currentList.add(allProducts.get(i));
            }
        }
        return currentList;
    }

    // update a particular part
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    // update a particular product
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    // delete part from allparts list
    public static boolean deletePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++){
            if (selectedPart.getId() == allParts.get(i).getId()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    // delete product from allproducts list
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    // get allparts list
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    // get allproducts list
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
