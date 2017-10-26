/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author mbr9995
 */
public class state<E> {
    
    public boolean accepting;
    public boolean initial;
    public int stateLabel;
    public Map<E, ArrayList<state<E>>> transitions;
    public int nextStateNum;
    
    public int xpos;
    public int ypos;
    
    //conver to Regex (BFS)
    public boolean isVisit;
    
    
    
    
    public state(int label){
        accepting = false;
        initial = false;
        this.stateLabel = label;
        transitions = new HashMap< E, ArrayList<state<E>>>();
        nextStateNum = transitions.size();
        
        xpos=label*100+100;
	ypos=200;
        
        isVisit = false;
    }
    
    
    
    public state transit(E alphabet){
        if(!transitions.containsKey(alphabet)){
            System.out.println("Not such alphabet.");
            return null;
        }
        
        
        ArrayList<state<E>> nextList = transitions.get(alphabet);//all the next States
        
        
        
        //If we still have time try DFS
        Random ran = new Random();
        int numNext = nextList.size();
        state<E> next = nextList.get(0);
        
        for(int i=1; i<numNext; i++){
            state<E> anotherNext = nextList.get(i);
            if(ran.nextBoolean()){
                next = anotherNext;
            }
        }
        
        System.out.println(this +"Current transitions: "+transitions);
        
        //Check if it is an accepting state
        if(next.accepting){
            System.out.println("accepted");
        }
        return next;
    }
    
    
    
    public void addNext(E alphabet, state<E> next){
        ArrayList<state<E>> nextList = new ArrayList<state<E>>();
        if(!transitions.containsKey(alphabet)){
            
            nextList.add(next);
            this.transitions.put(alphabet, nextList);
        }else{
            nextList = this.transitions.get(alphabet);
            nextList.add(next);
            this.transitions.replace(alphabet, nextList);
        }
        nextStateNum++;
        System.out.println(this +" = " + alphabet + " =>: " + nextList);
    }
    
    public void removeNext(E alphabet, state<E> state){
        if(!transitions.containsKey(alphabet)){
            System.out.println(this +" does not have alphabet: " + alphabet);
            return;
        }
        ArrayList<state<E>> transList = transitions.get(alphabet);
        transList.remove(state);
        nextStateNum--;
        System.out.println(this +" transition: " + transitions);
    }
    
    
    public void setAsInitial(boolean isInit){
        this.initial = isInit;
        //System.out.println(this+" is INITIAL.");
    }
    
    public void setAsFinal(boolean isAccpet){
        this.accepting = isAccpet;
        //System.out.println(this+" state is FINAL.");
    }
    public String toString(){
        String output = "State_"+stateLabel;
        if(stateLabel == -10){
            output = "";
        }
        return output;
    }
    
    
    public void draw(Graphics g, Color color){
	Graphics2D g2d = (Graphics2D) g;
	g2d.setStroke(new BasicStroke(2));

	g2d.setColor(color);

	g2d.drawOval(xpos-20, ypos-20,40,40);
        
        //if it is an initial state
        if(initial){
            
            g2d.drawLine(xpos-35, ypos, xpos-20, ypos);
            
        }
        //if it is an accepting state
        if(accepting){
            g2d.drawOval(xpos-16, ypos-16,33,33);
        }

	g2d.setColor(Color.BLUE);
	g2d.drawString(""+stateLabel,xpos-3, ypos+4);
    }
    
    //compute the x coordinate of the start of the edge from this to the specified node
    public int getEdgeX(state<E> node){
    double direction=1.0;
	if(node.xpos<xpos){
            direction=-1.0;
	}
	double x2subx1sqr = Math.pow((node.xpos-xpos),2);
	double y2suby1sqr = Math.pow((node.ypos-ypos),2);
	double rsqr = Math.pow(40*1.0/2,2);
	double x=Math.sqrt((x2subx1sqr*rsqr/ (x2subx1sqr+y2suby1sqr))) * direction+xpos;
	return (int)Math.round(x);
    }

	//compute the x coordinate of the start of the edge from this to the specified node
    public int getEdgeY(state<E> node){
	double direction=1.0;
	if(node.ypos<ypos){
		direction=-1.0;
	}
	double x2subx1sqr = Math.pow((node.xpos-xpos),2);
	double y2suby1sqr = Math.pow((node.ypos-ypos),2);
	double rsqr = Math.pow(40*1.0/2,2);
	double y=Math.sqrt((y2suby1sqr*rsqr/ (x2subx1sqr+y2suby1sqr))) * direction+ypos;
	return (int)Math.round(y);
    }
    
    public ArrayList<state<E>> getAllNext(){
        ArrayList<state<E>> nexts = new ArrayList<state<E>>();
        for(E alp : transitions.keySet()){
            ArrayList<state<E>> alpList = transitions.get(alp);
            nexts.addAll(alpList);
        }
        System.out.println("All next: "+ nexts);
        return nexts;
    }
    
}
