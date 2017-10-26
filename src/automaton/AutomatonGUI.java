/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automaton;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import java.lang.String;

/**
 *
 * @author mbr9995
 */
public class AutomatonGUI extends JPanel implements  ActionListener{
    
    public Automaton<String> auto;
    private JTextField tf;
    private JPanel buttonPanel;
    private JButton NFAButton;
    private JButton DFAButton;
    
    public AutomatonGUI(Automaton auto){
        this.auto = auto;
        
        //create button
        JPanel panel = new JPanel();
        buttonPanel=new JPanel();
        NFAButton=new JButton("Show NFA");
        DFAButton=new JButton("Conver to DFA (in CUI)");
        NFAButton.addActionListener(this);
        DFAButton.addActionListener(this);
        buttonPanel.setLayout(new GridLayout(3,1));
        
        tf = new JTextField();
        tf.addActionListener(this);
        buttonPanel.add(tf);
        
        buttonPanel.add(NFAButton);
        buttonPanel.add(DFAButton);
        
        this.setBackground(Color.WHITE);
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(buttonPanel,BorderLayout.SOUTH);
        
        
        
        
        
        JFrame frame = new JFrame("Automaton");
	frame.setSize(450,450);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocation(0,0);
	frame.getContentPane().add(this);
	frame.setVisible(true);
        
    }
    
    
    
    //Paint the automaton to the panel
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
	g2d.fillRect(0,0,1500,1500);

	g2d.setColor(Color.BLACK);

	//Draw
        ArrayList<state<String>> stateList = auto.states;
        //System.out.println("States set: "+auto.states);
        Map<state<String>, LinkedList<state<String>>> stateMap = auto.stateMap;
        ArrayList<String> alphabetList = auto.alphabetList;
        int ctrl = 30;
        
        
        for(state<String> i: stateList){
            i.draw(g, Color.BLACK);  
                for(String alp : alphabetList){
                    
                    if(i.transitions.containsKey(alp)){
                        ArrayList<state<String>> iTrans = i.transitions.get(alp);
                        
                        
                        
                        for(state<String> j : iTrans){
                            System.out.println("Draw edge "+i+"-"+j);
                            //drawEdge(i,j,ctrl,alp,g);
                            drawEdge(i,j,ctrl,alp,g);
                            
                            
                        }
                        if(stateMap.get(i).size()!= 1){
                            
                            ctrl = ctrl-50;
                        }
                        
                    }
                }
        }
    }
    
    
    
    
    //Draw an edge
	public void drawEdge(state<String> node1, state<String> node2,
                double ctrl, String alp, Graphics g){
            Graphics2D g2 = (Graphics2D) g;

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);

            state<String> start = node1;
            state<String> end = node2;
            
            
            //self-loop
            
            if(node1 == node2){
                
                double rsqr = Math.pow(40*1.0/2,2);
                double X = start.xpos-20;
                double Y = start.ypos;
                double Middle = (X+Y)/2;
                
                //System.out.println("self-loop");
                Shape self = new QuadCurve2D.Double(X,Y, 
                        Middle+ctrl*2,Middle+ctrl*7,X+40,Y);
                g2.draw(self);
                g2.drawString(""+alp,start.xpos+(int)ctrl/2, start.ypos+10+(int)ctrl*2);
                
            }else{
            

                int startX = start.getEdgeX(end);
                int startY = start.getEdgeY(end);
                int endX = end.getEdgeX(start);
                int endY = end.getEdgeY(start);
                int middleX = (startX+endX)/2;
                int middleY = (startY+endY)/2;



                //double x1, y1,(control point) ctrlx, ctrly, x2,y2
                Shape s = new QuadCurve2D.Double(startX,startY, 
                                (startX+endX)/2,(startY+endY)/3+ctrl,endX,endY);
                g2.draw(s);
                g2.drawString(""+alp,middleX, middleY+(int)ctrl);
                //g2.drawLine(startX, startY, endX, endY);

                double theta,x,y;
                //g2.setPaint(Color.BLUE);
                g2.setPaint(Color.BLACK);
                theta=Math.atan2(endY-startY,endX-startX);
                drawArrow(g2,theta,endX,endY);
            }
	}
    
    //draw arrow
	private void drawArrow(Graphics2D g2, double theta, double x0, double y0){
            g2.setStroke(new BasicStroke(3));
            
            double x = x0 - 10*Math.cos(theta+Math.PI/12);
            double y = y0 - 10*Math.sin(theta+Math.PI/12);
            g2.draw(new Line2D.Double(x0,y0,x,y));
            x=x0-10*Math.cos(theta-Math.PI/12);
            y=y0-10*Math.sin(theta-Math.PI/12);
            g2.draw(new Line2D.Double(x0,y0,x,y));
	}
    
    
    
    
    


    @Override
    public void actionPerformed(ActionEvent e) {
        
        //deal with Button
        Object source=e.getSource();
        if(source==NFAButton){
           
            fileIO file = new fileIO();
            //buttonPanel.setBackground(Color.BLUE);
        }
        if(source==DFAButton){
            convertDFA convert = new convertDFA(auto);
            buttonPanel.setBackground(Color.RED);
        }
        
        if(source==tf){
        //deal with TexField
        String command = tf.getText();
        System.out.println("Command: "+ command);
        StringTokenizer st = new StringTokenizer(command);
        String token,opt;
        Integer state1, state2;
        String alp;
        String fileName;
        
        
        
        
        
        if(st.hasMoreTokens()){
            token = st.nextToken();
            token=token.toLowerCase();

            switch(token){
                //if the input command is "add numstate 1" or "add transition 0 'a' 1"
                case "add":
                    try{
                        opt = st.nextToken();
                        opt = opt.toLowerCase();
                        if(opt.equals("numstate")){
                            state1 = Integer.parseInt(st.nextToken());
                            if(st.hasMoreTokens()) break;
                            System.out.println("Now:   add state "+ state1);
                            auto.addState(state1);
                        }else if(opt.equals("transition")){
                            state1 = Integer.parseInt(st.nextToken());
                            alp = (String)st.nextToken();
                            state2 = Integer.parseInt(st.nextToken());
                            if(st.hasMoreTokens()) break;
                            System.out.println("Now:   add transition "+ state1+alp+state2 );
                            auto.createTransition(state1, alp, state2);
                        }
                    }catch(Exception ex){
                        System.out.println("Invalid command");
                    }
                break;
                //if the input command is "remove state 0" or "remove transition 0 'b' 1"
                case "remove":
                    try{
                        opt = st.nextToken();
                        opt = opt.toLowerCase();
                        if(opt.equals("state")){
                            state1 = Integer.parseInt(st.nextToken());
                            if(st.hasMoreTokens()) break;
                            auto.removeState(state1);
                        }else if(opt.equals("transition")){
                            state1 = Integer.parseInt(st.nextToken());
                            alp = (String)st.nextToken();
                            state2 = Integer.parseInt(st.nextToken());
                            if(st.hasMoreTokens()) break;
                            auto.deleteTransition(state1, alp, state2);
                        }
                    }catch(Exception ex){
                        System.out.println("Invalid command");
                    }
                break;
                    
                case "open":
                    try{
                        fileIO file = new fileIO();
                        opt = st.nextToken();
                        opt = opt.toLowerCase();
                        if(opt.equals("file")){
                            fileName = (String)st.nextToken();
                            if(st.hasMoreTokens()) break;
                           file.readDocument(fileName);
                        }
                    }catch(Exception ex){
                        System.out.println("Invalid command");
                    }
                break;    
                    
                case "save":
                    try{
                        fileIO file = new fileIO();
                        opt = st.nextToken();
                        opt = opt.toLowerCase();
                        if(opt.equals("file")){
                            fileName = (String)st.nextToken();
                            if(st.hasMoreTokens()) break;
                           file.saveFile(fileName, auto);
                        }
                    }catch(Exception ex){
                        System.out.println("Invalid command");
                    }
                break;    
                    
                    
                case "regex":
                    try{
                        
                        if(st.hasMoreTokens()) break;
                        WriteRegex wr = new WriteRegex(auto);
                        
                    }catch(Exception ex){
                        System.out.println("Invalid command");
                    }
                break; 
                    
                    
                //if the input command is non of the above
                default:
                    System.out.println("Invalid command");
                    break;
           }
       }
        // repaint the graph to show the change
     	repaint();
    }
    }    
    
}
