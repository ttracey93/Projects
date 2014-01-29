import java.io.*;
import java.util.Scanner;

//the item class defines the object that will be put into the hash table
class Item
{
    //the items have an address, and a string used as the key
    private int address;
    private String itemString;
    
    //constuctor 
    public Item(String item)
    {
        String[] strings = item.split(" ");
        itemString = strings[0];
        
        if(strings.length > 1)
        {
            address = Integer.parseInt(strings[1]);
        }
    }
    
    public String getKey()
    {
        return itemString;
    }
    
    public int getAddress()
    {
        return address;
    }
    
    //sets the key to a special key so that I can reuse
    //deleted spaces in the hash table
    public void illegitimize()
    {
        itemString = "*del";
        address = -1;
    }
}

class HashTable
{
    //array of items
    private Item[] hashArray;
    private int arraySize;
    
    public HashTable(int size)
    {
       arraySize = size;
       hashArray = new Item[arraySize];
    }
    
    //hash the item based on its key
    public int hash(Item item)
    {
        String key = item.getKey();
        
        //Horners hash method
        int hashVal = 0;
        for(int i=0; i<key.length(); i++)
        {
            int letter = key.charAt(i) - 96;
            hashVal = (hashVal * 26 + letter) % arraySize;
        }
        
        return hashVal;
    }
    
    //method to insert the item
    public void insert(Item item)
    {
        int hashVal = hash(item);
        
        Item existingItem = hashArray[hashVal];
		  
        //keep going until we find an empty cell,
        //a deleted item, or we realize that the hased value is
        //already in the table
        while(existingItem != null && 
                !(existingItem.getKey().contains("*")) &&
                !existingItem.getKey().equals(item.getKey()))
        {
            hashVal++;
            hashVal %= arraySize;
        }
		  
        if(existingItem == null)
        {
            hashArray[hashVal] = item;
            System.out.println("The item " + item.getKey() + " was placed at location " + hashVal);
        }
	else if(existingItem.getKey().equals(item.getKey()))
	{
            System.out.println("Error: " + item.getKey() + " already exists at location " + hashVal);
	}
	else
	{
            System.out.println("Unknown error");
	}
    }
    
    //deletion of items
    public Item delete(Item item)
    {
        int hashVal = hash(item);
        
        while(hashArray[hashVal] != null)
        {
            if(hashArray[hashVal].getKey().equals(item.getKey()))
            {
                Item temp = hashArray[hashVal];
                item.illegitimize();
                System.out.println(item.getKey() + " deleted from the table at "
                        + "locaton " + hashVal);
                return temp;
            }
            
            hashVal++;
            hashVal %= arraySize;
        }
        
        return null;
    }
    
    //item search based on key
    public Item find(Item item)
    {
        int hashVal = hash(item);
        
        while(hashArray[hashVal] != null)
        {
            if(hashArray[hashVal].getKey().equals(item.getKey()))
            {
                System.out.println(item.getKey() + " found at location "
                        + hashVal + " with address " + 
                        hashArray[hashVal].getAddress());
                return hashArray[hashVal];
            }
            
            hashVal++;
            hashVal %= arraySize;
        }
        
        System.out.println("Error: " + item.getKey() + " not found in the "
                + "table");
        return null;
    }
}

//main class
public class HashTableMain
{
    public static void main(String[] args) throws Exception
    {
        //make a new file
        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        
        //creation vars for storage
        Item currentItem;
        String currentItemsKey;
        int currentItemsAddress;
        
        //make the table
        HashTable table = new HashTable(50);
        
        //while there is more left in the file, make items based on the
        //input and put them into the hash table
        while(input.hasNextLine())
        {
            currentItem = new Item(input.nextLine());
            currentItemsKey = currentItem.getKey();
            currentItemsAddress = currentItem.getAddress();
            
            if(currentItemsAddress > 0)
            {
                table.insert(currentItem);
            }
            else
            {
                table.find(currentItem);
            }
        }
    }
}