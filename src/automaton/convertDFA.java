/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author asus1
 */
public class convertDFA<String> {
    Automaton<String> oldAU;
    int stateCounter;//DFA state Number
    Queue<Set<Integer>> queue;//working list
    Map<Set<Integer>, Integer> setMapping;//Store the reachable states
    Map<Integer, Map<String, Integer>> trans;//Store the mapping
    int transNum;
    
    public convertDFA(Automaton au){
        oldAU = au;
        
        //Store the reachable states
        setMapping = new HashMap<Set<Integer>, Integer>();
        
        
        //Store the mapping
        trans = new HashMap<Integer, Map<String, Integer>>();
        queue = new LinkedList();
        stateCounter = 0;
        transNum = 0;
        //System.out.println("Is a a NFA? ");
        if(detectNFA(oldAU)){
           Dfa d = toDFA();
           System.out.println();
           System.out.println();
           System.out.println(d);
        }
        
    }
    
    public boolean detectNFA(Automaton oldAU){
        System.out.println();
        Iterator<state<String>> stateIt = oldAU.states.iterator();
        while(stateIt.hasNext()){
            
            Iterator<String> alphabetIt = oldAU.alphabetList.iterator();
            state<String> currentState = stateIt.next();
            //System.out.println("state iterator start!"+currentState);
            //System.out.println("alphabet: "+ oldAU.alphabetList);
            while(alphabetIt.hasNext()){
                String alphabet = alphabetIt.next();
                //System.out.println("alphabet iterator start!");

                if(currentState.transitions.containsKey(alphabet)){
                    //System.out.println("Detecting: ");
                    ArrayList<state<String>> nextList 
                            = currentState.transitions.get(alphabet);
                    int numNext = nextList.size();
                    //System.out.println(currentState+" =" 
                    //        + alphabet + "=>"+ nextList);
                    if(numNext != 1){
                        System.out.println("This is an NFA!");
                        return true;
                    }
                }
            }
        }
        System.out.println("This is an DFA!");
        return false;
    }
    
    
    

    
    public Dfa toDFA(){
        Set<Integer> initSet = listToset(oldAU.initialStates);
        
        setMapping.put(initSet,0);
        transSet(initSet);
        
        
        while(!queue.isEmpty()){
            //System.out.println();
            //System.out.println("New Turn ");
            
            Set<Integer> checkNow = queue.poll();
            transSet(checkNow);
        }
        
        Set<Set<Integer>> acceptStates = new HashSet<Set<Integer>>();
        Set<Integer> origAccept = listToset(oldAU.acceptStates);
        for(Set<Integer> set : setMapping.keySet()){
            for(Integer a : origAccept){
                if(set.contains(a)){
                    acceptStates.add(set);
                    //System.out.println("Add set to acceptStates "+ set);
                }
            }
        }
        
        
        
        Dfa d = new Dfa(initSet, acceptStates, setMapping, trans);
        
        
        return d;
    }
    
    public void transSet(Set<Integer> checkState){
        Set<Integer> combine = new HashSet<Integer>();
        
        for(String alp : oldAU.alphabetList){
            
            combine = combineSets(checkState, alp);
            
            if(!checkState.equals(combine)){
                Integer stateNum = updateSetMapping(combine);

                //System.out.println("Add transition! "+ checkState +" -"+alp+"-> "+combine);

                //create a new DFA transition
                Map<String, Integer> AlpToDfa = new HashMap<String, Integer>();
                AlpToDfa.put(alp, stateNum);
                //System.out.println("add trans: "+ checkState+"->" +AlpToDfa);

                trans.put(transNum, AlpToDfa);
                //System.out.println("Current trans: "+trans);
                transNum++;
            }
        }
        
    }
    
    public Integer updateSetMapping(Set<Integer> combine){
        Integer stateNum = stateCounter;
        
        //if there is a new DFA state create new one. 
        //With label: statecounter
        if(!setMapping.containsKey(combine)){
                
                
            stateCounter = stateCounter+1;
            setMapping.put(combine, stateCounter);//new a DFA state
            //System.out.println("All the current DFA set(SetMapping): "+ setMapping);
             
            //put into working queue
            queue.add(combine);    
            //System.out.println("Current queue: "+ queue);
        }else{
            stateNum = setMapping.get(combine);
        }
        return stateNum;
    }
    
    
    //get next DFA state with "alp"
    public Set<Integer> combineSets(Set<Integer> checkState, String alp){
        
        Set<Integer> combine = new HashSet<Integer>();
        for(Integer i : checkState){
                state<String> state = oldAU.states.get(i);
                Map<String, ArrayList<state<String>>> stateTrans = state.transitions;
                if(stateTrans.containsKey(alp)){
                    Set<Integer> currentSet = listToset(stateTrans.get(alp));
                    combine.addAll(currentSet);//put into the Combine Set
                }
                
            }
        
        return combine;
    }
    
    
    
    
    public Set<Integer> listToset(List<state<String>> states){
        Set<Integer> stateSet = new HashSet<Integer>();
        //System.out.println("transfer "+states+" into Set");
        for(state<String> state : states){
            
            stateSet.add(state.stateLabel);
        }
        //System.out.println("stateSet: "+stateSet);
        return stateSet;
    }    
    

    
    
}
