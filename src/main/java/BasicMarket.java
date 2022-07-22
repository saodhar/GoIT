import java.util.*;
public class BasicMarket {
    public Buyer[] buyerList;
    public Product[] productList;
    public Map<Integer, List<Integer>> purchases;

    public BasicMarket() {
        // 3 initial buyers
        Buyer buyer1 = new Buyer(1, "John", "Smith", 1000);
        Buyer buyer2 = new Buyer(2, "John", "Doe", 15);
        Buyer buyer3 = new Buyer(3, "Jane", "Doe", 15000);

        // 3 initial products
        Product product1 = new Product(1, "this", 10);
        Product product2 = new Product(2, "that", 50);
        Product product3 = new Product(3, "everything", 10000);

       this.buyerList = new Buyer[] {buyer1, buyer2, buyer3};
       this.productList = new Product[] {product1, product2, product3};
       this.purchases = new HashMap<>();
    }

    public void menu() {
        System.out.println("Choose action:");
        System.out.println("1. Display list of all users");
        System.out.println("2. Display list of all products");
        System.out.println("3. Purchase a product for a user");
        System.out.println("4. Display list of user products by user id");
        System.out.println("5. Display list of buyers by product id");
        System.out.println("6. Quit");

        System.out.print("Action: ");
        Scanner scanner = new Scanner(System.in);
        try {
            switch (scanner.nextInt()) {
                case 1:
                    this.printBuyers();
                    break;
                case 2:
                    this.printProducts();
                    break;
                case 3:
                    this.buyProduct();
                    break;
                case 4:
                    this.showBuyerPurchases();
                    break;
                case 5:
                    this.showProductPurchases();
                    break;
                case 6:
                    System.out.println("Quitting...");
                    break;
                default:
                    System.out.println("You should input a number between 1 and 6" + "\n");
                    this.menu();
            }
        } catch (BrokeException e) {
            System.out.println(e.getMessage());
            this.menu();
        }
        catch(Exception e) {
            System.out.println("You should input a number between 1 and 6" + "\n");
            this.menu();
        }
    }

    public void printBuyers () {
        System.out.println("List of Users:");
        for (Buyer buyer: buyerList) {
            System.out.println(buyer.getId() + ". Name: " + buyer.getBName() + ". Available money: " + buyer.getMoney());
        }
        System.out.println();
        this.menu();
    }

    public void printProducts () {
        System.out.println("List of Products:");
        for (Product product: productList) {
            System.out.println(product.getId() + ". Name: " + product.getPName() + ". Price: " + product.getPrice());
        }
        System.out.println();
        this.menu();
    }

    public void buyProduct () throws BrokeException {
        List<Integer> tempPurchases = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int buyerID = 0;
        int productID = 0;
        int money = 0;
        int price = 0;
        System.out.print("Enter buyer ID: ");
        try {
            buyerID = scanner.nextInt();
            money = buyerList[buyerID - 1].getMoney();
        } catch (Exception e) {
            System.out.println("Wrong buyer ID format" + "\n");
            this.menu();
        }
        System.out.print("Enter product ID: ");
        try {
            productID = scanner.nextInt();
            price = productList[productID - 1].getPrice();
        } catch (Exception e) {
            System.out.println("Wrong product ID format" + "\n");
            this.menu();
        }
        if (money < price) {
            throw new BrokeException();
        } else {
            buyerList[buyerID - 1].spendMoney(price);
            if (purchases.containsKey(buyerID)) {
                tempPurchases = purchases.get((buyerID));
                tempPurchases.add(productID);
                purchases.replace(buyerID, tempPurchases);
            } else {
                tempPurchases.add(productID);
                purchases.put(buyerID, tempPurchases);
            }
        }
        System.out.println();
        this.menu();
    }

    public void showBuyerPurchases () {
        Scanner scanner = new Scanner(System.in);
        int buyerID = 0;
        System.out.print("Enter buyer ID: ");
        try {
            buyerID = scanner.nextInt();
            buyerList[buyerID - 1].getBName();
        } catch (Exception e) {
            System.out.println("Wrong buyer ID format" + "\n");
            this.menu();
        }
        if (purchases.containsKey(buyerID)) {
            //Transform list of product indices onto product names
            List<String> purchasesPNames = new ArrayList<>();
            for (Integer purchase: purchases.get(buyerID)) {
                purchasesPNames.add(productList[purchase - 1].getPName());
                }
            System.out.println("Purchases for user " + buyerList[buyerID - 1].getBName() + ": " + purchasesPNames + "\n");
        } else {
            System.out.println("No purchases" + "\n");
        }
        this.menu();
    }

    public void showProductPurchases () {
        Scanner scanner = new Scanner(System.in);
        int productID = 0;
        System.out.print("Enter buyer ID: ");
        try {
            productID = scanner.nextInt();
            productList[productID - 1].getPName();
        } catch (Exception e) {
            System.out.println("Wrong product ID format" + "\n");
            this.menu();
        }

        List<String> buyers = new ArrayList<>();

        for (Integer key: purchases.keySet()) {
            if (purchases.get(key).contains(productID)) {
                buyers.add(buyerList[key - 1].getBName());
            }
        }

        if (!buyers.isEmpty()) {
            System.out.println("Product purchased by users: " + buyers + "\n");
        } else {
            System.out.println("Not purchased" + "\n");
        }
        this.menu();
    }

    public static void main (String[] args) {
        BasicMarket testMarket = new BasicMarket();
        testMarket.menu();

    }
}

class Buyer {
    private int id;
    private String fName;
    private String lName;
    private int money;

    public Buyer (int id, String fName, String lName, int money){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.money = money;
    }

    public int getId () {
        return this.id;
    }

    public String getFName () {
        return this.fName;
    }

    public String getLName () {
        return this.lName;
    }

    public String getBName () {
        return this.fName + " " + this.getLName();
    }

    public int getMoney () {
        return this.money;
    }

    public void spendMoney(int amount) {
        this.money -= amount;
    }
}


class Product {
    private int id;
    private String pName;
    private int price;

    public Product (int id, String pName, int price){
        this.id = id;
        this.pName = pName;
        this.price = price;
    }

    public int getId () {
        return this.id;
    }

    public String getPName () {
        return this.pName;
    }

    public int getPrice () {
        return this.price;
    }
}

class BrokeException extends Exception {
    BrokeException() {
        super("Not enough money!" + "\n");
    }
}