/*
Tyler Tracey, n00767255
COP3530 Spring 2013
Dr. Martin
Project 6, Java
Due April 9, 2013

********I'm sorry about the lack of comments. I JUST finished and the project
********is due in about 20 minutes. I am going to comment as much as I can,
********but please try to excuse me if the program is lacking them
*/

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

//main class
public class n00767255 
{
    //main method
    public static void main(String[] args) 
    {
        //vars
        HashTable emptyHash = new HashTable(0);  
        Scanner scan = new Scanner(System.in);
	DecimalFormat df = new DecimalFormat("#####.####");
	int maxInsrt, maxSrch, temp, comparisons;
        Random r = new Random();
	double[] loadFactor = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
	double[] theoFail = new double[9];
	double[] theoSuccess = new double [9];
	Boolean isFound;
	int[] found = new int[9];
	int[] notfound = new int [9];
	int[] hashSize = new int[9];
	double[] foundComp = new double[9];			
	double[] notfoundComp = new double[9];

        //print small menu and read input
	System.out.print("Please enter max value of ints to insert, and max\n"
                + "Value of ints to search for. You can enter them with a space\n" 
                + "Or one after the other."
                + "\nEnter here: ");
        maxInsrt = scan.nextInt();
	maxSrch = scan.nextInt(); 
		
        //print the first two lines for formatting
	printFirstTwoLines();
	
        //figure out the actual hash table sizes based on load factor
	for(int i = 0; i < 9; i++)
	{
            temp = (int) Math.ceil((10000/loadFactor[i]));
	    if(emptyHash.isPrime(temp)== false)
            { 
                hashSize[i] = emptyHash.nextPrime(temp);
            }
            else
            {
	    	hashSize[i] = temp; 
            }
        }
	     
        //get the theoretical succ and fail for each linear load factor
	for(int j = 0; j < 9; j++)
        {
            theoFail[j] = emptyHash.theoreticalLinearFailure(loadFactor[j]);
            theoSuccess[j] = emptyHash.theoreticalLinearSuccess(loadFactor[j]);
	}
		
	for(int i = 0; i < 9; i++)
	{
            //temp table
            HashTable hash = new HashTable(hashSize[i]);
	
            //load each table with 10k random ints
	    for(int j = 0; j < 10000 ; j++) 
	    { 
	    	temp = r.nextInt((maxInsrt - 1) + 1) + 1;
	    	Item t = new Item(temp);
	    	hash.linearInsertion(temp, t);
	    }
	    
            double fcomp = 0, nfcomp = 0;
            //search for 100 random ints
	    for(int j = 0; j < 100; j++)
	    {
                temp = r.nextInt((maxSrch - 1) + 1) + 1;
	    	isFound = hash.linearSearch(temp);
	    	if(isFound == true) 
	 	{
                    found[i] = found[i] + 1; 
                    comparisons = hash.getPC();
	    	    fcomp = (fcomp + comparisons );
	    	    hash.resetPC(1);        
	 	}  
	    	else 
                {
                    notfound[i] = notfound[i] + 1;
                    nfcomp += hash.getPC();
                    hash.resetPC(1);
	    	}
	    	
                if(found[i] != 0)
                {
	    	    foundComp[i] = fcomp / found[i];
                }
	    	else 
                {
                    foundComp[i] = -1; 
                }
	    	
                if(notfound[i] != 0)
                {
	    	   notfoundComp[i] = nfcomp / notfound[i];
                }
                else
                {
	    		notfoundComp[i] = -1; 
                }
	    }
	    
            //print results for linear
            if(foundComp[i] == -1) 
            {
                System.out.println(loadFactor[i] + "\t"+ "N/A" + "\t     "+
                        df.format(found[i])+ "\t"+ df.format(theoSuccess[i]) + 
                        "\t   " + df.format(notfoundComp[i])  +"   \t "+
                        df.format(notfound[i]) +"   \t "+ 
                        df.format(theoFail[i]));
            }
            else if(notfoundComp[i] == -1)
            { 
	    	    System.out.println(loadFactor[i] + "\t"+ 
                            df.format(foundComp[i]) + "\t"+df.format(found[i])+ 
                            "\t "+ df.format(theoSuccess[i]) + "\t " + "N/A"  +
                            "\t "+df.format(notfound[i]) +"\t "+ 
                            df.format(theoFail[i]));
            }
            else
            { 
            System.out.println(loadFactor[i] + "\t    "+ 
                    df.format(foundComp[i]) + "         "+df.format(found[i])+
                    "  \t   "+ df.format(theoSuccess[i]) + "\t   " + 
                    df.format(notfoundComp[i])  +"\t "+df.format(notfound[i]) +
                    "\t "+ df.format(theoFail[i]));
            }

        }
	
        //print the two lines for formatting
        printFirstTwoLines();
	 
        //reset vars
	for(int i = 0; i < 9; i++)
	{
            found[i] = 0;
            notfound[i] = 0;
            theoFail[i] = 0;
            theoSuccess[i] = 0;
	}
	 
        //get the theoretical succ and fail for quad
	for(int j = 0; j < 9; j++)
	{
            theoFail[j] = emptyHash.theoreticalQuadraticFailure(loadFactor[j]);
            theoSuccess[j] = emptyHash.theoreticalQuadraticSuccess(loadFactor[j]);
	}
	
	for(int i = 0; i < 9; i++)
        {
            //temp table
            HashTable hash = new HashTable(hashSize[i]);
            
            //load each table
            for(int j = 0; j < 10000 ; j++) 
            { 
                temp = r.nextInt((maxInsrt - 1) + 1) + 1;
		Item t = new Item(temp);
		hash.quadraticInsertion(temp, t);
            }
	
            //search for 100 random ints
            //store info
            for(int j = 0; j < 100; j++)
            {
                int fcomp = 0, nfcomp = 0;
		temp = r.nextInt((maxSrch - 1) + 1) + 1;
		isFound = hash.quadraticSearch(temp);
		if(isFound == true)
		{
                    found[i] = found[i] + 1; 
                    fcomp += hash.getPC(); 
		    hash.resetPC(0);         
                }  
		else 
		{
                    notfound[i] = notfound[i] + 1;
		    nfcomp += hash.getPC();
		    hash.resetPC(0);
                }
		
                if(found[i] != 0) 
                {
                    foundComp[i] = fcomp / found[i];
                }
                else 
                {
                    foundComp[i] = -1;
                } 
		
                if(notfound[i] != 0) 
                {
                    notfoundComp[i] = nfcomp / notfound[i];
                }
		else 
                {
                    notfoundComp[i] = -1;
                }
            }
		 
            //print the results
            if(foundComp[i] == -1) 
            {
                System.out.println(loadFactor[i] + "\t"+ "N/A" + "\t"+
                        df.format(found[i])+ "\t "+ df.format(theoSuccess[i]) + 
                        "\t " + df.format(notfoundComp[i])  +"\t "+
                        df.format(notfound[i]) +"\t "+ df.format(theoFail[i]));
            }
            else if(notfoundComp[i] == -1) 
            {
                System.out.println(loadFactor[i] + "\t"+ 
                        df.format(foundComp[i]) + "\t"+df.format(found[i])+
                        "\t "+ df.format(theoSuccess[i]) + "\t " + "N/A"  +
                        "\t "+df.format(notfound[i]) +"\t "+
                        df.format(theoFail[i]));
            }
            else 
            {
                System.out.println(loadFactor[i] + "       "+
                        df.format(foundComp[i]) + "       \t"+
                        df.format(found[i])+ "\t "+ df.format(theoSuccess[i]) +
                        "  \t " + df.format(notfoundComp[i])  +"  \t "+
                        df.format(notfound[i]) +"\t "+ df.format(theoFail[i]));
            }
        }
	 
	
    }//end main
    
    //method to quickly and easily rewrite the two lines for formatting
    //of the tables
    public static void printFirstTwoLines()
    {
        System.out.println("load    emp-success  denom "
                + "theo-success  emp-failure denom "
                + "theo-failure");
        System.out.println("----    -----------  ----- "
                + "------------  ----------- ------  "
                + "------------");
    }//end print
}//end main class

//hash class
class HashTable
{
    //vars
    private Item[] hashArray;   
    private int size;
    private int comp = 0; 

    //constructor
    public HashTable(int s)
    {
        size = s;
        hashArray = new Item[size];
    }
	
    //hash function
    public int hash(int key)
    {
        int x ;
	int hashOut = 0;
	String	number;
	int n;

	number = Integer.toString(key);
	x = number.length();

        for (int i = 0; i < x; i++)
	{
            n = number.charAt (i);
            hashOut = ( ( (hashOut * 10) +n) % size);
	}
        
        return hashOut;
    }
    
    //linear insert func
    public void linearInsertion(int key, Item item)
    {
        int hashVal = hash(key);  
        while(hashArray[hashVal] != null)
        {
            hashVal++;
            hashVal %= size;           
        }
        
        hashArray[hashVal] = item;    
    } 

    //quadratic insert func. step is the square of the real step
    //if step is x+1, quad step is x+1^2. if step is 2, quad step
    //is x+2^2, etc
    public void quadraticInsertion(int key, Item item)
    {
        int hashVal = hash(key);  
        int stepSize = 0;   
        while(hashArray[hashVal] != null)
        {
            hashVal += Math.pow(stepSize, 2); 
            hashVal %= size;           
            stepSize++;
        }
        
        hashArray[hashVal] = item;     
    }  
     
    //reset num pc
    public void resetPC(int s)
    {   
        comp = s;
    }

    //sned pc to call
    public int getPC()
    {
        return comp;
    }
	   
    //linear search
    public boolean linearSearch(int key)  
    {
        int hashVal = hash(key);    

	while(hashArray[hashVal] != null)  
	{ 
            if(hashArray[hashVal].getKey() == key) 
	    { 
                return true;
	    } 
       
	    hashVal++;						
	    hashVal %= size;           
	    comp++; 
	}
        
        return false;                   
    }
    
    //quad search
    public Boolean quadraticSearch(int key) 
    {
        int hashVal = hash(key);  
	int stepSize = 0;              
		     
	while(hashArray[hashVal] != null)  
	{
            comp++; 
	    if(hashArray[hashVal].getKey() == key) 
	    {
                comp++;
	        return true; 
            } 
	         
            hashVal += Math.pow(stepSize, 2); 
	    hashVal %= size;            
	    stepSize++;   
	}
	
        return false; 
    }
           
    //returns next prime above table size
    public int nextPrime(int min) 
    {
        for(int j = min+1; true; j++) 
        {
            if( isPrime(j) ) 
            {
                return j;
            }
        } 
    }

    //return if a value is prime
    public boolean isPrime(int n) 
    {
        Boolean prime = false;
	for(int j=2; (j*j <= n); j++) 
        {
            if( n % j == 0) 
            {
                return prime;
            }
        } 
            return true; 
    }
    
    //return theo. linear success with the given load factor. load factors
    //start at 0.1, up to 0.9.
    public double theoreticalLinearSuccess(double loadFactor)
    {
        double L =( 1 + 1 / (1 - loadFactor) ) / 2;
	return L;
    }

    //return theo. linear failure with the given load factor. load factors
    //start at 0.1, up to 0.9.
    public double theoreticalLinearFailure(double loadFactor)
    {
        double L =1 + ((1/Math.pow((1 - loadFactor),2))/2); 
	return L;
    }
 
    //return theo. quadratic success with the given load factor. load factors
    //start at 0.1, up to 0.9.
    public double theoreticalQuadraticSuccess(double loadFactor)
    {
        double L = -(Math.log(1- loadFactor)/Math.log(2)) / loadFactor;
	return L;
    }

    //return theo. quadratic failure with the given load factor. load factors
    //start at 0.1, up to 0.9.
    public double theoreticalQuadraticFailure(double loadFactor)
    {
        double L = 1 / (1 - loadFactor);
	return L;
    }

}//end hash class

//item class
class Item
{
    private int integer;
    
    public Item(int iInt)
    {
        integer = iInt;
    }
    
    public int getKey()
    {
        return integer;
    }
}//end item class