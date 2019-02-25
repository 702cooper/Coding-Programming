import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;

public class CodingProgramming extends JFrame implements ActionListener {
	//static final int LIMIT = (2147483647 - 8) / 5;
	//main
	JFrame mainFrame;
	JPanel mainPanel;
	//add JButtons here
	JPanel buttons;
	JButton enter;
	JButton view;
	JButton edit;
	static JOptionPane editOptionPane;
	JButton renew;
	JButton delete;
	
	//JOptionPane editOptionPane;
	//Panels
	JPanel nameListPanel;
	JPanel checkedoutListPanel;
	JPanel overdueListPanel;
	JPanel booksListPanel;
	//SortBox
	JComboBox<String> sortBox;
	//Lists
	JList<String> nameListList;
	JList<String> checkedoutListList;
	JList<String> overdueListList;
	JList<String> booksListList;
	//DLMs
	DefaultListModel<String> nameDLM;
	DefaultListModel<String> checkedoutDLM;
	DefaultListModel<String> overdueDLM;
	DefaultListModel<String> booksDLM;
	//List Values
	int nameListValue;
	int checkedoutListValue;
	int overdueListValue;
	int booksListValue;
	//String Arrays
	//First Name, Last Name
	String[][] nameList = fileRead(0);
	//First Name, Last Name, Serial of Book, When It Was Checked Out, How Long Till It's Overdue
	String[][] checkedoutList = fileRead(1);
	//First Name, Last Name, Serial of Book, How Long It's Been Overdue
	String[][] overdueList = fileRead(2);
	//Serial Number, Book Name, Author, Number in Stock, Total Inventory
	String[][] booksList = fileRead(3);
	
	boolean enterCheck = false;
	
	public static void main(String[] args) {
		new CodingProgramming();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CodingProgramming() {
		
		//This is for TEST purposes
		//REMOVE WHEN DONE
		/*for(int x = 0; x < checkedoutList.length; x++) {
			for(int y = 0; y < checkedoutList[x].length; y++) {
				if(checkedoutList[x][0] != null)
					System.out.println(checkedoutList[x][y]);
			}
			if(checkedoutList[x][0] != null)
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
				//if(enterCheck) {
					int options = JOptionPane.showConfirmDialog(mainFrame, "Save Before Quitting?");
					if(options == JOptionPane.YES_OPTION){
						fileWrite(nameList, checkedoutList, overdueList, booksList);
						mainFrame.setVisible(false);
						mainFrame.dispose();
					}
					else if(options == JOptionPane.NO_OPTION) {
						mainFrame.setVisible(false);
						mainFrame.dispose();
					}
				//}
				/*else {
					mainFrame.setVisible(false);
					mainFrame.dispose();
				}*/
			}
		});
		
		nameDLM = new DefaultListModel();
		nameListPanel = new JPanel();
		JLabel nameListLabel = new JLabel("");
		nameListPanel.add(nameListLabel);
		nameListList = new JList(nameDLM);
		for(int x = 0; x < nameList.length; x++) {
			if(nameList[x][0] != null) {
				nameDLM.addElement(nameList[x][0]);
			}
		}
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
		
		checkedoutDLM = new DefaultListModel();
		checkedoutListPanel = new JPanel();
		JLabel checkedoutListLabel = new JLabel("");
		checkedoutListPanel.add(checkedoutListLabel);
		String[] checkedoutListSort = new String[checkedoutList.length];
		String[] temp = new String[checkedoutList.length];
		for(int x = 0; x < checkedoutList.length; x++) {
			temp[x] = checkedoutList[x][0];
		}
		for(int x = 0; x < temp.length; x++) {
			if(temp[x] != null && temp[x] != "")
				checkedoutListSort[x] = temp[x];
		}
		for(int x = 0; x < checkedoutListSort.length; x++) {
			for(int y = 0; y < booksList.length; y++) {
				if(checkedoutListSort[x] != null && booksList[y][0] != null) {
					if(checkedoutListSort[x].equals(booksList[y][0]))
						checkedoutListSort[x] = booksList[y][1];
				}
			}
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
		String[] overdueListSort = new String[overdueList.length];
		for(int x = 0; x < overdueList.length; x++) {
			if(overdueList[x][0] != null) {
				overdueListSort[x] = overdueList[x][0];
			}
		}
		for(int x = 0; x < overdueListSort.length; x++) {
			for(int y = 0; y < booksList.length; y++) {
				if(overdueListSort[x] != null && booksList[y][0] != null) {
					if(booksList[y][0].equals(overdueListSort[x])) {
						overdueListSort[x] = booksList[y][1];
					}
				}
			}
		}
		for(int x = 0; x < overdueListSort.length; x++) {
			if(overdueListSort[x] != null) {
				overdueDLM.addElement(overdueListSort[x]);
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
		
		booksDLM = new DefaultListModel();
		booksListPanel = new JPanel();
		JLabel booksListLabel = new JLabel("");
		booksListPanel.add(booksListLabel);
		booksListList = new JList<String>(booksDLM);
		for(int x = 0; x < booksList.length; x++) {
			if(booksList[x][1] != null) {
				booksDLM.addElement(booksList[x][1]);
			}
		}
		booksListList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				JList list = (JList) event.getSource();
				booksListValue = list.getSelectedIndex();
			}
		});
		booksListList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane booksListScroll = new JScrollPane(booksListList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		booksListPanel.add(booksListScroll);
		mainFrame.add(booksListPanel);
		booksListPanel.setVisible(false);
		
		//Allows user to sort the list by Name, Books Checked Out, Overdue Books, or All Books
		JPanel sortListPanel = new JPanel();
		JLabel sortList = new JLabel("Sort By");
		sortListPanel.add(sortList);
		String[] sort = {"Name", "Checked Out", "Overdue", "Books"};
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
	
	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent event) {
		Object control = event.getSource();
		
		if(control == sortBox) {
			JComboBox<?> cb = (JComboBox<?>)event.getSource();
			
			int typeSort = (int)cb.getSelectedIndex();
			if(typeSort == 0) {
				checkedoutListPanel.setVisible(false);
				overdueListPanel.setVisible(false);
				booksListPanel.setVisible(false);
				nameListPanel.setVisible(true);
			}	
			else if(typeSort == 1) {
				nameListPanel.setVisible(false);
				overdueListPanel.setVisible(false);
				booksListPanel.setVisible(false);
				checkedoutListPanel.setVisible(true);
			}
			else if(typeSort == 2) {
				nameListPanel.setVisible(false);
				checkedoutListPanel.setVisible(false);
				booksListPanel.setVisible(false);
				overdueListPanel.setVisible(true);
			}
			else if(typeSort == 3) {
				nameListPanel.setVisible(false);
				checkedoutListPanel.setVisible(false);
				overdueListPanel.setVisible(false);
				booksListPanel.setVisible(true);
			}
			//System.out.println("Selected from " + event.getStateChange());
		}
		else if(control == view) {
			if(nameListPanel.isVisible()) {
				String[] sum = new String[4];
				String summary = "";
				try {
					sum[0] = nameList[nameListValue][0];
					sum[1] = nameList[nameListValue][1];
					
					String[] books = new String[checkedoutList.length];
					for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][1] != null && checkedoutList[x][2] != null && nameList[nameListValue][0] != null && nameList[nameListValue][1] != null) {
							if(checkedoutList[x][1].equals(nameList[nameListValue][0]) && checkedoutList[x][2].equals(nameList[nameListValue][1])) {
								books[x] = checkedoutList[x][0];
							}
						}
					}
					for(int x = 0; x < books.length; x++) {
						if((x + 1) < books.length) {
							if(books[x] == null && books[x + 1] != null) {
								books[x] = books[x + 1];
								books[x + 1] = null;
								x = 0;
							}
						}
					}
					for(int x = 0; x < books.length; x++) {
						for(int y = 0; y < booksList.length; y++) {
							if(books[x] != null && booksList[y][0] != null) {
								if(books[x].equals(booksList[y][0])) {
									books[x] = booksList[y][1];
								}
							}
						}
					}
					sum[2] = "";
					for(int x = 0; x < books.length; x++) {
						if(books[x] != null)
							sum[2] = sum[2] + books[x] + "\n";
					}
					
					String[] oBooks = new String[overdueList.length];
					for(int x = 0; x < overdueList.length; x++) {
						if(overdueList[x][1] != null && overdueList[x][2] != null && nameList[nameListValue][0] != null && nameList[nameListValue][1] != null) {
							if(overdueList[x][1].equals(nameList[nameListValue][0]) && overdueList[x][2].equals(nameList[nameListValue][1])) {
								oBooks[x] = overdueList[x][0];
							}
						}
					}
					for(int x = 0; x < oBooks.length; x++) {
						if((x + 1) < oBooks.length) {
							if(oBooks[x] == null && oBooks[x + 1] != null) {
								oBooks[x] = oBooks[x + 1];
								oBooks[x + 1] = null;
								x = 0;
							}
						}
					}
					for(int x = 0; x < oBooks.length; x++) {
						for(int y = 0; y < booksList.length; y++) {
							if(oBooks[x] != null && booksList[y][0] != null) {
								if(oBooks[x].equals(booksList[y][0])) {
									oBooks[x] = booksList[y][1];
								}
							}
						}
					}
					sum[3] = "";
					for(int x = 0; x < oBooks.length; x++) {
						if(oBooks[x] != null)
							sum[3] = sum[3] + oBooks[x] + "\n";
					}
					
					summary = "Name: " + sum[0] + " " + sum[1]
							+ "\n\nBooks Checked Out: " + sum[2]
							+ "\nBooks Overdue: " + sum[3];
					JOptionPane.showMessageDialog(null, summary);
					
					/*for(int x = 0; x < nameList.length; x++) {
						for(int y = 0; y < nameList[x].length; y++) {
							nameList[x][y] = null;
						}
					}*/
				}
				catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You haven't selected anything", "Error", JOptionPane.ERROR_MESSAGE);
				}
				//Name, School, Grade, # of Books checked out, Books checked out
			}
			else if(checkedoutListPanel.isVisible()) {
				String[] sum = new String[4];
				try {
					sum[0] = checkedoutList[checkedoutListValue][0];
					for(int x = 0; x < booksList.length; x++) {
						if(booksList[x][0] != null) {
							if(booksList[x][0].equals(sum[0]))
								sum[0] = booksList[x][1];
						}
					}
					sum[1] = checkedoutList[checkedoutListValue][1] + " " + checkedoutList[checkedoutListValue][2];
					sum[2] = checkedoutList[checkedoutListValue][3];
					sum[3] = checkedoutList[checkedoutListValue][4];
					
					renew = new JButton("Renew");
					renew.addActionListener(this);
					
					Object[] summary = {
							"Book: ", sum[0],
							"\nName: ", sum[1],
							"\nChecked Out: ", sum[2],
							"\nOverdue at: ", sum[3],
							renew
					};
					JOptionPane.showMessageDialog(null, summary, "Checked Out", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You haven't selected anything", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(overdueListPanel.isVisible()) {
				String[] sum = new String[4];
				String summary = "";
				try {
					sum[0] = overdueList[overdueListValue][0];
					for(int x = 0; x < booksList.length; x++) {
						if(booksList[x][0] != null) {
							if(booksList[x][0].equals(sum[0]))
								sum[0] = booksList[x][1];
						}
					}
					sum[1] = overdueList[overdueListValue][1] + " " + overdueList[overdueListValue][2];
					sum[2] = overdueList[overdueListValue][3];
					
					summary = "Book: " + sum[0]
							+ "\nName: " + sum[1]
							+ "\nChecked Out: " + sum[2];
					JOptionPane.showMessageDialog(null, summary, "Checked Out", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You haven't selected anything", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(booksListPanel.isVisible()) {
				String[] sum = new String[7];
				String summary = "";
				try {
					sum[0] = booksList[booksListValue][0];
					sum[1] = booksList[booksListValue][1];
					sum[2] = booksList[booksListValue][2];
					sum[3] = booksList[booksListValue][3];
					sum[4] = booksList[booksListValue][4];
					
					sum[5] = "";
					for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][0] != null) {
							if(booksList[booksListValue][0].equals(checkedoutList[x][0])) {
								sum[5] = sum[5] + "\t" + checkedoutList[x][1] + " " + checkedoutList[x][2] + "\n\t\t\t";
							}
						}
					}
					sum[6] = "";
					for(int x = 0; x < overdueList.length; x++) {
						if(overdueList[x][0] != null) {
							if(booksList[booksListValue][0].equals(overdueList[x][0])) {
								sum[6] = sum[6] + "\t" + overdueList[x][1] + " " + overdueList[x][2] + "\n\t\t\t";
							}
						}
					}
					
					if(sum[5].equals(""))
						sum[5] = "N/A\n";
					if(sum[6].equals(""))
						sum[6] = "N/A";
				
					summary = "Serial of Book: " + sum[0]
							+ "\nBook: " + sum[1]
							+ "\nAuthor: " + sum[2]
							+ "\nTotal in Inventory: " + sum[3]
							+ "\nTotal in Stock: " + sum[4]
							+ "\n\nChecked out to:	" + sum[5]
							+ "Overdue: " + sum[6];
					JOptionPane.showMessageDialog(null, summary, "Book", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null, "You haven't selected anything", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(control == renew) {
			//checkedoutList[checkedoutListValue][4]
			//ADD TWO WEEKS TO THE THING
		}
		else if(control == edit) {
			enterCheck = true;
			editOptionPane = new JOptionPane();
			if(nameListPanel.isVisible()) {
				String firstName = null;
				String lastName = null;
				JTextField firstNameTextField = new JTextField(nameList[nameListValue][0]);
				JTextField lastNameTextField = new JTextField(nameList[nameListValue][1]);
				
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
					"First Name:", firstNameTextField,
			    	"Last Name:", lastNameTextField,
			    	delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					firstName = firstNameTextField.getText();
			    	lastName = lastNameTextField.getText();
			    	
			    	for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][0] != null) {
							if(checkedoutList[x][1].equals(nameList[nameListValue][0]) && checkedoutList[x][2].equals(nameList[nameListValue][1])) {
								checkedoutList[x][1] = firstName;
								checkedoutList[x][2] = lastName;
							}
						}
					}
					for(int x = 0; x < overdueList.length; x++) {
						if(overdueList[x][0] != null) {
							if(overdueList[x][1].equals(nameList[nameListValue][0]) && overdueList[x][2].equals(nameList[nameListValue][1])) {
								overdueList[x][1] = firstName;
								overdueList[x][2] = lastName;
							}
						}
					}
			    	
			    	nameList[nameListValue][0] = firstName;
					nameList[nameListValue][1] = lastName;
					
					nameDLM.add(nameListValue, nameList[nameListValue][0]);
					nameDLM.removeElementAt(nameListValue);
					nameListPanel.validate();
					nameListPanel.repaint();
				}
			}
			else if(checkedoutListPanel.isVisible()) {
				String serial = null;
				String name = null;
				String firstName = null;
				String lastName = null;
				
				int mark = 0;
				for(int x = 0; x < booksList.length; x++) {
					if(booksList[x][0] != null)
						mark++;
				}
				String[] serialList = new String[mark];
				for(int x = 0; x < serialList.length; x++) {
					serialList[x] = booksList[x][1];
				}
				JComboBox<String> serialComboBox = new JComboBox<String>(serialList);
				String temp = "";
				for(int x = 0; x < booksList.length; x++) {
					if(booksList[x][0] != null && booksList[x][0].equals(checkedoutList[checkedoutListValue][0])) {
						temp = booksList[x][1];
					}
				}
				serialComboBox.setSelectedItem(temp);
				
				mark = 0;
				for(int x = 0; x < nameList.length; x++) {
					if(nameList[x][0] != null)
						mark++;
				}
				String[] namesList = new String[mark];
				for(int x = 0; x < namesList.length; x++) {
					namesList[x] = nameList[x][0] + " " + nameList[x][1];
				}
				JComboBox<String> namesComboBox = new JComboBox<String>(namesList);
				namesComboBox.setSelectedItem(checkedoutList[checkedoutListValue][1] + " " + checkedoutList[checkedoutListValue][2]);
				
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
						"Book: ", serialComboBox,
						"Name: ", namesComboBox,
						"Checked out at: ", checkedoutList[checkedoutListValue][3],
						"Overdue by: ", checkedoutList[checkedoutListValue][4],
						delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					serial = serialList[serialComboBox.getSelectedIndex()];
					name = namesList[namesComboBox.getSelectedIndex()];
					
					for(int x = 0; x < booksList.length; x++) {
						if(booksList[x][0] != null && serial.equals(booksList[x][1]))
							serial = booksList[x][0];
					}
					
					temp = "";
					for(int x = 0; x < name.length(); x++) {
						if(name.charAt(x) == ' ') {
							firstName = temp;
							temp = "";
						}
						else if(name.charAt(x) != ' ')
							temp += name.charAt(x);
					}
					lastName = temp;
					
					checkedoutList[checkedoutListValue][0] = serial;
					checkedoutList[checkedoutListValue][1] = firstName;
					checkedoutList[checkedoutListValue][2] = lastName;
					
					temp = "";
					for(int y = 0; y < booksList.length; y++) {
						if(booksList[y][0] != null) {
							if(checkedoutList[checkedoutListValue][0].equals(booksList[y][0]))
								temp = booksList[y][1];
						}
					}
					checkedoutDLM.add(checkedoutListValue, temp);
					checkedoutDLM.removeElementAt(checkedoutListValue);
					checkedoutListPanel.repaint();
					checkedoutListPanel.validate();
				}
			}
			else if(overdueListPanel.isVisible()) {
				String serial = null;
				String name = null;
				String firstName = null;
				String lastName = null;
				
				int mark = 0;
				for(int x = 0; x < booksList.length; x++) {
					if(booksList[x][0] != null)
						mark++;
				}
				String[] serialList = new String[mark];
				for(int x = 0; x < serialList.length; x++) {
					serialList[x] = booksList[x][1];
				}
				JComboBox<String> serialComboBox = new JComboBox<String>(serialList);
				String temp = "";
				for(int x = 0; x < booksList.length; x++) {
					if(booksList[x][0] != null && booksList[x][0].equals(overdueList[overdueListValue][0])) {
						temp = booksList[x][1];
					}
				}
				serialComboBox.setSelectedItem(temp);
				
				mark = 0;
				for(int x = 0; x < nameList.length; x++) {
					if(nameList[x][0] != null)
						mark++;
				}
				String[] namesList = new String[mark];
				for(int x = 0; x < namesList.length; x++) {
					namesList[x] = nameList[x][0] + " " + nameList[x][1];
				}
				JComboBox<String> namesComboBox = new JComboBox<String>(namesList);
				namesComboBox.setSelectedItem(overdueList[overdueListValue][1] + " " + overdueList[overdueListValue][2]);
				
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
						"Book: ", serialComboBox,
						"Name: ", namesComboBox,
						"Checked out at: ", overdueList[overdueListValue][3],
						"Overdue by: ", overdueList[overdueListValue][4],
						delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					serial = serialList[serialComboBox.getSelectedIndex()];
					name = namesList[namesComboBox.getSelectedIndex()];
					
					for(int x = 0; x < booksList.length; x++) {
						if(booksList[x][0] != null && serial.equals(booksList[x][1]))
							serial = booksList[x][0];
					}
					
					temp = "";
					for(int x = 0; x < name.length(); x++) {
						if(name.charAt(x) == ' ') {
							firstName = temp;
							temp = "";
						}
						else if(name.charAt(x) != ' ')
							temp += name.charAt(x);
					}
					lastName = temp;
					
					overdueList[overdueListValue][0] = serial;
					overdueList[overdueListValue][1] = firstName;
					overdueList[overdueListValue][2] = lastName;
					
					temp = "";
					for(int y = 0; y < booksList.length; y++) {
						if(booksList[y][0] != null) {
							if(overdueList[overdueListValue][0].equals(booksList[y][0]))
								temp = booksList[y][1];
						}
					}
					overdueDLM.add(overdueListValue, temp);
					overdueDLM.removeElementAt(overdueListValue);
					overdueListPanel.repaint();
					overdueListPanel.validate();
				}
			}
			else if(booksListPanel.isVisible()) {
				String serial = null;
				String book = null;
				String author = null;
				String total = null;
				
				//total textfield
				String[] totalList = new String[21];
				for(int x = 0; x < totalList.length; x++)
					totalList[x] = Integer.toString(x);
				JComboBox<String> totalComboBox = new JComboBox<String>(totalList);
				totalComboBox.setSelectedIndex(Integer.parseInt(booksList[booksListValue][3]));
				
				JTextField bookTextField = new JTextField(booksList[booksListValue][1]);
				JTextField authorTextField = new JTextField(booksList[booksListValue][2]);
				
				//serial combobox
				String[] serialList = new String[1000];
				for(int x = 0; x < serialList.length; x++) {
					String temp;
					if(x < 10)
						temp = "00" + Integer.toString(x);
					else if(x > 9 && x < 100)
						temp = "0" + Integer.toString(x);
					else
						temp = Integer.toString(x);
					
					boolean mark = false;
					for(int y = 0; y < booksList.length; y++) {
						if(booksList[y][0] != null) {
							if(booksList[y][0].equals(temp)) {
								if(!temp.equals(booksList[booksListValue][0])) {
									mark = true;
									y = booksList.length + 1;
								}
							}
						}
					}
					if(!mark) {
						serialList[x] = temp;
					}
				}
				for(int x = 0; x < serialList.length; x++) {
					if((x + 1) < serialList.length) {
						if(serialList[x] == null && serialList[x + 1] != null) {
							serialList[x] = serialList[x + 1];
							serialList[x + 1] = null;
							x = 0;
						}
					}
				}
				int mark = 0;
				for(int x = 0; x < serialList.length; x++) {
					if(serialList[x] != null)
						mark++;
				}
				String[] serialListSort = new String[mark];
				for(int x = 0; x < serialListSort.length; x++)
					serialListSort[x] = serialList[x];
				JComboBox<String> serialComboBox = new JComboBox<String>(serialListSort);
				mark = 0;
				for(int x = 0; x < serialListSort.length; x++) {
					if(serialListSort[x].equals(booksList[booksListValue][0])) {
						mark = x;
						x = booksList.length + 1;
					}
				}
				serialComboBox.setSelectedIndex(mark);
				
				delete = new JButton("Delete");
				delete.addActionListener(this);
				Object[] message = {
						"Serial Number: ", serialComboBox,
						"Book Name: ", bookTextField,
						"Author Name: ", authorTextField,
						"Total in Inventory: ", totalComboBox,
						delete
				};
				int option = editOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					serial = serialList[serialComboBox.getSelectedIndex()];
					book = bookTextField.getText();
					author = authorTextField.getText();
					total = totalList[totalComboBox.getSelectedIndex()];
					
					for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][0] != null && checkedoutList[x][0].equals(booksList[booksListValue][0])) {
							checkedoutList[x][0] = serial;
						}
					}

					for(int x = 0; x < overdueList.length; x++) {
						if(overdueList[x][0] != null && overdueList[x][0].equals(booksList[booksListValue][0])) {
							overdueList[x][0] = serial;
						}
					}
					
					booksList[booksListValue][0] = serial;
					booksList[booksListValue][1] = book;
					booksList[booksListValue][2] = author;
					booksList[booksListValue][3] = total;
					
					booksDLM.add(checkedoutListValue, booksList[booksListValue][1]);
					booksDLM.removeElementAt(booksListValue);
					booksListPanel.repaint();
					booksListPanel.validate();
				}
			}
		}
		if(control == delete) {
			if(nameListPanel.isVisible()) {
				Object[] warningMessage = {
						"Are you sure you want to delete this name?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					for(int x = 0; x < nameList[nameListValue].length; x++)
						nameList[nameListValue][x] = null;
					
					for(int x = 0; x < nameList.length; x++) {
						if((x + 1) < nameList.length) {
							if(nameList[x][0] == null && nameList[x + 1][0] != null) {
								for(int y = 0; y < nameList[x].length; y++) {
									nameList[x][y] = nameList[x + 1][y];
									nameList[x + 1][y] = null;
								}
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					nameDLM.removeElementAt(nameListValue);
					nameListPanel.validate();
					nameListPanel.repaint();
				}
			}
			else if(checkedoutListPanel.isVisible()) {
				Object[] warningMessage = {
						"Are you sure you want to delete this check out?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					for(int x = 0; x < checkedoutList[checkedoutListValue].length; x++)
						checkedoutList[checkedoutListValue][x] = null;
					
					for(int x = 0; x < checkedoutList.length; x++) {
						if((x + 1) < checkedoutList.length) {
							if(checkedoutList[x][0] == null && checkedoutList[x + 1][0] != null) {
								for(int y = 0; y < checkedoutList[x].length; y++) {
									checkedoutList[x][y] = checkedoutList[x + 1][y];
									checkedoutList[x + 1][y] = null;
								}
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					checkedoutDLM.removeElementAt(checkedoutListValue);
					checkedoutListPanel.validate();
					checkedoutListPanel.repaint();
				}
			}
			else if(overdueListPanel.isVisible()) {
				Object[] warningMessage = {
						"Are you sure you want to delete this overdue check out?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					for(int x = 0; x < overdueList[overdueListValue].length; x++)
						overdueList[overdueListValue][x] = null;
					
					for(int x = 0; x < overdueList.length; x++) {
						if((x + 1) < overdueList.length) {
							if(overdueList[x][0] == null && overdueList[x + 1][0] != null) {
								for(int y = 0; y < overdueList[x].length; y++) {
									overdueList[x][y] = overdueList[x + 1][y];
									overdueList[x + 1][y] = null;
								}
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					overdueDLM.removeElementAt(overdueListValue);
					overdueListPanel.validate();
					overdueListPanel.repaint();
				}
			}
			else if(booksListPanel.isVisible()) {
				Object[] warningMessage = {
						"Are you sure you want to delete this book?"
				};
				int option = JOptionPane.showConfirmDialog(null, warningMessage, "Warning", JOptionPane.YES_NO_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					//removing book from checkedout
					for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][0] != null && checkedoutList[x][0].equals(booksList[booksListValue][0])) {
							checkedoutList[x][0] = null;
							checkedoutDLM.removeElementAt(x);
							for(int y = x; y < checkedoutList.length; y++) {
								if(checkedoutList[y + 1][0] != null) {
									checkedoutList[y] = checkedoutList[y + 1];
									checkedoutList[y + 1] = null;
								}
							}
						}
					}
					checkedoutListPanel.repaint();
					checkedoutListPanel.validate();
					
					//removing book from overdue
					for(int x = 0; x < overdueList.length; x++) {
						if(overdueList[x][0] != null && overdueList[x][0].equals(booksList[booksListValue][0])) {
							overdueList[x][0] = null;
							overdueDLM.removeElementAt(x);
							for(int y = x; y < overdueList.length; y++) {
								if(overdueList[y + 1][0] != null) {
									overdueList[y] = overdueList[y + 1];
									overdueList[y + 1] = null;
								}
							}
						}
					}
					overdueListPanel.repaint();
					overdueListPanel.validate();
					
					for(int x = 0; x < booksList[booksListValue].length; x++)
						booksList[booksListValue][x] = null;
					
					for(int x = 0; x < booksList.length; x++) {
						if((x + 1) < booksList.length) {
							if(booksList[x][0] == null && booksList[x + 1][0] != null) {
								for(int y = 0; y < booksList[x].length; y++) {
									booksList[x][y] = booksList[x + 1][y];
									booksList[x + 1][y] = null;
								}
							}
						}
					}
					editOptionPane.getRootFrame().dispose();
					booksDLM.removeElementAt(booksListValue);
					booksListPanel.validate();
					booksListPanel.repaint();
				}
			}
		}
		else if(control == enter) {
			enterCheck = true;
			if(nameListPanel.isVisible()) {
				String firstName = null;
				String lastName = null;
				JTextField firstNameTextField = new JTextField();
				JTextField lastNameTextField = new JTextField();
				
				Object[] message = {
					"First Name: ", firstNameTextField,
			    	"Last Name: :", lastNameTextField
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					firstName = firstNameTextField.getText();
			    	lastName = lastNameTextField.getText();
					for(int x = 0; x < nameList.length; x++) {
						if(nameList[x][0] == null) {
							nameList[x][0] = firstName;
							nameList[x][1] = lastName;
							nameDLM.addElement(nameList[x][0]);
							nameListPanel.validate();
							nameListPanel.repaint();
							x = nameList.length + 1;
						}
					}
				}
			}
			else if(checkedoutListPanel.isVisible()) {
				String serial = null;
				String name = null;
				String firstName = null;
				String lastName = null;
				
				int mark = 0;
				for(int x = 0; x < booksList.length; x++) {
					if(booksList[x][0] != null)
						mark++;
				}
				String[] serialList = new String[mark];
				for(int x = 0; x < serialList.length; x++) {
					serialList[x] = booksList[x][1];
				}
				JComboBox<String> serialComboBox = new JComboBox<String>(serialList);
				
				mark = 0;
				for(int x = 0; x < nameList.length; x++) {
					if(nameList[x][0] != null)
						mark++;
				}
				String[] namesList = new String[mark];
				for(int x = 0; x < namesList.length; x++) {
					namesList[x] = nameList[x][0] + " " + nameList[x][1];
				}
				JComboBox<String> namesComboBox = new JComboBox<String>(namesList);
				
				String[] month = new String[12];
				for(int x = 0; x < month.length; x++) {
					if((Integer.toString(x)).length() == 1) {
						month[x] = "0" + (Integer.toString(x + 1));
					}
					else {
						month[x] = Integer.toString(x + 1);
					}
				}
				JComboBox monthComboBox = new JComboBox(month);
				monthComboBox.addActionListener(this);
				String[] day = new String[31];
				String year = new String();
				
				
				String[] overdueListWeeks = new String[3];
				for(int x = 0; x < overdueListWeeks.length; x++)
					overdueListWeeks[x] = Integer.toString(x + 1) + " weeks";
				JComboBox<String> overdueComboBox = new JComboBox<String>(overdueListWeeks);
				overdueComboBox.setSelectedIndex(1);
				
				Object[] message = {
						"Book: ", serialComboBox,
						"Name: ", namesComboBox,
						"Checked out at: ", monthComboBox, dayComboBox, yearTextField,
						"Overdue by: ", overdueComboBox,
						delete
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					for(int x = 0; x < checkedoutList.length; x++) {
						if(checkedoutList[x][0] == null) {
							serial = serialList[serialComboBox.getSelectedIndex()];
							name = namesList[namesComboBox.getSelectedIndex()];
							
							for(int y = 0; y < booksList.length; y++) {
								if(booksList[y][0] != null && serial.equals(booksList[y][1])) {
									serial = booksList[y][0];
									booksList[y][4] = Integer.toString(Integer.parseInt(booksList[y][4]) - 1);
									if(booksList[y][4].equals("-1")) {
										JOptionPane.showMessageDialog(null, "There are not enough books in inventory", "Error", JOptionPane.ERROR_MESSAGE);
										return;
									}
								}
							}
							
							String temp = "";
							for(int y = 0; y < name.length(); y++) {
								if(name.charAt(y) == ' ') {
									firstName = temp;
									temp = "";
								}
								else if(name.charAt(y) != ' ')
									temp += name.charAt(y);
							}
							lastName = temp;
							
							checkedoutList[x][0] = serial;
							checkedoutList[x][1] = firstName;
							checkedoutList[x][2] = lastName;
							
							temp = "";
							for(int y = 0; y < booksList.length; y++) {
								if(booksList[y][0] != null) {
									if(checkedoutList[x][0].equals(booksList[y][0]))
										temp = booksList[y][1];
								}
							}
							checkedoutDLM.addElement(temp);
							checkedoutListPanel.repaint();
							checkedoutListPanel.validate();
							x = checkedoutList.length + 1;
						}
					}
				}
			}
			else if(overdueListPanel.isVisible()) {
				JOptionPane.showMessageDialog(null, "You can't enter an overdue book", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(booksListPanel.isVisible()) {
				String serial = null;
				String book = null;
				String author = null;
				String total = null;
				
				//total textfield
				String[] totalList = new String[21];
				for(int x = 0; x < totalList.length; x++)
					totalList[x] = Integer.toString(x);
				JComboBox<String> totalComboBox = new JComboBox<String>(totalList);
				
				JTextField bookTextField = new JTextField(booksList[booksListValue][1]);
				JTextField authorTextField = new JTextField(booksList[booksListValue][2]);
				
				//serial combobox
				String[] serialList = new String[1000];
				for(int x = 0; x < serialList.length; x++) {
					String temp;
					if(x < 10)
						temp = "00" + Integer.toString(x);
					else if(x > 9 && x < 100)
						temp = "0" + Integer.toString(x);
					else
						temp = Integer.toString(x);
					
					boolean mark = false;
					for(int y = 0; y < booksList.length; y++) {
						if(booksList[y][0] != null) {
							if(booksList[y][0].equals(temp)) {
								if(!temp.equals(booksList[booksListValue][0])) {
									mark = true;
									y = booksList.length + 1;
								}
							}
						}
					}
					if(!mark) {
						serialList[x] = temp;
					}
				}
				for(int x = 0; x < serialList.length; x++) {
					if((x + 1) < serialList.length) {
						if(serialList[x] == null && serialList[x + 1] != null) {
							serialList[x] = serialList[x + 1];
							serialList[x + 1] = null;
							x = 0;
						}
					}
				}
				int mark = 0;
				for(int x = 0; x < serialList.length; x++) {
					if(serialList[x] != null)
						mark++;
				}
				String[] serialListSort = new String[mark];
				for(int x = 0; x < serialListSort.length; x++)
					serialListSort[x] = serialList[x];
				JComboBox<String> serialComboBox = new JComboBox<String>(serialListSort);
				
				Object[] message = {
						"Serial Number: ", serialComboBox,
						"Book Name: ", bookTextField,
						"Author Name: ", authorTextField,
						"Total in Inventory: ", totalComboBox,
						delete
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.OK_OPTION) {
					for(int x = 0; x < booksList.length; x++) {
						if(booksList[x][0] == null) {
							serial = serialList[serialComboBox.getSelectedIndex()];
							book = bookTextField.getText();
							author = authorTextField.getText();
							total = totalList[totalComboBox.getSelectedIndex()];
							
							booksList[x][0] = serial;
							booksList[x][1] = book;
							booksList[x][2] = author;
							booksList[x][3] = total;
							
							booksDLM.addElement(booksList[x][1]);
							booksListPanel.repaint();
							booksListPanel.validate();
							x = booksList.length + 1;
						}
					}
				}
			}
		}
	}
	
	public static void fileWrite(String[][] nameListTemp, String[][] checkedoutListTemp, String[][] overdueListTemp, String[][] booksListTemp) {
		// The name of the file to open.
		String fileName = "list.txt";
		
		String[] list = new String[9999];
		boolean nMark = false;
		boolean cMark = false;
		boolean oMark = false;
		boolean bMark = false;
		
		for(int x = 0; x < nameListTemp.length; x++) {
			if(nameListTemp[x][0] != null && nameListTemp[x + 1][0] != null && (x + 1) < nameListTemp.length) {
				String[] comp = new String[2];
				comp[0] = nameListTemp[x][0];
				comp[1] = nameListTemp[x + 1][0];
				Arrays.sort(comp);
				if(nameListTemp[x][0] != comp[0] && nameListTemp[x + 1][0] != comp[1]) {
					String[] temp = nameListTemp[x];
					nameListTemp[x] = nameListTemp[x + 1];
					nameListTemp[x + 1] = temp;
					x = 0;
				}
				/*if(nameListTemp[x][0].charAt(0) > nameListTemp[x + 1][0].charAt(0)) {
					String[] temp = nameListTemp[x];
					nameListTemp[x] = nameListTemp[x + 1];
					nameListTemp[x + 1] = temp;
					x = 0;
				}
				else if(nameListTemp[x][0].charAt(0) == nameListTemp[x + 1][0].charAt(0)) {
					//add second character+ sort
				}*/
			}
		}
		for(int x = 0; x < checkedoutListTemp.length; x++) {
			if(checkedoutListTemp[x][0] != null && checkedoutListTemp[x + 1][0] != null && (x + 1) < checkedoutListTemp.length) {
				String[] comp = new String[2];
				comp[0] = checkedoutListTemp[x][0];
				comp[1] = checkedoutListTemp[x + 1][0];
				Arrays.sort(comp);
				if(checkedoutListTemp[x][0] != comp[0] && checkedoutListTemp[x + 1][0] != comp[1]) {
					String[] temp = checkedoutListTemp[x];
					checkedoutListTemp[x] = checkedoutListTemp[x + 1];
					checkedoutListTemp[x + 1] = temp;
					x = 0;
				}
			}
		}
		for(int x = 0; x < overdueListTemp.length; x++) {
			if(overdueListTemp[x][0] != null && overdueListTemp[x + 1][0] != null && (x + 1) < overdueListTemp.length) {
				String[] comp = new String[2];
				comp[0] = overdueListTemp[x][0];
				comp[1] = overdueListTemp[x + 1][0];
				Arrays.sort(comp);
				if(overdueListTemp[x][0] != comp[0] && overdueListTemp[x + 1][0] != comp[1]) {
					String[] temp = overdueListTemp[x];
					overdueListTemp[x] = overdueListTemp[x + 1];
					overdueListTemp[x + 1] = temp;
					x = 0;
				}
			}
		}
		for(int x = 0; x < booksListTemp.length; x++) {
			if(booksListTemp[x][0] != null && booksListTemp[x + 1][0] != null && (x + 1) < booksListTemp.length) {
				String[] comp = new String[2];
				comp[0] = booksListTemp[x][0];
				comp[1] = booksListTemp[x + 1][0];
				Arrays.sort(comp);
				if(booksListTemp[x][0] != comp[0] && booksListTemp[x + 1][0] != comp[1]) {
					String[] temp = booksListTemp[x];
					booksListTemp[x] = booksListTemp[x + 1];
					booksListTemp[x + 1] = temp;
					x = 0;
				}
			}
		}
		
		for(int x = 0; x < list.length; x++) {
			if(nMark == false) {
				
				list[x] = "//First Name, Last Name";
				x++;
				list[x] = "[NAMES] {";
				x++;
				for(int y = 0; y < nameListTemp.length; y++) {
					if(nameListTemp[y][0] != null) {
						for(int z = 0; z < nameListTemp[y].length; z++) {
							if(list[x] != null && nameListTemp[y][z] != null) {
								list[x] = list[x] + nameListTemp[y][z];
							}
							else {
								if(nameListTemp[y][z] != null)
									list[x] = "	" + nameListTemp[y][z];
							}
							if(z < 4) {
								if(nameListTemp[y][z + 1] != null)
									list[x] = list[x] + ", ";
							}
						}
						x++;
					}
				}
				list[x] = "}";
				x++;
				nMark = true;
			}
			if(cMark == false) {
				list[x] = "//Serial of Book, First Name, Last Name, When It Was Checked Out, How Long Till It's Overdue";
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
				cMark = true;
			}
			if(oMark == false) {
				list[x] = "//Serial of Book, First Name, Last Name, How Long It's Been Overdue";
				x++;
				list[x] = "[OVERDUE] {";
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
				oMark = true;
			}
			if(bMark == false) {
				list[x] = "//Serial Number, Book Name, Author, Total Inventory, Number in Stock";
				x++;
				list[x] = "[BOOKS] {";
				x++;
				for(int y = 0; y < booksListTemp.length; y++) {
					if(booksListTemp[y][0] != null) {
						for(int z = 0; z < booksListTemp[y].length; z++) {
							if(list[x] != null && booksListTemp[y][z] != null)
								list[x] = list[x] + booksListTemp[y][z];
							else {
								if(booksListTemp[y][z] != null)
									list[x] = "	" + booksListTemp[y][z];
							}
							if(z < 4) {
								if(booksListTemp[y][z + 1] != null)
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
            	keyword = "[NAMES]";
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