import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;



public class GUI
{
	
	
	static JFrame mainWindow;
	
	static Container mainContainer;
	
	
	
	
	
	public static GridBagConstraints createConstraints(	int x, int y, int xPixel, int yPixel, double xWeight, double yWeight, int xSize, int ySize,
														int fill, int anchor,
														int padTop, int padLeft, int padBottom, int padRight )
	{
		
		//Create a set of constraints that will determine an object's position, size and behavior within the window
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		//Which column (x), and which row (y) should it start on ?
		constraints.gridx = x;
		
		constraints.gridy = y;
		
		
		//How much larger (pixels*2) should it be compared to other thing on the same row (x), and same column (y) ?
		constraints.ipadx = xPixel;
		
		constraints.ipady = yPixel;
		
		
		//How fast will it resize compared to other thing on the same row (x), and same column (y) ?
		constraints.weightx = xWeight;
		
		constraints.weighty = yWeight;
		
		
		//How many columns (x), and how many rows (y) should it span ?
		constraints.gridwidth = xSize;
		
		constraints.gridheight = ySize;
		
		
		//If it's smaller than it's spot on the grid, how should it fill it ?
		constraints.fill = fill;
		
		
		//If it must not fill it's spot on the grid, where should it be anchored ?
		constraints.anchor = anchor;
		
		
		//How much pixels should it leave between itself and the edges of it's spot on the grid ?
		constraints.insets = new Insets(padTop, padLeft, padBottom, padRight);
		
		
		//Return the constraints
		return constraints;
		
	}
	
	public static GridBagConstraints createConstraints(int x, int y, int xPixel, int yPixel, int xSize, int ySize)
	{
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		
		
		constraints.gridx = x;
		
		constraints.gridy = y;
		
		
		
		constraints.ipadx = xPixel;
		
		constraints.ipady = yPixel;
		
		
		
		constraints.weightx = 1d;
		
		constraints.weighty = 1d;
		
		
		
		constraints.gridwidth = xSize;
		
		constraints.gridheight = ySize;
		
		
		
		constraints.fill = GridBagConstraints.BOTH;				//Fill horizontaly and verticaly
		
		
		
		constraints.anchor = GridBagConstraints.PAGE_END;		//Anchor at bottom middle
		
		
		
		constraints.insets = new Insets(5, 5, 5, 5);			//Leave no space
		
		
		
		return constraints;
		
	}
	
	
	
	
	
	public static JComboBox<String> addComboBox(Container container, GridBagConstraints constraints, String[] labelTexts, ActionListener actionListener)
	{
		
		//Create the ComboBox object (it's a dropbox)
		JComboBox<String> comboBox = new JComboBox<String>(labelTexts);
		
		
		//Configure some of the ComboBox's options
		comboBox.setEditable(false);
		
		
		//Add an ActionListener that will perform an action when an option is selected, or when the enter key is pressed
		if (actionListener != null)
		{
			
			comboBox.addActionListener(actionListener);
			
		}
		
		
		//Add the ComboBox with it's constraints to the container
		container.add(comboBox, constraints);
		
		
		//Return the ComboBox
		return comboBox;
		
	}
	
	public static JComboBox<String> addComboBox(Container container, GridBagConstraints constraints, String[] labelTexts)
	{
		
		return addComboBox(container, constraints, labelTexts, null);
		
	}
	
	
	
	
	
	public static JButton addButton(Container container, GridBagConstraints constraints, String text, ActionListener actionListener)
	{
		
		JButton button = new JButton(text);
		
		button.addActionListener(actionListener);
		
		container.add(button, constraints);
		
		return button;
		
	}
	
	
	
	
	
	public static JCheckBox addCheckBox(Container container, GridBagConstraints constraints, ActionListener actionListener)
	{
		
		JCheckBox checkbox = new JCheckBox();
		
		if (actionListener != null)
		{
			
			checkbox.addActionListener(actionListener);
			
		}
		
		container.add(checkbox, constraints);
		
		return checkbox;
		
	}
	
	public static JCheckBox addCheckBox(Container container, GridBagConstraints constraints)
	{
		
		return addCheckBox(container, constraints, null);
		
	}
	
	
	
	
	
	public static JLabel addLabel(Container container, GridBagConstraints constraints, String text, Color backgroundColor, int alignment)
	{
		
		JLabel label = new JLabel(text);
		
		label.setHorizontalAlignment(alignment);
		
		if (backgroundColor != null)
		{
			
			label.setBackground(backgroundColor);
			
			label.setOpaque(true);
			
		}
		
		container.add(label, constraints);
		
		return label;
		
	}
	
	public static JLabel addLabel(Container container, GridBagConstraints constraints, String text, Color backgroundColor)
	{
		
		return addLabel(container, constraints, text, backgroundColor, JLabel.CENTER);
		
	}
	
	public static JLabel addLabel(Container container, GridBagConstraints constraints, String text, int alignment)
	{
		
		return addLabel(container, constraints, text, null, alignment);
		
	}
	
	public static JLabel addLabel(Container container, GridBagConstraints constraints, String text)
	{
		
		return addLabel(container, constraints, text, null, JLabel.CENTER);
		
	}
	
	
	
	
	
	public static JTextField addTextField(Container container, GridBagConstraints constraints, String defaultText, int columns, int horizontalAlignment)
	{
		
		JTextField text = new JTextField(defaultText, columns);
		
		text.setHorizontalAlignment(horizontalAlignment);
		
		container.add(text, constraints);
		
		return text;
		
	}
	
	public static JTextField addTextField(Container container, GridBagConstraints constraints, String defaultText, int columns)
	{
		
		return addTextField(container, constraints, defaultText, columns, JLabel.CENTER);
		
	}
	
	
	
	
	
	public static void addLabeledTextField(Container container, int y, String labelText, String defaultText, boolean labelOnTheLeft)
	{
		
		JLabel label = new JLabel(labelText);
		
		GridBagConstraints labelConstraints = createConstraints(labelOnTheLeft ? 0 : 1, y, 20, 10, 1, 1);
		
		container.add(label, labelConstraints);
		
		
		
		JTextField text = new JTextField(defaultText, 20);
		
		GridBagConstraints textConstrints = createConstraints(labelOnTheLeft ? 1 : 0, y, 100, 10, 3, 1);
		
		container.add(text, textConstrints);
		
		
		
		label.setLabelFor(text);
		
	}
	
	public static void addLabeledTextField(Container container, int y, String labelText, String defaultText)
	{
		
		addLabeledTextField(container, y, labelText, defaultText, true);
		
	}
	
	
	
	
	
	public static void addLabeledTextArea(Container container, int y, int ySize, String labelText, String defaultText, boolean labelOnTheLeft)
	{
		
		JLabel label = new JLabel(labelText);
		
		GridBagConstraints labelConstraints = createConstraints(labelOnTheLeft ? 0 : 1, y, 0, 0, 1, 1);
		
		container.add(label, labelConstraints);
		
		
		
		JTextArea text = new JTextArea(defaultText, ySize, 20);
		
		GridBagConstraints textConstrints = createConstraints(labelOnTheLeft ? 1 : 0, y, 0, 0, 2, 1);
		
		container.add(text, textConstrints);
		
		
		
		label.setLabelFor(text);
		
	}
	
	public static void addLabeledTextArea(Container container, int y, int ySize, String labelText, String defaultText)
	{
		
		addLabeledTextArea(container, y, ySize, labelText, defaultText, true);
		
	}
	
	
	
	
	
	public static void addLabeledComboBox(Container container, int y, String labelText, String[] comboBoxTexts, boolean labelOnTheLeft)
	{
		
		JLabel label = new JLabel(labelText);
		
		GridBagConstraints labelConstraints = createConstraints(labelOnTheLeft ? 0 : 1, y, 20, 10, 1, 1);
		
		container.add(label, labelConstraints);
		
		
		
		JComboBox<String> comboBox = new JComboBox<String>(comboBoxTexts);
		
		comboBox.setEditable(false);
		
		GridBagConstraints comboBoxConstrints = createConstraints(labelOnTheLeft ? 1 : 0, y, 100, 10, 3, 1);
		
		container.add(comboBox, comboBoxConstrints);
		
		
		
		label.setLabelFor(comboBox);
		
	}
	
	public static void addLabeledComboBox(Container container, int y, String labelText, String[] comboBoxTexts)
	{
		
		addLabeledComboBox(container, y, labelText, comboBoxTexts, true);
		
	}
	
	
	
	
	
	public static void updateConstraints(Container container, Component component, GridBagConstraints constraints)
	{
		
		container.remove(component);
		
		container.add(component,constraints);
		
		container.revalidate();
		
		container.repaint();
		
	}
	
	
	
	
	
	public static String findTextFromLabel(Component[] components, String labelText)
	{
		
		for (int i=0; i<components.length; i++)
		{
			
			Component component = components[i];
			
			if (component instanceof JLabel)
			{
				
				JLabel label = (JLabel) component;
				
				if (label.getText().equals(labelText))
				{
					
					JTextComponent text = (JTextComponent) label.getLabelFor();
					
					return text.getText();
					
				}
			}
		}
		
		System.out.println(new Date() + "   ERROR:    Impossible de trouver un composant avec le label ' " + labelText + " '");
		
		return null;
		
	}
	
	
	
	
	
	public static Boolean findCheckBoxFromLabel(Component[] components, String labelText)
	{
		
		for (int i=0; i<components.length; i++)
		{
			
			Component component = components[i];
			
			if (component instanceof JLabel)
			{
				
				JLabel label = (JLabel) component;
				
				if (label.getText().equals(labelText))
				{
					
					JCheckBox checkbox = (JCheckBox) label.getLabelFor();
					
					return checkbox.isSelected();
					
				}
			}
		}
		
		System.out.println(new Date() + "   ERROR:    Impossible de trouver un composant avec le label ' " + labelText + " '");
		
		return null;
		
	}
	
	
	
	
	
	public static String findComboBoxFromLabel(Component[] components, String labelText)
	{
		
		for (int i=0; i<components.length; i++)
		{
			
			Component component = components[i];
			
			if (component instanceof JLabel)
			{
				
				JLabel label = (JLabel) component;
				
				if (label.getText().equals(labelText))
				{
					
					@SuppressWarnings("unchecked")
					JComboBox<String> box = (JComboBox<String>) label.getLabelFor();
					
					String text = (String)box.getSelectedItem();
					
					return text; 
					
				}
			}
		}
		
		System.out.println(new Date() + "   ERROR:    Impossible de trouver un composant avec le label ' " + labelText + " '");
		
		return null;
		
	}
	
	
	
	
	
	public static void setFont(javax.swing.plaf.FontUIResource font)
	{
		
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		
		while (keys.hasMoreElements())
		{
			
			Object key = keys.nextElement();
			
			Object value = UIManager.get(key);
			
			if (value instanceof javax.swing.plaf.FontUIResource)
			{
				
				UIManager.put (key, font);
				
			}
		}
	} 
	
	
	
	
	
	public static List<String> stringToList(String string, String splittingRegex, boolean trim, boolean toLowerCase, boolean intern)
	{
		
		if (string.equals("")) return new ArrayList<String>(0);
		
		List<String> list = new ArrayList<String>(Arrays.asList(string.split(splittingRegex)));
		
		if (trim)
		{
			
			for (int i = 0; i < list.size(); i++)
			{
				
				list.set(i, list.get(i).trim());
				
			}
		}
		
		if (toLowerCase)
		{
			
			for (int i = 0; i < list.size(); i++)
			{
				
				list.set(i, list.get(i).toLowerCase());
				
			}
		}
		
		if (intern)
		{
			
			for (int i = 0; i < list.size(); i++)
			{
				
				list.set(i, list.get(i).intern());
				
			}
		}
		
		return list;
		
	}
	
	
	
	
	
	static String[] stringListToArray(List<String> list)
	{
		
		int size = list.size();
		
		String[] array = new String[size];
		
		for (int i=0; i<size; i++)
		{
			
			array[i] = list.get(i);
			
		}
		
		return array;
		
	}
	
	
	
	
	
	public static String pad(String string, int length)
	{
		
		while(string.length() < length) string = string + " ";
		
		return string;
		
	}
	
	
	
	
	
	static List<File> getFilesInFolder(String pathName, int getInSubFolders)
	{
		
		//S'assurer que le nom est complet
		if (!pathName.endsWith("/")) pathName = pathName +"/";
		
		
		//Pr�parer la liste des fichiers
		List<File> returnedFiles = new ArrayList<File>();
		
		
		//Charger le dossier
		File folder = new File(pathName);
		
		
		//V�rifier que c'est un dossier
		if (!folder.isDirectory())
		{
			
			System.out.println(new Date().toString().substring(11, 20) + " ERROR:   Specified path ' " + pathName + " ' is not a folder");
			
			return returnedFiles;
			
		}
		
		
		//Trouver tous les fichiers
		File[] files = folder.listFiles();
		
		
		//Cycler sur les fichiers
		for (int i=0;i<files.length;i++)
		{
			
			//Le nom du fichier
			String filePathName = files[i].getPath();
			
			
			//Regarder si c'est un sous-dossier
			if (files[i].isDirectory())
			{
				
				//Si on doit charger les sous-dossiers, le faire
				if (getInSubFolders > 0) returnedFiles.addAll(getFilesInFolder(filePathName, getInSubFolders-1));
			
			}
			
			
			//Sinon, ajouter � la liste
			else
			{
				
				//Le charger
				returnedFiles.add(files[i]);
				
			}
		}
		
		
		//Renvoyer
		return returnedFiles;
		
	}
}








class ComboBoxListener implements ActionListener
{
	
	@Override
	public void actionPerformed(ActionEvent event)
	{
		
		JComboBox<?> comboBox = (JComboBox<?>) event.getSource();
		
		String text = (String) comboBox.getSelectedItem();
		
		System.out.println(new Date() + "   " + text);
		
		System.exit(0);
		
	}
}