/*Programmer: Dylan Driscoll
Class: CS 145
Assignment: Assignment 1 Word Search Generator
Date 2/17/2023*/

/*This is the main for my word search program. It will create a word search using
a list of words from the user or from a text file the user provides. It will try
to fit a word 500 times before it will tell the user the word will not fit. Words
will overlap if the spot found will also fit the word and the grid size. The user 
can print their created word searches and solutions to a file or to the console.*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class WordSearchMain{
   
   //main method
   public static void main(String[] args) throws FileNotFoundException {
      runProgram();
   }
   
   /*This method handles the variety of things the user can do from the
   program meu.*/
   public static void runProgram() throws FileNotFoundException {
      System.out.println(
      "Welcome to my word search program!");
      WordSearc userWordSearch = new WordSearch(0, 0, null);
      Scanner input = new Scanner(System.in);
      String userCommand = "";
         while (!userCommand.equals("q")) {
            printIntro();
            userCommand = input.next().trim();
               switch (userCommand.toLowerCase()) {
               case "g":
               case"generate":
                  userWordSearch = generate(input, userCommand);
                  break;
               case "p":
               case "print":
                  userWordSearch.print(System.out);
                  break;
               case "s": 
               case "solution":
                  userWordSearch.printSolution(System.out);
                  break;
               case "t":
               case "text":
                  userWordSearch = fileWords(input);
                  break;
               case "f":
               case "save":
                  saveToFile(input, userWordSearch);
                  break;
               case "e":
                  saveSolutionToFile(input, userWordSearch);
               case "q":
                  break;
               default:
                  System.out.println(
                  "Please enter a command from the menu");
               }
         }
      System.out.println(
      "Thanks for using my program!");  
      input.close();
   }//end of runProgram method
   
   /*This method will introduce the user to the program*/
   public static void printIntro() {
      System.out.println();
      System.out.println(
      "Please type the correspoinding letter to what you want to do");
      System.out.println(
      "Type (g) to generate a new word search puzzle");
      System.out.println(
      "Type (p) to print and view your word search");
      System.out.println(
      "Type (s) to show the solution to your word search");
      System.out.println(
      "Type (t) to create a puzzle with words from a text file");
      System.out.println(
      "Type (f) to save your current word search to a text file");
      System.out.println(
      "Type (e) to save your current word search solution to a text file");
      System.out.println(
      "Type (q) to quit this program.");
   }//End of printIntro method
   
   /*This method will generate a word search based on the words inputted by
   the user*/
   public static WordSearch generate(Scanner input, String userCommand) {
      int wordsTotal = numberOfWords(input);
      List <String> userWords = new ArrayList<>();
      userWords = userWords(input, wordsTotal);
      int length = getLongestWord(userWords)*2;
      WordSearch userWordSearch = new WordSearch(length, length, userWords);
      userWordSearch.generate();
      System.out.println();
      System.out.println(
      "Here is the word search generated using those words:");
      System.out.println();
      userWordSearch.print(System.out);
      return userWordSearch;
   }//end of generate method*/
   
   /*This method will prompt the user for words that contain only alphabetic
   characters*/
   public static List<String> userWords(Scanner input, int wordsTotal) {
      List<String> words = new ArrayList<>();
      System.out.println(
      "Enter the words to include in this puzzle by entering");
      System.out.println(
      "one word and then pressing enter:");
      String word;
      while (wordsTotal > 0) {
         word = input.next().trim().toUpperCase();
         if (word.matches("^[a-zA-Z]*$")) {
            words.add(word);
            wordsTotal--;
         }
         else {
            System.out.println(
            "Words for this program can only contain alphabetic letters.");
         }
      }
      return words;
   }//End of userWords method
    
   /*This method will prompt the user for the number of words in their word search
   and ensure that the user has entered an integer.*/
   public static int numberOfWords(Scanner input) {
   boolean intCheck = false;
   String numberOfWords = "";
   int wordsInt;
      while (!intCheck) {
         System.out.println("How many words will be in this puzzle?");
         numberOfWords = input.next().trim();
         if (numberOfWords.matches("-?\\d+")) {
            intCheck = true;
         }
         else {
            System.out.println(
            "Please enter a valid integer.");
         }
      }
      wordsInt = Integer.parseInt(numberOfWords);
      return wordsInt;
   }//End of numberOfWords method
   
   /*This method will get and return the longst word in the list to help
   with size calculation for the word search*/
   public static int getLongestWord(List<String> userWords) {
   int longestWord = 0; 
      for(String word : userWords) { 
	      if (word.length() > longestWord) { 
		      longestWord = word.length(); 
	      } 
      } 
   return longestWord; 
   } //Enf of getLongestWord method
   
   /*This method will prompt the user for a file to get words from and create a word
   search using the words from that file. It will first remove non alphabetic words and 
   then create a word search.*/
   public static WordSearch fileWords(Scanner input) throws FileNotFoundException {
      List<String> fileWords = new ArrayList<>();
      System.out.println(
      "Please note the .txt file must be in the same directory as this program.");
      System.out.print("Enter input file name:");
      String inputFile = input.next().trim();
      File userInputFile = new File(inputFile);
      while (!userInputFile.exists()) {
         System.out.println();
         System.out.println(
         "A file with that name could not be found.");
         System.out.print(
         "Re-enter input file name:");
         inputFile = input.next().trim();
         userInputFile = new File(inputFile);
      }
      Scanner reader = new Scanner(userInputFile);
      while(reader.hasNext()) {
         String word = reader.next().trim().toUpperCase();
         if (word.matches("^[a-zA-Z]*$")) {
            fileWords.add(word);
         }
         else if (!word.matches("^[a-zA-Z]*$"))  {
            System.out.println();
            System.out.println(
            word + " had to be removed because it contained non-alphabetic symbols.");
         }
      }
      int length = getLongestWord(fileWords)*2;
      WordSearch userWordSearch = new WordSearch(length, length, fileWords);
      System.out.println();
      userWordSearch.generate();
      reader.close();
      userWordSearch.print(System.out);
      return userWordSearch;
   }//End of fileWordSearch method
   
   /*This method will print a word search to a text file*/
   public static void saveToFile (Scanner input, WordSearch userWordSearch) throws FileNotFoundException {
      System.out.print("Enter output file name: ");
      String outputFile = input.next().trim();
      File userOutputFile = new File(outputFile);
      PrintStream output = new PrintStream(userOutputFile);
      userWordSearch.print(output);
      if (userWordSearch.getSize() > 0) {
         System.out.println(
         "Your word search has been printed to a file named " + outputFile);
      }
      output.close();
   }//End of saveToFile method
   
   /*This method will print the solution to the word search to a text file*/
   public static void saveSolutionToFile (Scanner input, WordSearch userWordSearch) throws FileNotFoundException {
      System.out.print("Enter output file name: ");
      String outputFile = input.next().trim();
      File userOutputFile = new File(outputFile);
      PrintStream output = new PrintStream(userOutputFile);
      userWordSearch.printSolution(output);
      if (userWordSearch.getSize() > 0) {
         System.out.println(
         "The solution to your word search has been saved to a file named " + outputFile);
      }
      output.close();
   }//End of saveSolutionToFile
}