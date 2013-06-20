package neo.util.image;

import java.io.*;
import java.awt.*;						// Graphics2D, Point2D
import java.awt.color.*;				// ColorSpace
import java.awt.image.*;				// BufferedImage
import java.awt.geom.*;					// AffineTransform
import com.sun.image.codec.jpeg.*;		// JPEGImageDecoder, JPEGCodec, JPEGEncodeParam

/**
 * The JpgImage class can be used to resize JPEG images and
 * either return them as either BufferedImages or save them
 * as files. Here's a short example:
 *  * JpgImage ji = new JpgImage("picture.jpg");
 * ji.scalePercent(0.5);
 * ji.cropProportions(5, 7, true);
 * ji.sendToFile("new_picture.jpg");
 * 
 * Some of the code used in this class was a modification of
 * the excellent example code provided by Will Bracken in
 * the Java Developer's Forum, at:
 * http://forum.java.sun.com/thread.jsp?thread=260711&forum=20&message=985157
 * 
 * The "Java Examples in a Nutshell" book by David Flanagan
 * was also a good reference. There are some nice examples of
 * using transform matrices to blur and sharpen an image in
 * there, if you want to add that functionality. While most
 * of the methods here are fairly trivial wrappers around various
 * transform objects, the rotate method is a bit more complicated,
 * due to the fact that the image dimensions and origin are
 * changed -- see the code itself for more details.
 * 
 * The Java 2D classes are used for the image manipulation,
 * so this will only work with Java 1.2 or higher. Also, the
 * com.sun.image.codec.jpeg.JPEGImageDecoder, 
 * com.sun.image.codec.jpeg.JPEGEncodeParam, and
 * com.sun.image.codec.jpeg.JPEGCodec classes are used to read and
 * save images, so this may not work with non-Sun implementations 
 * of Java. You may be able to use the more generic Image and/or 
 * ImageIcon classes to perform similar functions.
 * 
 * Program version 1.0. Author Julian Robichaux, http://www.nsftools.com
 *
 * @author Julian Robichaux ( http://www.nsftools.com )
 * @version 1.0
 */
/**
 * 	@Class Name	: 	JpgImage.java
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
public class JpgImage
{
	private BufferedImage bi = null;
	
	/**
	 * Creates a JpgImage from a specified file name
	 *
	 * @param  fileName    the name of a JPEG file
	 * @exception  IOException    if the file cannot be opened or read
	 * @exception  ImageFormatException    if the JPEG file is invalid
	 */
	public JpgImage (String fileName) throws IOException, ImageFormatException
	{
		FileInputStream fis = new FileInputStream(fileName);
		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);
		bi = decoder.decodeAsBufferedImage();
		fis.close();
	}
	
	/**
	 * Creates a JpgImage from the specified BufferedImage
	 *
	 * @param  image    a BufferedImage object
	 * @exception  ImageFormatException    if the BufferedImage is null
	 */
	public JpgImage (BufferedImage image) throws ImageFormatException
	{
		if (image == null)
			throw new ImageFormatException("BufferedImage is null");
		else
			bi = image;
	}
	
	/**
	 * Returns the height (in pixels) of the current JpgImage object
	 * 
	 * @return  the height of the current image
	 */
	public int getHeight ()
	{
		return bi.getHeight();
	}
	
	/**
	 * Returns the width (in pixels) of the current JpgImage object
	 * 
	 * @return  the width of the current image
	 */
	public int getWidth ()
	{
		return bi.getWidth();
	}
	
	/**
	 * Shrinks or enlarges the current JpgImage object so that the
	 * height of the image (in pixels) equals the given height
	 *
	 * @param  height    scale the image to this height
	 */
	public void scaleHeight (int height)
	{
		double scale = (double)height / (double)bi.getHeight();
		scalePercent(scale);
	}
	
	/**
	 * Shrinks or enlarges the current JpgImage object so that the
	 * width of the image (in pixels) equals the given width
	 *
	 * @param  width    scale the image to this width
	 */
	public void scaleWidth (int width)
	{
		double scale = (double)width / (double)bi.getWidth();
		scalePercent(scale);
	}
	
	/**
	 * Shrinks or enlarges the current JpgImage object so that the
	 * size of the image (in pixels) is the greater of the height and width
	 * dictated by the parameters.
	 * For example, if the image has to be enlarged by a factor of 60% 
	 * in order to be the given height, and it has to be enlarged by a
	 * factor of 80% in order to be the given width, then the image will
	 * be enlarged by 80% (the greater of the two). Use this method if
	 * you need to make sure that an image is at least the given height
	 * and width.
	 *
	 * @param  height    scale the image to at least this height
	 * @param  width    scale the image to at least this width
	 */
	public void scaleHeightWidthMax (int height, int width)
	{
		double scaleH = (double)height / (double)bi.getHeight();
		double scaleW = (double)width / (double)bi.getWidth();
		scalePercent(Math.max(scaleH, scaleW));
	}
	
	/**
	 * Shrinks or enlarges the current JpgImage object so that the
	 * size of the image (in pixels) is the lesser of the height and width
	 * dictated by the parameters.
	 * For example, if the image has to be enlarged by a factor of 60% 
	 * in order to be the given height, and it has to be enlarged by a
	 * factor of 80% in order to be the given width, then the image will
	 * be enlarged by 60% (the lesser of the two). Use this method if
	 * you need to make sure that an image is no larger than the given
	 * height or width.
	 *
	 * @param  height    scale the image to at most this height
	 * @param  width    scale the image to at most this width
	 */
	public void scaleHeightWidthMin (int height, int width)
	{
		double scaleH = (double)height / (double)bi.getHeight();
		double scaleW = (double)width / (double)bi.getWidth();
		scalePercent(Math.min(scaleH, scaleW));
	}
	
	/**
	 * Shrinks or enlarges the current JpgImage object by the given scale
	 * factor, with a scale of 1 being 100% (or no change).
	 * For example, if you need to reduce the image to 75% of the current size, 
	 * you should use a scale of 0.75. If you want to double the size of the
	 * image, you should use a scale of 2. If you attempt to scale using a
	 * negative number, the image will not be modified.
	 *
	 * @param  scale    the amount that this image should be scaled (1 = no change)
	 */
	public void scalePercent (double scale)
	{
		if ((scale > 0) && (scale != 1)) {
			AffineTransformOp op = new AffineTransformOp
				(AffineTransform.getScaleInstance(scale, scale), null);
			bi = op.filter(bi, null);
		}
	}
	
	/**
	 * Crops the current JpgImage object using the given proportions.
	 * The resulting image will be as large as possible, with either
	 * the width or the height of the image unchanged, and the other
	 * measurement of the image cropped equally on either side.
	 * 
	 * For example, to crop an image with the proportions of a 5x7 picture,
	 * you could pass a width of 5 and a height of 7 (or a width of 7 
	 * and a height of 5).
	 *
	 * @param  width    the proportional width to crop
	 * @param  height    the proportional height to crop
	 */
	public void cropProportions (double width, double height)
	{
		int currentHeight = bi.getHeight();
		int currentWidth = bi.getWidth();
		int cropHeight = (int)(currentWidth * (height / width));
		int cropWidth = (int)(currentHeight * (width / height));
		
		if (cropHeight > currentHeight) {
			bi = bi.getSubimage((int)((currentWidth - cropWidth) / 2), 0, 
					cropWidth, currentHeight);
		} else {
			bi = bi.getSubimage(0, (int)((currentHeight - cropHeight) / 2), 
					currentWidth, cropHeight);
		}
	}
	
	/**
	 * Crops the current JpgImage object using the given proportions,
	 * optionally "optimizing" the cropping by swapping the height and
	 * width proportions if doing so would crop less of the image.
	 * The resulting image will be as large as possible, with either
	 * the width or the height of the image unchanged, and the other
	 * measurement of the image cropped equally on either side.
	 * 
	 * For example, to crop an image with the proportions of a 5x7 picture,
	 * you could pass a width of 5 and a height of 7. In this case, if the
	 * "optimize" flag was set and the image was wider than it was tall,
	 * this method would automatically crop with proportions of 7x5 instead.
	 *
	 * @param  width    the proportional width to crop
	 * @param  height    the proportional height to crop
	 * @param  optimize    if true, indicates that the width and height can be
	 *                     swapped if that would cause less of the image to be
	 *                     cropped
	 */
	public void cropProportions (double width, double height, boolean optimize)
	{
		double big = Math.max(width, height);
		double small = Math.min(width, height);
		if (optimize) {
			if (bi.getWidth() > bi.getHeight())
				cropProportions(big, small);
			else
				cropProportions(small, big);
		} else {
			cropProportions(width, height);
		}
	}
	
	/**
	 * Rotates the current JpgImage object a specified number of degrees,
	 * with a default background color of white (also see the notes for 
	 * the rotate(double, Color) method). This is the equivalent of calling:
	 * 
	 * JpgImage.rotate(degrees, Color.white);
	 *
	 * @param  degrees    the number of degrees to rotate the image
	 */
	public void rotate (double degrees)
	{
		rotate(degrees, Color.white);
	}
	
	/**
	 * Rotates the current JpgImage object a specified number of degrees.
	 * 
	 * You should be aware of 2 things with regard to image rotation.
	 * First, the more times you rotate an image, the more the image 
	 * degrades. So instead of rotating an image 90 degrees and then
	 * rotating it again 45 degrees, you should rotate it once at a
	 * 135 degree angle. 
	 * 
	 * Second, a rotated image will always have a rectangular border 
	 * with sides that are vertical and horizontal, and all of the area
	 * within this border will become part of the resulting image. 
	 * Therefore, if you rotate an image at an angle that's not a 
	 * multiple of 90 degrees, your image will appear to be placed 
	 * at an angle against a rectangular background of the specified Color. 
	 * For this reason, an image rotated 45 degrees and then another 45 degrees
	 * will not be the same as an image rotated 90 degrees.
	 *
	 * @param  degrees    the number of degrees to rotate the image
	 * @param  backgroundColor    the background color used for areas
	 *                            in the resulting image that are not
	 *                            covered by the image itself
	 */
	public void rotate (double degrees, Color backgroundColor)
	{
		/*
		 * Okay, this required some strange geometry. Before an image
		 * is rotated, the origin is at the top left corner of the 
		 * rectangle that contains the image. After an image is rotated,
		 * you want the origin to get moved to a spot that will allow
		 * the entire rotated image to be framed within a rectangle.
		 * Unfortunately, this does not happen automatically.
		 *
		 * That's where the strange geometry comes in. We essentially
		 * need to rotate the image, and then determine what the width
		 * and height of the new image is, and then determine where the
		 * new origin should be. The width and height is easy (you can
		 * also use the AffineTransform getWidth and getHeight methods),
		 * but the new origin...well...not so easy. Unfortunately, my
		 * trigonometry skills aren't sharp enough to be able to give you
		 * a good explanation of what's going on with this method without
		 * drawing everything out for you. If you want to figure it out
		 * for yourself, just draw an axis on a sheet of paper, place a
		 * smaller rectangular piece of paper on the axis, and start
		 * rotating it along the axis to see what's going on. Then pull
		 * out your old trig books and start calculating.
		 *
		 * BTW, if there's an easier way to do this, I'd love to know about it.
		 */
		
		// adjust the angle that was passed so it's between 0 and 360 degrees
		double positiveDegrees = (degrees % 360) + ((degrees < 0) ? 360 : 0);
		double degreesMod90 = positiveDegrees % 90;
		double radians = Math.toRadians(positiveDegrees);
		double radiansMod90 = Math.toRadians(degreesMod90);
		
		// don't bother with any of the rest of this if we're not really rotating
		if (positiveDegrees == 0)
			return;
		
		// figure out which quadrant we're in (we'll want to know this later)
		int quadrant = 0;
		if (positiveDegrees < 90)
			quadrant = 1;
		else if ((positiveDegrees >= 90) && (positiveDegrees < 180))
			quadrant = 2;
		else if ((positiveDegrees >= 180) && (positiveDegrees < 270))
			quadrant = 3;
		else if (positiveDegrees >= 270)
			quadrant = 4;
		
		// get the height and width of the rotated image (you can also do this
		// by applying a rotational AffineTransform to the image and calling
		// getWidth and getHeight against the transform, but this should be a
		// faster calculation)
		int height = bi.getHeight();
		int width = bi.getWidth();
		double side1 = (Math.sin(radiansMod90) * height) + (Math.cos(radiansMod90) * width);
		double side2 = (Math.cos(radiansMod90) * height) + (Math.sin(radiansMod90) * width);
		
		double h = 0;
		int newWidth = 0, newHeight = 0;
		if ((quadrant == 1) || (quadrant == 3)) {
			h = (Math.sin(radiansMod90) * height);
			newWidth = (int)side1;
			newHeight = (int)side2;
		} else {
			h = (Math.sin(radiansMod90) * width);
			newWidth = (int)side2;
			newHeight = (int)side1;
		}
		
		// figure out how much we need to shift the image around in order to
		// get the origin where we want it
		int shiftX = (int)(Math.cos(radians) * h) - ((quadrant == 3) || (quadrant == 4) ? width : 0);
		int shiftY = (int)(Math.sin(radians) * h) + ((quadrant == 2) || (quadrant == 3) ? height : 0);
		
		// create a new BufferedImage of the appropriate height and width and
		// rotate the old image into it, using the shift values that we calculated
		// earlier in order to make sure the new origin is correct
		BufferedImage newbi = new BufferedImage(newWidth, newHeight, bi.getType());
		Graphics2D g2d = newbi.createGraphics();
		g2d.setBackground(backgroundColor);
		g2d.clearRect(0, 0, newWidth, newHeight);
		g2d.rotate(radians);
		g2d.drawImage(bi, shiftX, -shiftY, null);
		bi = newbi;
	}
	
	/**
	 * Inverts the current JpgImage object
	 */
	public void invert ()
	{
		AffineTransform at = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
		at.scale(-1.0, 1.0);
		AffineTransformOp op = new AffineTransformOp(at, null);
		bi = op.filter(bi, null);
	}
	
	/**
	 * Makes the current JpgImage object a greyscale image
	 */
	public void grayscale ()
	{
		ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		bi = op.filter(bi, null);
	}
	
	/**
	 * Makes the current JpgImage object a negative of the original image
	 */
	public void negative ()
	{
		RescaleOp op = new RescaleOp(-1.0f, 255f, null);
		bi = op.filter(bi, null);
	}
	
	/**
	 * Returns the current JpgImage object as a BufferedImage
	 *
	 * @return  a BufferedImage representing the current JpgImage
	 */
	public BufferedImage sendToBufferedImage ()
	{
		return bi;
	}
	
	/**
	 * Writes the current JpgImage object to a file, with a quality
	 * of 0.75
	 *
	 * @param  fileName    the name of the file to write the image to
	 *                     (if the file already exists, it will be
	 *                     overwritten)
	 * @exception  IOException    if there is an error writing to the file
	 */
	public void sendToFile (String fileName) throws IOException
	{
		sendToFile(fileName, 0.75f);
	}
	
	/**
	 * Writes the current JpgImage object to a file, with the
	 * specified quality
	 *
	 * @param  fileName    the name of the file to write the image to
	 *                     (if the file already exists, it will be
	 *                     overwritten)
	 * @param  quality    the JPEG quality of the resulting image file,
	 *                    from 0 to 1
	 * @exception  IOException    if there is an error writing to the file
	 */
	public void sendToFile (String fileName, float quality) throws IOException
	{
		if (quality < 0) quality = 0f;
		if (quality > 1) quality = 1f;
		
		FileOutputStream out = new FileOutputStream(fileName);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
		param.setQuality(quality, false);
		encoder.encode(bi, param);
		out.close();
	}
	
}

