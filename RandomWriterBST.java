// randomwriterBST.java
// ******************************************************************
// This program under development *will* [eventually] generate random
//   passages based on an input source file (likely a classic).  It
//   will choose a random word from the source, and based on the 
//   probability of a word following the previously chosen word,
//   choose the next word.
//
//  Uses Binary Search Tree for faster lookup than the LL implementation
//
//   Command line inputs: sourcefile resultfile NWordsToGenerate 
//
// Author: L. Rhodes  start 3/12/04
//                    revision 3/2015
//                    revision 3/2016
// 
// Ethan McGee and Carrik McNerlin  
// started modifications: 18 April 2018
// -----------------------------------------------------------------
import java.io.*;
import java.util.*;

public class RandomWriterBST{
  public static void main (String[] args) throws IOException {
    Scanner keyboard = new Scanner (System.in);
    
    //Get file name interactively from user
    System.out.println("Please choose a file to start from.");
    String sourceFileName = FileChooser.pickAFile();
    
    System.out.println("What would you like the name of the result file to be?");
    String resultFileName  = keyboard.next();
    System.out.println("How many words would you like to generate?");
    int N = Integer.parseInt(keyboard.next());
    
    int nWords = 0;   //words in file
    int nFirst = 100; //words output from original file unaltered
    long startTime;   //for emperical time measurement
    long stopTime;    //elapsed time is difference in millisec
    
    
    //Data structure declarations
    TreeMap<String, LinkedList<String>> binarySearchTree = new TreeMap<String,LinkedList<String>>();
    LinkedList<String> follows = new LinkedList<String>();
     
    //this is a simple linked list of all words (strings)
    LinkedList<String> words= new LinkedList<String>();
   
    //this is an arraylist of linked lists of strings
    ArrayList<LinkedList<String>> byFirstLetter = new ArrayList<LinkedList<String>>(27);
    //initialize array of LLs
    for(int i=0; i<27; i++){
      byFirstLetter.add(i,new LinkedList<String>());
    } 
   
    //Prepare files
    Scanner dataFile = new Scanner(new FileReader(sourceFileName));
    PrintWriter outFile = new PrintWriter(new FileWriter(resultFileName));
    

    //Read a line from the source file until end of file, BEGIN ACTUAL WORK
    String current = dataFile.next(); 
    String next;
    startTime = System.currentTimeMillis();
    while(dataFile.hasNext()){
      next = dataFile.next();    
      
      //print cascading count, 1000 words at a time
      nWords++;
      if(nWords % 1000 ==0){System.out.println(nWords+" words");}

      
      //only print the first N words for debugging
      //remove or comment out when you have built in a 
      //a storage structure
      if(nWords<= nFirst){
        outFile.print(current+" ");
        outFile.flush();
      } 
      words.add(current);//adding word to lone linked list for demo only
   
      //and put words into lists by their first letter
      char c = Character.toLowerCase(current.charAt(0));
      int ci = (c>='a' && c<='z')? c-'a' : 26;
      ((LinkedList)(byFirstLetter.get(ci))).add(current);
      
      if (binarySearchTree.containsKey(current)){ //word found
        follows = binarySearchTree.get(current);
        follows.add(next); //add to end of LL
      }else{ //unique thus far
        //create new temporary LL, add "next" to it, and add that to follows
        LinkedList<String> temp= new LinkedList<String>();
        temp.add(next);
        follows = temp;
      }
      binarySearchTree.put(current, follows);
      
      current = next; //move to next word     
    } //end while
    
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