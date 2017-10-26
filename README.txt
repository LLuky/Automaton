



            Please run the main class in "fileIO" class.



-------------------------------------------------Instruction for GUI------------------------------------------------------

	On the button of the frame, there are one TextField and two button (show NFA, convert).
	
	-You can write command in TextField as follows:
		Add a certain number of states:   add numstate NumberOfState  						e.g. "add NumState 1"
		Remove a certain state:           remove state StateLabel     							 "remove state 3"
		Add a transition:                 add transition StartState Alphabet TargetState     	 "add transition 0 a 1"
		Remove a transition:              remove transition StartState Alphabet TargetState      "remove transition 0 b 1"
		Open a file:					  open file FileName.txt	   							 "open file test.txt"
		Save a file:					  save file FileName.txt	   							 "save file test.txt"
		Conver into regular expression:   regex													 "regex"
		
		
	
	
	-If you click "Show NFA" button, it would show the Atomata in default file "test.txt".
	
	-If you click "Convert to DFA (in CUI)" button, it would first detect if it is a NFA, then convert current NFA into DFA.



	
	
	
	
	
-----------------------------------------------Question description-------------------------------------------------------
	

Q1: Read Text File (I add last two line representing initial states and final states respectively.)
	
	Refer to the method "readDocument" in class "fileIO".


Q2: GUI for NFA

	Refer to class "AutomatonGUI".
	

Q3: Save NFA as descrived. 
	
	Refer to the method "saveFile" in class "fileIO".
	

Q4: Add/remove states and transitions to/from NFA
	
	Refer to methods "addState", "removeState", "createTransition" and "deleteTransition" in class Automaton
	You can apply them by writting command in TextField, as we mentioned at the end.


Q5: Generate a regular expression from the current NFA
	Refer to class "WriteRegex"


Q6: Detect if the NFA is a DFA

	Refer to method "detectNFA" in class "convertDFA".
	
	
Q7: Convert the NFA to DFA
	
	Refer to class "convertDFA".


