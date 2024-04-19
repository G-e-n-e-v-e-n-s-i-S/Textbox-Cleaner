import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;



public class Cleaner
{
	
	
	
	public static boolean logToConsole = false;
	
	
	
	
	
	public static void main(String[] args) throws Exception
	{
		
		logToConsole = true;
		
		bulkClean("D:/MTG Galleries/Karlov Manor/", "Black");
		
	}
	
	
	
	
	
	public static void bulkClean(String folderPath, String textColor) throws Exception
	{
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Starting.");
		
		
		
		String savePath = (folderPath.endsWith("/") ? folderPath.substring(0, folderPath.length()-1) : folderPath) + " Results/";
		
		List<File> subFolders = loadFoldersInFolder(folderPath, 0);
		
		for (File folder : subFolders)
		{
			
			clean(folder.getAbsolutePath(), textColor, savePath + folder.getName() + ".png");
			
		}

		
		
		System.gc();
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Done.");
		
	}
	
	
	
	public static void clean(String loadPath, String textColor, String savePath) throws Exception
	{
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Starting.");
		
		
		
		//Parse text color
		boolean textIsBlack = false;
		
		if (textColor.equals("Black")) textIsBlack = true;
		
		else if (!textColor.equals("White"))
		{
			
			log("Could not interpret text color ' " + textColor + " '.", Color.red, "image");
			
			return;
			
		}
		
		
		
		//Do the thing we're here for
		List<BufferedImage> images = loadImagesInFolder(loadPath, true);
		
		if (images == null) return;
		
		BufferedImage result = getExtremePixels(images, textIsBlack);
		
		if (result == null) return;
		
		saveImage(result, "", savePath);
		
		
		
		System.gc();
		
		System.out.println(new Date().toString().substring(11, 20) + "  INFO:    Done.");
		
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
		
		log("Textboxes cleaned succesfully", Color.black, "image");
		
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
	
	
	
	public static List<File> loadFoldersInFolder(String pathName, int getInSubFolders)
	{
		
		if (!pathName.endsWith("/")) pathName = pathName +"/";
		
		
		
		List<File> returnedFolders = new ArrayList<File>();
		
		
		
		File folder = new File(pathName);
		
		if (!folder.isDirectory())
		{
			
			log("Specified path ' " + pathName + " ' is not a folder", Color.red, "folder");
			
			return returnedFolders;
			
		}
		
		
		File[] files = folder.listFiles();
		
		for (int i=0;i<files.length;i++)
		{
			
			String filePathName = files[i].getPath();
			
			if (files[i].isDirectory())
			{
				
				returnedFolders.add(files[i]);
				
				if (getInSubFolders != 0) returnedFolders.addAll(loadFoldersInFolder(filePathName, getInSubFolders-1));
				
			}
		}
		
		
		
		return returnedFolders;
		
	}
	
	
	
	public static List<BufferedImage> loadImagesInFolder(String folderAbsolutePath, boolean loadSubFolders)
	{
		
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		try
		{
			
			File folder = new File(folderAbsolutePath);
			
			if (!folder.exists() || !folder.isDirectory())
			{
				
				log("No folder found at specified path", Color.red, "folder");
				
				return null;
				
			}
			
			File[] files = folder.listFiles();
			
			
			
			for (int f=0;f<files.length;f++)
			{
				
				File file = files[f];
				
				if (loadSubFolders && file.isDirectory())
				{
					
					images.addAll(loadImagesInFolder(file.getAbsolutePath(), true));
					
				}
				
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
		
		int count = images.size();
		
		if (count == 0)
		{
			
			log("Couldn't find any png images in specified folder", Color.red, "folder");
			
			return null;
				
		}
		
		else
		{
			
			log(images.size() + " png images loaded succesfully", Color.black, "folder");
			
		}
		
		return images;
		
	}
	
	
	
	
	
	public static void saveImage(BufferedImage image, String folderAbsolutePath, String fileName)
	{
		
		if (!folderAbsolutePath.equals("") && !folderAbsolutePath.endsWith("/")) folderAbsolutePath = folderAbsolutePath + "/";
		
		if (!fileName.endsWith(".png")) fileName = fileName + ".png";
		
		if (fileName.contains("/"))
		{
			
			String[] path = fileName.split("/");
			
			fileName = path[path.length-1];
			
			for (int i = 0; i < path.length-1; i++)
			{
				
				folderAbsolutePath = folderAbsolutePath + path[i] + "/";
				
			}
		}
		
		
		
		try
		{
			
			File outputDir = new File(folderAbsolutePath);
			
			outputDir.mkdirs();
			
		    File outputfile = new File(folderAbsolutePath + fileName);
		    
		    ImageIO.write(image, "png", outputfile);
		    
		    log("Result saved succesfully", Color.black, "save");
		    
		}
		
		catch (IOException e)
		{
			
			log("Couldn't save result to specified path", Color.red, "save");
			
		}
	}
	
	
	

	
	public static void log(String text, Color color, String out)
	{
		
		if (logToConsole)
		{
			
			System.out.println(new Date().toString().substring(11, 20) + (color == Color.black ? "  INFO:    " : "  ERROR:   " ) + text);
			
		}
		
		else
		{
			
			if (out.equals("folder"))
			{
				
				StartClass.folderMessage.setText(text);
				
				StartClass.folderMessage.setForeground(color);
				
				StartClass.window.pack();
				
			}
			
			else if (out.equals("image"))
			{
				
				StartClass.imageMessage.setText(text);
				
				StartClass.imageMessage.setForeground(color);
				
				StartClass.window.pack();
				
			}
			
			else
			{
				
				StartClass.saveMessage.setText(text);
				
				StartClass.saveMessage.setForeground(color);
				
				StartClass.window.pack();
				
			}
		}
	}
}





class CleanerWorker extends SwingWorker<Integer, Integer>
{
	
	String loadPath;
	
	String textColor;
	
	String savePath;
	
	CleanerWorker(String loadPath, String textColor, String savePath)
	{ 
		
		this.loadPath = loadPath;
		
		this.textColor = textColor;
		
		this.savePath = savePath;
		
    }
	
	@Override
    protected Integer doInBackground() throws Exception
    {
		
		Cleaner.clean(loadPath, textColor, savePath);
    	
    	return 0;
    	
    }
}