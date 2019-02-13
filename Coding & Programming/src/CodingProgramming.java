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

public class CodingProgramming extends JFrame implements ActionListener {
	//static final int LIMIT = (2147483647 - 8) / 5;
	JFrame mainFrame;
	JPanel mainPanel;
	//add JButtons here
	JPanel buttons;
	JButton enter;
	JButton view;
	JButton edit;
	JPanel searchPanel;
	JTextField searchTextField;
	String searchValue;
	JOptionPane editOptionPane;
	JButton delete;
	JPanel nameListPanel;
	JPanel checkedoutListPanel;
	JPanel overdueListPanel;
	JComboBox<String> sortBox;
	JList<String> nameListList;
	JList<String> checkedoutListList;
	JList<String> overdueListList;
	DefaultListModel<String> nameDLM;
	DefaultListModel<String> checkedoutDLM;
	DefaultListModel<String> overdueDLM;
	int nameListValue;
	int checkedoutListValue;
	int overdueListValue;
	String[] nameListSort;
	//First Name, Last Name
	String[][] userList = fileRead(0);
	//First Name, Last Name, Serial of Book, When It Was Checked Out, How Long Till It's Overdue
	String[][] checkedoutList = fileRead(1);
	//First Name, Last Name, Serial of Book, How Long It's Been Overdue
	String[][] overdueList = fileRead(2);
	//Serial Number, Book Name, Author, Number in Stock, Total Inventory
	String[][] bookList = fileRead(3);
	boolean enterCheck = false;
	
	public static void main(String[] args) {
		new CodingProgramming();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CodingProgramming() {
		
		//This is for TEST purposes
		//REMOVE WHEN DONE
		/*for(int x = 0; x < 15; x++) {
			if(userList[x][0] != null)
				System.out.println(x + 1);
			for(int y = 0; y < userList[x].length; y++) {
				if(userList[x][y] != null)
					System.out.println(userList[x][y]);
			}
			if(userList[x][0] != null)
				System.out.println();
		}*/
		
		//Creates mainFrame(window)
		mainFrame = new JFrame();
		mainFrame.setTitle("E-Book Library");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainPanel = (JPanel)mainFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		mainFrame.addWindowListener(new WindowAdapter() {
			//I skipped unused callbacks for readability
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(enterCheck) {
					int options = JOptionPane.showConfirmDialog(mainFrame, "Save Before Quitting?");
					if(options == JOptionPane.YES_OPTION){
						fileWrite(userList, checkedoutList, overdueList, bookList);
						mainFrame.setVisible(false);
						mainFrame.dispose();
					}
					else if(options == JOptionPane.NO_OPTION) {
						mainFrame.setVisible(false);
						mainFrame.dispose();
					}
				}
				else {
					mainFrame.setVisible(false);
					mainFrame.dispose();
				}
			}
		});
		
		searchPanel = new JPanel();
		searchTextField = new JTextField();
		searchTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JTextField text = (JTextField) event.getSource();
				searchValue = text.getText();
			}
		});
		searchPanel.add(searchTextField);
		mainFrame.add(searchPanel);
		
		nameDLM = new DefaultListModel();
		
		nameListPanel = new JPanel();
		JLabel nameListLabel = new JLabel("");
		nameListPanel.add(nameListLabel);
		int mark = 0;
		for(int x = 0; x < userList.length; x++) {
			if(userList[x][0] != null)
				mark++;
		}
		nameListSort = new String[mark];
		for(int x = 0; x < nameListSort.length; x++) {
			if(userList[x][0] != null)
				nameListSort[x] = userList[x][0];
		}
		nameListList = new JList(nameDLM);
		for(int x = 0; x < nameListSort.length; x++)
			nameDLM.addElement(nameListSort[x]);
		//listValue = (Integer) null;
		nameListList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				JList list = (JList) event.getSource();
				nameListValue = list.getSelectedIndex();
			}
		});
		nameListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane nameListScroll = new JScrollPane(nameListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		nameListPanel.add(nameListScroll);
		mainFrame.add(nameListPanel);
		nameListPanel.setVisible(true);
		
		//Add checkedoutDLM
		checkedoutDLM = new DefaultListModel();
		
		checkedoutListPanel = new JPanel();
		JLabel checkedoutListLabel = new JLabel("");
		checkedoutListPanel.add(checkedoutListLabel);
		String[] checkedoutListSort = new String[checkedoutList.length];
		String[] temp = new String[checkedoutList.length];
		for(int x = 0; x < checkedoutList.length; x++) {
			temp[x] = checkedoutList[x][0];
		}
		temp = removeDuplicates(temp);
		for(int x = 0; x < temp.length; x++) {
			if(temp[x] != null && temp[x] != "")
				checkedoutListSort[x] = temp[x];
		}
		checkedoutListList = new JList(checkedoutDLM);
		for(int x = 0; x < checkedoutListSort.length; x++) {
			if(checkedoutListSort[x] != null)
				checkedoutDLM.addElement(checkedoutListSort[x]);
		}
		checkedoutListList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				JList list = (JList) event.getSource();
				checkedoutListValue = list.getSelectedIndex();
			}
		});
		checkedoutListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane checkedoutListScroll = new JScrollPane(checkedoutListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		checkedoutListPanel.add(checkedoutListScroll);
		mainFrame.add(checkedoutListPanel);
		checkedoutListPanel.setVisible(false);
		
		overdueDLM = new DefaultListModel();
		
		overdueListPanel = new JPanel();
		JLabel overdueListLabel = new JLabel("");
		overdueListPanel.add(overdueListLabel);
		overdueListList = new JList<String>(overdueDLM);
		for(int x = 0; x < overdueList.length; x++) {
			if(overdueList[x][0] != null) {
				overdueDLM.addElement(overdueList[x][0]);
			}
		}
		overdueListList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				JList list = (JList) event.getSource();
				overdueListValue = list.getSelectedIndex();
				
			}
		});
		overdueListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane overdueListScroll = new JScrollPane(overdueListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		overdueListPanel.add(overdueListScroll);
		mainFrame.add(overdueListPanel);
		overdueListPanel.setVisible(false);
		
		//Allows user to sort the list by Name, checkedout, or overdue
		JPanel sortListPanel = new JPanel();
		JLabel sortList = new JLabel("Sort By");
		sortListPanel.add(sortList);
		String[] sort = {"Name", "School", "Grade"};
		sortBox = new JComboBox<String>(sort);
		sortBox.addActionListener(this);
		
		sortListPanel.add(sortBox);
		mainFrame.add(sortListPanel);
		
		buttons = new JPanel();
		enter = new JButton("Enter");
		view = new JButton("View");
		edit = new JButton("Edit");
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

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void actionPerformed(ActionEvent event) {
		Object control = event.getSource();
		if(control == sortBox) {
			JComboBox<?> cb = (JComboBox<?>)event.getSource();
			
			int typeSort = (int)cb.getSelectedIndex();
			if(typeSort == 0) {
				//Make these
				checkedoutListPanel.setVisible(false);
				overdueListPanel.setVisible(false);
				nameListPanel.setVisible(true);
			}	
			else if(typeSort == 1) {
				nameListPanel.setVisible(false);
				overdueListPanel.setVisible(false);
				checkedoutListPanel.setVisible(true);
			}
			else if(typeSort == 2) {
				nameListPanel.setVisible(false);
				checkedoutListPanel.setVisible(false);
				overdueListPanel.setVisible(true);
			}
			//System.out.println("Selected from " + event.getStateChange());
		}
		else if(control == view){
			if(nameListPanel.isVisible()) {
				String[] sum = {""};
				String summary = "";
				try {
					sum = userList[nameListValue];
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
			else if(checkedoutListPanel.isVisible()) {
				/*JOptionPane schoolUserOptionPane = new JOptionPane();
				//JList schoolUserListList;
				schoolUserView = new JButton("View");
				schoolUserEdit = new JButton("Edit");
				
				schoolUserList = new String[userList.length];
				for(int x = 0; x < userList.length; x++) {
					if(userList[x][0] != null) {
						if(userList[x][1].equals(schoolList[schoolListValue][0]))
							schoolUserList[x] = userList[x][0];
					}
				}
				
				for(int x = 0; x < schoolUserList.length; x++) {
					if((x + 1) < schoolUserList.length) {
						if(schoolUserList[x] == null && schoolUserList[x + 1] != null) {
							schoolUserList[x] = schoolUserList[x + 1];
							schoolUserList[x + 1] = null;
							x = -1;
						}
					}
				}
				
				int mark = 0;
				for(int x = 0; x < schoolUserList.length; x++) {
					if(schoolUserList[x] != null)
						mark++;
				}
				String[] schoolUserListSort = new String[mark];
				for(int x = 0; x < schoolUserListSort.length; x++) {
					if(schoolUserList[x] != null)
						schoolUserListSort[x] = schoolUserList[x];
				}
				
				schoolUserListList = new JList(schoolUserListSort);
				schoolUserListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				schoolUserListList.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent event) {
						JList list = (JList) event.getSource();
						schoolUserListValue = list.getSelectedIndex();
						
					}
				});
				JScrollPane schoolUserListScroll = new JScrollPane(schoolUserListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				schoolUserView.addActionListener(this);
				schoolUserEdit.addActionListener(this);
				
				Object[] message = {
						schoolUserListList,
						schoolUserListScroll,
						schoolUserView,
						schoolUserEdit
				};
				JOptionPane.showConfirmDialog(null, message, schoolList[schoolListValue][0], JOptionPane.OK_CANCEL_OPTION);
				
				//Make the view and delete and idk i'm tired man*/
			}
		}
		else if(control == edit) {
			/*enterCheck = true;
			editOptionPane = new JOptionPane();
			if(nameListPanel.isVisible()) {
				String name = null;
				String school = null;
				String grade = null;
				String num = null;
				String cereal = null;
				JTextField nameTextField = new JTextField(userList[nameListValue][0]);
				
				//schoolComboBox array setup
				int mark = 0;
				for(int x = 0; x < schoolList.length; x++) {
					if(schoolList[x][0] != null)
						mark++;
				}
				String[] schoolListSort = new String[mark];
				for(int x = 0; x < schoolListSort.length; x++) {
					if(schoolList[x][0] != null)
						schoolListSort[x] = schoolList[x][0];
				}
				JComboBox schoolComboBox = new JComboBox(schoolListSort);
				mark = 0;
				for(int x = 0; x < schoolListSort.length; x++) {
					mark++;
					if(userList[nameListValue][1].equals(schoolListSort[x])) {
						mark--;
						x = userList.length + 1;
					}
				}
				schoolComboBox.setSelectedIndex(mark);
				
				//gradeComboBox array setup
				mark = 0;
				for(int x = 0; x < gradeList.length; x++) {
					if(gradeList[x][0] != null)
						mark++;
				}
				String[] gradeListSort = new String[mark];
				for(int x = 0; x < gradeListSort.length; x++) {
					if(gradeListSort != null)
						gradeListSort[x] = gradeList[x][0];
				}
				JComboBox gradeComboBox = new JComboBox(gradeListSort);
				mark = 0;
				for(int x = 0; x < gradeListSort.length; x++) {
					mark++;
					if(userList[nameListValue][2].equals(gradeListSort[x])) {
						mark--;
						x = userList.length + 1;
					}
				}
				gradeComboBox.setSelectedIndex(mark);
				
				JTextField numTextField = new JTextField(userList[nameListValue][3]);
				JTextField cerealTextField = new JTextField(userList[nameListValue][4]);
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
					"Name:", nameTextField,
			    	"School:", schoolComboBox,
			    	"Grade:", gradeComboBox,
			    	"Number of Books Checked Out:", numTextField,
			    	"Serial of Books:", cerealTextField,
			    	delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
			    	name = nameTextField.getText();
			    	school = schoolList[(schoolComboBox.getSelectedIndex())][0];
			    	grade = gradeList[(gradeComboBox.getSelectedIndex())][0];
			    	num = numTextField.getText();
			    	cereal = cerealTextField.getText();
					userList[nameListValue][0] = name;
					userList[nameListValue][1] = school;
					userList[nameListValue][2] = grade;
					userList[nameListValue][3] = num;
					userList[nameListValue][4] = cereal;
					nameDLM.add(nameListValue, userList[nameListValue][0]);
					nameDLM.removeElementAt(nameListValue);
					nameListPanel.validate();
					nameListPanel.repaint();
				}
			}
			else if(schoolListPanel.isVisible()) {
				String school = null;
				JTextField schoolTextField = new JTextField(schoolList[schoolListValue][0]);
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
						"School:", schoolTextField,
						delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					school = schoolTextField.getText();
					
					for(int x = 0; x < userList.length; x++) {
						if(userList[x][1] != null) {
							if(userList[x][1].equals(schoolList[schoolListValue][0])) {
								userList[x][1] = school;
							}
						}
					}
					
					schoolList[schoolListValue][0] = school;
					schoolDLM.add(schoolListValue, schoolList[schoolListValue][0]);
					schoolDLM.removeElementAt(schoolListValue);
					schoolListPanel.validate();
					schoolListPanel.repaint();
				}
			}
			else if(gradeListPanel.isVisible()) {
				String grade = null;
				String[] gradeSelections = new String[14];
				for(int x = 0; x < gradeSelections.length; x++) {
					int mark = 0;
					String temp = null;
					if(x == 0)
						temp = "Pre-K";
					else if(x == 1)
						temp = "Kinder";
					else if(x >= 2)
						temp = (Integer.toString(x - 1));
					
					for(int y = 0; y < gradeList.length; y++) {
						if(gradeList[y][0] != null) {
							if(gradeList[y][0].equals(temp))
								mark++;
						}
					}
					if(mark == 0) {
						gradeSelections[x] = temp;
					}
				}
				gradeSelections[Integer.parseInt(gradeList[gradeListValue][0])] = gradeList[gradeListValue][0];
				for(int x = 0; x < gradeSelections.length; x++) {
					if((x + 1) < gradeSelections.length) {
						if(gradeSelections[x] == null && gradeSelections[x + 1] != null) {
							gradeSelections[x] = gradeSelections[x + 1];
							gradeSelections[x + 1] = null;
							x = 0;
						}
					}
				}
				
				int mark = 0;
				for(int x = 0; x < gradeSelections.length; x++) {
					if(gradeSelections[x] != null)
						mark++;
				}
				String[] gradeSelectionsSort = new String[mark];
				for(int x = 0; x < gradeSelectionsSort.length; x++) {
					if(gradeSelections[x] != null)
						gradeSelectionsSort[x] = gradeSelections[x];
				}
				
				JComboBox gradeComboBox = new JComboBox(gradeSelectionsSort);
				gradeComboBox.setSelectedItem(gradeList[gradeListValue][0]);
				
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
						"Grade:", gradeComboBox,
						delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					grade = gradeSelectionsSort[gradeComboBox.getSelectedIndex()];
					for(int x = 0; x < gradeList.length; x++) {
						if(gradeList[x][0] == null) {
							gradeList[x][0] = grade;
							
							mark = 0;
							for(int y = 0; y < gradeList.length; y++) {
								if(gradeList[y][0] != null)
									mark++;
							}
							String[] gradeListSort = new String[mark];
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeList[y][0] != null)
									gradeListSort[y] = gradeList[y][0];
							}
							
							for(int y = 0; y < gradeListSort.length; y++) {
								if(!isInteger(gradeListSort[y])) {
									if(y != 0) {
										String temp = gradeListSort[y - 1];
										gradeListSort[y - 1] = gradeListSort[y];
										gradeListSort[y] = temp;
										temp = "";
										y = 0;
									}
								}
								else if(y != gradeListSort.length && y != gradeListSort.length - 1) {
									if(isInteger(gradeListSort[y]) && isInteger(gradeListSort[y + 1]) && 
											Integer.parseInt(gradeListSort[y]) > Integer.parseInt(gradeListSort[y + 1])) {
										String temp = gradeListSort[y + 1];
										gradeListSort[y + 1] = gradeListSort[y];
										gradeListSort[y] = temp;
										temp = "";
										y = 0;
									}
								}
							}
							
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeListSort != null)
									gradeList[y][0] = gradeListSort[y];
							}
							
							gradeDLM.clear();
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeListSort[y] != null)
									gradeDLM.addElement(gradeListSort[y]);
							}
							gradeListPanel.validate();
							gradeListPanel.repaint();
							x = gradeList.length + 1;
						}
					}
				}
			}*/
		}
		if(control == delete) {
			if(nameListPanel.isVisible()) {
				/*Object[] warningMessage = {
						"Are you sure you want to delete this user?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					for(int x = 0; x < userList[nameListValue].length; x++)
						userList[nameListValue][x] = null;
					
					for(int x = 0; x < userList.length; x++) {
						if((x + 1) < userList.length) {
							if(userList[x][0] == null && userList[x + 1][0] != null) {
								for(int y = 0; y < userList[x].length; y++) {
									userList[x][y] = userList[x + 1][y];
									userList[x + 1][y] = null;
								}
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					nameDLM.removeElementAt(nameListValue);
					nameListPanel.validate();
					nameListPanel.repaint();
				}*/
			}
			else if(checkedoutListPanel.isVisible()) {
				/*Object[] warningMessage = {
						"Are you sure you want to delete this school?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					for(int x = 0; x < userList.length; x++) {
						if(userList[x][1] != null) {
							if(userList[x][1] == schoolList[schoolListValue][0]) {
								userList[x][1] = schoolList[0][0];
							}
						}
					}
					
					schoolList[schoolListValue][0] = null;
					for(int x = 0; x < schoolList.length; x++) {
						if((x + 1) < schoolList.length) {
							if(schoolList[x][0] == null && schoolList[x + 1][0] != null) {
								schoolList[x][0] = schoolList[x + 1][0];
								schoolList[x + 1][0] = null;
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					schoolDLM.removeElementAt(schoolListValue);
					schoolListPanel.validate();
					schoolListPanel.repaint();
				}
			}
			else if(gradeListPanel.isVisible()) {
				Object[] warningMessage = {
						"Are you sure you want to delete this grade?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					gradeList[gradeListValue][0] = null;
					
					for(int x = 0; x < gradeList.length; x++) {
						if((x + 1) < gradeList.length) {
							if(gradeList[x][0] == null && gradeList[x + 1][0] != null) {
								gradeList[x][0] = gradeList[x + 1][0];
								gradeList[x + 1][0] = null;
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					gradeDLM.removeElementAt(gradeListValue);
					gradeListPanel.validate();
					gradeListPanel.repaint();
				}*/
			}
		}
		else if(control == enter) {
			/*enterCheck = true;
			if(nameListPanel.isVisible()) {
				String name = null;
				String school = null;
				String grade = null;
				String num = null;
				String cereal = null;
				JTextField nameTextField = new JTextField();
				
				//schoolComboBox array setup
				int mark = 0;
				for(int x = 0; x < schoolList.length; x++) {
					if(schoolList[x][0] != null)
						mark++;
				}
				String[] schoolListSort = new String[mark];
				for(int x = 0; x < schoolListSort.length; x++) {
					if(schoolList[x][0] != null)
						schoolListSort[x] = schoolList[x][0];
				}
				JComboBox schoolComboBox = new JComboBox(schoolListSort);
				
				//gradeComboBox array setup
				mark = 0;
				for(int x = 0; x < gradeList.length; x++) {
					if(gradeList[x][0] != null)
						mark++;
				}
				String[] gradeListSort = new String[mark];
				for(int x = 0; x < gradeListSort.length; x++) {
					if(gradeListSort != null)
						gradeListSort[x] = gradeList[x][0];
				}
				JComboBox gradeComboBox = new JComboBox(gradeListSort);
				
				JTextField numTextField = new JTextField();
				JTextField cerealTextField = new JTextField();
				Object[] message = {
					"Name:", nameTextField,
			    	"School:", schoolComboBox,
			    	"Grade:", gradeComboBox,
			    	"Number of Books Checked Out:", numTextField,
			    	"Serial of Books:", cerealTextField,
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
			    	name = nameTextField.getText();
			    	school = schoolList[(schoolComboBox.getSelectedIndex())][0];
			    	grade = gradeList[(gradeComboBox.getSelectedIndex())][0];
			    	num = numTextField.getText();
			    	cereal = cerealTextField.getText();
					for(int x = 0; x < userList.length; x++) {
						if(userList[x][0] == null) {
							userList[x][0] = name;
							userList[x][1] = school;
							userList[x][2] = grade;
							userList[x][3] = num;
							userList[x][4] = cereal;
							nameDLM.addElement(userList[x][0]);
							nameListPanel.validate();
							nameListPanel.repaint();
							x = userList.length + 1;
						}
					}
				}
			}
			else if(schoolListPanel.isVisible()) {
				String school = null;
				JTextField schoolTextField = new JTextField();
				Object[] message = {
						"School Name:", schoolTextField
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					school = schoolTextField.getText();
					for(int x = 0; x < schoolList.length; x++) {
						if(schoolList[x][0] == null) {
							schoolList[x][0] = school;
							schoolDLM.addElement(schoolList[x][0]);
							schoolListPanel.validate();
							schoolListPanel.repaint();
							x = schoolList.length + 1;
						}
					}
				}
			}
			else if(gradeListPanel.isVisible()) {
				String grade = null;
				String[] gradeSelections = new String[14];
				for(int x = 0; x < gradeSelections.length; x++) {
					int mark = 0;
					String temp = null;
					if(x == 0)
						temp = "Pre-K";
					else if(x == 1)
						temp = "Kinder";
					else if(x >= 2)
						temp = (Integer.toString(x - 1));
					
					for(int y = 0; y < gradeList.length; y++) {
						if(gradeList[y][0] != null) {
							if(gradeList[y][0].equals(temp))
								mark++;
						}
					}
					if(mark == 0) {
						gradeSelections[x] = temp;
					}
				}
				for(int x = 0; x < gradeSelections.length; x++) {
					if((x + 1) < gradeSelections.length) {
						if(gradeSelections[x] == null && gradeSelections[x + 1] != null) {
							gradeSelections[x] = gradeSelections[x + 1];
							gradeSelections[x + 1] = null;
							x = 0;
						}
					}
				}
				
				int mark = 0;
				for(int x = 0; x < gradeSelections.length; x++) {
					if(gradeSelections[x] != null)
						mark++;
				}
				String[] gradeSelectionsSort = new String[mark];
				for(int x = 0; x < gradeSelectionsSort.length; x++) {
					if(gradeSelections[x] != null)
						gradeSelectionsSort[x] = gradeSelections[x];
				}
				
				JComboBox gradeComboBox = new JComboBox(gradeSelectionsSort);
				Object[] message = {
						"Grade: ", gradeComboBox
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					grade = gradeSelections[gradeComboBox.getSelectedIndex()];
					for(int x = 0; x < gradeList.length; x++) {
						if(gradeList[x][0] == null) {
							gradeList[x][0] = grade;
							
							mark = 0;
							for(int y = 0; y < gradeList.length; y++) {
								if(gradeList[y][0] != null)
									mark++;
							}
							String[] gradeListSort = new String[mark];
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeList[y][0] != null)
									gradeListSort[y] = gradeList[y][0];
							}
							
							for(int y = 0; y < gradeListSort.length; y++) {
								if(!isInteger(gradeListSort[y])) {
									if(y != 0) {
										String temp = gradeListSort[y - 1];
										gradeListSort[y - 1] = gradeListSort[y];
										gradeListSort[y] = temp;
										temp = "";
										y = 0;
									}
								}
								else if(y != gradeListSort.length && y != gradeListSort.length - 1) {
									if(isInteger(gradeListSort[y]) && isInteger(gradeListSort[y + 1]) && 
											Integer.parseInt(gradeListSort[y]) > Integer.parseInt(gradeListSort[y + 1])) {
										String temp = gradeListSort[y + 1];
										gradeListSort[y + 1] = gradeListSort[y];
										gradeListSort[y] = temp;
										temp = "";
										y = 0;
									}
								}
							}
							
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeListSort != null)
									gradeList[y][0] = gradeListSort[y];
							}
							
							gradeDLM.clear();
							for(int y = 0; y < gradeListSort.length; y++) {
								if(gradeListSort[y] != null)
									gradeDLM.addElement(gradeListSort[y]);
							}
							gradeListPanel.validate();
							gradeListPanel.repaint();
							x = gradeList.length + 1;
						}
					}
				}
				//Actually add the grade and stuff
			}*/
		}
	}
	
	public static void fileWrite(String[][] userListTemp, String[][] checkedoutListTemp, String[][] overdueListTemp, String[][] bookListTemp) {
		// The name of the file to open.
		String fileName = "list.txt";
		
		String[] list = new String[9999];
		boolean uMark = false;
		boolean sMark = false;
		boolean bMark = false;
		boolean gMark = false;
		
		for(int x = 0; x < list.length; x++) {
			if(uMark == false) {
				
				list[x] = "//Name, School, Grade, Number of Books Checked Out, Serial of Books";
				x++;
				list[x] = "[USERS] {";
				x++;
				for(int y = 0; y < userListTemp.length; y++) {
					if(userListTemp[y][0] != null) {
						for(int z = 0; z < userListTemp[y].length; z++) {
							if(list[x] != null && userListTemp[y][z] != null) {
								list[x] = list[x] + userListTemp[y][z];
							}
							else {
								if(userListTemp[y][z] != null)
									list[x] = "	" + userListTemp[y][z];
							}
							if(z < 4) {
								if(userListTemp[y][z + 1] != null)
									list[x] = list[x] + ", ";
							}
						}
						x++;
					}
				}
				list[x] = "}";
				x++;
				uMark = true;
			}
			if(sMark == false) {
				list[x] = "//First Name, Last Name, Serial of Book, When It Was Checked Out, How Long Till It's Overdue";
				x++;
				list[x] = "[CHECKEDOUT] {";
				x++;
				for(int y = 0; y < checkedoutListTemp.length; y++) {
					if(checkedoutListTemp[y][0] != null) {
						for(int z = 0; z < checkedoutListTemp[y].length; z++) {
							if(list[x] != null && checkedoutListTemp[y][z] != null)
								list[x] = list[x] + checkedoutListTemp[y][z];
							else {
								if(checkedoutListTemp[y][z] != null)
									list[x] = "	" + checkedoutListTemp[y][z];
							}
							if(z < 4) {
								if(checkedoutListTemp[y][z + 1] != null)
									list[x] = list[x] + ", ";
							}
						}
						x++;
					}
				}
				list[x] = "}";
				x++;
				sMark = true;
			}
			if(bMark == false) {
				list[x] = "//First Name, Last Name, Serial of Book, How Long It's Been Overdue";
				x++;
				list[x] = "[OVERDUE] {";
				x++;
				for(int y = 0; y < bookListTemp.length; y++) {
					if(bookListTemp[y][0] != null) {
						for(int z = 0; z < bookListTemp[y].length; z++) {
							if(list[x] != null && bookListTemp[y][z] != null)
								list[x] = list[x] + bookListTemp[y][z];
							else {
								if(bookListTemp[y][z] != null)
									list[x] = "	" + bookListTemp[y][z];
							}
							if(z < 4) {
								if(bookListTemp[y][z + 1] != null)
									list[x] = list[x] + ", ";
							}
						}
						x++;
					}
				}
				list[x] = "}";
				x++;
				bMark = true;
			}
			if(gMark == false) {
				list[x] = "//Serial Number, Book Name, Author, Number in Stock, Total Inventory";
				x++;
				list[x] = "[BOOKS] {";
				x++;
				for(int y = 0; y < overdueListTemp.length; y++) {
					if(overdueListTemp[y][0] != null) {
						for(int z = 0; z < overdueListTemp[y].length; z++) {
							if(list[x] != null && overdueListTemp[y][z] != null)
								list[x] = list[x] + overdueListTemp[y][z];
							else {
								if(overdueListTemp[y][z] != null)
									list[x] = "	" + overdueListTemp[y][z];
							}
							if(z < 4) {
								if(overdueListTemp[y][z + 1] != null)
									list[x] = list[x] + ", ";
							}
						}
						x++;
					}
				}
				list[x] = "}";
				x++;
				gMark = true;
			}
		}
		
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName);
			
			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			for(int x = 0; x < list.length; x++) {
				if(list[x] != null) {
					String temp = list[x];
					bufferedWriter.write(temp);
					bufferedWriter.newLine();
				}
				else {
					x = list.length + 1;
				}
			}
			
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
            
            String keyword = new String();
            if(opt == 0)
            	keyword = "[USERS]";
            else if(opt == 1)
            	keyword = "[CHECKEDOUT]";
            else if(opt == 2)
            	keyword = "[OVERDUE]";
            else if(opt == 3)
            	keyword = "[BOOKS]";
            
            String temp = "";
            int arRow = 0;
            int arCol = 0;
            int mark = 0;
            for(int x = 0; x < bufRes.length(); x++) {
            	if(mark != 0) {
            		temp += bufRes.charAt(x);
            		if(bufRes.charAt(x) == ']') {
            			mark = 0;
            			
            			if(temp.equals(keyword)) {
                       		temp = "";
                       		int parMark = 0;
                       		for(int y = (x + 1); y < bufRes.length(); y++) {
                       			if(parMark == 0) {
                       				if(bufRes.charAt(y) == '{') {
                       					y = y + 3;
                       					parMark = 1;
                       				}
                       			}
                       			if(parMark != 0) {
                       				if(bufRes.charAt(y) == '}') {
                       					parMark = 0;
                       					y = bufRes.length() + 1;
                       				}
                       				else if(bufRes.charAt(y) == ',') {
                       					temp = temp.trim();
                       					res[arRow][arCol] = temp;
                       					if(arCol == 4) {
                       						arCol = 0;
                       						arRow++;
                       					}
                       					else
                       						arCol++;
                       					temp = "";
                       					y++; 
                       				}
                       				else if(bufRes.charAt(y) == '\n') {
                       					temp = temp.trim();
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
               				x = bufRes.length() + 1;
                       	}
            			else {
            				temp = "";
            			}
            		}
            	}
            	else if(bufRes.charAt(x) == '[') {
            		mark = 1;
            		temp += bufRes.charAt(x);
            	}
            }
            //Last line is put in list variable
            //if(opt != 0)
            	//res[arRow][arCol] = temp;
            temp = "";
            mark = 0;
            
            for(int x = 0; x < res.length; x++) {
            	for(int y = 0; y < res[x].length; y++) {
            		if(res[x][y] != null)
            			res[x][y] = res[x][y].trim();
            	}
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
	
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (str.isEmpty()) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}
}