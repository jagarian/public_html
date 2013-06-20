package neo.swing;

/*
 * This is an example of how to use a ProgressBar in a Java agent in Lotus Notes.
 *
 * The ProgressBar class below uses the java.awt classes for windowing. java.awt
 * should already be included with the Java classes that ship with Notes, so you
 * don't have to include anything special (like the Java Swing classes) with any
 * agents that you use this class with. Example usage is in the NotesMain() method.
 *
 * Julian Robichaux -- http://www.nsftools.com
 */

import java.awt.*;
import java.awt.event.*;

/**
 * 	@Class Name	: 	ProgBar.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
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
public class ProgBar {

	public void NotesMain() {

		try {
			//Session session = getSession();
			//AgentContext agentContext = session.getAgentContext();

			// (Your code goes here) 
			int maxVal = 11;
			ProgressBar pb = new ProgressBar("This is a test bar", maxVal);
			
			for (int i = 0; i <= maxVal; i++)  {
				// if the user clicked the Cancel button, exit the loop
				if (pb.isCancelClicked())
					break;
				// for the sake of example, cycle through the different ProgressBar text displays
				pb.setBarText(i % 3);
				// update the amount of work we've done
				pb.updateValue(i);
				// wait a second
				Thread.sleep(1000);
			}
			pb.dispose();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * The ProgressBar class allows you to create non-modal progress bars
	 * using only the java.awt classes. All you have to do is create an instance
	 * of a ProgressBar, update the progress using the updateValue method as
	 * you're doing work, and use the dispose method to close the ProgressBar
	 * dialog when you're done. The ProgressBar also includes an optional 
	 * Cancel button, so the user can cancel the action while your process is
	 * running (if you use this button, make sure you check the isCancelClicked
	 * method occassionally to see if the user clicked Cancel).
	 *
	 * This class has been implemented here as an inner class, but there's
	 * no reason why it couldn't be a class all by itself. The progress bar
	 * component itself is actually an inner class to this inner class. Read
	 * the comments there to see how it works.
	 *
	 * Make sure you import java.awt.* and java.awt.event.*
	 *
	 * Julian Robichaux -- http://www.nsftools.com
	 */
	class ProgressBar {
		public final int BOXMSG_NONE = 0;			// display nothing in the progress bar
		public final int BOXMSG_PERCENT = 1;		// display the percent complete (like "20%")
		public final int BOXMSG_NUMBERS = 2;	// display the number complete (like "4 of 10")
		
		Frame parent;
		Dialog pbar;
		Label mess;
		ProgressBox pbox;
		Button cancel;
		long curVal, maxVal;
		boolean shouldCancel;
		
		/*
		 * This version of the constructor creates a ProgressBar that includes a Cancel button
		 */
		public ProgressBar (String message, long maxValue)  {
			this(message, maxValue, true);
		}
		
		/*
		 * When you create a new instance of a ProgressBar, it sets up the progress bar and
		 * immediately displays it. The message parameter is a label that displays just above
		 * the progress bar, the maxValue parameter indicates what value should be considered
		 * 100% on the progress bar, and the allowCancel parameter indicates whether or not
		 * a Cancel button should be displayed at the bottom of the dialog.
		 */
		public ProgressBar (String message, long maxValue, boolean allowCancel)  {
			shouldCancel = false;
			// this is the invisible Frame we'll be using to call all the Dialog
			parent = new Frame();
			pbar = new Dialog(parent, "Agent Progress Bar");
			
			// add the message to the top of the dialog
			Panel top = new Panel(new FlowLayout(FlowLayout.CENTER, 1, 1));
			mess = new Label(message);
			top.add(mess);
			pbar.add("North", top);
			
			// add the progress bar to the middle of the dialog
			Panel middle = new Panel(new FlowLayout(FlowLayout.CENTER, 1, 1));
			pbox = new ProgressBox(maxValue);
			middle.add(pbox);
			pbar.add("Center", middle);
			
			// add the Cancel button to the bottom of the dialog (if allowCancel is true)
			if (allowCancel)  {
				Panel bottom = new Panel(new FlowLayout(FlowLayout.CENTER, 1, 1));
				cancel = new Button("Cancel");
				cancel.addActionListener( new ActionListener()  {
					public void actionPerformed(ActionEvent e)  {
						//pbar.dispose();
						shouldCancel = true;
					}
				} );
				bottom.add(cancel);
				pbar.add("South", bottom);
			}
			
			// display the ProgressBar dialog
			Dimension d = pbar.getToolkit().getScreenSize();
			pbar.setLocation(d.width/3, d.height/3);		// center the ProgressBar (sort of)
			pbar.pack();						// organize all its components
			pbar.setResizable(false);		// make sure the user can't resize it
			pbar.toFront();					// give the ProgressBar focus
			pbar.show();					// and display it
			
		}
		
		/*
		 * The updateValue method allows you to update the value used by the progress bar
		 * in order to calculate the percent complete on the bar. Percent complete is the value
		 * parameter passed to this method divided by the maxValue you passed in when you
		 * initially instantiated the ProgressBar.
		 */
		public void updateValue (long value)  {
			pbox.updateValue(value);
		}
		
		/*
		 * The updateMaxValue method allows you to update the maximum value used by the
		 * progress bar in order to calculate the percent complete on the bar.
		 */
		public void updateMaxValue (long value)  {
			pbox.updateMaxValue(value);
		}
		
		/*
		 * The getCurrentValue method returns the current value used by the progress bar
		 * to determine the percent complete.
		 */
		public long getCurrentValue ()  {
			return pbox.getCurrentValue();
		}
		
		/*
		 * The getMaxValue method returns the maximum value used by the progress bar
		 * to determine the percent complete (once the current value equals the maximum
		 * value, we're at 100%)
		 */
		public long getMaxValue ()  {
			return pbox.getMaxValue();
		}
		
		/*
		 * The setBarText method sets the value of the dispText field in the progress bar,
		 * which indicates what kind of message should be displayed in the bar. You'll 
		 * normally use a value of BOXMSG_NONE, BOXMSG_PERCENT, or
		 * BOXMSG_NUMBERS for this value.
		 */
		public void setBarText (int boxMsgValue)  {
			pbox.setBarMsg(boxMsgValue);
		}
		
		/*
		 * The dispose method removes the ProgressBar from the screen display.
		 */
		public void dispose ()  {
			// use this when you're ready to clean up
			try { pbar.dispose(); }  catch (Exception e)  {}
			try { parent.dispose(); }  catch (Exception e)  {}
		}
		
		/*
		 * The isCancelClicked method indicates whether or not the user clicked the
		 * Cancel button. Normally, when you realize that the user has clicked Cancel,
		 * you'll want to call the dispose method to stop displaying the ProgressBar.
		 */
		public boolean isCancelClicked ()  {
			return shouldCancel;
		}
		
		
		/*
		 * The ProgressBox is the actual awt component that displays a progress bar. 
		 * It's implemented here as an inner class of the ProgressBar class, but it can
		 * be a separate class too. Just make sure you import java.awt.*
		 *
		 * Julian Robichaux -- http://www.nsftools.com
		 */
		class ProgressBox extends Canvas  {
			public final int MSG_NONE = 0;			// display nothing in the progress bar
			public final int MSG_PERCENT = 1;		// display the percent complete (like "20%")
			public final int MSG_NUMBERS = 2;		// display the number complete (like "4 of 10")
			
			private long maxVal, currentVal;
			private int cols, width, height, dispText;
			private Color barClr, borderClr, textClr;
			
			public ProgressBox (long maxValue)  {
				this(maxValue, 40);
			}
			
			public ProgressBox (long maxValue, int width)  {
				// one unit of width for this component is the width of
				// the letter 'X' in the current font (so a width of 10 is
				// a width of 'XXXXXXXXXX' using the current font)
				maxVal = maxValue;
				currentVal = 0;
				cols = width;
				dispText = MSG_PERCENT;
				barClr = new Color(50, 200, 255);		// make the progress bar light blue
				borderClr = Color.gray;					// make the bar border gray
				textClr = Color.darkGray;					// make the text dark gray
			}
			
			protected void measure ()  {
				// get the global values we use in relation to our current font
				FontMetrics fm = this.getFontMetrics(this.getFont());
				if (fm == null)
					return;
				width = fm.stringWidth("X") * cols;
				height = fm.getHeight() + 4;
			}
			
			public void addNotify ()  {
				// automatically invoked after our Canvas is created but 
				// before it's displayed (FontMetrics aren't available until
				// super.addNotify() has been called)
				super.addNotify();
				measure();
			}
			
			public Dimension getPreferredSize ()  {
				// called by the LayoutManager to find out how big we want to be
				return new Dimension(width + 4, height + 4);
			}
			
			public Dimension getMinimumSize ()  {
				// called by the LayoutManager to find out what our bare minimum
				// size requirements are
				return getPreferredSize();
			}
			
			public void updateValue (long value)  {
				// change the currentVal, which is used to determine what our percent
				// complete is, and update the progress bar
				currentVal = value;
				this.repaint();
			}
			
			public void updateMaxValue (long value)  {
				// change the maxVal, which is used to determine what our percent
				// complete is ((currentVal / maxVal) * 100 = percent complete), 
				// and update the progress bar
				maxVal = value;
				this.repaint();
			}
			
			public long getCurrentValue ()  {
				// return the currentVal
				return currentVal;
			}
			
			public long getMaxValue ()  {
				// return the maxVal
				return maxVal;
			}
			
			public void setBarMsg (int msgValue)  {
				// change the dispText value, which is used to determine what text,
				// if any, is displayed in the progress bar (use either MSG_NONE,
				// MSG_PERCENT, or MSG_NUMBERS)
				dispText = msgValue;
			}
			
			public void setColors (Color barColor, Color borderColor, Color textColor)  {
				// set the colors used by the progress bar components
				if (barColor != null) barClr = barColor;
				if (borderColor != null) borderClr = borderColor;
				if (textColor != null) textClr = textColor;
			}
			
			public void paint (Graphics g)  {
				// draw the actual progress bar to the screen
				// this is the bar itself
				g.setColor(barClr);
				g.fillRect(0, 0, (int)((currentVal * width) / maxVal), height);
				
				// this is the border around the bar
				g.setColor(borderClr);
				g.drawRect(0, 0, width, height);
				
				// this is the text to display (if any)
				g.setColor(textClr);
				if (dispText == MSG_PERCENT)
					centerText(String.valueOf((int)((currentVal * 100) / maxVal)) + "%", g, width, height);
				else if (dispText == MSG_NUMBERS)
					centerText("(" + currentVal + " of " + maxVal + ")", g, width, height);
			}
			
			private void centerText (String s, Graphics g, int w, int h)  {
				// from the centerText method in "Java Examples in a Nutshell"
				FontMetrics fm = this.getFontMetrics(this.getFont());
				if (fm == null)
					return;
				int sWidth = fm.stringWidth(s);
				int sx = (w - sWidth) / 2;
				int sy = (h - fm.getHeight()) / 2 + fm.getAscent();
				g.drawString(s, sx, sy);
			}
			
		}	// end of the ProgressBox class
		
	}	// end of the ProgressBar class
	
}


