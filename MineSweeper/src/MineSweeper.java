/*
 * Description: Minesweeper game. If a mine is clicked, the game ends. If a tile is clicked and it contains
 * 0 mines in the surrounding 8 tiles, it will recursively find all 0's until 0's are surrounding by integers.
 * If a tile is clicked, it displays how many mines contain in the surrounding. If a game is won, a music is played.
 * If a game is lost, a music is also played. User can choose to change difficulty and save to file during the process of game.
 * If the game is lost, the program will output how long the game has processed and how many mines the user has guessed correctly
 * If the game is won, the program will output how long the game has processed and congratulations.
 * Author: Andy Su, Philip Shi
 * Date start: 4/19/2016
 * Date end: 5/9/2016
 * Date submitted: 5/9/2016
 * IMPORTANT: PLEASE USE JAVASE-1.8 TO COMPILE THIS PROGRAM.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;




import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MineSweeper implements ActionListener,MouseListener{
	JFrame GameFrame = new JFrame("Minesweeper"); //game frame and frame name
	JFrame DifficultyFrame = new JFrame("Difficulty"); //difficulty frame and frame name
	JFrame InstructionFrame = new JFrame("Instruction"); //instruction frame and frame name
	JButton reset = new JButton(); //reset button
	JButton saveFile = new JButton("Save File"); //button to save file
	JButton loadFile = new JButton("Load File"); //button to load file
	JButton changeDifficulty = new JButton("Change Difficulty"); //button to change difficulty
	JButton instructionButton = new JButton("Instruction"); //button to show instruction
	JPanel command = new JPanel(); //this JPanal adds savefile and loadfile and instruction buttons
	Container grid = new Container(); //GUI
	int MAXrow,MAXcol ; //declare size of the grid
	int number_of_mines ; //declare the amount of mines
	JLabel remainMines = new JLabel() ; //create a label for remain mines
	JButton[][] buttons ; //initialize button array
	int[][] counts ; //initialize counts array to keep track of numbers
	Random rand = new Random(); //declare random number
	final int mine = 9; //initially set 9 as a mine and this value cannot be changed
	boolean isZero; //declare a boolean variable
	boolean isFirstClick = true; //detects if the user first clicks
	JTextField custom_colField, custom_rowField,custom_minesField; //declare text fields 
	JPanel panel = new JPanel(); //declare a panel
	int unclick=0;
	final int stateNone=0; //initially set no state to 0 and this value cannot be changed
	final int clicked = 100; //if a tile is clicked, set its state to clicked 
	final int stateFlag=200; //if a tile is flagged, set its state to flag 
	final int stateQuestion=300; //if a tile is put question mark
	int[][] states; //create a 2-D array to keep track of states of tiles
	JLabel timerLabel = new JLabel("time passed: 0 s"); //timer label
	int secondsPassed = 0; //how long the game has processed in seconds
	Timer timer = new Timer(); //declare a timer
	TimerTask task = new TimerTask(){ //declare a timer task to keep track of time
		public void run(){ //method to run timer task
			secondsPassed ++; //increment seconds by 1 each time
			timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s"); //output the message
		}
	};
	public MineSweeper() {
		difficultyframe(); //show the difficulty frame when the program is run
	}

	/* difficulty frame GUI */
	public void difficultyframe() {
		DifficultyFrame.setBounds(100, 100, 503, 332);
		DifficultyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DifficultyFrame.setLocationRelativeTo(null);

		DifficultyFrame.getContentPane().setLayout(null);

		JButton beginnerButton = new JButton("Beginner");
		beginnerButton.addActionListener(new ActionListener() { //if the user chooses to play beginner
			public void actionPerformed(ActionEvent arg0) {
				DifficultyFrame.setVisible(false); //close the frame
				MAXrow = 9; //initialize max row
				MAXcol = 9; //initialize max col
				buttons = new JButton[MAXrow][MAXcol]; //initialize buttons
				counts = new int[MAXrow][MAXcol]; //initialize counts
				states = new int[MAXrow][MAXcol]; //initialize states
				number_of_mines = 10; //initialize number of mines
				remainMines.setText("mines remain: " + number_of_mines); //show the remaining mines
				gameframe(); //create game frame
			}
		});
		beginnerButton.setBounds(17, 60, 117, 23); //set button size
		DifficultyFrame.getContentPane().add(beginnerButton); //add button to frame

		JButton intermediateButton = new JButton("Intermediate"); 
		intermediateButton.addActionListener(new ActionListener() { //if the user chooses to play intermediate
			public void actionPerformed(ActionEvent arg0) {
				DifficultyFrame.setVisible(false); //close the frame
				MAXrow = 16; //initialize max row
				MAXcol = 16; //initialize max col
				buttons = new JButton[MAXrow][MAXcol]; //initialize buttons
				counts = new int[MAXrow][MAXcol]; //initialize counts
				states = new int[MAXrow][MAXcol]; //initialize states
				number_of_mines = 40; //initialize number of mines
				remainMines.setText("mines remain: " + number_of_mines); //show the remaining mines
				gameframe(); //create game frame
			}
		});
		intermediateButton.setBounds(17, 120, 117, 23);
		DifficultyFrame.getContentPane().add(intermediateButton); //add button to frame

		JButton expertButton = new JButton("Expert");
		expertButton.addActionListener(new ActionListener() { //if the user chooses to play expert
			public void actionPerformed(ActionEvent e) {
				DifficultyFrame.setVisible(false); //close the frame
				MAXrow = 16; //initialize max row
				MAXcol = 30; //initialize max col
				buttons = new JButton[MAXrow][MAXcol]; //initialize buttons
				counts = new int[MAXrow][MAXcol]; //initialize counts
				states = new int[MAXrow][MAXcol]; //initialize states
				number_of_mines = 99; //initialize number of mines
				remainMines.setText("mines remain: " + number_of_mines); //show the remaining mines
				gameframe(); //create game frame
			}
		});
		expertButton.setBounds(17, 180, 117, 23);
		DifficultyFrame.getContentPane().add(expertButton); //add button to frame

		JButton customButton = new JButton("Custom");
		customButton.addActionListener(new ActionListener() { //if the user chooses custom
			public void actionPerformed(ActionEvent e) {
				DifficultyFrame.setVisible(false); //close the frame window
				if(checkinput(custom_rowField.getText(),custom_colField.getText(),custom_minesField.getText())) { //checks if inputs are valid
					MAXrow = Integer.parseInt(custom_rowField.getText()); //assign row field value to max row
					MAXcol = Integer.parseInt(custom_colField.getText()); //assign col field value to max col
					buttons = new JButton[MAXrow][MAXcol]; //create buttons
					counts = new int[MAXrow][MAXcol]; //create counts
					states = new int[MAXrow][MAXcol]; //create states
					number_of_mines = Integer.parseInt(custom_minesField.getText()); //assign mine field value to number of mines
					remainMines.setText("mines remain: " + number_of_mines); //show how many mines remain
					gameframe(); //create game frame
				}

			}

			/* checks the input of custom. row and col have to be <30 and >0. Also, mines cannot exceed row * col*/

			public boolean checkinput(String row, String col,String mines) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(row)<=0 || Integer.parseInt(col)<=0){ //if any of row or col is 0
					JOptionPane.showMessageDialog(null, "row or field cannot be 0 or negative values"); //show the message
					custom_rowField.setText(null); //clear text in row field
					custom_colField.setText(null); //clear text in col field
					DifficultyFrame.setVisible(true); //reshow difficulty frame
					return false; //return false
				}
				if (Integer.parseInt(mines)>=Integer.parseInt(row)*Integer.parseInt(col)){ //if mines > row * col
					JOptionPane.showMessageDialog(null, "too many mines");
					custom_minesField.setText(null); //clear text in minesfield
					DifficultyFrame.setVisible(true); //reshow difficulty frame
					return false; //return false
				}
				if (Integer.parseInt(row)>30 || Integer.parseInt(col)>30){
					JOptionPane.showMessageDialog(null, "row or col cannot exceed 30");
					custom_rowField.setText(null); //clear text in row field
					custom_colField.setText(null); //clear text in col field
					DifficultyFrame.setVisible(true); //reshow difficulty frame
					return false; //return false
				}
				if (Integer.parseInt(col)<8 ){
					JOptionPane.showMessageDialog(null, "column has minimum value of 8");
					custom_rowField.setText(null); //clear text in row field
					custom_colField.setText(null); //clear text in col field
					DifficultyFrame.setVisible(true); //reshow difficulty frame
					return false; //return false
				}

				return true; //if none of them is 0, return true
			}
		});
		customButton.setBounds(17, 240, 117, 23);
		DifficultyFrame.getContentPane().add(customButton); //add button to frame

		/* add buttons, text field, label to frame and set their size*/
		custom_rowField = new JTextField();
		custom_rowField.setBounds(183, 241, 55, 21);
		DifficultyFrame.getContentPane().add(custom_rowField);
		custom_rowField.setColumns(10);

		custom_colField = new JTextField();
		custom_colField.setBounds(303, 241, 55, 21);
		DifficultyFrame.getContentPane().add(custom_colField);
		custom_colField.setColumns(10);

		JLabel rowLabel = new JLabel("Row");
		rowLabel.setBounds(187, 10, 46, 15);
		DifficultyFrame.getContentPane().add(rowLabel);

		JLabel colLabel = new JLabel("Column");
		colLabel.setBounds(303, 10, 46, 15);
		DifficultyFrame.getContentPane().add(colLabel);

		JLabel beginner_row_label = new JLabel("9");
		beginner_row_label.setBounds(187, 64, 46, 15);
		DifficultyFrame.getContentPane().add(beginner_row_label);

		JLabel intermediate_row_label = new JLabel("16");
		intermediate_row_label.setBounds(187, 124, 46, 15);
		DifficultyFrame.getContentPane().add(intermediate_row_label);

		JLabel expert_row_label = new JLabel("16");
		expert_row_label.setBounds(187, 184, 46, 15);
		DifficultyFrame.getContentPane().add(expert_row_label);

		JLabel beginner_col_label = new JLabel("9");
		beginner_col_label.setBounds(303, 64, 46, 15);
		DifficultyFrame.getContentPane().add(beginner_col_label);

		JLabel intermediate_col_label = new JLabel("16");
		intermediate_col_label.setBounds(303, 124, 46, 15);
		DifficultyFrame.getContentPane().add(intermediate_col_label);

		JLabel expert_row_label_1 = new JLabel("30");
		expert_row_label_1.setBounds(303, 184, 46, 15);
		DifficultyFrame.getContentPane().add(expert_row_label_1);

		JLabel mines_label = new JLabel("Mines");
		mines_label.setBounds(411, 10, 46, 15);
		DifficultyFrame.getContentPane().add(mines_label);

		JLabel beginner_mines_label = new JLabel("10");
		beginner_mines_label.setBounds(411, 64, 46, 15);
		DifficultyFrame.getContentPane().add(beginner_mines_label);

		JLabel intermediate_mines_label = new JLabel("40");
		intermediate_mines_label.setBounds(411, 124, 46, 15);
		DifficultyFrame.getContentPane().add(intermediate_mines_label);

		JLabel expert_mines_labels = new JLabel("99");
		expert_mines_labels.setBounds(411, 184, 46, 15);
		DifficultyFrame.getContentPane().add(expert_mines_labels);

		custom_minesField = new JTextField();
		custom_minesField.setColumns(10);
		custom_minesField.setBounds(411, 241, 55, 21);
		DifficultyFrame.getContentPane().add(custom_minesField);
		/* end*/

		DifficultyFrame.setVisible(true); //set the frame visible
		instructionframe(); //call instruction frame to show instructions
	}

	/* end of difficulty frame constructor */


	/*instruction frame GUI*/
	private void instructionframe() {
		InstructionFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); //close the current window without termincating the program
		InstructionFrame.setBounds(100, 100, 650, 400);//set the preferred boundary

		/* show instructions*/
		JLabel instruction1 = new JLabel("1. Click on a tile");

		JLabel instruction2 = new JLabel("2. The first tile always contains 0 mines in the surrounding 8 directions");

		JLabel instruction3 = new JLabel("3. The numbers shown indicate the number of mines in the sorrounding 8 directions of the current tile");

		JLabel instruction4 = new JLabel("4. If a mine is clicked, game ends and sound is played");

		JLabel instruction5 = new JLabel("5. If you uncover all the tiles without mines, game is won and sound is played");

		JLabel instruction6 = new JLabel("6. You can flag a tile for suspected mine, or put a question mark for unsureness");

		JLabel instruction7 = new JLabel("7. You can choose to save file or load file");

		JLabel instruction8 = new JLabel("8. The maximum size of grid is 30x30 and mines cannot exceed row*column");

		JLabel instruction9 = new JLabel("                                         !!!CAREFULLY READ THIS BEFORE YOU PLAY THE GAME!!!                         ");

		/* end*/

		/*set preferred layout*/
		GroupLayout groupLayout = new GroupLayout(InstructionFrame.getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGap(30)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(instruction9)
								.addComponent(instruction1, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
								.addComponent(instruction2)
								.addComponent(instruction3)
								.addComponent(instruction4)
								.addComponent(instruction5)
								.addComponent(instruction6)
								.addComponent(instruction7)
								.addComponent(instruction8))
						.addContainerGap(131, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
						.addContainerGap(22, Short.MAX_VALUE)
						.addComponent(instruction9)
						.addGap(18)
						.addComponent(instruction1)
						.addGap(18)
						.addComponent(instruction2)
						.addGap(18)
						.addComponent(instruction3)
						.addGap(18)
						.addComponent(instruction4)
						.addGap(18)
						.addComponent(instruction5)
						.addGap(18)
						.addComponent(instruction6)
						.addGap(18)
						.addComponent(instruction7)
						.addGap(18)
						.addComponent(instruction8)
						.addContainerGap())
				);

		/* end*/
		InstructionFrame.getContentPane().setLayout(groupLayout); //set frame layout
		InstructionFrame.setVisible(true); //set the frame visible

	}

	/* game frame GUI */
	public void gameframe() {

		GameFrame.setLayout(new BorderLayout()); //GUI
		reset.setIcon(new ImageIcon("face.png")); //set reset button icon
		command.setLayout(new FlowLayout(FlowLayout.CENTER,50, 0)); //set layout flow layout
		command.add(changeDifficulty); //add button
		changeDifficulty.addActionListener(new ActionListener(){ //if change difficulty is called
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameFrame.setVisible(false); //close the frame
				timer.cancel(); //cancel the timer first
				task.cancel(); //cancel the scheduled task
				deleteOriginalGrid(); //delete the original grid
				isFirstClick = true; //reset first click
				clear(); //clear the mines
				timerLabel.setText("time passed: 0 s"); //reset text
				secondsPassed = 0; //reset seconds passed
				timer = new Timer(); //declare a timer
				task = new TimerTask(){ //declare a timer task to keep track of time
					public void run(){ //method to run timer task
						secondsPassed ++; //increment seconds by 1 each time
						timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s"); //output the message
					}
				};
				DifficultyFrame.setVisible(true);
			}
		});

		command.add(saveFile); //add button to panel
		command.add(reset); 
		command.add(loadFile);
		command.add(instructionButton);
		panel.setLayout(new BorderLayout());
		panel.add(remainMines,BorderLayout.WEST); //set panel layout
		panel.add(timerLabel, BorderLayout.NORTH);
		GameFrame.add(panel,BorderLayout.SOUTH);
		GameFrame.add(command,BorderLayout.NORTH);
		reset.addActionListener(this); //add action listener to reset button
		saveFile.addActionListener(new ActionListener() { //if save file is clicked
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String filename = JOptionPane.showInputDialog("Name this file. no extension needed \nsame file name will result in file overwrite");				
				//use JFileChooser to get the path
				JFileChooser savefile = new JFileChooser(); //use jave tool to save file
				FileFilter filter = new FileNameExtensionFilter("text file", "txt"); //set file name filter
				savefile.setAcceptAllFileFilterUsed(false); //save file
				savefile.setFileFilter(filter); //save file 


				//set the file path of selected file
			//	savefile.setSelectedFile(new File(filename));

				//use buffered writer to write the file
				BufferedWriter writer;
				int sf = savefile.showSaveDialog(null);
				if(sf == JFileChooser.APPROVE_OPTION){
					try {
						//get selected file directory
						writer = new BufferedWriter(new FileWriter(savefile.getSelectedFile()));
						//write seconds passed to first line
						writer.write(Integer.toString(secondsPassed));
						writer.newLine();
						//write number of mines to the second line
						writer.write(Integer.toString(number_of_mines));
						writer.newLine();
						//write max row to the third line
						writer.write(Integer.toString(MAXrow));
						writer.newLine();
						//write MAXcol to the forth line
						writer.write(Integer.toString(MAXcol));
						writer.newLine();
						//write counts starting from the fifth line
						//use nested for loop to write the remaining values to file, starting from third line
						for (int row=0; row<MAXrow ; row++){
							for (int col=0; col<MAXcol; col++){
								writer.write(Integer.toString(counts[row][col])); //write to file. since it only accepts string, parse integer to string
								writer.newLine();
							}
						}
						//write states after counts
						for (int i=0; i<MAXrow; i++){
							for (int j=0; j<MAXcol; j++){
								writer.write(Integer.toString(states[i][j]));
								writer.newLine();

							}
						}
						writer.close(); //close the writer
						JOptionPane.showMessageDialog(null, "File has been saved","File Saved",JOptionPane.INFORMATION_MESSAGE); //output message
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(sf == JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "File save has been canceled");
				}
			}
		});
		loadFile.addActionListener(new ActionListener(){ //if load file is clicked

			@Override
			public void actionPerformed(ActionEvent e) {

				GameFrame.setVisible(false); //close the frame
				openFile(); //open file
			}

			public void loadgameprocess() {
				for (int row=0; row<MAXrow; row++){ 
					for (int col=0; col<MAXcol; col++){
						if (counts[row][col]==-1){ //if counts = -1, meaning this button was clicked
							buttons[row][col].setBackground(Color.lightGray); //set the button background to light gray
							displaySurrounding(row,col); //call display surrounding to check other tiles
						}
						if (states[row][col]==stateFlag){ //if the state was flag
							Image img=new ImageIcon("flag.png").getImage(); //set it flag
							ScaleButtonImage(buttons[row][col], img);	
							buttons[row][col].setBackground(Color.lightGray);
						}
						if (states[row][col]==stateQuestion){ //if the state was question
							Image img=new ImageIcon("questionmark.png").getImage(); //set the question mark
							ScaleButtonImage(buttons[row][col], img);
							buttons[row][col].setBackground(Color.lightGray);
						} 
						if (states[row][col]==clicked){ //if state was clicked
							buttons[row][col].setBackground(Color.lightGray); //set is light gray
							icon(row,col);
							buttons[row][col].removeActionListener(this); //disable the button

						}
					}
				}

				timer = new Timer(); //recreate timer object
				timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s");
				task = new TimerTask(){ //recreate task object
					public void run() {
						secondsPassed ++;
						timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s");
					}
				};
				startTimer();

			}

			public void openFile() {
				JFileChooser f = new JFileChooser(); //use jave tool to open file
				//FileFilter filter = new FileNameExtensionFilter("file"); //set file filter
				f.setAcceptAllFileFilterUsed(false); 
			//	f.setFileFilter(filter);

				int returnVal = f.showOpenDialog(null); 
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = f.getSelectedFile(); //get the selected file
					timer.cancel(); //cancel the timer first
					task.cancel(); //cancel the scheduled task
					deleteOriginalGrid(); //delete the original grid
					isFirstClick = false; //reset first click
					clear(); //clear the mines
					readfile(file); //read file
					gameframe(); //create game frame
					loadgameprocess(); //load the game process
					mineRemain(); //show the remaining mines

				}else if (returnVal==JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "cancelled");
					GameFrame.setVisible(true);

				}
			}



			public void readfile(File file) {
				try {
					FileReader f = new FileReader(file);
					Scanner input = new Scanner(f);
					int counter =4; //create a counter starting from index 4
					secondsPassed = Integer.parseInt(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0)); //get seconds passed from first line of file
					number_of_mines= Integer.parseInt(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(1)); //get number of mines from second line of file 
					//the third line stores the info of max row
					MAXrow = Integer.parseInt(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(2));
					//the forth line stores the info of max col
					MAXcol = Integer.parseInt(Files.readAllLines(Paths.get(file.getAbsolutePath())).get(3));
					//create an array to store the info from text file
					buttons = new JButton[MAXrow][MAXcol];
					counts = new int[MAXrow][MAXcol];
					states = new int[MAXrow][MAXcol];
					for (int row=0; row<MAXrow; row++){
						for (int col=0; col<MAXcol; col++){
							//read line from the fifth line
							String tempCount = Files.readAllLines(Paths.get(file.getAbsolutePath())).get(counter);
							//store the value in array
							counts[row][col] = Integer.parseInt(tempCount);
							String tempState = Files.readAllLines(Paths.get(file.getAbsolutePath())).get(MAXrow*MAXcol+counter); //since states counter is stored after count counter, use math to write everything in one nested loop to save memory
							states[row][col] = Integer.parseInt(tempState);
							counter = counter + 1; //increment counter by 1
						}
					}
					//close input
					input.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "not a valid file"); //if file doesn't matchm show message
					GameFrame.setVisible(true);
					System.exit(0); //terminate the program
					e.printStackTrace();
				}
			}

		});

		instructionButton.addActionListener(new ActionListener() { //if instruction button is clicked

			@Override
			public void actionPerformed(ActionEvent arg0) {
				instructionframe(); //call instruction frame
			}

		});


		grid.setLayout(new GridLayout(MAXrow, MAXcol)); //GUI

		/* create board */
		for (int row = 0; row < MAXrow; row++) {
			for (int col = 0; col < MAXcol; col++) {
				buttons[row][col] = new JButton();
				buttons[row][col].addActionListener(this); //add actionlisteners to button
				buttons[row][col].addMouseListener(this); //add mouselisteners to button
				grid.add(buttons[row][col]); //add button to grid
			}
		}
		/* done */

		GameFrame.add(grid, BorderLayout.CENTER); //GUI
		GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //GUI
		GameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		GameFrame.setVisible(true); //set frame visible
		saveFile.setEnabled(false); //disable save file button unless a move is made


	}
	/* end of game frame GUI */

	//this method displays the number of mines in the surrounding 8 tiles of the current tile
	public void calculations() {
		for (int row = 0; row < MAXrow; row++) { //for loop to count all the rows
			for (int col = 0; col < MAXcol; col++) { //for loop to count all the columns
				if (counts[row][col] != mine) { // if the current tile is not a mine
					check_8_dimension(row, col); // calculate how many mines are in 8 surrounding buttons
				}
			}
		}
	}

	//this method looks for the surrounding 8 tiles
	public void check_8_dimension(int row, int col) {
		int counter = 0; //declare and initialize a counter
		for (int start_row = row - 1; start_row <= row + 1; start_row++) { // check 8 directions
			for (int start_col = col - 1; start_col <= col + 1; start_col++) { // check 8 directions
				if (start_row >= 0 && start_col >= 0 && start_row < MAXrow && start_col < MAXcol) { //if indexes are within the range
					if (counts[start_row][start_col] == mine) { // if a mine is detected
						counter = counter + 1; // add 1 to count
					}
				}
			}
		}
		counts[row][col] = counter; // store the count
	}

	public static void main(String[] args) {
		new MineSweeper(); //display GUI
	}

	public void startTimer() {
		timer.scheduleAtFixedRate(task, 1000,1000); //schedule timer at fix rate (1s)
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(reset)) { //if reset button is clicked
			timer.cancel(); //cancel the timer first
			task.cancel(); //cancel the scheduled task
			deleteOriginalGrid(); //delete original grid
			resizeMethod(); //resize the grid
			isFirstClick = true; //reset first click
			clear(); //clear the counts and states
			mineRemain(); //show how many mines remain
			secondsPassed = 0; //reset variable
			timer = new Timer(); //recreate timer object
			timerLabel.setText("time passed: 0 s");
			task = new TimerTask(){ //recreate task object
				public void run() {
					secondsPassed ++;
					timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s");
				}
			};

		} else{
			for (int row = 0; row < MAXrow; row++) {
				for (int col = 0; col < MAXcol; col++) {
					if (buttons[row][col] == event.getSource()&& states[row][col]!=stateFlag) { // if a button is clicked and it's not in flag state
						if (saveFile.isEnabled()==false){ //if user makes a move, and the save file button is disabled 
							saveFile.setEnabled(true);  //enable the button
						}

						if (isFirstClick){ //if it is first click
							firstClick(row,col); //call the first click method, generate the mines
							calculations(); //calculate how many mines are in surrounding
							check(row,col); // check if there are 0's
							checkwin(); //check if the user is won
							saveFile.setEnabled(true); //enable the save file button once the user first clicks
							isFirstClick = false; //set isFirstClick to false to indicate that it is not first click anymore
							timer = new Timer(); //recreate timer object
							task = new TimerTask(){ //recreate task object
								public void run() {
									secondsPassed ++;
									timerLabel.setText("time passed: " + Integer.toString(secondsPassed) + " s");
								}
							};
							startTimer(); //start the timer
						}else {
							if (counts[row][col] != 0 && counts[row][col] != mine) {
								states[row][col] = clicked; //indicates that this tile is clicked
								icon(row,col);
								checkwin(); //check win
							}
							if (counts[row][col] == 0) {
								states[row][col]=clicked; //indicates that this tile is clicked
								counts[row][col]=-1; //set counts to -1 to prevent stack overflow
								check(row, col); // row and col indicates the location of 0
								checkwin(); //check win
							}
							if (counts[row][col]==-1){ //if the value of tile is -1, meaning there is no 0's in the surrounding
								states[row][col] = clicked; //indicates that this tile is clicked
								buttons[row][col].setIcon(null); //set the icon to null
								buttons[row][col].setBackground(Color.lightGray); //set the background to gray to indicate that this tile is not available to be clicked
								checkwin(); //check win
							}
							if (counts[row][col] == mine) { //if the user clicks on a mine
								icon(row,col); 
								mineClicked(); //show the game is lost
							}
						}
					}
				}
			}
		}
	}

	public void firstClick(int row, int col) { 
		int mine_row;
		int mine_col;
		int number =0;
		while(number<number_of_mines){
			mine_row = rand.nextInt(MAXrow-1) + 0; // create random value for x
			mine_col = rand.nextInt(MAXcol-1) + 0; // create random value for y
			if (mine_row!=(row-1) && mine_row != row && mine_row != row+1 && mine_col!=(col-1) && mine_col != (col) && mine_col != col+1 ){ //purposely made this so first tile clicked is always 0
				if (counts[mine_row][mine_col]!=mine){ //if current tile is not a mine
					counts[mine_row][mine_col] = mine; // put a mine here
					number = number + 1; //increment number by 1
				}
			}

		}
	}

	public void clear() {
		for (int row=0; row<MAXrow; row++){
			for (int col=0; col<MAXcol; col++){
				counts[row][col] = 0; //reset counts array
				states[row][col]=stateNone; //reset states array
				buttons[row][col].setBackground(null); //set buttons back to original
			}
		}
	}
	public void disableButtons(){
		for (int row=0; row<MAXrow;row++){
			for (int col=0; col<MAXcol; col++){
				buttons[row][col].removeActionListener(this); //remove action listener from all buttons
			}
		}
	}
	public void checkwin() {
		int count = 0;
		for (int row=0; row<MAXrow; row++){
			for (int col=0; col<MAXcol; col++){
				if (states[row][col]!=stateFlag){ //if button is not a flag
					if (buttons[row][col].getBackground()==Color.lightGray){ //if button background is light gray
						count = count+ 1; //add 1 to count
					}
				}

			}
		}

		if(count == (MAXrow*MAXcol-number_of_mines)){ //if count = total tiles - mines , so the user wins
			//reveal any unflagged mines
			for (int row=0; row<MAXrow; row++){
				for (int col=0; col<MAXcol; col++){
					if (counts[row][col]==mine && states[row][col]==unclick){
						Image img=new ImageIcon("flag.png").getImage();
						ScaleButtonImage(buttons[row][col], img);
						buttons[row][col].setBackground(Color.lightGray);
						states[row][col]=stateFlag;
					}
				}
			}
			timer.cancel(); //close timer
			task.cancel(); 
			cheerSound(); //play music
			saveFile.setEnabled(false);
			JOptionPane.showMessageDialog(GameFrame, "congratulations! you win \n time passed : " + secondsPassed + " s"); //show message
			disableButtons(); //disable buttons
		}
	}

	public void cheerSound() {
		try {
			// Open an audio input stream.
			File soundFile = new File("cheer.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void mineClicked() {
		for (int row = 0; row < MAXrow; row++) {
			for (int col = 0; col < MAXcol; col++) {
				if (counts[row][col] == mine && states[row][col]!=stateFlag) { //if tile is mine and not flag
					Image img=new ImageIcon("mine.png").getImage(); //show mine
					buttons[row][col].setBackground(Color.lightGray);
					ScaleButtonImage(buttons[row][col], img);
				}
				if (states[row][col]==stateFlag && counts[row][col]!=mine){ //if the user puts a flag on a button but it's not a mine

					buttons[row][col].setBackground(null); //reset button
					buttons[row][col].setIcon(null); //reset icon

				}
			}
		}
		timer.cancel(); //close the timer if the game is lost
		task.cancel(); //close the task if the timer is lost
		disableButtons(); //disable all the buttons
		gameoverSound(); //play the game over sound
		saveFile.setEnabled(false);

		JOptionPane.showMessageDialog(GameFrame, "mine clicked. Game Over \nmines discovered: " + checkCorrect() + "\ntime passed : " + secondsPassed + " s"); //output the lost message
	}

	public void gameoverSound() {
		try {
			// Open an audio input stream.
			File soundFile = new File("gameover.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public int checkCorrect() {
		int count= 0;
		for (int row=0; row<MAXrow; row++){
			for (int col=0; col<MAXcol; col++){
				if (counts[row][col]==mine && states[row][col]==stateFlag){ //if tile is mine and it's flagged
					count = count + 1; //add 1 to count
				}
			}
		}
		return count; //return count
	}

	public void resizeMethod() {
		grid.setLayout(new GridLayout(MAXrow, MAXcol));
		for (int row = 0; row < MAXrow; row++) {
			for (int col = 0; col < MAXcol; col++) {
				buttons[row][col] = new JButton(); //create buttons
				buttons[row][col].addActionListener(this); //aadd actionlistener to buttons
				buttons[row][col].addMouseListener(this); //add mouselistener to buttons
				counts[row][col] = 0; // reset counts array
				states[row][col]=stateNone; //reset states array
				grid.add(buttons[row][col]);
			}
		}
		GameFrame.add(grid, BorderLayout.CENTER);
		GameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameFrame.setVisible(true);
	}

	public void deleteOriginalGrid() {
		for (int row = 0; row < MAXrow; row++) {
			for (int col = 0; col < MAXcol; col++) {
				grid.remove(buttons[row][col]); //remove buttons from grid
			}
		}
	}

	public void check(int row, int col) { // check other 0's
		if (check_0(row, col) == false) {
			displaySurrounding(row, col); //display the surrounding 8 areas
			return;
		} else {
			for (int start_row = row - 1; start_row <= row + 1; start_row++) { // check 8 directions
				for (int start_col = col - 1; start_col <= col + 1; start_col++) { // check 8 directions
					if (start_row >= 0 && start_col >= 0 && start_row < MAXrow && start_col < MAXcol ) { //if it's within boundary
						if (counts[start_row][start_col] == 0) { //if current tile is 0
							displaySurrounding(start_row, start_col); //display surrounding
							counts[start_row][start_col] = -1; //change the value of counts to prevent stackoverflow error
							check(start_row, start_col); //recursively call this method
						}
					}
				}
			}
		}
	}

	// checks if there are still 0's in the surrounding directions
	public boolean check_0(int row, int col) {
		isZero = false; // set it to false every time the method is called
		for (int start_row = row - 1; start_row <= row + 1; start_row++) { // check directions          
			for (int start_col = col - 1; start_col <= col + 1; start_col++) { // check 8 directions
				if (start_row >= 0 && start_col >= 0 && start_row < MAXrow && start_col < MAXcol) {
					if (counts[start_row][start_col] == 0) { //if a 0 is detected
						isZero = true; //set isZero to true
					}
				}

			}
		}
		return isZero; //return the value of isZero
	}

	public void ScaleButtonImage(javax.swing.JButton Button, Image imageIcon) {
		double Width  = imageIcon.getWidth(Button); //get button width
		double Height = imageIcon.getHeight(Button); //get button height
		double xScale = Button.getWidth() / Width; //get x scale
		double yScale = Button.getHeight() / Height; //get y scale
		double Scale;
		if(unclick==0){
			Scale = Math.min(xScale, yScale);}//ToFit
		else{
			Scale = Math.max(xScale, yScale);//ToFill
			unclick=0;
		} 
		java.awt.Image scaled = imageIcon.getScaledInstance((int)(Scale * Width), (int)(Scale * Height), java.awt.Image.SCALE_SMOOTH);
		Button.setIcon(new javax.swing.ImageIcon(scaled)); //set the image icon
	}

	public void icon(int row, int col){
		buttons[row][col].setBackground(Color.lightGray);
		if(counts[row][col]==1){
			Image img=new ImageIcon("one.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==2){
			Image img=new ImageIcon("two.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==3){
			Image img=new ImageIcon("three.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==4){
			Image img=new ImageIcon("four.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==5){
			Image img=new ImageIcon("five.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==6){
			Image img=new ImageIcon("six.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==7){
			Image img=new ImageIcon("seven.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==8){
			Image img=new ImageIcon("eight.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}
		else if(counts[row][col]==mine){
			Image img=new ImageIcon("mine.png").getImage();
			ScaleButtonImage(buttons[row][col], img);
		}


	}

	public void displaySurrounding(int row, int col) {

		for (int start_row = row - 1; start_row <= row + 1; start_row++) {
			for (int start_col = col - 1; start_col <= col + 1; start_col++) {
				if (start_row >= 0 && start_col >= 0 && start_row < MAXrow && start_col < MAXcol) { //if it's within boundary 
					if (states[start_row][start_col]!=stateFlag){ //if current tile is not in flag state
						states[start_row][start_col]=clicked; //set the states to clicked
						icon(start_row,start_col); //set icon
					}
				}
			}
		}

	}

	public void mineRemain() {
		int count = 0;
		for (int row=0; row<MAXrow; row++){
			for (int col=0; col<MAXcol; col++){
				if (states[row][col]==stateFlag){ //if it's flagged
					count = count + 1; //add 1 to count
				}
			}
		}
		remainMines.setText("mines remain: " + (number_of_mines-count)); //show text
	}

	/* mouse listeners method */
	@Override
	public void mouseClicked(MouseEvent e) {
		for (int row=0; row<MAXrow; row++){
			for (int col=0; col<MAXcol; col++){
				if (buttons[row][col]==(JButton) e.getSource()){
					if(e.getButton()==3){ //if the user right clicks
						if (states[row][col] == stateNone){ //change the state to none
							Image img=new ImageIcon("flag.png").getImage();
							ScaleButtonImage(buttons[row][col], img);
							buttons[row][col].setBackground(Color.lightGray);
							states[row][col]=stateFlag;
							mineRemain();
						}
						else if (states[row][col]==stateFlag){ //if state was originally flag

							Image img=new ImageIcon("questionmark.png").getImage();
							ScaleButtonImage(buttons[row][col], img);
							buttons[row][col].setBackground(Color.lightGray);
							states[row][col]= stateQuestion; //set the state to question mark
							mineRemain();
						}
						else if (states[row][col]==stateQuestion){ //if the state was originally question mark
							buttons[row][col].setIcon(null);
							buttons[row][col].setBackground(null);
							states[row][col]=stateNone; //set the state to none
							mineRemain();
						}
					}

				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}