/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author mbr9995
 */
public class fileIO{
    Automaton<String> auto;
    
    public fileIO(){
        auto = new Automaton();
        readDocument("test.txt");
        
        /*constructAutomaton(3,2);
        auto.createTransition(1, "a", 2);
        auto.setInit(2);
        auto.setAccept(2);
        saveFile("newAutomata.txt",auto);*/
        
    }
    
    //Translate the sources from file into a Automaton
    public Automaton readDocument(String fileName){
        //Automaton auto = new Automaton();
        String line = null;
        try{
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                    new BufferedReader(fileReader);
            
            // First Line: Number of states
            line = bufferedReader.readLine();
	    int stateNum = Integer.parseInt(line);
            //System.out.println("stateNum: "+stateNum);
            //Second Line: Number of Atomic Propositions
            line = bufferedReader.readLine();
            int apNum = Integer.parseInt(line);
            //System.out.println("apNum: "+apNum);
            
            constructAutomaton(stateNum, apNum);//construct
            
            
            
            
            for(int state = 0; state < stateNum; state++){
                line = bufferedReader.readLine();
                StringTokenizer st = new StringTokenizer(line);
                
                String ap = "a";
               // System.out.println("change ap: "+ap);
                while(st.hasMoreTokens()){
                    
                    String currentToken = st.nextToken();
                    //System.out.println("currentToken: "+currentToken);
                    if(currentToken.equalsIgnoreCase(";")){
                        ap = "b";
                        //System.out.println("change ap: "+ap);
                    }else{
                        //make transition
                        int nextState = Integer.parseInt(currentToken);
                        auto.createTransition(state, ap, nextState);
                    }
                }
                
            }
            
            line = bufferedReader.readLine();//this line is empty
            
            //get initial states
            line = bufferedReader.readLine();
            StringTokenizer st = new StringTokenizer(line);
            while(st.hasMoreTokens()){
                String initToken = st.nextToken();
                int inital = Integer.parseInt(initToken);
                auto.setInit(inital);
                //System.out.println("Set "+inital +" Initial");
            }
            
            //get initial states
            line = bufferedReader.readLine();
            st = new StringTokenizer(line);
            while(st.hasMoreTokens()){
                String acceptToken = st.nextToken();
                int accept = Integer.parseInt(acceptToken);
                auto.setAccept(accept);
                //System.out.println("Set "+accept +" Accept");
            }
            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +
                fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return auto;
    }
   
    
     public void constructAutomaton(int numState, int numAlphabet){
      
         auto.addState(numState);
        
        System.out.println("Show all states: "+auto.states);
        //test states
       // state<String> state0 = auto.states.get(0);
        //state<String> state1 = auto.states.get(1);
        //state<String> state2 = auto.states.get(2);
        //state<E> state3 = states.get(3);
        
        //System.out.println("States has: "+state0+state1+state2);
        
        //auto.setInitial(state0);//set state0 as an initial state
        
        for(int j=0; j<numAlphabet; j++){
            if(j==0){
                auto.alphabetList.add("a");
            }
            if(j==1){
                auto.alphabetList.add("b");
            }
            if(j==2){
                auto.alphabetList.add("c");
            }
        }
        System.out.println("alphabetList: "+auto.alphabetList);
    }
     
    public void saveFile(String fileName, Automaton<String> savedAuto){
        int numState = savedAuto.states.size();
        
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter( fileName));
            
            //StateNum & APNum
            writer.write(numState+"");
            writer.newLine();
            writer.write(savedAuto.alphabetList.size()+"");
            writer.newLine();
            
            for(int i=0; i<numState; i++){
                state<String> current = savedAuto.states.get(i);
                
                Iterator<String> alphabetIt = savedAuto.alphabetList.iterator();
                writer.write(writeNexts(alphabetIt.next(),current));
                while(alphabetIt.hasNext()){
                    String alphabet = alphabetIt.next();
                    writer.write(" ; ");
                    writer.write(writeNexts(alphabet,current));
                }
                writer.newLine();
            }
            
            writer.newLine();
            
            //write inital states
            Iterator<state<String>> initIt = savedAuto.initialStates.iterator();
            while(initIt.hasNext()){
                state<String> init = initIt.next();
                
                writer.write(init.stateLabel+" ");
            }
            
            writer.newLine();
            
            //write accept states
            Iterator<state<String>> acceptIt = savedAuto.acceptStates.iterator();
            while(acceptIt.hasNext()){
                state<String> accept = acceptIt.next();
                
                writer.write(accept.stateLabel+" ");
            }
            
        }
        catch ( IOException e)
        {
        }
        finally
        {
            try
            {
                if ( writer != null)
                writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
        
        
    } 
    
    private String writeNexts(String alphabet,state<String> current){
        String output = "";
        
        if(current.transitions.containsKey(alphabet)){
            ArrayList<state<String>> nextList = current.transitions.get(alphabet);
            Iterator<state<String>> nextIt = nextList.iterator();
            if(nextIt.hasNext()){
                state<String> next = nextIt.next();
                output = output+" "+next.stateLabel;
            }
            
            
            
        }
        
        
        
        return output;
    }
    
    public static void main(String[] args){
        fileIO file = new fileIO();
        convertDFA convert = new convertDFA(file.auto);
        AutomatonGUI gui = new AutomatonGUI(file.auto);
        
        //<String>
        //WriteRegex wr = new WriteRegex(file.auto);
    }
     
     
     
}
