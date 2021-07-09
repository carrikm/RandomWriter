// randomwriter.java
// ******************************************************************
// This program under development *will* [eventually] generate random
//   passages based on an input source file (likely a classic).  It
//   will choose a random word from the source, and based on the 
//   probability of a word following the previously chosen word,
//   choose the next word. 
//
//   Command line inputs: sourcefile resultfile NWordsToGenerate 
//
// Author: L. Rhodes  start 3/12/04
//                    revision 3/2015
//                    revision 3/2016
// 
// Ethan McGee and Carrik McNerlin  
// started modifications: 1 April 2018
// -----------------------------------------------------------------
import java.io.*;
import java.util.*;

public class RandomWriter{
  public static void main (String[] args) throws IOException {
    Scanner keyboard = new Scanner (System.in);
    //Get file name arguments from command line or interactively as entered by user
    System.out.println("Please choose a file to start from.");
    String sourceFileName = FileChooser.pickAFile();
    
    System.out.println("What would you like the name of the result file to be?");
    String resultFileName  = keyboard.next();
    System.out.println("How many words would you like to generate?");
    int N = Integer.parseInt(keyboard.next());
    int nWords = 0;
    int nFirst = 100; //words output from original file
    long startTime; //for emperical time measurement
    long stopTime;  //elapsed time is difference in millisec
    
    //Data structure declarations go here
    //...........
    ArrayList<String> unique = new ArrayList<String>();
    ArrayList<LinkedList<String>> follows = new ArrayList<LinkedList<String>>();
     
    //this is a simple linked list of all words (strings)
    LinkedList <String> words= new LinkedList <String>();
   
    //this is an arraylist of linked lists of strings
    ArrayList <LinkedList<String>> byFirstLetter = new ArrayList <LinkedList<String>>(27);
    //initialize this array of linkedlists
    for(int i=0; i<27; i++){
      byFirstLetter.add(i,new LinkedList<String>());
    } 
   
    //Prepare files
    Scanner dataFile = new Scanner(new FileReader(sourceFileName));
    PrintWriter outFile = new PrintWriter(new FileWriter(resultFileName));
    //Read a line from the source file until end of file
    String firstWord = dataFile.next(); 
    String secondWord, current, next;
    
    startTime = System.currentTimeMillis();
    while(dataFile.hasNext()){
      secondWord = dataFile.next();      
      current = firstWord;
      next = secondWord;
      
      nWords++;
      if(nWords % 1000 ==0){
        System.out.println(nWords+" words");
      }
        //only print the first N words for debugging
        //remove or comment out when you have built in a 
        //a storage structure
        //..............
      if(nWords<= nFirst){
        outFile.print(current+" ");
        outFile.flush();
      } 
      words.add(current);//adding word to lone linked list for demo only
   
      //and put words into lists by their first letter
      char c = Character.toLowerCase(firstWord.charAt(0));
      int ci = (c>='a' && c<='z')? c-'a' : 26;
      ((LinkedList)(byFirstLetter.get(ci))).add(firstWord);
      

      if (unique.indexOf(current) != -1){ //word found
        follows.get(unique.indexOf(current)).add(next); //add to end of LL
      }else{ //unique thus far
        unique.add(current);
        //create new temporary LL, add "next" to it, and add that to follows
        LinkedList<String> addHere= new LinkedList<String>();
        addHere.add(next);
        follows.add(follows.size(), addHere);
      }
      
      firstWord = secondWord;      
    } //end while
  


    //add the final word to the structure
    //it may be the only entry without a follow word
    //but it needs to be in the list of unique words
    //...........
    stopTime = System.currentTimeMillis();
    System.out.println("Elapsed input time = "+(stopTime-startTime)+" msecs.");
   
    startTime = stopTime;
    
    //  Let's dump the front of the list to verify same sequence
    outFile.println(N+"\n------------from list----------------");
    outFile.flush();
    System.gc();
    for(int i=0; i<N; i++){
      outFile.print(words.get(i)+" ");
    }
     
    //   Level 0: random selection of words from lone list
    outFile.println("\n------------random list----------------");
    outFile.flush();
    Random rand = new Random(); 
    for (int i=0; i<N; i++){
      outFile.print(words.get(rand.nextInt(nWords))+" ");
    }
   
    //  Random words from each letter category
    outFile.println("\n------------random alpha list----------------");
    outFile.flush();
    for (int i=0; i<27; i++){
      LinkedList ll = (LinkedList)(byFirstLetter.get(i));
      int lllen = ll.size();
      //     outFile.print(ll);
      if(lllen>1){
        outFile.print(ll.get(rand.nextInt(lllen))+" ");
      }
    } // end for
    
    outFile.flush();
    outFile.close();
    stopTime = System.currentTimeMillis();
    System.out.println("Elapsed output time = "+(stopTime-startTime)+" msecs.");
  } // end main
} // end class