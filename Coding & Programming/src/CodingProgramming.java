import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//READ THESE
//Make List of schools proper
//Figure out view and stuff
//make fileWrite run when closed

public class CodingProgramming extends JFrame implements ActionListener {
	static final int LIMIT = 89489404;
	
	//add JButtons here
	JPanel buttons;
	JButton enter;
	JButton view;
	JButton edit;
	JPanel nameListPanel;
	JPanel schoolListPanel;
	JPanel gradeListPanel;
	JComboBox<?> sortBox;
	JList nameListList;
	int listValue;
	String[] nameListSort;		
	//Name, School, Grade, # of Books checked out, Books checked out
	String[][] userList = fileRead(0);
	String[][] schoolList = fileRead(1);
	String[][] bookList = fileRead(2);
	//JList<Object> nameListList;
	//JList<Object> schoolListList;
	//JList<Object> gradeListList;
	
	public static void main(String[] args) {
		new CodingProgramming();
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public CodingProgramming() {
		
		//This is for TEST purposes
		//REMOVE WHEN DONE
		/*for(int x = 0; x < 4; x++) {
			for(int y = 0; y < userList[x].length; y++) {
				System.out.println(userList[x][y]);
			}
			System.out.println("");
		}*/
		
		//Creates mainFrame(window)
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle("E-Book Library");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = (JPanel)mainFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		nameListPanel = new JPanel();
		JLabel nameListLabel = new JLabel("");
		nameListPanel.add(nameListLabel);
		nameListSort = new String[userList.length];
		for(int x = 0; x < userList.length; x++) {
			if(userList[x][0] != null)
				nameListSort[x] = userList[x][0];
			else
				x++;
		}
		nameListList = new JList(nameListSort);
		//listValue = (Integer) null;
		nameListList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if(event.getValueIsAdjusting())
					listValue = event.getLastIndex();
				return;
				//System.out.println("Selected from " + event.getFirstIndex() + " to " + event.getLastIndex());
			}
		});
		nameListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane nameListScroll = new JScrollPane(nameListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		nameListPanel.add(nameListScroll);
		mainFrame.add(nameListPanel);
		nameListPanel.setVisible(true);
		
		schoolListPanel = new JPanel();
		JLabel schoolListLabel = new JLabel("");
		schoolListPanel.add(schoolListLabel);
		String[] schoolListSort = new String[schoolList.length];
		String[] temp = new String[schoolList.length];
		for(int x = 0; x < schoolList.length; x++) {
			temp[x] = schoolList[x][1];
		}
		temp = removeDuplicates(temp);
		for(int x = 0; x < temp.length; x++) {
			schoolListSort[x] = temp[x];
		}
		//for(int x = 0; x < 5; x++)
			//System.out.println(schoolListSort[x]);
		JList schoolListList = new JList(schoolListSort);
		schoolListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane schoolListScroll = new JScrollPane(schoolListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		schoolListPanel.add(schoolListScroll);
		mainFrame.add(schoolListPanel);
		schoolListPanel.setVisible(false);
		
		gradeListPanel = new JPanel();
		JLabel gradeListLabel = new JLabel("");
		gradeListPanel.add(gradeListLabel);
		String[] gradeListSort = new String[userList.length];
		for(int x = 0; x < userList.length; x++) {
			gradeListSort[x] = userList[x][2];
		}
		JList gradeListList = new JList(gradeListSort);
		gradeListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane gradeListScroll = new JScrollPane(gradeListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		gradeListPanel.add(gradeListScroll);
		mainFrame.add(gradeListPanel);
		gradeListPanel.setVisible(false);
		
		//Allows user to sort the list by Name, School, or Grade
		JPanel sortListPanel = new JPanel();
		JLabel sortList = new JLabel("Sort By");
		sortListPanel.add(sortList);
		String[] sort = {"Name", "School", "Grade"};
		sortBox = new JComboBox(sort);
		sortBox.addActionListener(this);
		
		sortListPanel.add(sortBox);
		mainFrame.add(sortListPanel);
		
		buttons = new JPanel();
		enter = new JButton("Enter");
		view = new JButton("View");
		edit = new JButton("Edit");
		//buttonGroup.add(enter);
		//buttonGroup.add(view);
		//buttonGroup.add(edit);
		buttons.add(enter);
		buttons.add(view);
		buttons.add(edit);
		mainFrame.add(buttons);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		enter.addActionListener(this);
		view.addActionListener(this);
		edit.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
		@SuppressWarnings("null")
		int listSelect = -1;
		if(event.getSource() == sortBox) {
			JComboBox cb = (JComboBox)event.getSource();
			
			int typeSort = (int)cb.getSelectedIndex();
			if(typeSort == 0) {
				//Make these
				schoolListPanel.setVisible(false);
				gradeListPanel.setVisible(false);
				nameListPanel.setVisible(true);
				listSelect = 0;
			}	
			else if(typeSort == 1) {
				nameListPanel.setVisible(false);
				gradeListPanel.setVisible(false);
				schoolListPanel.setVisible(true);
				listSelect = 1;
			}
			else if(typeSort == 2) {
				nameListPanel.setVisible(false);
				schoolListPanel.setVisible(false);
				gradeListPanel.setVisible(true);
				listSelect = 2;
			}
			//System.out.println("Selected from " + event.getStateChange());
		}
		else if(event.getSource() == view){
			if(listSelect == 0) {
				String[] sum = {""};
				String summary = "";
				try {
					sum = userList[listValue];
					summary = "Name: " + sum[0]
							+ "\nSchool: " + sum[1]
							+ "\nGrade: " + sum[2]
							+ "\n# of Books checked: " + sum[3]
							+ "\nSerial of Books: " + sum[4];
					JOptionPane.showMessageDialog(null, summary);
				}
				catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You haven't selected anything", "Error", JOptionPane.ERROR_MESSAGE);
				}
				//Name, School, Grade, # of Books checked out, Books checked out
			}
			/*else if(listSelect == 1) {
				schoolListPanel.setVisible(false);
				JPanel tempPanel = new JPanel();
				JLabel tempLabel = new JLabel();
				//FIX THE ENTIRE SYSTEM
				nameListPanel.setVisible(true);
			}*/
		}
		else if(event.getSource() == edit) {
			
		}
		else if(event.getSource() == enter) {
			
		}
	}
	
	public static void fileWrite(String[][] list) {
		// The name of the file to open.
        String fileName = "list.txt";

        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            for(int x = 0; x < list.length; x++) {
            	for(int y = 0; y < list[x].length; y++) {
            		bufferedWriter.write(list[x][y] + ", ");
            	}
            	bufferedWriter.newLine();
            }
            //bufferedWriter.write("Hello there,");
            //bufferedWriter.write(" here is some text.");
            //bufferedWriter.newLine();
            //bufferedWriter.write("We are writing");
            //bufferedWriter.write(" the text to the file.");

            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
        	System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
	}
	
	public String[][] fileRead(int opt) {
		// The name of the file to open.
        String fileName = "list.txt";
        String[][] res = null;

        try {
            // Use this for reading the data.
            byte[] buffer = new byte[1000];

            FileInputStream inputStream = new FileInputStream(fileName);
            
            String bufRes = "";
            // read fills buffer with data and returns
            // the number of bytes read (which of course
            // may be less than the buffer size, but
            // it will never be more).
            int total = 0;
            int nRead = 0;
            while((nRead = inputStream.read(buffer)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
                bufRes += (new String(buffer));
                total += nRead;
            }
            
            res = new String[total][5];
            
            if(opt == 0) {
            	String temp = "";
            	int arRow = 0;
            	int arCol = 0;
            	int mark = 0;
            	for(int x = 0; x < bufRes.length(); x++) {
            		if(mark != 0) {
            			temp += bufRes.charAt(x);
            			if(bufRes.charAt(x) == ']') {
            				mark = 0;
            				if(temp.equals("[USERS]")) {
                        		temp = "";
                        		int parMark = 0;
                        		for(int y = (x + 1); y < bufRes.length(); y++) {
                        			if(parMark == 0) {
                        				if(bufRes.charAt(y) == '{') {
                        					y++;
                        					parMark = 1;
                        				}
                        			}
                        			if(parMark != 0) {
                        				if(bufRes.charAt(y) == '}') {
                        					parMark = 0;
                        					y = bufRes.length() + 1;
                        				}
                        				else if(bufRes.charAt(y) == ',') {
                        					res[arRow][arCol] = temp;
                        					if(arCol == 4)
                        						arCol = 0;
                        					else
                        						arCol++;
                        					temp = "";
                        					y++; 
                        				}
                        				else if(bufRes.charAt(y) == '\n') {
                        					res[arRow][arCol] = temp;
                        					arRow++;
                        					arCol = 0;
                        					temp = "";
                        				}
                        				else {
                        					temp += bufRes.charAt(y);
                        				}
                        			}
                        		}
                        	}
            				x = bufRes.length() +1;
            			}
            		}
            		else if(bufRes.charAt(x) == '[') {
            			mark = 1;
            			temp += bufRes.charAt(x);
            		}
                	//System.out.println(temp);
            	}
            	//System.out.println(temp);
            	//Last line is put in list variable
            	res[arRow][arCol] = temp;
            	temp = "";
            }
            else if(opt == 1) {
            	String temp = "";
            	int arRow = 0;
            	int arCol = 0;
            	int mark = 0;
            	for(int x = 0; x < bufRes.length(); x++) {
            		if(mark != 0) {
            			temp += bufRes.charAt(x);
            			if(bufRes.charAt(x) == ']') {
            				mark = 0;
            				if(temp.equals("[SCHOOLS]")) {
                        		temp = "";
                        		int parMark = 0;
                        		for(int y = (x + 1); y < bufRes.length(); y++) {
                        			if(parMark == 0) {
                        				if(bufRes.charAt(y) == '{') {
                        					y++;
                        					parMark = 1;
                        				}
                        			}
                        			if(parMark != 0) {
                        				if(bufRes.charAt(y) == '}') {
                        					parMark = 0;
                        					y = bufRes.length() + 1;
                        				}
                        				else if(bufRes.charAt(y) == ',') {
                        					res[arRow][arCol] = temp;
                        					if(arCol == 4)
                        						arCol = 0;
                        					else
                        						arCol++;
                        					temp = "";
                        					y++; 
                        				}
                        				else if(bufRes.charAt(y) == '\n') {
                        					res[arRow][arCol] = temp;
                        					arRow++;
                        					arCol = 0;
                        					temp = "";
                        				}
                        				else {
                        					temp += bufRes.charAt(y);
                        				}
                        			}
                        		}
                        	}
            				x = bufRes.length() +1;
            			}
            		}
            		else if(bufRes.charAt(x) == '[') {
            			mark = 1;
            			temp += bufRes.charAt(x);
            		}
                	//System.out.println(temp);
            	}
            	//System.out.println(temp);
            	//Last line is put in list variable
            	res[arRow][arCol] = temp;
            	temp = "";
            }
            // Always close files.
            inputStream.close();        

            //System.out.println("Read " + total + " bytes");
        }
        catch(FileNotFoundException ex) {
            System.out.print("Unable to open file '" + fileName + "'");
            return res;
        }
        catch(IOException ex) {
            System.out.print("Error reading file '" + fileName + "'");  
            return res;
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        //This is required or the IDE will freak, saying that the function doesn't have return statement
		return res;
	}
	
	public String[] removeDuplicates(String[] A) {
		if (A.length < 2)
			return A;
	 
		int j = 0;
		int i = 1;
		try {
			while (i < A.length) {
				if (A[i].equals(A[j])) {
					i++;
				} else {
					j++;
					A[j] = A[i];
					i++;
				}
			}
		}
		catch(NullPointerException e) {
			i++;
		}
	 
		String[] B = Arrays.copyOf(A, j + 1);
	 
		return B;
	}
}