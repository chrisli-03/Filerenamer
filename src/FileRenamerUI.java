import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Panel;
import javax.swing.DefaultListModel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FileRenamerUI {

	private boolean keepExtention;
	private File fileList[];
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileRenamerUI window = new FileRenamerUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FileRenamerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		Panel fileDisplayPanel = new Panel();
		fileDisplayPanel.setBounds(0, 1, 289, 216);
		frame.getContentPane().add(fileDisplayPanel);
		fileDisplayPanel.setLayout(null);
		
		JScrollPane fileScrollPane = new JScrollPane();
		fileScrollPane.setBounds(0, 0, 289, 216);
		fileDisplayPanel.add(fileScrollPane);
	
		JList fileDisplayList = new JList();
		fileScrollPane.setViewportView(fileDisplayList);
		
		Panel buttonDisplayPanel = new Panel();
		buttonDisplayPanel.setBounds(0, 223, 484, 38);
		frame.getContentPane().add(buttonDisplayPanel);
		
		JButton btnSelectFiles = new JButton("Select Files");
		buttonDisplayPanel.add(btnSelectFiles);
		
		JButton btnRename = new JButton("Rename");
		buttonDisplayPanel.add(btnRename);
		
		Panel optionDisplayPanel = new Panel();
		optionDisplayPanel.setBounds(295, 1, 189, 216);
		frame.getContentPane().add(optionDisplayPanel);
		optionDisplayPanel.setLayout(null);
		
		JTextPane readMe = new JTextPane();
		readMe.setFont(new Font("Dialog", Font.PLAIN, 9));
		readMe.setBounds(12, 5, 167, 58);
		optionDisplayPanel.add(readMe);
		readMe.setText("use # to indicate where to insert sequence number. eg 'test#abc' will give 'test1abc' to 'test10abc' for 10 files");
		
		JEditorPane userInputFilename = new JEditorPane();
		userInputFilename.setBounds(85, 74, 94, 20);
		optionDisplayPanel.add(userInputFilename);
		userInputFilename.setText("prefix#suffix.ext");
		
		JRadioButton rdbtnKeepExtension = new JRadioButton("keep extension");
		rdbtnKeepExtension.setBounds(12, 183, 167, 26);
		optionDisplayPanel.add(rdbtnKeepExtension);
		
		textField = new JTextField();
		textField.setBounds(12, 74, 65, 20);
		optionDisplayPanel.add(textField);
		textField.setColumns(10);
		textField.setText("new name");
		
		textField_1 = new JTextField();
		textField_1.setBounds(12, 105, 65, 20);
		optionDisplayPanel.add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText("starting#");
		
		JEditorPane startingSequenceNumber = new JEditorPane();
		startingSequenceNumber.setBounds(85, 105, 94, 20);
		optionDisplayPanel.add(startingSequenceNumber);
		startingSequenceNumber.setText("1");
		
		SimpleFileChooser JFileChooser = new SimpleFileChooser ();
		
		btnSelectFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				fileList = JFileChooser.run();
				if (fileList != null) {
					updateFileDisplay(fileDisplayList);
				}
			}
		});
		
		btnRename.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String newFilename = userInputFilename.getText();
				
				if (newFilename.length() > 0) {
					keepExtention = rdbtnKeepExtension.isSelected();
					int numberOfFiles = fileList.length;
					int numberOfDigits = countDigits(numberOfFiles);
					
					int sequenceNumberPosition = getSequenceNumberPosition(newFilename);
					List<String> nameList = new ArrayList<String>();
					for (int i = 0; i < numberOfFiles; i++) {
						String prefix = newFilename.substring(0, sequenceNumberPosition);
						String suffix = "";
						String extension = "";
						if (keepExtention) {
							int dot = fileList[i].getName().lastIndexOf('.');
							if (dot > 0) {
							    extension = fileList[i].getName().substring(dot);
							}
						}
						if (sequenceNumberPosition < newFilename.length()-1) {
							suffix = newFilename.substring(sequenceNumberPosition+1, newFilename.length());
						}
						String sequenceNumber = Integer.toString(i);
						int offset = Integer.parseInt(startingSequenceNumber.getText());
						if (offset > 0) {
							sequenceNumber = Integer.toString(i+offset);;
						}
						while (sequenceNumber.length() < numberOfDigits) {
							sequenceNumber = "0" + sequenceNumber;
						}
						nameList.add(prefix + sequenceNumber + suffix + extension);
						
					}
					String[] newNamelist = new String[nameList.size()];
					nameList.toArray(newNamelist);
					for (int i = 0; i < numberOfFiles; i++) {
						Path source = Paths.get(fileList[i].getAbsolutePath());
						try {
							source = Files.move(source, source.resolveSibling(newNamelist[i]));
							fileList[i] = new File(source.toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					updateFileDisplay(fileDisplayList);
				}
			}
		});
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	private void updateFileDisplay(JList list) {
		DefaultListModel model = new DefaultListModel();
		for (int i = 0; i < fileList.length; i++) {
			model.addElement(fileList[i].getName());
		}
		list.setModel(model);
	}
	
	private int getSequenceNumberPosition(String str) {
		int pos = str.length();
		int numberOfIndicator = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i,i+1).equals("#")) {
				pos = i;
				numberOfIndicator++;
			}
		}
		if (numberOfIndicator > 1) pos = str.length();
		return pos;
	}
	
	private int countDigits(int n) {
		int digits= 0;
		for (int i = 1; i < n; i = i*10) {
			if (n/i >= 1) {
				digits++;
			}
		}
		return digits;
	}
}
