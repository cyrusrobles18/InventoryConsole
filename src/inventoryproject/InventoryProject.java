/*
 * Cyrus Robles
 * Marc Christian Cendaña
 * Kyle Christian De Vera
 * INF 193
 * This is program shows a inventory that can store data into a file and can read a file.
 */
package inventoryproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Cyrus Robles, Marc Christian Cendaña, Kyle Christian De Vera
 */
public class InventoryProject {

    private static Scanner scan;

    public static void main(String[] args) {
        homePage();
    }

    /**
     * This method shows the home page of the system.
     */
    public static void homePage() {
        scan = new Scanner(System.in);
        boolean breaker = true;
        while (breaker) {
            try {
                System.out.println("_-_-_-***Welcome to BACSI Vapeshop***-_-_-_");
                System.out.println("[1] Create Account");
                System.out.println("[2] Log in");
                System.out.println("[3] Exit");
                System.out.print(">>> ");
                int choice = scan.nextInt();

                switch (choice) {
                    case 1:
                        createAcc();
                        breaker = true;
                        break;
                    case 2:
                        logIn();
                        breaker = false;
                        break;
                    case 3:
                        System.out.println("---*Thank you please come again*---");
                        System.exit(0);
                        breaker = false;
                        break;
                    default:
                        System.out.println("Please try again!");
                        breaker = true;
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * This method consist of logic in creating an account.
     */
    public static void createAcc() {
        File file = new File("accounts.txt");
        String filepath = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\accounts.txt";

        scan = new Scanner(System.in);

        boolean succ = true;
        while (succ) {
            boolean valid = true;
            System.out.println("Creating an account....");
            System.out.print("Username: ");
            String user = scan.next();
            System.out.print("Password: ");
            String pass = scan.next();
            System.out.print("Confirm Password: ");
            String conpass = scan.next();
            System.out.println("          :Type:        ");
            System.out.println("[1]Consumer or [2]Admin");
            System.out.print(">>> ");
            int typeOfUser = scan.nextInt();

            if (!pass.equals(conpass)) {
                System.out.println("Not the same");
                System.out.println("");
                valid = false;
            }

            if (pass.length() < 8 || pass.length() > 15) {
                System.out.println("Password should be less than 15 and more than 8 characters in length");
                System.out.println("");
                valid = false;
            }

            if (pass.equals(user)) {
                System.out.println("Username and password are the same");
                System.out.println("");
                valid = false;
            }
            String numbers = "(.*[0-9].*)";

            if (!pass.matches(numbers)) {
                System.out.println("Password should contain atleast one number");
                System.out.println("");
                valid = false;
            }

            if (typeOfUser > 3) {
                System.out.println("Wrong input of type of user");
                System.out.println("");
                valid = false;
            }

            String userType = Integer.toString(typeOfUser);

            boolean sameacc = verifyLogin(filepath, user, pass, userType);

            if (sameacc) {
                System.out.println("Account already exist");
                System.out.println("");
                valid = false;
            }
            int inpsc, secretcode = 1111;
            if (typeOfUser == 2 && valid) {
                System.out.print("Enter the secret code: ");
                inpsc = scan.nextInt();
                
                if (secretcode != inpsc) {
                    System.out.println("Do not use forbidden jutsu");
                    System.out.println("");
                    valid = false;
                }
            }

            if (valid) {
                System.out.println("+-+Successful Registration+-+");
                succ = false;
                FileWriter fw = null;
                PrintWriter pr = null;
                try {
                    fw = new FileWriter(filepath, true);
                    pr = new PrintWriter(fw);
                    
                    pr.print(user);
                    pr.print(",");
                    pr.print(pass);
                    pr.print(",");
                    pr.print(userType);

                    pr.print(",");
                    pr.println("");
                    pr.close();
                    fw.close();

                } catch (IOException ioe) {
                    System.out.printf("%s", ioe);
                    System.out.println("\nLine 106");
                }
            }

        }

    }

    /**
     * This method use for login of the user. Whether if the use is consumer or
     * admin.
     */
    public static void logIn() {
        scan = new Scanner(System.in);
        boolean verified = false;
        String username, password, type;
        int typeOfUser;
        while (!verified) {
            String filepath = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\accounts.txt";
            System.out.println("Log in an account....");
            System.out.print("Username: ");
            username = scan.next();
            System.out.print("Password: ");
            password = scan.next();
            System.out.println("      ---:Type:---");
            System.out.println("[1]Consumer or [2]Admin");
            System.out.print(">>> ");
            type = scan.next();
            typeOfUser = Integer.parseInt(type);
//        boolean verified
            verified = verifyLogin(filepath, username, password, type);
            if (!verified) {
                System.out.println("Please try again...");
                System.out.println();
            }
            if (verified) {
                switch (typeOfUser) {
                    case 1:
                        System.out.println("Log in successful");
                        forConsumers(username);
                        break;
                    case 2:
                        System.out.println("Log in successful");
                        forAdmins(username);
                        break;
                    default:
                        System.out.println("You've enter a wrong input please try again...");
                        break;
                }
                break;
            }
        }

    }

    /**
     * This method use to determine if a account exist to accounts file.
     *
     * @param username
     * @param filepath
     * @param password
     * @param type
     * @return
     */
    private static boolean verifyLogin(String filepath, String username, String password, String type) {
        boolean found = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String tempLine;
            String tempUsername;
            String tempPassword;
            String tempType;
            String[] acc;
//            int count = 0;

            while ((tempLine = br.readLine()) != null) {
                acc = tempLine.split(",");
//                for (int i = 0; i < acc.length; i++) {
//                    System.out.println("Index " + (i) + " " + acc[i]);
//
//                }

                tempUsername = acc[0];
                tempPassword = acc[1];
                tempType = acc[2];

                if (tempUsername.trim().equals(username.trim()) && tempPassword.trim().trim().equals(password.trim()) && tempType.trim().equals(type.trim())) {
                    found = true;
                    break;

                }

            }

            br.close();

        } catch (Exception e) {
            System.out.printf("%s", e);
            System.out.println("\nLine 234");
        }
        return found;

    }

    /**
     * This method is for the selection of admins.
     *
     * @param username
     */
    public static void forAdmins(String username) {
        scan = new Scanner(System.in);
        System.out.println("Welcome " + username + "...");
        System.out.println("[1] Maintenance");
//        System.out.println("[2] Display Transaction");
        System.out.println("[0] Exit..");
        System.out.print(">>> ");
        int selection = scan.nextInt();

        switch (selection) {
            case 1:
                adMaintain();
                break;
            case 2:
                break;
            case 0:
                System.out.println("...--Keep Grindin'--...");
                break;
            default:
                break;
        }

    }

    /**
     * This method indicates for maintenance of admins only.
     */
    public static void adMaintain() {
        scan = new Scanner(System.in);
        boolean breaker = true;
        while (breaker) {
            System.out.println("Maintenance ongoing...");
            System.out.println("[1] DISPLAY");
            System.out.println("[2] ADD");
            System.out.println("[3] EDIT");
            System.out.println("[4] DELETE");
            System.out.println("[0] Exit..");
            System.out.print("What do you what to do? ");
            int choice = scan.nextInt();

            switch (choice) {
                case 1:
                    showProd();
                    breaker = false;
                    break;
                case 2:
                    addProd();
                    breaker = false;
                    break;
                case 3:
                    editProd();
                    breaker = false;
                    break;
                case 4:
                    deleteProd();
                    breaker = false;
                    break;
                case 0:
                    System.out.println("...--Keep Grindin'--...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("You've enter a wrong input");
                    breaker = true;
                    break;
            }
        }

    }

    /**
     * This method is use by the admins to add a particular product.
     */
    public static void addProd() {
        scan = new Scanner(System.in);
        System.out.println("Adding products...");
        System.out.print("Product name: ");
        String pName = scan.next();
        System.out.print("Product brand: ");
        String pBrand = scan.next();
        System.out.print("Quantity: ");
        String pQuan = scan.next();
        System.out.print("Price: ");
        String pPrice = scan.next();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            String filepathNOP = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\nameOfProduct.txt";
            fw = new FileWriter(filepathNOP, true);
            bw = new BufferedWriter(fw);
            bw.write(pName);
            bw.write(",");

            bw.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            String filepathBOP = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\brandOfProduct.txt";
            fw = new FileWriter(filepathBOP, true);
            bw = new BufferedWriter(fw);
            bw.write(pBrand);
            bw.write(",");

            bw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            String filepathQOP = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\quantityOfProduct.txt";
            fw = new FileWriter(filepathQOP, true);
            bw = new BufferedWriter(fw);
            bw.write(pQuan);
            bw.write(",");

            bw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        try {
            String filepathPOP = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\priceOfProduct.txt";
            fw = new FileWriter(filepathPOP, true);
            bw = new BufferedWriter(fw);
            bw.write(pPrice);
            bw.write(",");

            bw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        adMaintain();

    }

    /**
     * This method is use by the admins to edit a particular product.
     */
    public static void editProd() {
        scan = new Scanner(System.in);
        String[] nprod, brOfProd, priceOfProd, quanOfProd;
        List<String> nprodList, brOfProdList, priceOfProdList, quanOfProdList;
        boolean breaker = true;
        while (breaker) {
            System.out.println("Edditing product...");
            nameOfProd();
            System.out.print("What to edit? ");
            int choice = scan.nextInt();

            System.out.println("[1] Product name");
            System.out.println("[2] Product brand");
            System.out.println("[3] Product price");
            System.out.println("[4] Product Quantity");
            System.out.println("[0] Exit..");
            System.out.print("What to change? ");
            int change = scan.nextInt();
            switch (change) {
                case 1:
                    //nameOfProduct
                    System.out.print("Enter the new product name: ");
                    String npName = scan.next();
                    String tempFileNP = "tempNP.text";
                    File oldForName = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\nameOfProduct.txt");
                    File newFileNP = new File(tempFileNP);

                    String tempLine = "";

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("nameOfProduct.txt"));
                        FileWriter fw = new FileWriter(tempFileNP);
                        BufferedWriter bw = new BufferedWriter(fw);
                        while ((tempLine = br.readLine()) != null) {
                            nprod = tempLine.split(",");
                            nprodList = new ArrayList<>(Arrays.asList(nprod));
                            nprodList.set(choice - 1, npName);

                            for (int i = 0; i < nprodList.size(); i++) {
                                bw.write(nprodList.get(i) + ",");

                            }

                        }
                        bw.flush();
                        bw.close();
                        br.close();
                        oldForName.delete();
                        File dumpNP = new File("nameOfProduct.txt");
                        newFileNP.renameTo(dumpNP);

                    } catch (Exception e) {
                        System.out.println("Line editProd()");
                    }
                    breaker = false;
                    adMaintain();
                    break;
                case 2:
                    //brandOfProduct
                    System.out.print("Enter the new brand name: ");
                    String npBrand = scan.next();
                    String tempFileBP = "tempFileBP.txt";
                    File newFileBP = new File(tempFileBP);
                    File oldForBrand = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\brandOfProduct.txt");

                    try {
                        BufferedReader br = new BufferedReader(new FileReader("brandOfProduct.txt"));
                        FileWriter fw = new FileWriter(tempFileBP);
                        BufferedWriter bw = new BufferedWriter(fw);
                        while ((tempLine = br.readLine()) != null) {
                            brOfProd = tempLine.split(",");
                            brOfProdList = new ArrayList<>(Arrays.asList(brOfProd));
                            brOfProdList.set(choice - 1, npBrand);

                            for (int i = 0; i < brOfProdList.size(); i++) {
                                bw.write(brOfProdList.get(i) + ",");

                            }

                        }
                        bw.flush();
                        bw.close();
                        br.close();
                        oldForBrand.delete();
                        File dumpBP = new File("brandOfProduct.txt");
                        newFileBP.renameTo(dumpBP);

                    } catch (Exception e) {
                        System.out.println("Line editProd()");
                    }
                    breaker = false;
                    adMaintain();
                    break;
                case 3:
                    //priceOfProduct
                    System.out.print("Enter a new product price: ");
                    String npPrice = scan.next();
                    String tempFilePP = "tempFilePP.txt";
                    File newFilePP = new File(tempFilePP);
                    File oldForPrice = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\priceOfProduct.txt");
                    try {
                        BufferedReader br = new BufferedReader(new FileReader("priceOfProduct.txt"));
                        FileWriter fw = new FileWriter(tempFilePP);
                        BufferedWriter bw = new BufferedWriter(fw);
                        while ((tempLine = br.readLine()) != null) {
                            priceOfProd = tempLine.split(",");
                            priceOfProdList = new ArrayList<>(Arrays.asList(priceOfProd));
                            priceOfProdList.set(choice - 1, npPrice);

                            for (int i = 0; i < priceOfProdList.size(); i++) {
                                bw.write(priceOfProdList.get(i) + ",");

                            }

                        }
                        bw.flush();
                        bw.close();
                        br.close();
                        oldForPrice.delete();
                        File dumpPP = new File("priceOfProduct.txt");
                        newFilePP.renameTo(dumpPP);

                    } catch (Exception e) {
                        System.out.println("Line editProd()");
                    }
                    breaker = false;
                    adMaintain();
                    break;
                case 4:
                    //quantityOfProduct
                    System.out.print("Enter a new product quantity: ");
                    String npQuan = scan.next();
                    String tempFileQP = "tempFileQP.txt";
                    File newFileQP = new File(tempFileQP);
                    File oldForQuan = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\quantityOfProduct.txt");
                    try {
                        BufferedReader br = new BufferedReader(new FileReader("quantityOfProduct.txt"));
                        FileWriter fw = new FileWriter(tempFileQP);
                        BufferedWriter bw = new BufferedWriter(fw);
                        while ((tempLine = br.readLine()) != null) {
                            quanOfProd = tempLine.split(",");
                            quanOfProdList = new ArrayList<>(Arrays.asList(quanOfProd));

                            quanOfProdList.set(choice - 1, npQuan);

                            for (int i = 0; i < quanOfProdList.size(); i++) {
                                bw.write(quanOfProdList.get(i) + ",");

                            }

                        }
                        bw.flush();
                        bw.close();
                        br.close();
                        oldForQuan.delete();
                        File dumpQP = new File("quantityOfProduct.txt");
                        newFileQP.renameTo(dumpQP);

                    } catch (Exception e) {
                        System.out.println("Line editProd()" + e);
                    }
                    breaker = false;
                    adMaintain();
                    break;
                case 0:
                    System.out.println("...--Keep Grindin'--...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please try again");
                    breaker = true;
                    break;
            }
        }
    }

    /**
     * This method is use by the admins to delete a particular product.
     */
    public static void deleteProd() {
        scan = new Scanner(System.in);
        System.out.println("Deleting products...");
        nameOfProd();
        System.out.print("What do you want to delete? ");
        int choice = scan.nextInt();

        //nameOfProduct
        String tempFileNP = "tempNP.text";
        File oldForName = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\nameOfProduct.txt");
        File newFileNP = new File(tempFileNP);

        String tempLine = "";
        //Declaring different kinds of variable
        String[] nprod, brOfProd, priceOfProd, quanOfProd;
        List<String> nprodList, brOfProdList, priceOfProdList, quanOfProdList;

        try {
            BufferedReader br = new BufferedReader(new FileReader("nameOfProduct.txt"));
            FileWriter fw = new FileWriter(tempFileNP);
            BufferedWriter bw = new BufferedWriter(fw);
            while ((tempLine = br.readLine()) != null) {
                nprod = tempLine.split(",");
                nprodList = new ArrayList<>(Arrays.asList(nprod));
                nprodList.remove(choice - 1);

                for (int i = 0; i < nprodList.size(); i++) {
                    bw.write(nprodList.get(i) + ",");

                }

            }
            bw.flush();
            bw.close();
            br.close();
            oldForName.delete();
            File dumpNP = new File("nameOfProduct.txt");
            newFileNP.renameTo(dumpNP);

        } catch (Exception e) {
            System.out.println("Line deleteProd()");
        }
        //brandOfProduct
        String tempFileBP = "tempFileBP.txt";
        File newFileBP = new File(tempFileBP);
        File oldForBrand = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\brandOfProduct.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader("brandOfProduct.txt"));
            FileWriter fw = new FileWriter(tempFileBP);
            BufferedWriter bw = new BufferedWriter(fw);
            while ((tempLine = br.readLine()) != null) {
                brOfProd = tempLine.split(",");
                brOfProdList = new ArrayList<>(Arrays.asList(brOfProd));
                brOfProdList.remove(choice - 1);

                for (int i = 0; i < brOfProdList.size(); i++) {
                    bw.write(brOfProdList.get(i) + ",");

                }

            }
            bw.flush();
            bw.close();
            br.close();
            oldForBrand.delete();
            File dumpBP = new File("brandOfProduct.txt");
            newFileBP.renameTo(dumpBP);

        } catch (Exception e) {
            System.out.println("Line deleteProd()");
        }
        //priceOfProduct
        String tempFilePP = "tempFilePP.txt";
        File newFilePP = new File(tempFilePP);
        File oldForPrice = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\priceOfProduct.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader("priceOfProduct.txt"));
            FileWriter fw = new FileWriter(tempFilePP);
            BufferedWriter bw = new BufferedWriter(fw);
            while ((tempLine = br.readLine()) != null) {
                priceOfProd = tempLine.split(",");
                priceOfProdList = new ArrayList<>(Arrays.asList(priceOfProd));
                priceOfProdList.remove(choice - 1);

                for (int i = 0; i < priceOfProdList.size(); i++) {
                    bw.write(priceOfProdList.get(i) + ",");

                }

            }
            bw.flush();
            bw.close();
            br.close();
            oldForPrice.delete();
            File dumpPP = new File("priceOfProduct.txt");
            newFilePP.renameTo(dumpPP);

        } catch (Exception e) {
            System.out.println("Line deleteProd()");
        }
        //quantityOfProduct
        String tempFileQP = "tempFileQP.txt";
        File newFileQP = new File(tempFileQP);
        File oldForQuan = new File("C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\quantityOfProduct.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader("quantityOfProduct.txt"));
            FileWriter fw = new FileWriter(tempFileQP);
            BufferedWriter bw = new BufferedWriter(fw);
            while ((tempLine = br.readLine()) != null) {
                quanOfProd = tempLine.split(",");
                quanOfProdList = new ArrayList<>(Arrays.asList(quanOfProd));

                quanOfProdList.remove(choice - 1);

                for (int i = 0; i < quanOfProdList.size(); i++) {
                    bw.write(quanOfProdList.get(i) + ",");

                }

            }
            bw.flush();
            bw.close();
            br.close();
            oldForQuan.delete();
            File dumpQP = new File("quantityOfProduct.txt");
            newFileQP.renameTo(dumpQP);

        } catch (Exception e) {
            System.out.println("Line deleteProd()" + e);
        }

        adMaintain();
    }

    /**
     * This method is for showing products for admins only.
     */
    public static void showProd() {

        System.out.println("---Available Products are the following---");
        //Display Products
        nameOfProd();
        System.out.println();

        adMaintain();
    }

    /**
     * This method is for selection for consumers only.
     *
     * @param username
     */
    public static void forConsumers(String username) {
        scan = new Scanner(System.in);
        boolean stopper = true;
        System.out.println("");
        System.out.println("Welcome " + username + "....");
        while (stopper) {

            System.out.println("---Available Products are the following---");
            //Display Products
            nameOfProd();
            System.out.println("[0] Exit");
            System.out.println("");
            System.out.print("What do you want to buy? ");

            int choice = scan.nextInt();

            if (choice == 0) {
                System.out.println("****Thanks for choosing BACSI Vape Shop****");
                stopper = false;
            }

            if (choice != 0) {
                System.out.print("Quantity: ");
                int quantity = scan.nextInt();
                System.out.print("Money: ");
                double pay = scan.nextDouble();

                manageProducts(choice, quantity, pay, username);// For managing the products
            }

        }

    }

    /**
     * This method use for showing the products. It uses the name of product to
     * show it. Two in one method it uses the name of the product to show the
     * brand, price, and quantity of the product
     */
    public static void nameOfProd() {
        String filepathNOP = "C:\\Users\\Cristina L Cendana\\Desktop\\NU Project\\InventoryProject\\nameOfProduct.txt";
        String tempLine;
        String[] tempBr = new String[0];
        String[] tempPrice = new String[0];
        String[] tempQuan = new String[0];
        String[] perProd, brOfProd, strPrice, theQuan;
        double[] doublePrice;

        try {

            BufferedReader br = new BufferedReader(new FileReader(filepathNOP));

            while ((tempLine = br.readLine()) != null) {
                perProd = tempLine.split(",");
                ArrayList<String> nprod = new ArrayList<>(Arrays.asList(perProd));
                brOfProd = brandOfProd(tempBr);
                strPrice = priceOfProd(tempPrice);
                doublePrice = Arrays.stream(strPrice).mapToDouble(Double::parseDouble).toArray();
                theQuan = quanOfProd(tempQuan);
                for (int count = 0; count < perProd.length; count++) {
                    System.out.println("[" + (count + 1) + "] " + nprod.get(count) + " - " + brOfProd[count] + " - " + "P" + doublePrice[count] + " - " + theQuan[count] + "PCS");
                }
            }

            br.close();

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println(ioe);
            ioe.printStackTrace();
        }

    }

    /**
     * This method read the file brandOfProduct file and use it.
     *
     * @param brOfProd
     * @return
     */
    public static String[] brandOfProd(String[] brOfProd) {
        String tempLine;

        try {
            BufferedReader br = new BufferedReader(new FileReader("brandOfProduct.txt"));
            while ((tempLine = br.readLine()) != null) {
                brOfProd = tempLine.split(",");

            }

            br.close();

        } catch (Exception e) {
            System.out.println("Line 275");
        }
        return brOfProd;
    }

    /**
     * This method read the priceOfProduct file and use it.
     *
     * @param priceOfProd
     * @return
     */
    public static String[] priceOfProd(String[] priceOfProd) {
        String tempLine;
        try {
            BufferedReader br = new BufferedReader(new FileReader("priceOfProduct.txt"));
            while ((tempLine = br.readLine()) != null) {
                priceOfProd = tempLine.split(",");

            }

            br.close();

        } catch (Exception e) {
            System.out.println("Line 291");
        }
        return priceOfProd;
    }

    /**
     * This method read the file quantityOfProduct file and use it.
     *
     * @param quanOfProd
     * @return
     */
    public static String[] quanOfProd(String[] quanOfProd) {
        String tempLine;
        try {
            BufferedReader br = new BufferedReader(new FileReader("quantityOfProduct.txt"));
            while ((tempLine = br.readLine()) != null) {
                quanOfProd = tempLine.split(",");

            }

            br.close();

        } catch (Exception e) {
            System.out.println("Line 291");
        }
        return quanOfProd;
    }

    /**
     * This method calculate the quantity * price and managing in how many the
     * product is.
     *
     * @param choice
     * @param quantity
     * @param pay
     * @param username
     */
    public static void manageProducts(int choice, int quantity, double pay, String username) {
        //Quantity of product
        String tempLine, tempFile = "temp.txt";
        File oldFile = new File("quantityOfProduct.txt");
        File newFile = new File(tempFile);
        double total = 0, change = 0;
        String[] quanOfProd;
        int[] intQuan;
        double[] doubPrice;
        List<Integer> intQuanList = null;
        List<Double> doubPriceList = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader("priceOfProduct.txt"));
            while ((tempLine = br.readLine()) != null) {
                quanOfProd = tempLine.split(",");
                doubPrice = Arrays.stream(quanOfProd).mapToDouble(Double::parseDouble).toArray();
                doubPriceList = Arrays.stream(doubPrice).boxed().collect(Collectors.toList());

                total = quantity * doubPriceList.get(choice - 1);
                change = pay - total;

            }
            if (total > pay) {
                System.out.println("The total is bigger than you money");

                br.close();
            } else {
                try {
                    BufferedReader brQP = new BufferedReader(new FileReader("quantityOfProduct.txt"));
                    FileWriter fw = new FileWriter(tempFile);
                    BufferedWriter bw = new BufferedWriter(fw);
                    while ((tempLine = brQP.readLine()) != null) {
                        quanOfProd = tempLine.split(",");
                        intQuan = Arrays.stream(quanOfProd).mapToInt(Integer::parseInt).toArray();
                        intQuanList = Arrays.stream(intQuan).boxed().collect(Collectors.toList());

//                ArrayList<Integer> intQuanArr = new ArrayList<>(Arrays.asList(intQuan));
//                intQuan[choice - 1] = intQuan[choice - 1] - quantity;
                        intQuanList.set(choice - 1, intQuanList.get(choice - 1) - quantity);

                        for (int i = 0; i < intQuan.length; i++) {
                            bw.write(intQuanList.get(i).toString() + ",");

                        }

                    }

                    bw.flush();
                    bw.close();
                    brQP.close();
                    oldFile.delete();
                    File dump = new File("quantityOfProduct.txt");
                    newFile.renameTo(dump);

                } catch (Exception e) {
                    System.out.println("Line 473" + e);
                }

                System.out.println("Successful, your change is P" + change);

                br.close();
            }
        } catch (IndexOutOfBoundsException iobe) {
            System.out.println("Wrong input");
        } catch (Exception e) {
            System.out.println("Error Line 591" + e);
        }

    }
}
