package probgame;
import java.util.Scanner;

class AI {
    int choice;
    Choice[] choices;
    
    public AI(Choice[] newChoices) {
        choices = newChoices;
    }
    
    public int getChoice() {
        makeChoice();
        return choice;
    }
    
    private void makeChoice() {
        int newChoice = choices[0].ID;        
        double biggestProb = choices[0].prob;
        
        for(int i=0; i<choices.length; i++) {
            if(choices[i].prob == 0) {
                newChoice = choices[i].ID;
                break;
            }
            if(choices[i].prob > biggestProb) {
                newChoice = choices[i].ID;
                biggestProb = choices[i].prob;
            }
        }
        
        choice = newChoice;
    }
}

class Choice {
    int ID;
    int timesChosen = 0;
    double prob;
    
    public Choice(int newID) {
        ID = newID;
    }
    
    public void setProb(double newProb) {
        prob = newProb;
    }
    
    public double getProb() {
        return prob;
    }
    
    public int getChosen() {
        return timesChosen;
    }
}

class Game {
    Scanner in = new Scanner(System.in);
    int numChoices, rounds, count;
    Choice choice, AIchoice;
    
    public Game(int num, int numRounds) {
        numChoices = num;
        rounds = numRounds;
        count = 0;
        choice = new Choice(-1);
        AIchoice = new Choice(-1);
    }
    
    public void start() {
        this.run();
    }
    
    private void run() {
        Choice[] choices = new Choice[3];
        for(int i=0; i<numChoices; i++) {
            choices[i] = new Choice(i+1);
        }
        
        AI ai = new AI(choices);
        
        while(count < rounds) {
            analyzeProb(choices);
            AIchoice.ID = ai.getChoice();
            choice.ID = getChoice();
            choices[choice.ID-1].timesChosen++;
            revealChoices(choices);
            count++;
        }
    }
    
    private void revealChoices(Choice[] choices) {
        System.out.print("The AI said you would choose choice " + AIchoice.ID + " " + choices[AIchoice.ID-1].prob +
                "\nand you chose choice " + choice.ID);
    }
    
    private int getChoice() {
        System.out.print("\nEnter a choice, 1-" + numChoices + ": ");
        int newChoice = in.nextInt();
        return newChoice;
    }
    
    private void analyzeProb(Choice[] choices) {
        if (count == 0) {
            double prob = 1.0/choices.length;
            //System.out.print("\n Analyzing prob: " + prob);
            for(int i=0; i<choices.length; i++) {
                choices[i].setProb(prob);
            }
            
            return;
        }
        
        for(int i=0; i<choices.length; i++) {
            choices[i].setProb((double)choices[i].timesChosen/count);
        }
    }
}

public class ProbGame {
    public static void main(String[] args) {
        Game game = new Game(3,10);
        game.start();
    }
}
