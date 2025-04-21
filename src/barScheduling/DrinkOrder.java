//M. M. Kuttel 2025 mkuttel@gmail.com
//class representing a drink order for a patron
//DO NOT CHANGE ANYTHING IN THIS CLASS
package barScheduling;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class DrinkOrder  {

    //preparation time for drink assumes milliseconds are seconds
	//otherwise simulation would be realtime!
    public enum Drink { 
        Tequila("Tequila shot", 20, 3),
        Vodka("Vodka shot", 20, 3),
        BottledBeer("Beer", 25, 100),
        Cider("Cider", 25, 100),
        WineRed("Red Wine", 25,  150),
        WineWhite("White Wine", 25,  150),
        GinAndTonic("Gin and Tonic", 40, 110),
        DraftBeer("Draft Beer", 50, 100),
        Martini("Martini", 60, 110),
        Cosmopolitan("Cosmopolitan", 65, 50),
        BloodyMary("Bloody Mary", 70, 150),
        Margarita("Margarita", 75,80),
        Mojito("Mojito", 75, 110),
        PinaColada("Pina Colada", 100, 120),
        B52("B52", 200,10);
    	
    	
        private final String name;
        private int preparationTime; // time to prepare the drink (in minutes)
        private final int imbibingTime; // time to drink the drink (in minutes)
        

        Drink(String name, int preparationTime, int drinkingTime) {
            this.name = name;
            this.preparationTime = preparationTime;
            this.imbibingTime = drinkingTime;
        }

        public String getName() {
            return name;
        }

        public int getPreparationTime() {
            return preparationTime;
        }

        
        public int getImbibingTime() {
			return imbibingTime;
		}

		@Override
        public String toString() {
            return name;
        }
    }
    
    private final Drink drink;
    private int prepTime;
    public  static Random random = new Random();
    private int orderer;
    private AtomicBoolean orderComplete;

    //Timings to be recorded 
    private long orderPlacedTime = 0;
    private long orderStartTime = 0;
    private long orderCompletedTime = 0;

 //constructor
    public DrinkOrder(int patron, long orderPlacedTime) {
    	this(patron,random.nextInt(Drink.values().length), orderPlacedTime);    	
    }
    
    public DrinkOrder(int patron, int i, long orderPlaced) {
        Drink[] drinks = Drink.values();  // Get all enum constants
        drink=drinks[i];
    	orderComplete = new AtomicBoolean(false);
    	orderer=patron;
        prepTime=drink.getPreparationTime();
        orderPlacedTime = orderPlaced;
    }
    
    public static Drink getRandomDrink() {
        Drink[] drinks = Drink.values();  // Get all enum constants
        int randomIndex = random.nextInt(drinks.length);  // Generate a random index
        return drinks[randomIndex];  // Return the randomly selected drink
    }
    
    public int getExecutionTime() {
        return prepTime;
    }
    
    //time to drink the drink
    public int getImbibingTime() {
        return drink.getImbibingTime();
    }

    // Add method to indicate the time when barman starts making the drink
    public void startPreparation() {
        orderStartTime= System.currentTimeMillis();
    }
    
    //when interrupted making it
    public void setRemainingPreparationTime(int timeLeft) {
    	prepTime=timeLeft;
    }

    //Barman signals when order is done
    public synchronized void orderDone() {
        orderCompletedTime = System.currentTimeMillis();
        orderComplete.set(true);
        this.notifyAll();
    }
    
    //patrons wait for their orders
    public synchronized void waitForOrder() throws InterruptedException {
    	while(!orderComplete.get()) {
    		this.wait();
    	}
    }
    
    @Override
    public String toString() {
        return Integer.toString(orderer) +": "+ drink.getName();
    }

    public long getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public long getOrderStartTime() {
        return orderStartTime;
    }

    public long getOrderCompletedTime() {
        return orderCompletedTime;
    }
    
}