/*This class represents the word search object*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class WordSearch {
   private final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

   private int numRows;
   private int numCols;
   private int attempts;
   private List<String> words;
   private List<String> removedWords;
   private char[][] grid;
   private char[][] solution;
   private Random random;
   
   /*Constructor method for word search object*/
   public WordSearch(int numRows, int numCols, List<String> words) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.attempts = 0;
      this.words = words;
      this.removedWords = new ArrayList<>();
      this.grid = new char[numRows][numCols];
      this.solution = new char[numRows][numCols];
      this.random = new Random();
    }//End of constructor

   /*This method will generate a word search by calling many of the helper
   method in this class. It will try 500 times to fit a word before giving
   up and removing the word from the list*/
   public void generate() {
      Collections.shuffle(words);
      for (String word: words) {
         boolean isPlaced = false;
         while (!isPlaced) {
            //randomly generate starting point and direction
            int direction = random.nextInt(7);
            int[] newDirection = getDirection(direction);
            int locationR = random.nextInt(numRows-1);
            int locationC = random.nextInt(numCols-1);
            int counter = 0;
            boolean willItFit;
            willItFit = checkSpots(newDirection, word, locationR, locationC, direction);
               if (willItFit) {
                  char[] wordCharArray = word.toCharArray();
                     /*adds each letter from the word to the puzzle unless the spot 
                     is already filled with the correct letter. Also re collects the 
                     direction of the word as it will always be 0 after the first loop*/
                     for (int i = 0; i <= word.length()-1; i++) {
                        char c = wordCharArray[i];
                           {
                              int dir1 = newDirection[0] * i;
                              int dir2 = newDirection[1] * i; 
                              newDirection[0] = dir1;
                              newDirection[1] = dir2;
                              if (grid[locationR+newDirection[0]][locationC+newDirection[1]] == c) {
                                 newDirection = getDirection(direction);
                                 counter++;
                              }
                              else {
                                 grid[locationR+newDirection[0]][locationC+newDirection[1]] = wordCharArray[i];
                              }
                              newDirection = getDirection(direction);
                              counter++;
                           }
                           if (counter == word.length()) {
                              isPlaced = true;
                           }
                     }
               }
               /*Counts attempts to fit a word and removes it if attempts
               has reached more than 500*/
               else if (!willItFit) {
               attempts++;
                  if (attempts > 500) {
                     removedWords.add(word);
                     System.out.println(
                     word + " had to be removed because it won't fit.");
                     System.out.println();
                     isPlaced = true;
                  }
               }
          }        
      }
      //fills empty spaces on grid with *
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            if (grid[i][j] == '\u0000') {
            grid[i][j] = '*';
            }
         }
      }
      //creates solution before adding random letters
      solution = createSolution(grid, solution);
      
      //adds random letters to any spot on grid with *
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            if (grid[i][j] == '*') {
            grid[i][j] = LETTERS[random.nextInt(LETTERS.length)];
            }
         }
      }
   }//End of generate method

   /*This method will print the word search to the console or to a
   file depending on which PrintStream object is passed*/
   public void print(PrintStream output) {
      if (numRows == 0) {
         System.out.println(
         "Looks like you haven't created a word search yet.");
         return;
      }
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            output.print(grid[i][j] + " ");
         }
      output.println();
      }
      printWordsToFind(output);
   }//End of print method
   
   /*This method will print the words the user can find within
   the word search and remove the ones that couldn't fit.*/
   public void printWordsToFind(PrintStream output) {
      words.removeAll(removedWords);
      output.println();
      output.println(
      "Words to find:");
      for(String word: words) {
         output.println(word);
      }
   }//End of printWordsToFind method
   
   /*This method will fill the solution char[][] with only the letters from 
   the words that fit*/
   public char[][] createSolution(char[][] grid, char[][]solution) {
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            solution[i][j] = grid[i][j];
            }
         }
      return solution;
   }//Enf of createSolution method
   
   /*This method will print the solution to the word search either to a
   text file or to the console depnding on the PrintStream obj passed.*/
   public void printSolution(PrintStream output) {
      if (numRows == 0) {
         System.out.println(
         "Looks like you haven't created a word search yet.");
         return;
      }
      output.println(
      "Here is the solution to your word search:");
      output.println();
      for (int i = 0; i < numRows; i++) {
         for (int j = 0; j < numCols; j++) {
            output.print(solution[i][j] + " ");
         }
         output.println();
      }
      output.println();
      printWordsToFind(output);
   }//End of printSolution method
   
   /*This method returns one of the 8 possible directions*/
   public int[] getDirection(int direction) {
      int[][] directions ={ 
      {1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1} };
      int[] chosenDirection = new int[2];
      chosenDirection[0] = directions[direction][0];
      chosenDirection[1] = directions[direction][1];
      return chosenDirection;
   }//End of getDirection method
   
   /*This method checks if the word passed can fit into the puzzle given
   the cdirection passed, the starting point and size of the word*/
   public boolean checkSpots(int[] direction, String word, int locationR, int locationC, int directionInt) {
      int leaveGridR = direction[0] * word.length() + locationR;
      int leaveGridC = direction[1] * word.length() + locationC;
         if (leaveGridR > numRows || leaveGridC > numCols || leaveGridR < 0 || leaveGridC < 0) {
            return false;
         }
      char[] wordCharArray = word.toCharArray();
      for (int i = 0; i < word.length(); i++) {
         int dir1 = direction[0] * i;
         int dir2 = direction[1] * i; 
         direction[0] = dir1;
         direction[1] = dir2;
            if (grid[locationR+direction[0]][locationC+direction[1]] == '\u0000' ||
                grid[locationR+direction[0]][locationC+direction[1]] == wordCharArray[i]) {
               direction = getDirection(directionInt);
               continue;
         }
            else {
               return false;
            }
      }
      return true;
   }//End of checkSpots method
   
   /*Getter method to return the size of the grid. Used to make
   sure that the grid size is not 0 before trying to print a puzzle.*/
   public int getSize() {
      return this.numRows;
   }//End of getSize method
}
