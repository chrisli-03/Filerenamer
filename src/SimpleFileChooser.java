//Copyright 2015 Hecheng Li

import javax.swing.*;
import java.io.File;

public class SimpleFileChooser extends JFrame {
	public static File fileList[];
  
	public File[] run() {
    	JFileChooser chooser = new JFileChooser();
    	chooser.setMultiSelectionEnabled(true);
    	int option = chooser.showOpenDialog(SimpleFileChooser.this);
    	if (option == JFileChooser.APPROVE_OPTION) {
    		fileList = chooser.getSelectedFiles();
    	}
    	return fileList;
	}
}