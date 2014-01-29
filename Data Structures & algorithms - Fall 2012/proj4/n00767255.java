import java.util.StringTokenizer;
import javax.swing.JOptionPane;

class Knapsack 
{

    public static boolean findSolution(int[] weights, int target)
    {
        return findSolution(weights, 0, target);
    }

    private static boolean findSolution(int[] weights, int start, int target)
    {
        if(start == weights.length)
        {
            return false;
        }
        
        int curr = weights[start];
        
        if(curr == target)
        {
            System.out.println(curr);
            return true;
        }
        else if(curr > target || !findSolution(weights, start+1, target-curr))
        {
            return findSolution(weights, start+1, target);
        }
        
        System.out.println(curr);
        return true;
    }
}

public class n00767255
{
    public static void main(String[] args) 
    {
        String sInput = JOptionPane.showInputDialog(null, "Enter target weight "
                + "followed by weights of objects");
        StringTokenizer tokens = new StringTokenizer(sInput);
        int target = Integer.parseInt(tokens.nextToken());
        int[] weights = new int[tokens.countTokens()];
        
        int i = 0;
        
        while(tokens.hasMoreElements())
        {
            weights[i] = Integer.parseInt(tokens.nextToken());
            i++;
        }
        

        System.out.println("The weights used are: ");
        Knapsack.findSolution(weights,target);
    }
}