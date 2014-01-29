import java.util.StringTokenizer;
import javax.swing.JOptionPane;

class CircularList
{
    private Link first;       

    public CircularList()   
    {
        
    }         
    
    public void insertFirst(int key)
    {                           
        Link link = new Link(key);
        link.next = first;      
        first = link;            
    }

    public Link getFirst()         
    { 
        return first; 
    }

    public void setFirst(Link link)  
    { 
        first = link; 
    }

    public boolean isEmpty()       
    { 
        return first==null; 
    }

    public ListIterator getIterator()  
    {
        ListIterator iterator = new ListIterator(this);
        return iterator;
    }                              

    public Link find(int kee)     
    {                           
        Link current = first;
        
        while(current.key != kee)       
        {
            if(current.next == null)
            {
                return null;
            }
            else
            {
                current = current.next;    
      
            }    
        }
        
        return current;                  
    }
    
    public Link delete(int kee)    
    {                           
        Link current = first;            
        Link previous = first;
        
        while(current.key != kee)
        {
            if(current.next == null)
            {
                return null;
            }
            
            else
            {
                previous = current;          
                current = current.next;
            }
        }                            
        
        if(current == first)
        {
            first = first.next;             
        }
        else
        {
            previous.next = current.next;
        }   
        
        return current;
   }

    public int getKey(Link link)
    {
        return link.key;
    }

    public void displayList()
    {
        Link current = first;     
	
        do{
            current.displayLink();  
            current = current.next; 
        }while(current != first);     
      
        System.out.println();
    }
}

class Link
{
    public int key;
    public Link next;
    
    public Link()
    {
    
    }

    public Link(int kee)   
    { 
        key = kee; 
    }

    public void displayLink()
    {
        System.out.print(key + " "); 
    }
} 

class ListIterator
{
    private Link current;        
    private Link previous;         
    private CircularList list;     

    public ListIterator(CircularList cList) 
    {
        list = cList;
        reset();
    }

    public void reset()            
    {
        current = list.getFirst();
        previous = null;
    }

    public boolean linksLeft()         
    { 
        return (current == previous); 
    }

    public void nextLink()        
    {
        previous = current;
        current = current.next;
    }

    public Link getCurrent()       
    { 
        return current; 
    }

    public int getCurrentKey()       
    {  
        return list.getKey(current);
    }

    public void insertAfter(int key)   
    {                                 
        Link link = new Link(key);

        if(list.isEmpty())  
        {
            list.setFirst(link);
            current = link;
            link.next = link;
        }
        else                     
        {
            link.next = current.next;
            current.next = link;
            nextLink();              
        }
    }

    public void insertBefore(int key)   
    {                              
        Link link = new Link(key);

        if(previous == null)       
        {                        
            link.next = list.getFirst();
            list.setFirst(link);
            reset();
        }
        else                        
        {
            link.next = previous.next;
            previous.next = link;
            current = link;
        }
    }

    public int deleteCurrent()  
    {
        int kee = current.key;
	
	if(current == list.getFirst())
        {
            list.setFirst(current.next);
        }
        
        if(linksLeft())
        {
            current = null;
            previous = null;
        }
        else
        {
            previous.next = current.next;
            current = current.next;
        }
        
        return kee;
    }
}

public class n00767255
{
    public static void main(String[] args) throws Exception{
        CircularList list = new CircularList();
	ListIterator iterator = new ListIterator(list);  
	String input;
		 
	do{ 
            input = JOptionPane.showInputDialog(null, "Please enter 3 ints "
                    + "separated by spaced. Enter stop to quit"); 
            
            if(input == null)
            {
                break;
            }
            
            if(!input.equals("stop"))
            {	
                StringTokenizer tokens = new StringTokenizer(input);
		int numPeople, count, start;
		
                numPeople = Integer.parseInt(tokens.nextToken());
		count = Integer.parseInt(tokens.nextToken());
		start = Integer.parseInt(tokens.nextToken());
			  
		for (int i = 1; i <= numPeople; i++)
                {
                    iterator.insertAfter(i);
                }
		
                while(iterator.getCurrentKey() != start)
                {
                    iterator.nextLink();
                }
		
                while(iterator.linksLeft() == false)
                {	  
                    for(int i = 0; i < count; i++)
                    {
                        iterator.nextLink();
                    }
				
                    iterator.deleteCurrent();
                } 
			  
		int winner = iterator.getCurrentKey();
		JOptionPane.showMessageDialog(null, "Josephus is at location: " 
                        + winner ); 
			 
			  
		list = new CircularList();
		iterator = new ListIterator(list);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Qutting");
            }}while(!input.equals("stop")); 
        }
}