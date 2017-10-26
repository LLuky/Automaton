/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
//import java.lang.String;;

/**
 *
 * @author asus1
 */
public class WriteRegex {
    Automaton<String> auto;
    
    public WriteRegex(Automaton au){
        this.auto = au;
        //System.out.println("Got"+auto.stateMap);
        AutomataToRegex();
    }
    
    public String AutomataToRegex(){
        
        //create new initial and final: s, t
        state<String> s = new state<String>(-1);
        state<String> t = new state<String>(-2);
        s.setAsInitial(true);
        t.setAsFinal(true);
        
        LinkedList<state<String>> list = new LinkedList<state<String>>();
        for(state<String> v : auto.initialStates){
           v.setAsInitial(false);
           list.add(v);
        }
        s.transitions.put("#", auto.initialStates);
        auto.inEdges.put(s, list);
        
        for(state<String> v : auto.acceptStates){
           v.setAsFinal(false);
           v.addNext("#",t);
        }
        
        //get a list of vertices in the order that doing a bfs
        LinkedList<state<String>> orderList = BFSauto(s);
        
        //Concadination
        state<String> removingState;
        ArrayList<state<String>> unremovedList = auto.states;
        
        
        String regex = "";
        for(state<String> i : orderList){
            removingState = i;
            if(unremovedList.contains(removingState)){
                //System.out.println("removing state: " + removingState);
                
                
                //Construct Self-loop
                Map<state<String>, ArrayList<String>> selfMap;
                
                selfMap = getSelfPointedEdges(removingState);
                String selfRegex = getSelfRegex(selfMap);
                //System.out.println("Only selfRegex"+selfRegex);
                
                //Construct Incoming Edges and Outgoing Edges
                Map<state<String>, ArrayList<String>> inEdges, outEdges;
                
                inEdges = getInEdges(removingState);
                inEdges.remove(selfMap.keySet());
                outEdges = getOutEdges(removingState);
                outEdges.remove(selfMap.keySet());
                
                
                
                //remove self-loop in Automaton
                if(selfMap.containsKey(removingState)){
                    for(String alp : selfMap.get(removingState)){
                        auto.deleteTransition(removingState.stateLabel, alp, removingState.stateLabel);

                    }
                }
                
                
                //System.out.println("inedges: " + inEdges);
                //System.out.println("outedges: " + outEdges);
                
                
                //remove state 
                state<String> fromState, toState;
                for(state<String> inEdge : inEdges.keySet()){
                    fromState = inEdge;
                    for(state<String> outEdge : outEdges.keySet()){
                        toState = outEdge;
                        
                        String newLetter ="";
                        //for(String letter: inEdges.get(inEdge)){
                        if(!inEdges.isEmpty()){
                            newLetter += alternative(inEdges.get(inEdge));
                            
                        }
                            
                            //concadinate with self-loop
                            if(inEdges.containsKey(inEdge) && !selfRegex.isEmpty()){
                                newLetter += ".";
                            }
                            newLetter += selfRegex;
                            
                            //concadinate with outgoing edges
                            if(!selfRegex.isEmpty() && outEdges.containsKey(outEdge)){
                                newLetter += ".";
                            }
                            if(inEdges.containsKey(inEdge) && selfRegex.isEmpty() && outEdges.containsKey(outEdge)){
                                newLetter += ".";
                            }
                            newLetter += alternative(outEdges.get(outEdge));
                            
                            //System.out.println("from state: " + fromState + " to state: " + toState);
                            System.out.println("new letter: " + newLetter);
                            auto.createNewTrans(fromState, newLetter, toState);
                        //} 
                        regex = newLetter;
                    }
                        
                }
            }
            unremovedList.remove(removingState);
            if(removingState.stateLabel==-1){
                //System.out.println("Did not remove: "+removingState);
            }else if(removingState.stateLabel==-2){
                //System.out.println("Did not remove: "+removingState);
            }else {
                auto.removeState(removingState.stateLabel);
            }
            
        }
        /*LinkedList<state<String>> stEdges = auto.stateMap.get(s);
        System.out.println(stEdges);
        LinkedList<String> regexFragments = new LinkedList();
        //String regex = "";
        String fragment;
        
        //for(int i=0; i<stEdges.size(); i++){
            fragment = auto.getCertainAlp(s, "", t);
            System.out.println("fragment: "+fragment);
            if(fragment.equals("")){
                fragment = "EMPTY";
            }
            if(!regexFragments.contains(fragment)){
                regexFragments.add(fragment);
                regex+=fragment;
                //if(i < stEdges.size()-1){
                //    regex+="|";
                //}
            }
            
        //}
        
        
        
        
        //String regex = "";
        /*for(String p : s.transitions.keySet()){
            regex += p;
        }*/
        System.out.println();
        System.out.println("Regular Expression: " + regex);
        return regex;
    }
    
    private String alternative(ArrayList<String> list){
        String output = ""+list.get(0);
        
        for(int i=1; i<list.size(); i++){
            output += " | "+list.get(i);
        }
        return output;
    }
    
    private String getSelfRegex(Map<state<String>, ArrayList<String>> selfMap){
        String output = "";
        
        for(state<String> e : selfMap.keySet()){
            ArrayList<String>alpList = selfMap.get(e);
            output  = output+ "( "+alpList.get(0);
            
            for(int i=1; i< alpList.size(); i++){
                output += " | "+alpList.get(i);
            }
             output  = output+ " )";
        }
        
        
        //System.out.println("output: "+output);
        if (!output.isEmpty()) {
            output="("+output;
            output+=")*";
        }
        //System.out.println("self regex: " + output);
        
        return output;
    }
    
    private Map<state<String>, ArrayList<String>> getSelfPointedEdges(state<String> v){
        Map<state<String>, ArrayList<String>> selfPointedEdges = new HashMap<state<String>, ArrayList<String>>();
        Map<String, ArrayList<state<String>>> outEdges = new HashMap<String, ArrayList<state<String>>>(v.transitions);
        
        for (String alp : outEdges.keySet()){
            ArrayList<state<String>> edgeList = outEdges.get(alp);
            
            for(state<String> e : edgeList){
                ArrayList<String> alpList;
                if(v.equals(e)){
                    if(selfPointedEdges.containsKey(e)){
                        alpList = selfPointedEdges.get(e);
                    }else{
                        alpList = new ArrayList<String>();
                    }
                    alpList.add(alp);

                    selfPointedEdges.put(e, alpList);
                }
            }
            
        }
        
        return selfPointedEdges;
    }
    
    public Map<state<String>, ArrayList<String>> getInEdges(state<String> v){
        Map<state<String>, ArrayList<String>> inEdges = new HashMap<state<String>, ArrayList<String>>();
                
        for(state<String> inEdge : auto.inEdges.get(v)){
            for(String alp : inEdge.transitions.keySet()){
                for(state<String> e : inEdge.transitions.get(alp)){
                    ArrayList<String> alpList;
                    if(inEdges.containsKey(e)){
                        alpList = inEdges.get(e);
                    }else{
                        alpList = new ArrayList<String>();
                    }
                    alpList.add(alp);
                    inEdges.put(e, alpList);
                }
                
                
            }
                    
        }
        return inEdges;
    }
    
    public Map<state<String>, ArrayList<String>> getOutEdges(state<String> v){
        Map<state<String>, ArrayList<String>> outEdges = new HashMap<state<String>, ArrayList<String>>();
        if(!auto.stateMap.containsKey(v)){
            //System.out.println(v+" has no outgoing edges.");
            return outEdges;
        }
        for(state<String> outEdge : auto.stateMap.get(v)){
            for(String alp : outEdge.transitions.keySet()){
                for(state<String> e : outEdge.transitions.get(alp)){
                    ArrayList<String> alpList;
                    if(outEdges.containsKey(e)){
                        alpList = outEdges.get(e);
                    }else{
                        alpList = new ArrayList<String>();
                    }
                    alpList.add(alp);
                    outEdges.put(e, alpList);
                }
                
                
            }
                    
        }
        return outEdges;
    }
    
    
    public LinkedList<state<String>> BFSauto(state<String> s){
        Queue<state<String>> queue = new LinkedList<state<String>>();
        LinkedList<state<String>> orderList = new LinkedList<state<String>>();
        
        queue.add(s);
        orderList.add(s);
        while(!queue.isEmpty()){
            //removes from front of queue
            state<String> r= queue.poll();
            //System.out.println("Dequeue "+r);
            
            ArrayList<state<String>> nexts = r.getAllNext();
            for(state<String> next : nexts){
                if(!next.isVisit){
                    queue.add(next);
                    next.isVisit=true;
                    orderList.add(next);
                    //System.out.println("Visiting "+ next);
                }
            }
            
        }
        
        //System.out.println("orderList: "+ orderList);
        return orderList;
    } 
    
}

