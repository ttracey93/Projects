import java.util.Random;
import java.util.Scanner;

public class ProducerConsumerTest 
{
    //make a bunch of producers and consumers based on the users input
	public static void main(String[] args) 
	{
		System.out.println("Enter the number of extra producers and consumers" 
				+ "\n" + "you want as one integer. There will always" +
				  "\n" + "be the original set. So if you want five sets" +
				"\n of producers/consumers going enter 4." +
				  "\nBeware that the output will stream quickly." + "\n\nHow many? ");
		Scanner in = new Scanner(System.in);
		int randy = in.nextInt();
		
                //make the default producer/consumer set
		Cell c = new Cell();
		Producer p1 = new Producer(c, 1);
		Consumer c1 = new Consumer(c, 1);
		p1.start(); 
		c1.start();
		
                //make all the extra producer/consumer sets
		for(int i=0; i<randy; i++)
		{
			Cell c2 = new Cell();
			Producer prod = new Producer(c2, i+2);
			Consumer con = new Consumer(c2, i+2);
			prod.start();
			con.start();
		}
		
                //let them go until they are done
		in.close();
    }
}

//cell class that monitors when it is being accessed 
class Cell 
{
	private int contents;
    private boolean available = false;
    
    //only the producer will access this method, so it can safely wait
    //until the integer is present in the cell
    public synchronized int get() 
    {
    	while (available == false) 
    	{
    		try 
    		{
    			wait();
    		}
    		catch (InterruptedException e) 
    		{}
    	}
        available = false;
        notifyAll();
        return contents;
    }
    //the producer simply puts the object in the contents and sets the available flag
    public synchronized void put(int value) 
    {
        while (available == true) 
        {
        	try 
        	{
        		wait();
        	}
        	catch (InterruptedException e) 
        	{} 
        }
        contents = value;
        available = true;
        notifyAll();
    }
}

//consumer object
class Consumer extends Thread 
{
	private Cell cell;
    private int number;
    
    public Consumer(Cell c, int number) 
    {
    	cell = c;
        this.number = number;
    }
    
    //get the value input by the producer 100 times
    public void run() 
    {
    	int value = 0;
        for (int i = 0; i < 100; i++) 
        {
        	value = cell.get();
            System.out.println("Consumer " 
            		+ this.number
            		+ " got " + value);
        }
    }
}

//producer thread, which can be instantiated to allow more than
//one producer at a time
class Producer extends Thread 
{
    
	private Cell cell;
	private int number;

	public Producer(Cell c, int number) 
	{
		cell = c;
		this.number = number;
	}

    @Override
    //randomly produce integers and put them into the cell
	public void run() 
	{
		Random rand= new Random();
		for (int i = 0; i < 100; i++) 
		{
			int num = rand.nextInt(1000);
			cell.put(num);
			System.out.println("Producer " + this.number
					+ " put " + num);
			try 
			{
				sleep((int)(Math.random() * 100));
			} 
			catch (InterruptedException e) 
			{}
		}
	}
}