import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class StartClass
{
	
	
	
	static JFrame window;
	
	static Container container;
	
	
	
	static Dimension screenSize;
	
	
	
	static JLabel folderMessage;
	
	static JLabel imageMessage;
	
	static JLabel saveMessage;
	
	
	
	
	
	public static void main(String[] args)
	{
		
		GUI.setFont(new javax.swing.plaf.FontUIResource("Arial",java.awt.Font.BOLD,20));
		
		
		
		window = new JFrame("Textbox Cleaner");
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		window.setLocation(screenSize.width/10, screenSize.height/10);
		
		
		
		container = window.getContentPane();
		
		container.setLayout(new GridBagLayout());
		
		
		
		String desktopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
		
		
		
		JTextField loadText = GUI.addLabeledTextField(container, 0, 0, "Folder Path :", desktopPath, true, 0, 6);
		
		
		
		GUI.addLabeledComboBox(container, 1, "Text Color :", new String[] {"Black", "White"});
		
		GUI.addVoid(container, GUI.createConstraints(4, 1, 60, 10, 1, 1));
		
		JComboBox<String> subfoldersComboBox = GUI.addLabeledComboBox(container, 5, 1, "Clean Subfolders :", new String[] {"No", "Yes"}, true, 50, 1);
		
		
		
		JTextField saveText = GUI.addLabeledTextField(container, 0, 2, "Save Path :", desktopPath + "Cleaner Result.png", true, 0, 6);
		
		
		
		JButton button = GUI.addButton(container, GUI.createConstraints(0, 3, 120, 20, 7, 1), "Clean Textboxes");
		
		
		
		subfoldersComboBox.addActionListener(new SubfolderComboBoxListener(subfoldersComboBox, loadText, saveText));
		
		loadText.getDocument().addDocumentListener(new LoadTextListener(subfoldersComboBox, loadText, saveText));
		
		button.addActionListener(new CleanButtonListener(button));
		
		
		
		folderMessage = GUI.addLabel(container, GUI.createConstraints(0, 4, 120, 10, 4, 1), "");
		
		imageMessage = GUI.addLabel(container, GUI.createConstraints(0, 5, 120, 10, 4, 1), "");
		
		saveMessage = GUI.addLabel(container, GUI.createConstraints(0, 6, 120, 10, 4, 1), "");
		
		
		
		window.pack();
		
		window.setVisible(true);
		
	}
	
	
	
	
	
	static class CleanButtonListener implements ActionListener
	{
		
		JButton button;
		
		public CleanButtonListener(JButton button)
		{
			
			this.button = button;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			button.setEnabled(false);
			
			folderMessage.setText("");
			
			imageMessage.setText("");
			
			saveMessage.setText("");
			
			Component[] components = window.getContentPane().getComponents();
			
			String loadPath = GUI.findTextFromLabel(components, "Folder Path :");
			
			String textColor = GUI.findComboBoxFromLabel(components, "Text Color :");
			
			String cleanSubfolders = GUI.findComboBoxFromLabel(components, "Clean Subfolders :");
			
			String savePath = GUI.findTextFromLabel(components, "Save Path :");
			
			try
			{
				
				new CleanerWorker(loadPath, textColor, cleanSubfolders, savePath).execute();
				
			} catch (Exception e) {}
			
			button.setEnabled(true);
			
		}
	}
	
	
	
	static class SubfolderComboBoxListener implements ActionListener
	{
		
		JComboBox<String> subfolderComboBox;
		
		JTextField loadTextField;
		
		JTextField saveTextField;
		
		public SubfolderComboBoxListener(JComboBox<String> subfolderComboBox, JTextField loadTextField, JTextField saveTextField)
		{
			
			this.subfolderComboBox = subfolderComboBox;
			
			this.loadTextField = loadTextField;
			
			this.saveTextField = saveTextField;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			if (String.valueOf(subfolderComboBox.getSelectedItem()).equals("Yes"))
			{
				
				saveTextField.setEditable(false);
				
				String loadTextFieldString = loadTextField.getText();
				
				if (!loadTextFieldString.endsWith(File.separator))
				{
					
					loadTextFieldString = loadTextFieldString + File.separator;
					
					loadTextField.setText(loadTextFieldString);
					
				}
				
				saveTextField.setText(loadTextFieldString + Cleaner.saveFolderName + File.separator);
				
			}
			
			else
			{
				
				saveTextField.setEditable(true);
				
				String saveTextFieldString = saveTextField.getText();
				
				if (saveTextFieldString.endsWith(Cleaner.saveFolderName + File.separator))
				{
					
					saveTextFieldString = saveTextFieldString.replaceAll(Cleaner.saveFolderName + Pattern.quote(File.separator) + "$", "Cleaner Result.png");
					
				}
				
				saveTextField.setText(saveTextFieldString);
				
			}
		}
	}
	
	
	
	static class LoadTextListener implements DocumentListener
	{
		
		JComboBox<String> subfolderComboBox;
		
		JTextField loadTextField;
		
		JTextField saveTextField;
		
		public LoadTextListener(JComboBox<String> subfolderComboBox, JTextField loadTextField, JTextField saveTextField)
		{
			
			this.subfolderComboBox = subfolderComboBox;
			
			this.loadTextField = loadTextField;
			
			this.saveTextField = saveTextField;
			
		}
		
		@Override
		public void insertUpdate(DocumentEvent e)
		{
			
			changedUpdate(e);
			
		}
		
		@Override
		public void removeUpdate(DocumentEvent e)
		{
			
			changedUpdate(e);
			
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			
			if (String.valueOf(subfolderComboBox.getSelectedItem()).equals("Yes"))
			{
				
				saveTextField.setEditable(false);
				
				String loadTextFieldString = loadTextField.getText();
				
				if (!loadTextFieldString.endsWith(File.separator))
				{
					
					loadTextFieldString = loadTextFieldString + File.separator;
					
				}
				
				saveTextField.setText(loadTextFieldString + Cleaner.saveFolderName + File.separator);
				
			}
		}
	}
}
