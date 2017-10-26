/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mbr9995
 */
public class Automaton<E> {
    
    public ArrayList<state<E>> states;//store all states
    public ArrayList<state<E>> initialStates; //store all initial states
    public ArrayList<state<E>> acceptStates;
    public Map<state<E>, LinkedList<state<E>>> stateMap;//test reachable
    public Map<state<E>, LinkedList<state<E>>> inEdges;
    //private Map<Integer,List<Transition>> trans;
    public ArrayList<E> alphabetList;// store the alphabets
    
    public Automaton(){
        states = new ArrayList<state<E>>();
        initialStates = new ArrayList<state<E>>();
        acceptStates = new ArrayList<state<E>>();
        alphabetList = new ArrayList<E>();
        stateMap = new HashMap<state<E>, LinkedList<state<E>>>();
        inEdges = new HashMap<state<E>, LinkedList<state<E>>>();
        
//        trans = new HashMap<Integer,List<Transition>>();
//        Iterator<state<E>> acceptIt = acceptStates.iterator();
//            while(acceptIt.hasNext()){
//                state<E> accept = acceptIt.next();
//                trans.put(accept.stateLabel, new LinkedList<Transition>());
//            } 
       
        
        
    }
    
    
    public E getCertainAlp(state<E> state1, E what, state<E> state2){
        E alp = what;
        if(!stateMap.containsKey(state1)&& !inEdges.containsKey(state2)){
            return alp;
        }
        for(E a : state1.transitions.keySet()){
            ArrayList<state<E>> aList = state1.transitions.get(a);
            if(aList.contains(state2)){
                return a;
            }
        }
        
        System.out.println("Did not find the alp between "+ state1+", "+state2);
        return alp;
    }

    
    public void setInitial(state<E> iniState){
        iniState.setAsInitial(true);
        initialStates.add(iniState);
    }
    
    
    //use for regex
    public void createNewTrans(state<E> currentState, E alphabet, state<E> nextState){
        if(!alphabetList.contains(alphabet)){
            //System.out.println("Illegal alphabet.");
            return;
        }
        
        //add to next in currentState
        currentState.addNext(alphabet, nextState);
        
        //add to stateMap
        LinkedList<state<E>> newStates = new LinkedList<state<E>>();
        if(stateMap.containsKey(currentState)){
            newStates = stateMap.get(currentState);
        }
        newStates.add(nextState);
        stateMap.put(currentState, newStates);
        
        
        //add to inEdges
        LinkedList<state<E>> newInStates = new LinkedList<state<E>>();
        if(inEdges.containsKey(nextState)){
            newInStates = inEdges.get(nextState);
        }
        newInStates.add(currentState);
        inEdges.put(nextState, newInStates);
        
    }
    
    public void createTransition(int current, E alphabet, int next){
        if(!alphabetList.contains(alphabet)){
            System.out.println("Illegal alphabet.");
            return;
        }
        
        //add to next in currentState
        state<E> currentState = states.get(current);
        state<E> nextState = states.get(next);
        currentState.addNext(alphabet, nextState);
        
        //add to stateMap
        LinkedList<state<E>> newStates = new LinkedList<state<E>>();
        if(stateMap.containsKey(currentState)){
            newStates = stateMap.get(currentState);
        }
        newStates.add(nextState);
        stateMap.put(currentState, newStates);
        
        
        //add to inEdges
        LinkedList<state<E>> newInStates = new LinkedList<state<E>>();
        if(inEdges.containsKey(nextState)){
            newInStates = inEdges.get(nextState);
        }
        newInStates.add(currentState);
        inEdges.put(nextState, newInStates);
        
    }
    
    public void deleteTransition(int current, E alphabet, int next){
        if(!alphabetList.contains(alphabet)){
            System.out.println("Illegal alphabet.");
            return;
        }
        
        state<E> currentState = states.get(current);
        state<E> nextState = states.get(next);
        
        //remove from "next" in currentState
        currentState.removeNext(alphabet, nextState);
        
        
        
        
        //remove from stateMap
        if(stateMap.containsKey(currentState)){
            LinkedList<state<E>> newList = stateMap.get(currentState);
            System.out.println("Remove from stateMap ");
            System.out.println("List: "+newList);
            newList.remove(nextState);
            for(state<E> s : newList){
                for(E alp : alphabetList){
                    System.out.println("remove next");
                    s.removeNext(alp, currentState);
                    
                }
                
                
            }
            //System.out.println("newList: "+newList);
            stateMap.replace(currentState, newList);
            //System.out.println("stateMap: "+stateMap);
        }
        
        
        //remove from inEdges
        if(inEdges.containsKey(nextState)){
            LinkedList<state<E>> newList = inEdges.get(nextState);
            //System.out.println("Remove from remove ");
            //System.out.println("List: "+newList);
            
            
            newList.remove(currentState);
            //System.out.println("newList: "+newList);
            inEdges.replace(nextState, newList);
            //System.out.println("stateMap: "+inEdges);
        }
    }
    
    //for WriteRegex
    public void addState(state<E> state){
        states.add(state);
        if(stateMap.containsKey(state)){
            stateMap.put(state, stateMap.get(state));
        }
        
        if(inEdges.containsKey(state)){
            inEdges.put(state, inEdges.get(state));
        }
    }
    
    public void addState(int addNumState){
        int lastStateLabel;
        if(states.isEmpty()){
            lastStateLabel = 0;
        }else{
            lastStateLabel = states.get(states.size()-1).stateLabel+1;
        }
        
        for(int i=0; i<addNumState; i++){
            state<E> newState = new state<E>(lastStateLabel + i);
            
            states.add(newState);
            
            System.out.println("add state: "+newState);
        }
        
    }
    

    //for Regex
    public void removeState(state<E> newState){
        if(!states.contains(newState)){
            System.out.println("No "+newState);
            return;
        }
        
        states.remove(newState);
        
        //remove from other entry
        if(stateMap.containsValue(newState)){
            stateMap.remove(newState);
            inEdges.remove(newState);
            
            /*Set<state<E>> stateSet = stateMap.keySet();
            Iterator<state<E>> stateIt = stateSet.iterator();
            while(stateIt.hasNext()){
                state<E> state = stateIt.next();
                stateMap.remove(state, newState);
                
                
                Iterator<E> alphabetIt = alphabetList.iterator();
                while(alphabetIt.hasNext()){
                    E alphabet = alphabetIt.next();
                    state.transitions.remove(alphabet, newState);
                }
                
                if(inEdges.containsKey(state)){
                    inEdges.remove(state, newState);
                }
            }*/
            
            
        }
        if(initialStates.contains(newState)){
            initialStates.remove(newState);
        }
        
        //System.out.println("remove "+ newState);
    }
    
    public void removeState(int stateNo){
        if(stateNo > states.size()-1 || stateNo < 0){
            //System.out.println("Can't remove State_"+stateNo);
            //System.out.println("states.size(): "+states.size());
            return;
        }
        state<E> newState = states.get(stateNo);
        states.remove(newState);
        
        
        //remove from other entry
        if(stateMap.containsValue(newState)){
            stateMap.remove(newState);
            
            
            Set<state<E>> stateSet = stateMap.keySet();
            Iterator<state<E>> stateIt = stateSet.iterator();
            while(stateIt.hasNext()){
                state<E> state = stateIt.next();
                stateMap.remove(state, newState);
                
                
                Iterator<E> alphabetIt = alphabetList.iterator();
                while(alphabetIt.hasNext()){
                    E alphabet = alphabetIt.next();
                    state.transitions.remove(alphabet, newState);
                }
                
            }
            
        }
        
        System.out.println("Incoming edges for newState"+inEdges.get(newState));
            for(state<E> s : inEdges.get(newState)){
                for(E alp : alphabetList){
                    System.out.println("remove next");
                    s.removeNext(alp, newState);
                    stateMap.get(s).remove(newState);
                    
                }
            }
            
            inEdges.remove(newState);
        
        if(initialStates.contains(newState)){
            initialStates.remove(newState);
        }
        
    }
    
    public void setInit(int newInit){
        state<E> init = states.get(newInit);
        init.setAsInitial(true);
        initialStates.add(init);
    }
    
    public void setAccept(int newAccept){
        state<E> accept = states.get(newAccept);
        accept.setAsFinal(true);
        acceptStates.add(accept);
    }
    
    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        fileIO test = new fileIO();
        
        
        //test Automaton
        Automaton<String> auto = test.auto;
        System.out.println("auto states: "+ auto.states);
        System.out.println("stateMap: "+ auto.stateMap);
        //auto.addState(3);
        //System.out.println("after adding auto states: "+ auto.states);
        auto.removeState(2);
        System.out.println("After stateMap: "+ auto.stateMap);
        //auto.createTransition(2, "a", 3);
        //System.out.println("after adding trans: "+ auto.stateMap);
        //System.out.println("inEdges:"+auto.inEdges);
        //auto.deleteTransition(0, "a", 0);
        //System.out.println("after remove trans: "+ auto.stateMap);
        
    }
    
}
