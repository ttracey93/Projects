import java.util.Random;
import java.util.Scanner;

public class ProducerConsumerTest 
{
	public static void main(String[] args) 
	{
		System.out.println("Enter the number of extra producers and consumers" 
				+ "\n" + "you want as one integer. There will always" +
				  "\n" + "be the original set. So if you want five sets" +
				"\n ofproducers/consumers going enter 4." +
				  "\nBeware that the output will stream quickly." + "\n\nHow many? ");
		Scanner in = new Scanner(System.in);
		int randy = in.nextInt();
		
		Cell c = new Cell();
		Producer p1 = new Producer(c, 1);
		Consumer c1 = new Consumer(c, 1);
		p1.start(); 
		c1.start();
		
		for(int i=0; i<randy; i++)
		{
			Cell c2 = new Cell();
			Producer prod = new Producer(c2, i+2);
			Consumer con = new Consumer(c2, i+2);
			prod.start();
			con.start();
		}
		
		in.close();
    }
}
class Cell 
{
	private int contents;
    private boolean available = false;
    
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

class Consumer extends Thread 
{
	private Cell cell;
    private int number;
    
    public Consumer(Cell c, int number) 
    {
    	cell = c;
        this.number = number;
    }
    
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

class Producer extends Thread 
{
	private Cell cell;
	private int number;

	public Producer(Cell c, int number) 
	{
		cell = c;
		this.number = number;
	}

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