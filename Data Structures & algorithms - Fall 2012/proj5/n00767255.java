//Tyler Tracey, n00767255
//Spring 2013
//Data Structures, Assignment 5
//Dr. Martin

import java.io.*;
import java.util.*;

//defines the nodes for the huffman tree
class HuffmanNode
{
    public int freq;
    public char letter;
    public String code = "";      
    public HuffmanNode pLeft;       
    public HuffmanNode pRight;        
 
    //lets me know if the node is a leaf (no children)
    public boolean isLeaf()
    {
        if(pLeft == null && pRight == null)
        {
            return true;
	}
	else
	{
            return false;
	}
    }
 
    //display node
    public void displayHuffmanNode()     
    {
        System.out.print('{');
        System.out.print(freq);
        System.out.print(", ");
        System.out.print(letter);
        System.out.print(", ");
        System.out.print(code);
        System.out.print("} ");
    }
}

//defines the tree
class HuffmanTree 
{
    private HuffmanNode root;
    private int[] freqs;
    private TreeMap codes = new TreeMap();
 
    public HuffmanTree()
    { 
        root = null; 
    }
    
    public HuffmanTree(int[] iFreqs)
    {
        freqs = iFreqs;
        buildTree();
    }
    public void setRoot(HuffmanNode hRoot)
    {
        root = hRoot;
    }
 
    public HuffmanNode getRoot()
    {
        return root;
    }
 
    //code to display the tree. taken from the book
    public void displayHuffmanTree()
    {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;
        System.out.println();
        
        while(!isRowEmpty)
        {
            Stack localStack = new Stack();
            isRowEmpty = true;
 
            for(int j=0; j<nBlanks; j++)
            {
                System.out.print(' ');
            }
				  
            while(!globalStack.isEmpty())
            {
                HuffmanNode temp = (HuffmanNode)globalStack.pop();
                if(temp != null)
                {
                    System.out.print(temp.freq + " " + temp.letter);
                    localStack.push(temp.pLeft);
                    localStack.push(temp.pRight);
 
                    if(temp.pLeft != null || temp.pRight != null)
                    {
                        isRowEmpty = false;
                    }
                }
                else
                {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for(int j=0; j<nBlanks*2-2; j++)
                {
                    System.out.print(' ');
                }
            }  
            
            System.out.println();
            nBlanks /= 2;
            while(!localStack.isEmpty())
            {
                globalStack.push( localStack.pop() );
            }    
        }  
        System.out.println();
    } 
 
    //create the node for each letter
    private HuffmanNode[] createNodes()
    {
        HuffmanNode A = new HuffmanNode();
        HuffmanNode B = new HuffmanNode();
	HuffmanNode C = new HuffmanNode(); 
	HuffmanNode D = new HuffmanNode();
        HuffmanNode E = new HuffmanNode();
	HuffmanNode F = new HuffmanNode();
	HuffmanNode G = new HuffmanNode();
 
        HuffmanNode[] nodes = {A, B, C, D, E, F, G};
 
        char c = 'A';
        for (int i = 0; i < freqs.length; i++) 
        {
            HuffmanNode node = nodes[i];
 
            node.freq = freqs[i];
            node.letter = c++;
        }
 
        return nodes;
    }
 
    //build the full tree
    private HuffmanTree[] buildTrees()
    {
        HuffmanNode[] nodes = createNodes();
 
        HuffmanTree treeA = new HuffmanTree(); 
	HuffmanTree treeB = new HuffmanTree(); 
	HuffmanTree treeC = new HuffmanTree(); 
	HuffmanTree treeD = new HuffmanTree();
        HuffmanTree treeE = new HuffmanTree(); 
	HuffmanTree treeF = new HuffmanTree(); 
	HuffmanTree treeG = new HuffmanTree();
 
        HuffmanTree[] trees = {treeA, treeB, treeC, treeD, treeE, treeF, treeG};
 
        for (int i = 0; i < trees.length; i++) 
        {
            trees[i].setRoot(nodes[i]);
        }
 
        return trees;
    }
 
    //build a tree
    private void buildTree()
    {
        HuffmanTree[] trees = buildTrees();
        PriorityQ queue = new PriorityQ(freqs.length);
 
        for (HuffmanTree t : trees) 
        {
            queue.insert(t); 
        }
 
        while (!queue.lastOne()) 
        { 
            HuffmanTree tree1 = queue.remove();
            HuffmanTree tree2 = queue.remove();
 
            HuffmanTree tree = new HuffmanTree(); 
            HuffmanNode node = new HuffmanNode(); 
 
            node.freq = tree1.getRoot().freq + tree2.getRoot().freq; 
            node.pLeft = tree1.getRoot(); 
            node.pRight = tree2.getRoot(); 
            tree.setRoot(node); 
            queue.insert(tree); 
        }
        this.setRoot(queue.remove().getRoot());
    }
	 
    //construct the codes
    public void getCode(HuffmanNode localRoot, String code)
    {
        if(localRoot != null) 
        {
            localRoot.code = code; 
            getCode(localRoot.pLeft, localRoot.code + "0"); 
            getCode(localRoot.pRight, localRoot.code + "1"); 
 
            if (localRoot.pLeft == null && localRoot.pRight == null) { 
                codes.put(localRoot.letter, localRoot.code);  
            }
        }
 
    }
	 
    //return the code table for the file
    public TreeMap getTable()
    {
        getCode(this.getRoot(), "");
        System.out.println(codes.toString() + "\n");
        return codes;
    }
 
    //encode the message using the code table
    public String encode(String data, boolean display)
    {
        String encodedMessage = "";
 
        for (int i = 0; i < data.length(); i++) 
        {
            char crent = data.charAt(i);
 
            switch (crent) {
                case 'A':   encodedMessage += codes.get('A');
                            break;
                case 'B':   encodedMessage += codes.get('B');
                            break;
                case 'C':   encodedMessage += codes.get('C');
                            break;
                case 'D':   encodedMessage += codes.get('D');
                            break;
                case 'E':   encodedMessage += codes.get('E');
                            break;
                case 'F':   encodedMessage += codes.get('F');
                            break;
                case 'G':   encodedMessage += codes.get('G');
                            break;
            }
        }
 
        String[] bytes = encodedMessage.split("(?<=\\G.{8})");
 
        if (display) 
	{
            for (int i = 0; i < bytes.length; i++) 
				{
                System.out.print(bytes[i] + " ");
                if (i > 0 && (i + 1) % 3 == 0) {
                    System.out.println("");
                }
            }
            System.out.println("\n");
        }
 
        return encodedMessage;
    }
 
    //decode the message using the tree
    public void decode(String data, HuffmanNode root)
    {
        String decodedMessage = "";
        HuffmanNode current = root;
 
        for (int i = 0; i < data.length(); i++) 
        {
            if (current.isLeaf()) 
            {
                decodedMessage += current.letter;
                current = root;
            }
 
            if (data.charAt(i) == '0') 
            {
                current = current.pLeft;
            }
            else 
            {
                current = current.pRight;
            }
        }
 
        decodedMessage += current.letter;
 
        System.out.println(decodedMessage + "\n");
    }
}

//used to construct the full tree
class PriorityQ
{
    private int size;
    private HuffmanTree[] trees;
    private int nElems;

    public PriorityQ(int s)          
    {
        size = s;
        trees = new HuffmanTree[size];
        nElems = 0;
    }
   
    //priority queue insert method from the book
    public void insert(HuffmanTree tree)    
    {
        int j;
        int crentFrequency = tree.getRoot().freq;
 
 
        if(nElems == 0) 
        {
            trees[nElems++] = tree;        
        }
        else                             
        {
            for(j = nElems-1; j >= 0; j--)         
            {
                int compFrequency = trees[j].getRoot().freq;
 
                if(crentFrequency > compFrequency)   
                {
                    trees[j+1] = trees[j];
                }
                else 
                {                       
                    break;                     
                }
            }  
            trees[j+1] = tree;           
            nElems++;
        }  
    } 
  
    //queue methods
    public HuffmanTree remove()             
    { 
        return trees[--nElems]; 
    }
 
    public HuffmanTree peekMin()              
    { 
        return trees[nElems-1]; 
    }
  
    public boolean lastOne()
    { 
        return (nElems == 1); 
    }     
 
    public boolean isEmpty()         
    { 
        return (nElems==0); 
    }
  
    public boolean isFull()        
    { 
        return (nElems == size); 
    }

}

public class n00767255
{
    //implements the menu system
    public static void main(String[] args) throws Exception
    {
        Scanner input = new Scanner(System.in);
        String sInput = "";
        String stop = "stop";
        String fullInput = "";
        int[] freqs = new int[7]; 	

	    fullInput = read(args[0]);
	    freqs = getCharCount(fullInput);
	
	    HuffmanTree tree = new HuffmanTree(freqs);
    
		 printMenu();
                 System.out.print("Choice: ");
		 
                 boolean going = true;
  		 sInput = input.nextLine();
  		 while(going)
  		 {
                     if(sInput.equalsIgnoreCase("A"))
                     {
                         tree.displayHuffmanTree();
                     }
                     else if(sInput.equalsIgnoreCase("B"))
                     {
                         tree.getTable();
                     }
                     else if(sInput.equalsIgnoreCase("C"))
                     {
                         tree.encode(fullInput, going);
                     }
                     else if(sInput.equalsIgnoreCase("D"))
                     {
                         tree.decode(tree.encode(fullInput,!going), tree.getRoot());
                     }
                     else if(sInput.equalsIgnoreCase("stop"))
                     {
                         System.out.println("Thank you for using my program.");
                         going = false;
                         continue;
                     }   
                     else if(sInput.equalsIgnoreCase("help"))
                     {
                         printMenu();
                     }
                     else
                     {
                         System.out.println("Invalid Input: Printing the menu message again");
                         printMenu();
                     }
                     
                     System.out.print("Choice: ");
  		     sInput = input.nextLine(); 
                 }
    
            input.close();
    }

    //read in the whole file, leaving out what we dont need
    public static String read(String filepath)
    {
  	 String file = filepath; 
  	 String allLetters = "";
	 String message ="";
  	 
  	 
         BufferedReader reader = null;
         try 
         {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) 
            {
                allLetters += text; 
            }
  		reader.close();          
         } 
         catch (Exception e) 
         {
            System.out.println("Exception: " + e);
         }
            for (int i = 0; i < allLetters.length(); i++) 
            { 
                char temp = allLetters.charAt(i);
                if ((int) temp >= 65 && (int) temp <= 71) 
                {
                    message += temp; 
                }
            } 
        return message; 
    }		  
		  
    //get the freq of each char in the file
    private static int[] getCharCount(String message)
    {
        int[] freqs = new int[7];
        char c;
 
        for (int i = 0; i <= message.length() - 1; i++)
        { 
            c = message.charAt(i); 
		  switch (c) { 
                case 'A':   freqs[0]++;
                            break;
                case 'B':   freqs[1]++;
                            break;
                case 'C':   freqs[2]++;
                            break;
                case 'D':   freqs[3]++;
                            break;
                case 'E':   freqs[4]++;
                            break;
                case 'F':   freqs[5]++;
                            break;
                case 'G':   freqs[6]++;
                            break;
            }
        }
        
        return freqs;
    }
    
    //display the menu. i separated this mainly to add the "help" command
    public static void printMenu()
    {
        System.out.print("Enter a letter or command to identify what you want done\n"
                + "A: Print the Huffman Tree for the supplied file\n"
                + "B: Print the code table for the supplied file\n"
                + "C: Print the binary encoding of the supplied file]n"
                + "D: Print the decoded portion of the file using the HuffmanTree]n"
                + "Stop: Stop the program from running\n"
                + "Help: Print this menu again\n");
    }
}
