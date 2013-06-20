package neo.util.image;

import java.io.*;
import java.util.*;
import neo.util.image.JpgImage;		// available at http://www.nsftools.com

/**
 * This demonstrates the use of the custom JpgImage class to
 * manipulate JPEG images. It is really just a command-line
 * utility that can modify a JPEG image file on your
 * computer and save the modified image as a new file or
 * overwrite the existing file (depending on whether or not
 * you use the -o option).
 * 
 * Type "java JpgResizer" at the command line to get a
 * description of all the available command line parameters.
 * Some examples of use are:
 * 
 * To rotate the file "image.jpg" 90 degrees, overwriting the
 * existing file:
 * java JpgResizer image.jpg -r 90
 * 
 * To rotate the file "image.jpg" 90 degrees and save it to a
 * new file named "image2.jpg":
 * java JpgResizer image.jpg -r 90 -o image2.jpg
 * 
 * To crop the file "image.jpg" so it has the proportions of a
 * 5x7 picture, overwriting the existing file:
 * java JpgResizer image.jpg -p 5,7
 * 
 * To make thumbnails of all the .jpg files in the current directory,
 * so that all the thumbnails have a width of 50 pixels, saving all
 * the new images to a subdirectory of the current directory called
 * "thumbs" (this subdirectory must already exist):
 * java JpgResizer *.jpg -w 50 -o thumbs/%.jpg
 * 
 * To make thumbnails of all the .jpg files in the current directory,
 * so that all the thumbnails are 10% of the original image size, saving all
 * the new images with new names of *_thumb.jpg (for example, a file
 * named "image.jpg" would be saved to a filename of "image_thumb.jpg"):
 * java JpgResizer *.jpg -s 0.10 -o %_thumb.jpg
 * 
 * Program version 1.0. Author Julian Robichaux, http://www.nsftools.com
 *
 * @author Julian Robichaux ( http://www.nsftools.com )
 * @version 1.0
 */
/**
 * 	@Class Name	: 	JpgResizer.java
 * 	@파일설명		: 	
 * 	@Version			: 	1.0
 *	@Author			: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 	작업일 		버젼	구분	작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4		생성	hoon09		source create (삼성전기)
 *	2006-11-23 	1.4		수정	hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6		수정	hoon09		code convention apply (국민은행, 펜타시큐리티)
 *	2009-09-23	1.7		수정	hoon09		code valid check (푸르덴샬생명보험,뱅뱅)
 **********************************************************************************************             
 */
public class JpgResizer
{	
	public static void main (String args[])
	{
		Vector files = new Vector();
		String outputFile = null;
		String newFileName = null;
		boolean invert = false;
		boolean grayscale = false;
		boolean negative = false;
		int height = 0;
		int width = 0;
		double scale = 0;
		double pHeight = 0;
		double pWidth = 0;
		double degrees = 0;
		float quality = 1.0f;
		
		// Process all the command line arguments (they can be in any
		// order, but arguments that start with "-" will be treated
		// as parameters, and all other arguments are treated as file names).
		// Arguments that contain wildcard characters (like *.jpg) are
		// automatically expanded prior to program execution, so that
		// the individual file names that match the wildcard specification
		// will become individual arguments themselves.
		for (int i = 0; i < args.length; i++) {
			if ((args[i].startsWith("-")) && ((args[i].length() > 1))) {
				// looks like an argument
				
				if (args[i].substring(1, 2).equals("h")) {
					// we have a hard-coded height
					try {
						height = Integer.parseInt(args[++i]);
					} catch (Exception e) {
						System.err.println("Error parsing height " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("w")) {
					// we have a hard-coded width
					try {
						width = Integer.parseInt(args[++i]);
					} catch (Exception e) {
						System.err.println("Error parsing width " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("s")) {
					// we have a hard-coded scale percent value
					try {
						scale = Double.parseDouble(args[++i]);
					} catch (Exception e) {
						System.err.println("Error parsing scale " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("p")) {
					// we have a hard-coded proportion
					try {
						String prop = args[++i];
						pWidth = Double.parseDouble(prop.substring(0, prop.indexOf(",")));
						pHeight = Double.parseDouble(prop.substring(prop.indexOf(",") + 1));
					} catch (Exception e) {
						System.err.println("Error parsing proportions " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("r")) {
					// we have to rotate a certain number of degrees
					try {
						degrees = Double.parseDouble(args[++i]);
					} catch (Exception e) {
						System.err.println("Error parsing degrees " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("q")) {
					// we have a hard-coded image quality
					try {
						quality = Float.parseFloat(args[++i]);
					} catch (Exception e) {
						System.err.println("Error parsing quality " + args[i] + ": " + e);
					}
					
				} else if (args[i].substring(1, 2).equals("o")) {
					// the place where we're outputting the file(s)
					outputFile = args[++i];
					
				} else if (args[i].substring(1, 2).equals("i")) {
					// invert the image
					invert = true;
					
				} else if (args[i].substring(1, 2).equals("g")) {
					// make the image grayscale
					grayscale = true;
					
				} else if (args[i].substring(1, 2).equals("n")) {
					// negativize the image
					negative = true;
					
				}
			} else {
				// if this doesn't look like a parameter, 
				// we'll add it to the list of file names
				files.addElement(args[i]);
			}
		}
		
		// if we parsed all of the arguments and didn't find anything that
		// looked like a file name, just display some help information
		if (files.size() == 0) {
			System.out.println("Resizes one or more JPEG image files and optionally saves");
			System.out.println("the image(s) to a new file name and/or location");
			System.out.println("");
			System.out.println("USAGE: java JpgResizer [-h height] [-w width] [-s scale]");
			System.out.println("                       [-p x,y] [-q quality] [-r degrees]");
			System.out.println("                       [-i] [-g] [-n] [-o OutputFile] FileName(s)");
			System.out.println("");
			System.out.println("  FileName    the file(s) that should be processed. You can");
			System.out.println("              use a wildcard specification or one or many");
			System.out.println("              individual file names separated by spaces.");
			System.out.println("  -h    the new height (in pixels) that the image should be");
			System.out.println("        resized to (proportions are maintained)");
			System.out.println("  -w    the new width (in pixels) that the image should be");
			System.out.println("        resized to (proportions are maintained)");
			System.out.println("  -s    scale the image to the specified amount. For example,");
			System.out.println("        to scale the image to 75% of its original size, use");
			System.out.println("        a scale value of 0.75; to double the size of the image");
			System.out.println("        use a scale value of 2.");
			System.out.println("  -p    crop the image to the specified x and y proportions.");
			System.out.println("        For example, to crop it to the proportions of a 5x7");
			System.out.println("        picture, use a value of 5,7 or 7,5.");
			System.out.println("  -q    specify the quality of the resulting image, on a scale");
			System.out.println("        of 0 to 1, where 1 keeps the current quality (default is 1)");
			System.out.println("  -r    the number of degrees to rotate the image");
			System.out.println("  -o    the name of the output file, if any (if this is omitted");
			System.out.println("        the existing file will be overwritten). You can use a");
			System.out.println("        percent sign (%) to specify the current file name (with");
			System.out.println("        no file extension) if you are manipulating multiple");
			System.out.println("        files. For example, if *.jpg is your FileName parameter");
			System.out.println("        you might use %_new.jpg or new/%.jpg as your -o parameter.");
			System.out.println("  -i    invert the image");
			System.out.println("  -g    change the image to grayscale");
			System.out.println("  -n    make the new image a negative of the original image");
		}
		
		// for all the files we found, process them one at a time,
		// creating a JpgImage object for each image and then applying
		// the selected filters and saving it back out to a file
		for (int i = 0; i < files.size(); i++) {
			try {
				String fileName = (String)files.elementAt(i);
				System.out.println("Processing: " + fileName);
				JpgImage ji = new JpgImage(fileName);
				
				// manipulate the image
				if ((height > 0) && (width > 0)) {
					ji.scaleHeightWidthMax(height, width);
				} else {
					if (height > 0)
						ji.scaleHeight(height);
					if (width > 0)
						ji.scaleWidth(width);
				}
				
				if (scale > 0)
					ji.scalePercent(scale);
				if ((pHeight > 0) && (pWidth > 0))
					ji.cropProportions(pWidth, pHeight);
				if (invert)
					ji.invert();
				if (grayscale)
					ji.grayscale();
				if (negative)
					ji.negative();
				
				if (degrees > 0)
					ji.rotate(degrees);
				
				// save the image as a file
				if (outputFile == null)
					newFileName = fileName;
				else
					newFileName = replaceSubstring(outputFile, "%", fileName.substring(0, fileName.lastIndexOf(".")));
				
				ji.sendToFile(newFileName, quality);
				System.out.println("File successfully saved as " + newFileName);
			} catch (Exception e) {
				System.err.println(e);
				//e.printStackTrace();
			}
		}
		
	}
	
	private static String replaceSubstring (String str, String find, String replace)
	{
		for(int i = str.lastIndexOf(find); i >= 0; i = str.lastIndexOf(find, i-1))
			str = str.substring(0, i) + replace + str.substring(i + find.length(), str.length());
		
		return str;
	}
	
}

