//imports
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.*;
//enum to track the types of tokens being created
enum Type {Keyword, SpecialSymbol, ID, NUM, Float, letter, digit, Line, Error}

//defines the token object that will be the main point of concern in the
//project
class Token {
    public Type type;
    public String contents;
    
    public Token(Type type, String contents) {
        this.type = type;
        this.contents = contents;
    }
    
    //returns string for output based on Type
    @Override
    public String toString() {
        String returnString = "";
        
        if(type == Type.Keyword) {
            returnString = "keyword: " + contents;
        }
        else if(type == Type.SpecialSymbol) {
            returnString = contents;
        }
        else if(type == Type.ID || type == Type.letter) {
            returnString = "ID: " + contents;
        }
        else if(type == Type.NUM || type == Type.digit || type == Type.Float) {
            returnString = "NUM: " + contents;
        }
        else if(type == Type.Line) {
            returnString = "Input: " + contents;
        }
        else if(type == Type.Error) {
            returnString = "Error: " + contents; 
        }
        
        return returnString;
    }
}

//main class
public class LexicalAnalyzer2 {
    //main checks if there is a file passed first, 
    //and if so creates that file. Main then adds tokens
    //to a list based on each line of the file by
    //calling generateTokensFromLine
    
    static int depth = 0;
    public static void main(String[] args) throws Exception{
        if(args[0] == null) {
            System.out.println("No file specified");
            System.exit(0);
        }
        
        //make a file and scanner for that file
        ArrayList<Token> tokens = new ArrayList<>();
        String filename = args[0];
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        String line;
        int commentDepth = 0;
        
        //for every line, add it to the list
        //and generate tokens from it
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            tokens.add(new Token(Type.Line, line));
            commentDepth = generateTokensFromLine(tokens, line, commentDepth);
        }
        
        //output list in a readable manner
        outputTokens(tokens);
    }
    
    //take in a line, delimit it by whitespace, analyze each token
    //and add it to the list based on its properties
    private static int generateTokensFromLine(ArrayList<Token> tokens, String line, int cDepth) {
        //strings to check for important tokens
        List keywords = Arrays.asList("int", "float", "else", "if", "return", "void", "while");
        List specialSymbols = Arrays.asList("+", "-", "*", "/", "<", "<=", ">", ">=", "!=", "=", ";", ",", 
                "{", "}", "[", "]", "(", ")", "/*", "*/", "==");
        
        //variables for use in the algorithm
        StringTokenizer tokenizer = new StringTokenizer(line, " ");
        int count = tokenizer.countTokens();
        int characterCount = 0;
        String token;
        boolean commented = false;
        int commentDepth = cDepth;
        String temp = "";
        String temp2 = "";
        char c;
        
        //token creation algorithm
        for(int i=0; i<count; i++) {
            token = tokenizer.nextToken();
            characterCount = token.length();
            
            // if there is a // comment, deal with it
            if((token.equals("//") || token.contains("//")) && (commentDepth == 0)) {
                if(token.equals("//")) {
                    //do stuff
                    commented = true;
                    break;
                }
                else if(token.contains("//")) {
                    commented = true;
                    canUnderstand(tokens, token, commentDepth);
                    break;
                    //*******ADD LOGIC FOR ERRORS OR CHARACTERS BEFORE COMMENT
                }
            }
            //if the token is one of the keywords
            else if(keywords.contains(token) && commentDepth == 0) {
                tokens.add(new Token(Type.Keyword, token));
            }
            //if the token is a special symbol, with special cases for comments
            else if(specialSymbols.contains(token) || token.contains("/*") || token.contains("*/")) {
                //check if its a comment
                if(token.contains("/*") || token.equals("/*")) {
                    if(token.equals("/*")) { commentDepth++;}
                    else {
                        commentDepth = canUnderstand(tokens, token, commentDepth);
                    }
                }
                else if(token.contains("*/") || token.equals("*/")) {
                    if(commentDepth > 0) {
                        if(token.equals("*/")) { commentDepth--; }
                        else {
                            commentDepth = canUnderstand(tokens, token, commentDepth);
                        }
                    }
                    else {
                        tokens.add(new Token(Type.SpecialSymbol, "*"));
                        tokens.add(new Token(Type.SpecialSymbol, "/"));
                    }
                }
                else if(token.equals("!") && commentDepth == 0) {
                    tokens.add(new Token(Type.Error, token));
                }
                else if(commentDepth == 0){
                    tokens.add(new Token(Type.SpecialSymbol, token));
                }
                
                if(token.equals("}")) {
                    depth--;
                }
                if(token.equals("{")) {
                    depth++;
                }
            }
            //now we have to be meticulous
            //if every char is a letter, then we'll enter it as an ID
            else if(isAlpha(token) && commentDepth == 0) {
                if(token.length() > 1) {
                    tokens.add(new Token(Type.ID, token + " depth: " + depth));
                }
                else {
                    tokens.add(new Token(Type.letter, token));
                }
            }
            //if every char is a number, enter it as a NUM
            else if(isNumer(token) && commentDepth == 0) {
                if(token.length() > 1) {
                    tokens.add(new Token(Type.NUM, token));
                }
                else {
                    tokens.add(new Token(Type.digit, token));
                }
            }
            //check if the token can be accepted as a float
            else if(isFloat(token) && commentDepth == 0) {
                tokens.add(new Token(Type.Float, token));
            }
            //do handwork
            else if(commentDepth == 0) {
                dealWithIt(tokens, token);
            }
        }
        return commentDepth;
    }
    
    //output the list of tokens in a readable manner
    private static void outputTokens(ArrayList<Token> tokens) {
        for(int i=0; i<tokens.size(); i++) {
            System.out.println(tokens.get(i));
        }
    }
    
    //check if all chars are letters
    public static boolean isAlpha(String string) {
        char[] chars = string.toCharArray();
        
        for(char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        
        return true;
    }
    
    //check if all chars are numbers
    public static boolean isNumer(String string) {
        char[] chars = string.toCharArray();
        
        for(char c : chars) {
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        
        return true;
    }
    
    //check if the token is a valid float
    //*********NEED TO ACTUALLY WRITE LOGIC STILL******
    public static boolean isFloat(String string) {
        String regex = "\\d*\\.?\\d+([E][+-]?\\d+)?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        boolean b = m.matches();
        
        if(b) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static int canUnderstand(ArrayList<Token> tokens, String token, int cDepth) {
        List keywords = Arrays.asList("int", "float", "else", "if", "return", "void", "while");
        List specialSymbols = Arrays.asList("+", "-", "*", "/", "<", "<=", ">", ">=", "!=", "=", ";", ",", 
                "{", "}", "[", "]", "(", ")", "/*", "*/", "==");
        
        //tokens.add(new Token(Type.Keyword, "inside cu, cd: " + cDepth + " : " + token));
        
        boolean nums = false;
        boolean chars = false;
        boolean symbols = false;
        char[] characters = token.toCharArray();
        int commentDepth = cDepth;
        String temp = "";
        char cTemp = ' ';
        String temp2 = token;
        int i = 0;
        
        if(token.contains("//") || token.contains("*/") || token.contains("/*")) {
            if(token.contains("//")) {
                int loc = token.indexOf("//");
                if(loc == 0) {
                    //return commentDepth;
                }
                else {
                    temp = token.substring(0, loc);
                    canUnderstand(tokens, temp, commentDepth);
                }
            }
            else if(token.contains("/*")) {
                int loc = token.indexOf("/*");
                if(loc == 0) {
                    commentDepth++;
                    temp = token.substring(2, token.length());
                    commentDepth = canUnderstand(tokens, temp, commentDepth);
                    //return commentDepth;
                }
                else {
                    commentDepth++;
                    temp = token.substring(0, loc);
                    commentDepth = canUnderstand(tokens, temp, commentDepth);
                }
            }
            else if(token.contains("*/")) {
                int loc = token.indexOf("*/");
                //tokens.add(new Token(Type.Keyword, "contains */"));
                if(loc == 0) {
                    commentDepth--;
                    temp = token.substring(2, token.length());
                    commentDepth = canUnderstand(tokens,temp,commentDepth);
                    //return commentDepth;
                }
                else {
                    commentDepth--;
                    //temp = token.substring(0, loc);
                    //tokens.add(new Token(Type.Keyword, temp));
                    //canUnderstand(tokens, temp, commentDepth);
                }
            }
        }
        
        //tokens.add(new Token(Type.Keyword, "leaving cu, cd: " + commentDepth + " : " + token));
        return commentDepth;
    }
    
    //if i havent been able to to anything with the string, i go through it character by character
    private static void dealWithIt(List<Token> tokens, String token) {
        List delims = Arrays.asList("+", "-", "*", "/", "<", "<=", ">", ">=", "!=", "=", ";", ",", 
                "{", "}", "[", "]", "(", ")", "==");
        char c;
        char c2;
        String temp = "";
        boolean delimCheck = true;
        
        //for each character
        for(int i=0; i<token.length(); i++) {
            c = token.charAt(i);
            delimCheck = true;
            
            //change depth
            if(c == '{') {
                depth++;
            }
            if(c == '}') {
                depth--;
            }
            
            if((c == '+' || c == '-') && i >= 2) {
                if(token.charAt(i-1) == 'E' && Character.isDigit(token.charAt(i-2))) {
                    delimCheck = false;
                }
            }
            
            if((delims.contains("" + c) || c=='!') && delimCheck) {
                analyze(tokens,temp);
                temp = "";
                if(i+1 < token.length()) {
                    c2 = token.charAt(i+1);
                    if(delims.contains(""+c+c2)) {
                        tokens.add(new Token(Type.SpecialSymbol, ""+c+c2));
                        i++;
                    }
                    else if(!(c=='!')){
                        tokens.add(new Token(Type.SpecialSymbol, ""+c));
                    }
                    else {
                        tokens.add(new Token(Type.Error, "!"));
                    }
                }
                else {
                    tokens.add(new Token(Type.SpecialSymbol, ""+c));
                }
            }
            else {
                temp += c;
            }
            
            if(i == token.length() - 1 && !temp.isEmpty()) {
                analyze(tokens,temp);
            }
            
            
        }
        
        
    }
    
    //take in a string that has all delimiters removed and add it to the list
    private static void analyze(List<Token> tokens, String temp) {
        List keywords = Arrays.asList("int", "float", "else", "if", "return", "void", "while");
        
        if(!temp.isEmpty()) {
            if(keywords.contains(temp)) {
                tokens.add(new Token(Type.Keyword, temp));
            }
            else if(isAlpha(temp)) {
                tokens.add(new Token(Type.ID, temp + " depth: " + depth));
            }
            else if(isNumer(temp)) {
                tokens.add(new Token(Type.NUM, temp));
            }
            else if(isFloat(temp)) {
                tokens.add(new Token(Type.Float,temp));
            }
            else {
                tokens.add(new Token(Type.Error, temp));
            }
        }
    }
}
