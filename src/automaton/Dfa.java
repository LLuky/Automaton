/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author asus1
 */
public class Dfa<Character> {
    private Set<Integer> startState;
    private Set<Set<Integer>> acceptStates;
    private Map<Integer, Map<Character, Integer>> trans;
    private Map<Set<Integer>, Integer> setMapping;
    
    public Dfa(Set<Integer> startState, Set<Set<Integer>> acceptStates,
            Map<Set<Integer>, Integer> setMapping, Map<Integer, Map<Character, Integer>> trans){
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.setMapping = setMapping;
        this.trans = trans;
    }
    
    public Set<Integer> getStart() { return startState; }

    public Set<Set<Integer>> getAccept() { return acceptStates; }
    
    public Map<Set<Integer>, Integer> getSetMapping() { return setMapping; }

    public Map<Integer,Map<Character,Integer>> getTrans() { return trans; }

    public String toString() {
      return "DFA start=" + startState + "\naccept=" + acceptStates 
        + "\n" + trans;
    }
    
}
