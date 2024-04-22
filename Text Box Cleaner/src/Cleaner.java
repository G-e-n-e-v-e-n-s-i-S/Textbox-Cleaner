/*
 *	Removes the text from textboxes of the PNG images in the folder specified in 'loadFolderPath'
 *	Optionally, does the same in subfolders
 *	Saves the images in a sub folder of the 'loadFolderPath', or a given path
*/



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;



public class Cleaner
{
	
	
	
	public static boolean logToConsole = false;
	
	public static boolean packWhenLogging = true;
	
	public static int loadCount = 0;
	
	public static int successCount = 0;
	
	public static int failCount = 0;
	
	
	
	public static String saveFolderName = "Textbox Cleaner Results";
	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		
		logToConsole = true;
		
		clean("D:\\MTG Galleries\\murders at karlov manor", "Black", "Yes", "D:\\MTG Galleries\\murders at karlov manor\\Textbox Cleaner Results");
		
	}
	
	
	
	public static void clean(String loadFolderPath, String textColor, String cleanSubfoldersString, String savePath) throws Exception
	{
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Starting.");
		
		packWhenLogging = true;
		
		loadCount = 0;
		
		successCount = 0;
		
		failCount = 0;
		
		
		
		if (!loadFolderPath.endsWith(File.separator))
		{
			
			loadFolderPath = loadFolderPath + File.separator;
			
		}
		
		
		
		//Parse text color
		boolean textIsBlack = false;
		
		if (textColor.equals("Black")) textIsBlack = true;
		
		else if (!textColor.equals("White"))
		{
			
			log("Could not interpret text color ' " + textColor + " '.", Color.red, "image");
			
			return;
			
		}
		
		
		
		//Parse subfolders
		boolean cleanSubfolders = false;
		
		if (cleanSubfoldersString.equals("Yes")) cleanSubfolders = true;
		
		else if (!cleanSubfoldersString.equals("No"))
		{
			
			log("Could not interpret clean subfolders ' " + cleanSubfoldersString + " '.", Color.red, "image");
			
			return;
			
		}
		
		
		
		//Do the thing we're here for
		if (cleanSubfolders)
		{
			
			if (!savePath.endsWith(File.separator))
			{
				
				savePath = savePath + File.separator;
				
			}
			
			
			
			List<File> subfolders = loadFoldersInFolder(loadFolderPath, -1);
			
			for (int i = 0; i < subfolders.size(); i++)
			{
				
				 if (subfolders.get(i).getAbsolutePath().replace(loadFolderPath, "").contains(saveFolderName))
				 {
					 
					 subfolders.remove(i);
					 
					 i--;
					 
				 }
			}
			
			
			
			String loadFolderName = loadFolderPath.replaceAll(".*" + Pattern.quote(File.separator) + "([^" + Pattern.quote(File.separator) + "]+)" + Pattern.quote(File.separator) + "$", "$1.png");
			
			cleanFolder(loadFolderPath, textIsBlack, savePath + loadFolderName, false);
			
			
			
			packWhenLogging = false;
			
			for (File folder : subfolders)
			{
				
				String folderPath = folder.getAbsolutePath();
				
				String folderSavePath = folderPath.replace(loadFolderPath, savePath);
				
				cleanFolder(folderPath, textIsBlack, folderSavePath + ".png", false);
				
			}
			
			packWhenLogging = true;
			
			
			
			log(loadCount + (loadCount != 1 ? " total PNG images" : " PNG image") + " loaded", Color.black, "folder");
			
			
			
			if (failCount > 0)
			{
				
				log(failCount + " result" + (failCount > 1 ? "s" : "") + " could not be saved.", Color.red, "image");
				
			}
			
			else
			{
				
				log("", Color.black, "image");
				
			}
			
			
			
			if (successCount > 0)
			{
				
				log(successCount + " result" + (successCount > 1 ? "s" : "") + " saved in the following folder:", Color.black, "save");
				
				log(savePath, Color.black, "saveName");
			
			}
			
			else
			{
				
				log("", Color.black, "save");
				
				log("", Color.black, "saveName");
				
			}
		}
		
		else
		{
			
			cleanFolder(loadFolderPath, textIsBlack, savePath, true);
			
		}
		
		
		
		System.gc();
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Done.");
		
	}
	
	
	
	public static boolean cleanFolder(String loadFolderPath, boolean textIsBlack, String savePath, boolean warnIfEmpty) throws Exception
	{
		
		List<BufferedImage> images = loadImagesInFolder(loadFolderPath);
		
		if (images == null) return false;
		
		
		
		int count = images.size();
		
		if (count == 0)
		{
			
			if (warnIfEmpty)
			{
				
				log("Couldn't find any png images in specified folder", Color.red, "folder");
			
			}
			
			return false;
				
		}
		
		else
		{
			
			loadCount += count;
			
			log(images.size() + " png images loaded successfully", Color.black, "folder");
			
		}
		
		
		
		BufferedImage result = getExtremePixels(images, textIsBlack);
		
		if (result == null)
		{
			
			failCount++;
			
			return false;
			
		}
		
		boolean success = saveImage(result, "", savePath);
		
		if (success)
		{
			
			successCount++;
			
			return true;
			
		}
		
		else
		{
			
			failCount++;
			
			return false;
			
		}
	}
	
	
	
	public static BufferedImage getExtremePixels(List<BufferedImage> images, boolean textIsBlack)
	{
		
		int width = images.get(0).getWidth();
		
		int height = images.get(0).getHeight();
		
		int count = images.size();
		
		for (int f = 0; f < count; f++)
		{
			
			BufferedImage image = images.get(f);
			
			if (image.getWidth() != width || image.getHeight() != height)
			{
				
				log("Images are not all of the exact same dimensions", Color.red, "image");
				
				return null;
				
			}
		}
		
		
		
		//The color we'll be calculating the distance from
		//Pure white if textIsBlack, pure black if text is white
		int reference = textIsBlack ? -1 : -16777216;
		
		
		
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < width; x++)
		{
			
			for (int y = 0; y < height; y++)
			{
				
				//List available colors
				int[] colors = new int[count];
				
				for (int i=0; i<count; i++)
				{
					
					colors[i] = images.get(i).getRGB(x, y);
					
				}
				
				
				
				//Keep the one closest to pure white (brightest), or pure black (darkest)
				double minDistance = Double.MAX_VALUE;
				
				int minIndex = 0;
				
				for (int i=0; i<count; i++)
				{
						
					double distance = colorDistance(colors[i], reference);
					
					if (minDistance > distance)
					{
								
						minDistance = distance;
						
						minIndex = i;
						
					}
				}
				
				result.setRGB(x, y, colors[minIndex]);
				
			}
		}
		
		log("Textboxes cleaned successfully", Color.black, "image");
		
		return result;
		
	}
	
	
	
	
	
	static double colorDistance(int argb1, int argb2)
	{
		
		int blue1 = (argb1)&0x000000FF;
		
		int green1 = (argb1>>8)&0x000000FF;
		
		int red1 = (argb1>>16)&0x000000FF;
		
		
		
		int blue2 = (argb2)&0x000000FF;
		
		int green2 = (argb2>>8)&0x000000FF;
		
		int red2 = (argb2>>16)&0x000000FF;
		
		
		
		double redmean = ( red1 + red2 ) / 2;
		
		
		
		double reddiff = red1 - red2;
		
		double greendiff = green1 - green2;
		
		double bluediff = blue1 - blue2;
		
		
		
		//Redmean
		return Math.sqrt( (512d+redmean)/256d * reddiff*reddiff  +  4d * greendiff*greendiff  +  (767d-redmean)/256d * bluediff*bluediff );
		
		//Euclidian
		//return Math.sqrt( reddiff*reddiff  + greendiff*greendiff  +  bluediff*bluediff );
		
	}
	
	
	
	public static List<File> loadFoldersInFolder(String folderPath, int getInSubfolders)
	{
		
		if (!folderPath.endsWith(File.separator)) folderPath = folderPath + File.separator;
		
		
		
		List<File> returnedFolders = new ArrayList<File>();
		
		
		
		File folder = new File(folderPath);
		
		if (!folder.isDirectory())
		{
			
			log("Specified path ' " + folderPath + " ' is not a folder", Color.red, "folder");
			
			return returnedFolders;
			
		}
		
		
		File[] files = folder.listFiles();
		
		for (int i=0;i<files.length;i++)
		{
			
			String filePathName = files[i].getPath();
			
			if (files[i].isDirectory())
			{
				
				returnedFolders.add(files[i]);
				
				if (getInSubfolders != 0) returnedFolders.addAll(loadFoldersInFolder(filePathName, getInSubfolders-1));
				
			}
		}
		
		
		
		return returnedFolders;
		
	}
	
	
	
	public static List<BufferedImage> loadImagesInFolder(String folderPath)
	{
		
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		try
		{
			
			File folder = new File(folderPath);
			
			if (!folder.exists() || !folder.isDirectory())
			{
				
				log("No folder found at specified path", Color.red, "folder");
				
				return null;
				
			}
			
			File[] files = folder.listFiles();
			
			
			
			for (int f=0;f<files.length;f++)
			{
				
				File file = files[f];
				
				if (file.getName().endsWith(".png"))
				{
					
					BufferedImage image = ImageIO.read(file);
					
					images.add(image);
					
				}
			}
		} catch (IOException e)
		{
			
			log("Couldn't load png images from specified folder", Color.red, "folder");
			
			return null;
				
		}
		
		
		
		return images;
		
	}
	
	
	
	
	
	public static boolean saveImage(BufferedImage image, String folderPath, String fileName)
	{
		
		if (!folderPath.equals("") && !folderPath.endsWith(File.separator)) folderPath = folderPath + File.separator;
		
		if (!fileName.endsWith(".png")) fileName = fileName + ".png";
		
		if (fileName.contains(File.separator))
		{
			
			String[] path = fileName.split(Pattern.quote(File.separator));
			
			fileName = path[path.length-1];
			
			for (int i = 0; i < path.length-1; i++)
			{
				
				folderPath = folderPath + path[i] + File.separator;
				
			}
		}
		
		
		
		try
		{
			
			File outputDir = new File(folderPath);
			
			outputDir.mkdirs();
			
		    File outputfile = new File(folderPath + fileName);
		    
		    ImageIO.write(image, "png", outputfile);
		    
		    log("Result saved successfully to:", Color.black, "save");
		    
		    log(folderPath + fileName, Color.black, "saveName");
		    
		}
		
		catch (IOException e)
		{
			
			log("Couldn't save result to specified path", Color.red, "save");
			
			return false;
			
		}
		
		return true;
		
	}
	
	
	
	
	
	public static void log(String text, Color color, String out)
	{
		
		boolean textIsEmpty = text == null || text.equals("");
		
		if (logToConsole)
		{
			
			if (textIsEmpty) return;
			
			System.out.println(new Date().toString().substring(11, 20) + (color == Color.black ? "  INFO:    " : "  ERROR:   " ) + text);
			
		}
		
		else
		{
			
			if (textIsEmpty)
			{
				
				text = "";
				
				color = Color.black;
				
			}
			
			if (out.equals("folder"))
			{
				
				if (!color.equals(Color.red) && StartClass.folderMessage.getForeground().equals(Color.red)) return;
				
				StartClass.folderMessage.setText(text);
				
				StartClass.folderMessage.setForeground(color);
				
			}
			
			else if (out.equals("image"))
			{
				
				if (!color.equals(Color.red) && StartClass.imageMessage.getForeground().equals(Color.red)) return;
				
				StartClass.imageMessage.setText(text);
				
				StartClass.imageMessage.setForeground(color);
				
			}
			
			else if (out.equals("save"))
			{
				
				if (!color.equals(Color.red) && StartClass.saveMessage.getForeground().equals(Color.red)) return;
				
				StartClass.saveMessage.setText(text);
				
				StartClass.saveMessage.setForeground(color);
				
			}
			

			else
			{
				
				if (!color.equals(Color.red) && StartClass.saveNameMessage.getForeground().equals(Color.red)) return;
				
				StartClass.saveNameMessage.setText(text);
				
				StartClass.saveNameMessage.setForeground(color);
				
			}
			
			
			
			if (color.equals(Color.red) || packWhenLogging) StartClass.window.pack();
			
		}
	}
}





class CleanerWorker extends SwingWorker<Integer, Integer>
{
	
	String loadFolderPath;
	
	String textColor;
	
	String cleanSubfolders;
	
	String savePath;
	
	CleanerWorker(String loadFolderPath, String textColor, String cleanSubfolders, String savePath)
	{ 
		
		this.loadFolderPath = loadFolderPath;
		
		this.textColor = textColor;
		
		this.cleanSubfolders = cleanSubfolders;
		
		this.savePath = savePath;
		
    }
	
	@Override
    protected Integer doInBackground() throws Exception
    {
		
		Cleaner.clean(loadFolderPath, textColor, cleanSubfolders, savePath);
    	
    	return 0;
    	
    }
}