//M. M. Kuttel 2025 mkuttel@gmail.com
package barScheduling;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*class for the patrons at the bar*/

public class Patron extends Thread {
	
	private Random random;// for variation in Patron behaviour
	private CountDownLatch startSignal; //all start at once, actually shared
	private Barman theBarman; //the Barman is actually shared though

	private int ID; //thread ID 
	private int numberOfDrinks;

	//Timing values
	private long firstOrderPlacedTime;
	private long lastDrinkFinishTime;
	private long waitingTime = 0;
	private long turnaroundTime = 0;
	private long responseTime = 0;
	long[] orderPlaceTime = new long[5];
	long[] orderReceiveTime = new long[5];
	long[] orderDrinkFinishTime = new long[5];
	long[] waitingTimes = new long[5];


	private DrinkOrder [] drinksOrder;
	
	
	Patron( int ID,  CountDownLatch startSignal, Barman aBarman, long seed) {
		this.ID=ID;
		this.startSignal=startSignal;
		this.theBarman=aBarman;
		this.numberOfDrinks=5; // number of drinks is fixed
		drinksOrder=new DrinkOrder[numberOfDrinks];
		if (seed>0) random = new Random(seed);// for consistent Patron behaviour
		else random = new Random();
	}
	
	
//this is what the threads do	
	public void run() {
		try {
			
			//Do NOT change the block of code below - this is the arrival times
			startSignal.countDown(); //this patron is ready
			startSignal.await(); //wait till everyone is ready
			int arrivalTime = ID*50; //fixed arrival for testing
	        sleep(arrivalTime);// Patrons arrive at staggered  times depending on ID 
			System.out.println("+new thirsty Patron "+ this.ID +" arrived"); //Patron has actually arrived
			//End do not change
			

	        for(int i=0;i<numberOfDrinks;i++) {

	        	drinksOrder[i]=new DrinkOrder(this.ID); //order a drink (=CPU burst)	        
	        	//drinksOrder[i]=new DrinkOrder(this.ID,i); //fixed drink order (=CPU burst), useful for testing
				System.out.println("Order placed by " + drinksOrder[i].toString()); //output in standard format  - do not change this
				
				//Record time of order placed
				orderPlaceTime[i] = System.currentTimeMillis(); 

				theBarman.placeDrinkOrder(drinksOrder[i]);
				drinksOrder[i].waitForOrder();

				//Record time when order is received
				orderReceiveTime[i] = System.currentTimeMillis();
				//waitingTimes[i] = orderReceiveTime[i] - (drinksOrder[i].getPreparationTime()) - orderPlaceTime[i];

				System.out.println("Drinking patron " + drinksOrder[i].toString());
				sleep(drinksOrder[i].getImbibingTime()); //drinking drink = "IO"
				orderDrinkFinishTime[i] = System.currentTimeMillis();
				
				//code waiting time
			}

			// Record finish time for throughput window tracking
			TimingLog.logPatronFinishTime(System.currentTimeMillis());

			System.out.println("Patron "+ this.ID + " completed ");

			

			// Turnaround = last drink finish - first order placed
			turnaroundTime = lastDrinkFinishTime - firstOrderPlacedTime;

			//WRONG SHOULD BE Oorder start time
			//for (int i=0; i<numberOfDrinks; i++) {
			//	waitingTime += orderReceiveTime[i] - orderPlaceTime[i]; //waiting time for each drink
			//}

			//Response Time = Time between placing order for first drink and receiving it
			responseTime = orderReceiveTime[0] - orderPlaceTime[0];


			TimingLog.logPatronMetrics(this.ID, responseTime, waitingTime, turnaroundTime);
			
		} catch (InterruptedException e1) {  //do nothing
		}
}
}
	

