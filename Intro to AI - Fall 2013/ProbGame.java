package probgame;

import java.util.Scanner;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;

class AI {
    int choice;
    Choice[] choices;
    
    public AI(Choice[] newChoices) {
        choices = newChoices;
    }
    
    public int getChoice(Choice[] chosen, int count, int rounds, boolean[] correct) {
        if(count > 0)
        System.out.println("Chosen ID in AI: " + chosen[count].ID);
        makeChoice(chosen, count, rounds, correct);
        return choice;
    }
    
    //this is the meat of the prorgam
    private void makeChoice(Choice[] chosen, int count, int rounds, boolean[] correct) {
        int newChoice = choices[0].ID, tempChoice = -1, random = 0;        
        double biggestProb = choices[0].prob;
        Random rand = new Random();
        
        //not enough data, choose something randomly
        if(count < 2*choices.length) {
            choice = rand.nextInt(choices.length) + 1;
            return;
        }
        
        //chose same thing three times then something different
        if((chosen[count].ID != chosen[count-1].ID) && ((chosen[count-1].ID == chosen[count-2].ID) && (chosen[count-1].ID == chosen[count-3].ID))) {
            System.out.println("3 times then 1 new");
            Choice choice2 = chosen[count];
            choice = choice2.ID;
            return;
        }
        //if chosen same thing 3 times in a row
        if(chosen[count].ID == chosen[count-1].ID && chosen[count].ID == chosen[count-2].ID) {
            //choose the same thing again
            System.out.println("3 times");
            choice = chosen[count].ID;
            return;
        }
        
        //2 times chosen same thing
        //we have an important choice to make about the players tendencies
        if(chosen[count].ID == chosen[count-1].ID) {
            System.out.println("twice");
            random = rand.nextInt(2);
            if(random == 0) {
                //do thing
                choice = chosen[count].ID;
            }
            else {
                if(chosen[count].ID == 1) {
                    //possible change
                    choice = 2;
                }
                else if(chosen[count].ID == 2) {
                    if(chosen[0].timesChosen < chosen[2].timesChosen) {
                        choice = chosen[0].ID;
                    }
                    else {
                        choice = chosen[2].ID;
                    }
                }
                else {
                    choice = 2;
                }
            }
            
            return;
        }
        
        //the player is choosing different things every time, or is in a pattern
        if(chosen[count].ID != chosen[count-1].ID && chosen[count].ID != chosen[count-2].ID && chosen[count-1].ID != chosen[count-2].ID) {
            choice = chosen[count-2].ID;
            return;
        }
        
        //if we reach this point we are desparate, and so we choose randomly
        if(true) {
            random = rand.nextInt(3) + 1;
            choice = random;
            return;
        }//
        
        for(int i=0; i<choices.length; i++) {
            if(choices[i].prob > biggestProb) {
                newChoice = choices[i].ID;
                biggestProb = choices[i].prob;
            }
        }
        
        
        
        if(count == 0) {
            //do nothing
        }
        else if(count < rounds){
            if(count == 1) {
                int lastChoice = chosen[0].ID;
                switch(lastChoice) {
                    case 1: tempChoice = rand.nextInt(2) + 2;
                        break;
                    case 3: tempChoice = rand.nextInt(2) + 1;
                        break;
                    case 2: tempChoice = rand.nextInt(2) + 1;
                        if(tempChoice == 1) {
                            tempChoice = 1;
                        }
                        else {
                            tempChoice = 3;
                        }
                        break;
                }
            }
            else if(count == 2) {
                if(chosen[0].ID == chosen[1].ID) {
                    //is the player tricking me?
                    random = rand.nextInt(2);
                    if(random == 0) {
                        choice = chosen[0].ID;
                    }
                    else {
                        if(chosen[0].ID == 1) {
                            tempChoice = rand.nextInt(2) + 2;
                        }
                        else if(chosen[0].ID == 3) {
                            tempChoice = rand.nextInt(2) + 1;
                        }
                        else {
                            tempChoice = rand.nextInt(2) + 1;
                            if(tempChoice == 1) {
                                //do nothing
                            }
                            else {
                                tempChoice = 3;
                            }
                        }
                    }
                }
            }
            if(newChoice == tempChoice) {
                choice = newChoice;
            }
            else {
                random = rand.nextInt(2);
                if(random == 0) {
                    choice = tempChoice;
                }
                else {
                    choice = newChoice;
                }
            }
        }
        
        choice = newChoice;
    }
}

//definition of a choice object
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

//main game class
class Game extends JFrame implements ActionListener {
    Scanner in = new Scanner(System.in);
    int numChoices, rounds, count;
    Choice choice, AIchoice;
    Choice[] chosen, choices;
    boolean[] correct;
    private JButton choice1, choice2, choice3;
    JTextArea textField;
    boolean clicked = true;
    AI ai;
    
    public Game(int num, int numRounds) {
        //gui work
        super();
        this.setSize(350,150);
        this.setVisible(true);
        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        JPanel textPanel = new JPanel();
        
        textField = new JTextArea(200,10);
        textPanel.add(textField);
        mainPanel.add(textPanel);
        //textField.setText("test1");
        
        //init gui objects
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        this.choice1 = new JButton("1");
        this.choice1.addActionListener(this);
        choice1.setBackground(Color.black);
        choice1.setForeground(Color.white);
        buttonPanel.add(choice1);
        this.choice2 = new JButton("2");
        this.choice2.addActionListener(this);
        choice2.setBackground(Color.black);
        choice2.setForeground(Color.white);
        buttonPanel.add(choice2);
        this.choice3 = new JButton("3");
        this.choice3.addActionListener(this);
        choice3.setBackground(Color.black);
        choice3.setForeground(Color.white);
        buttonPanel.add(choice3);
        mainPanel.add(buttonPanel);
        this.add(mainPanel);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //game vars
        numChoices = num;
        rounds = numRounds;
        count = 0;
        choice = new Choice(-1);
        AIchoice = new Choice(-1);
        chosen = new Choice[numRounds];
        correct = new boolean[numRounds];
        
        //ambiguous number of choices for later expanding the project
        choices = new Choice[3];
        for(int i=0; i<numChoices; i++) {
            choices[i] = new Choice(i+1);
        }
        
        ai = new AI(choices);
    }
    
    public void start() {
        //this.run();
    }
    
    //run the game
    private void run() {
        
        if(count<rounds) {
            System.out.println("Count: "+count+" Max rounds: "+rounds);
            analyzeProb(choices);
            AIchoice.ID = ai.getChoice(chosen, count-1, rounds, correct);
            getChoice();
            choices[choice.ID-1].timesChosen++;
            revealChoices();
            chosen[count] = choices[choice.ID-1];
            System.out.println("Choice ID: " + choice.ID);
            System.out.println("Chosen ID: " + chosen[count].ID);
            //chosen[count].ID = choice.ID;
            correct[count] = (AIchoice.ID == choice.ID);
        }
        else {
            int tempCount = 0;
            
            for(int i=0; i<count; i++) {
                if(correct[i]) {
                    tempCount++;
                }
            }
            JOptionPane.showMessageDialog(null, "AI was correct " + tempCount 
                    + " out of " + count + " times", "stat", 
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    //reveal final statistics
    private void revealChoices() {
        if(AIchoice.ID == choice.ID)
        textField.setText("The AI guessed " + AIchoice.ID + " with probability " + String.format("%.5g%n", choices[AIchoice.ID-1].prob)
                + "\nand you chose " + choice.ID);
        else
        textField.setText("The AI guessed incorrectly");
    }
    
    private void getChoice() {
        textField.setText("\nEnter a choice, 1-" + numChoices + ": ");
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
    
    //deal with the clicking of the 3 buttons on the gui
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == choice1) {
            choice.ID = 1;
            this.run();
            count++;
        }
        else if(e.getSource() == choice2) {
            choice.ID = 2;
            this.run();
            count++;
        }
        else if(e.getSource() == choice3) {
            choice.ID = 3;
            this.run();
            count++;
        }
    }
}

//start the game!
public class ProbGame {
    public static void main(String[] args) {
        Game game = new Game(3,20);
        game.setVisible(true);
    }
}

