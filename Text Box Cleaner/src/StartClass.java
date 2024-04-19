import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;



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
		
		
		
		String desktopPath = System.getProperty("user.home") + "/Desktop/";
		
		desktopPath = desktopPath.replace("\\", "/");
		
		GUI.addLabeledTextField(container,  0, "Folder Path :", desktopPath);
		
		GUI.addLabeledComboBox(container,  1, "Text Color :", new String[] {"Black", "White"});
		
		GUI.addLabeledTextField(container,  2, "Save Path :", desktopPath + "Result.png");
		
		
		
		CleanButtonListener listener = new CleanButtonListener();
		
		JButton button = GUI.addButton(container, GUI.createConstraints(0, 3, 120, 20, 4, 1), "Clean Textboxes", listener);
		
		listener.button = button;
		
		
		
		folderMessage = GUI.addLabel(container, GUI.createConstraints(0, 4, 120, 10, 4, 1), "");
		
		imageMessage = GUI.addLabel(container, GUI.createConstraints(0, 5, 120, 10, 4, 1), "");
		
		saveMessage = GUI.addLabel(container, GUI.createConstraints(0, 6, 120, 10, 4, 1), "");
		
		
		
		window.pack();
		
		window.setVisible(true);
		
	}
	
	
	
	
	
	static class CleanButtonListener implements ActionListener
	{
		
		JButton button;
		
		@Override
		public void actionPerformed(ActionEvent event)
		{
			
			button.setEnabled(false);
			
			folderMessage.setText("");
			
			imageMessage.setText("");
			
			saveMessage.setText("");
			
			Component[] components = window.getContentPane().getComponents();
			
			String folderPath = GUI.findTextFromLabel(components, "Folder Path :");
			
			boolean textIsBlack = GUI.findComboBoxFromLabel(components, "Text Color :").equals("Black");
			
			String savePath = GUI.findTextFromLabel(components, "Save Path :");
			
			try
			{
				
				Cleaner.clean(folderPath, textIsBlack, savePath);
				
			} catch (Exception e) {}
			
			button.setEnabled(true);
			
		}
	}
}



