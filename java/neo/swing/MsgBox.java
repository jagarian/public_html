package neo.swing;

/*
 * This is an example of how to use MessageBoxes and InputBoxes in a Java
 * agent in Lotus Notes. This functionality wasn't included in the Java Notes classes,
 * so you have to write the interfaces yourself.
 *
 * The MessageBox class below uses the java.awt classes for windowing. java.awt
 * should already be included with the Java classes that ship with Notes, so you
 * don't have to include anything special (like the Java Swing classes) with any
 * agents that you use this class with. Example usage is in the NotesMain() method.
 *
 * Make sure you don't use a MessageBox with an agent that runs in the background!
 * The MessageBoxes never time out, so you could potentially hang an agent pretty
 * easily by trying to use one of these things in a scheduled or a Web agent.
 *
 * Julian Robichaux -- http://www.nsftools.com
 */
/**
 * 	@Class Name	: 	MessageBox.java
 * 	@파일설명		: 	
 * 	@Version		: 	1.0
 *	@Author		: 	hoon09
 * 	@Copyright		: 	All Right Reserved
 **********************************************************************************************
 * 작업일 			버젼	구분		작업자		내용
 * --------------------------------------------------------------------------------------------
 *	2005-05-01 	1.4	생성		hoon09		source create	 (삼성전기)
 *	2006-11-23 	1.4	수정		hoon09		code convention apply (멀티캠퍼스)
 *	2009-07-03	1.6	수정		hoon09		code convention apply (국민은행, 펜타시큐리티)
 **********************************************************************************************             
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 	@Class Name	: 	MsgBox.java
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
public class MsgBox  {

	public void MessageBox() {

		try {
			//Session session = getSession();
			//AgentContext agentContext = session.getAgentContext();
			
			// (Your code goes here)
			// These are examples of how to call the MessageBox class
			// to display dialogs and input boxes to the user in a Notes
			// Java agent
			MessageBox mb = new MessageBox();
			int selection = mb.doOkCancelDialog("Title", "Message\nMore message");
			//System.out.println("The button pressed was " + selection);
			
			String input = mb.doInputBox("Title", "This is a really long message. \n" + 
										"This is a really long message. " + "This is a really long message. " + 
										"This is a really long message. " + "This is a really long message. " + 
										"This is a really long message. " + "This is a really long message. " + 
										"This is a really long message. " + "This is a really long message. " 
										, "");
			System.out.println("The text entered was " + input);
			
			String[] choiceList = { "apples", "oranges", "bananas" };
			String choice = mb.doDropdownBox("Title", "Message", choiceList);
			System.out.println("The choice selected was " + choice);
			
			String[] choiceMulti = mb.doMultilistBox("Title", "Message", choiceList, true);
			StringBuffer cmString = new StringBuffer("");
			if (choiceMulti == null)  {
				cmString.append("");
			}  else  {
				for (int i = 0; i < java.lang.reflect.Array.getLength(choiceMulti); i++)
					cmString.append(choiceMulti[i] + "; ");
			}
			System.out.println("The choices selected were " + cmString);
			
			mb.dispose();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* 
	 * The MessageBox class allows you to create simple message boxes
	 * using only the java.awt classes. All you have to do is create an instance
	 * of a MessageBox and use it to call the doOkCancelDialog or the
	 * doInputBox methods. You can use a single MessageBox instance to
	 * create multiple dialogs.
	 *
	 * This class has been implemented here as an inner class, but there's
	 * no reason why it couldn't be a class all by itself. The functionality to
	 * create buttons and messageboxes has been modularized, so it should
	 * be easy for you to add your own methods to this class, or make global
	 * modifications as you desire.
	 *
	 * The only really complicated/weird thing I had to do was to write and
	 * include the MultiLineLabel class, which is included towards the end
	 * of this class as an inner class. Read the comments associated with
	 * that class to see what's going on there.
	 *
	 * Make sure you import java.awt.* and java.awt.event.*
	 *
	 * Julian Robichaux -- http://www.nsftools.com
	 */
	class MessageBox {
		
		public final int NO_ACTION = -1;
		public final int CANCEL_ACTION = 0;
		public final int OK_ACTION = 1;
		int actionCode = NO_ACTION;
		
		Frame parent;
		Dialog msgbox;
		MultiLineLabel msglabel;		// our custom class, defined later
		Panel buttons;
		Button ok, cancel;
		TextField textbox;
		Choice dropdown;
		List multilist;
		
		public MessageBox ()  {
			// this is the invisible Frame we'll be using to call all of our Dialogs
			parent = new Frame();
		}
		
		public void dispose ()  {
			// use this when you're ready to clean up
			try { msgbox.dispose(); }  catch (Exception e)  {}
			try { parent.dispose(); }  catch (Exception e)  {}
		}
		
		/* 
		 * This method will create a simple dialog box with a title, a message,
		 * and Ok/Cancel buttons. It will halt execution of your program until
		 * one of the buttons is clicked, and it will return either OK_ACTION
		 * if the Ok button is clicked, or CANCEL_BUTTON if the Cancel
		 * button is clicked.
		 */
		public int doOkCancelDialog (String title, String message)  {
			actionCode = NO_ACTION;
			try  {
				// create the messagebox
				msgbox = new Dialog(parent, title, true);
				msgbox.setLayout(new BorderLayout());
				
				// create the label
				initMsglabel(message);
				msgbox.add("North", msglabel);
				
				// create the OK and Cancel buttons
				buttons = new Panel();
				buttons.setLayout(new FlowLayout());
				initOkButton();
				buttons.add(ok);
				initCancelButton();
				buttons.add(cancel);
				msgbox.add("South", buttons);
				
				// display everything (the system will wait until a button is pressed
				// before returning)
				displayMsgbox();
				msgbox.dispose();		// just in case the ActionListeners didn't fire...
			}  catch (Exception e)  {
			}
			
			return actionCode;
		}
	
		/* 
		 * This method will create a dialog box that allows the user to enter
		 * text into a field. It also has a title, a message, and Ok/Cancel buttons, 
		 * and will halt execution of your program until one of the buttons is
		 * clicked or the user enters text into the field and presses "Enter".
		 * If the user clicks the Ok button or enters text and presses "Enter",
		 * then the text in the field will be returned; otherwise, this method will
		 * return null (which usually indicates that the user clicked Cancel).
		 */
		public String doInputBox (String title, String message, String defaultText)  {
			actionCode = NO_ACTION;
			try  {
				// create the messagebox
				msgbox = new Dialog(parent, title, true);
				msgbox.setLayout(new BorderLayout());
				
				// create the label
				initMsglabel(message);
				msgbox.add("North", msglabel);
				
				// create the text field
				initTextbox(defaultText);
				msgbox.add("Center", textbox);
				
				// create the OK and Cancel buttons
				buttons = new Panel();
				buttons.setLayout(new FlowLayout());
				initOkButton();
				buttons.add(ok);
				initCancelButton();
				buttons.add(cancel);
				msgbox.add("South", buttons);
				
				// display everything (the system will wait until a button is pressed
				// before returning)
				displayMsgbox();
				msgbox.dispose();		// just in case the ActionListeners didn't fire...
			}  catch (Exception e)  {
			}

			if (actionCode == OK_ACTION)
				return textbox.getText();
			else
				return null;
		}
		
		/* 
		 * This method will create a dialog box that allows the user to select from
		 * a dropdown list. It also has a title, a message, and Ok/Cancel buttons, 
		 * and will halt execution of your program until one of the buttons is
		 * clicked. If the user clicks the Ok button then the text in the field will be 
		 * returned; otherwise, this method will return null (which usually indicates 
		 * that the user clicked Cancel).
		 */
		public String doDropdownBox (String title, String message, String[] selections)  {
			actionCode = NO_ACTION;
			try  {
				// create the messagebox
				msgbox = new Dialog(parent, title, true);
				msgbox.setLayout(new BorderLayout());
				
				// create the label
				initMsglabel(message);
				msgbox.add("North", msglabel);
				
				// create the dropdown box
				initDropdown(selections);
				msgbox.add("Center", dropdown);
				
				// create the OK and Cancel buttons
				buttons = new Panel();
				buttons.setLayout(new FlowLayout());
				initOkButton();
				buttons.add(ok);
				initCancelButton();
				buttons.add(cancel);
				msgbox.add("South", buttons);
				
				// display everything (the system will wait until a button is pressed
				// before returning)
				displayMsgbox();
				msgbox.dispose();		// just in case the ActionListeners didn't fire...
			}  catch (Exception e)  {
			}

			if (actionCode == OK_ACTION)
				return dropdown.getSelectedItem();
			else
				return null;
		}
		
		/* 
		 * This method will create a dialog box that allows the user to select from
		 * a list of options (use the allowMultiSelect parameter to indicate whether
		 * the user can select multiple items from the list [true] or just one [false]). 
		 * It also has a title, a message, and Ok/Cancel buttons, and will halt 
		 * execution of your program until one of the buttons is clicked. If the user 
		 * clicks the Ok button then the text in the field will be returned; otherwise, 
		 * this method will return null (which usually indicates that the user clicked 
		 * Cancel or didn't select anything).
		 */
		public String[] doMultilistBox (String title, String message, String[] selections, boolean allowMultiSelect)  {
			actionCode = NO_ACTION;
			try  {
				// create the messagebox
				msgbox = new Dialog(parent, title, true);
				msgbox.setLayout(new BorderLayout());
				
				// create the label
				initMsglabel(message);
				msgbox.add("North", msglabel);
				
				// create the multilist field
				initMultilist(4, allowMultiSelect, selections);
				msgbox.add("Center", multilist);
				
				// create the OK and Cancel buttons
				buttons = new Panel();
				buttons.setLayout(new FlowLayout());
				initOkButton();
				buttons.add(ok);
				initCancelButton();
				buttons.add(cancel);
				msgbox.add("South", buttons);
				
				// display everything (the system will wait until a button is pressed
				// before returning)
				displayMsgbox();
				msgbox.dispose();		// just in case the ActionListeners didn't fire...
			}  catch (Exception e)  {
			}

			if ((actionCode == OK_ACTION) && 
				(java.lang.reflect.Array.getLength(multilist.getSelectedItems()) > 0))
				return multilist.getSelectedItems();
			else
				return null;
		}
		
		/*
		 * The private methods below are simply reusable modular functions for
		 * creating various elements that may appear in a MessageBox dialog.
		 * They make it easier to write new public methods that create different
		 * kinds of dialogs, and also allow you to make global changes to all your
		 * dialog components pretty easily.
		 */
		 private void initMsglabel (String message)  {
		 	// the actual message in the MessageBox (using the custom
		 	// MultiLineLabel class, included below)
		 	msglabel = new MultiLineLabel(message);
		 }
		
		private void initOkButton ()  {
			// the OK button
			ok = new Button("OK");
			ok.addActionListener( new ActionListener()  {
				public void actionPerformed(ActionEvent e)  {
					actionCode = OK_ACTION;
					msgbox.dispose();
				}
			} );
		}
		
		private void initCancelButton ()  {
			// the Cancel button
			cancel = new Button("Cancel");
			cancel.addActionListener( new ActionListener()  {
				public void actionPerformed(ActionEvent e)  {
					actionCode = CANCEL_ACTION;
					msgbox.dispose();
				}
			} );
		}
		
		private void initTextbox (String defaultText)  {
			// the field that allows a user to enter text in an InputBox
			textbox = new TextField(defaultText, 40);
			// the ActionListener here will get called if the user presses "Enter"
			textbox.addActionListener( new ActionListener()  {
				public void actionPerformed(ActionEvent e)  {
					actionCode = OK_ACTION;
					msgbox.dispose();
				}
			} );
		}
		
		private void initDropdown (String[] selections)  {
			// a dropdown box that allows a user to select from a list of
			// multiple items
			dropdown = new Choice();
			for (int i = 0; i < java.lang.reflect.Array.getLength(selections); i++)
				dropdown.add(selections[i]);
		}
		
		private void initMultilist (int numOfLines, boolean allowMultiSelect, String[] selections)  {
			// a multiple selection box that allows a user to select one or 
			// more items. numOfLines indicates how many lines should be
			// visible at a time, allowMultiSelect is a boolean value indicating 
			// whether or not the user should be allowed to select more than
			// one item from the list, and selections is an array of Strings that 
			// is used to populate the selection box
			multilist = new List(numOfLines, allowMultiSelect);
			for (int i = 0; i < java.lang.reflect.Array.getLength(selections); i++)
				multilist.add(selections[i]);
		}
		
		private void displayMsgbox ()  {
			// once all of the components have been added to a dialog,
			// use this method to display it
			Dimension d = msgbox.getToolkit().getScreenSize();
			msgbox.setLocation(d.width/3, d.height/3);		// center the dialog (sort of)
			msgbox.pack();						// organize all its components
			msgbox.setResizable(false);		// make sure the user can't resize it
			msgbox.toFront();					// give it focus
			msgbox.show();					// and display it
		}
		
		/*
		 * Okay, this is a pain, but java.awt doesn't natively have a Label-type class
		 * that allows you to display text that's more than one line. So I had to write one
		 * myself. The class below is a modification of some code from the fantastic
		 * book, "Java in a Nutshell".
		 *
		 * The big change I made was to allow this multi-line label to have a fixed width,
		 * so the Label wouldn't fly off the screen if you had a big paragraph of text to
		 * display. The width is specified in "columns", which I defined as the width of
		 * the letter "X" in whatever font is being used. The text that you add to the label
		 * is automatically split into chunks that are no longer than the number of columns
		 * specified (you'll see the code to do this in the parseLabel method).
		 *
		 * This sample implementation is an inner class of the MessageBox class, although
		 * it could also be a separate class all by itself. I made it an inner class to make it
		 * easier to copy and paste everything from one agent to another.
		 *
		 * Julian Robichaux -- http://www.nsftools.com
		 */
		class MultiLineLabel extends Canvas  {
			// a custom class that will display a text label at a fixed width across
			// multiple lines
			// (modification of MultiLineLabel class from "Java in a Nutshell")
			String label;
			String[] lines;
			int rows = 1;
			int cols = 40;
			int margin = 6;
			int rowHeight;
			int lineAscent;
			int maxWidth;
			
			public MultiLineLabel (String text)  {
				// create a label with the default width
				label = text;
			}
			
			public MultiLineLabel (String text, int columns)  {
				// create a label with the specified number of "columns" (where a column
				// is the width of "X" in the font that we're using)
				if (columns > 0)
					cols = columns;
				label = text;
			}
			
			protected void measure ()  {
				// get the global values we use in relation to our current font
				FontMetrics fm = this.getFontMetrics(this.getFont());
				if (fm == null)
					return;
				
				rowHeight = fm.getHeight();
				lineAscent = fm.getAscent();
				maxWidth = fm.stringWidth("X") * cols;
			}
			
			private int stringWidth (String text)  {
				// calculate the width of a String using our current font
				FontMetrics fm = this.getFontMetrics(this.getFont());
				if (fm == null)
					return 0;
				return fm.stringWidth(text);
			}
			
			public Font getFont ()  {
				// return the font that we're currently using
				return super.getFont();
			}
			
			public void setFont (Font f)  {
				// change the font that we're currently using, and redraw the
				// label with the new font
				super.setFont(f);
				repaint();
			}
			
			public void addNotify ()  {
				// automatically invoked after our label/Canvas is created but 
				// before it's displayed (FontMetrics aren't available until
				// super.addNotify() has been called)
				super.addNotify();
				measure();
			}
			
			public Dimension getPreferredSize ()  {
				// called by the LayoutManager to find out how big we want to be
				if (lines == null)
					setText(label);
				return new Dimension(maxWidth + (2 * margin), (rows * rowHeight) + (2 * margin));
			}
			
			public Dimension getMinimumSize ()  {
				// called by the LayoutManager to find out what our bare minimum
				// size requirements are
				if (lines == null)
					setText(label);
				return new Dimension(maxWidth, (rows * rowHeight));
			}
			
			public void setText (String text)  {
				// change the text we're using for this label
				label = text;
				parseLabel();
			}
			
			private void parseLabel ()  {
				// parses the text we want to display in the label, so that the lines[]
				// variable contains lines of text that are no wider than maxWidth
				String token, word;
				StringBuffer msg = new StringBuffer("");
				StringBuffer line = new StringBuffer("");
				
				// do an initial measurement, to make sure we have maxWidth
				measure();
				rows = 0;
				
				// we'll be tokenizing the label String twice, first at every end-of-line
				// character ('\n') and then at every space (if the lines are too long), 
				// in order to generate an StringBuffer of proper length lines, all 
				// separated by \n
	 			java.util.StringTokenizer st1 = new java.util.StringTokenizer(label, "\n", false);
				while (st1.hasMoreTokens())  {
					token = st1.nextToken();
					if (stringWidth(token) <= maxWidth)  {
						// if the whole line is shorter than the maxWidth allowed, we can
						// just add this line to the buffer and get the next one
						rows++;
						msg.append(token + "\n");
					}  else  {
						// if the line was too long, we'll have to break it up manually by
						// tokenizing the line at every space and adding words together
						// one by one until we have a line that's greater than maxWidth;
						// then we can add that shorter line to the buffer and keep doing
						// that until we're out of words
						java.util.StringTokenizer st2 = new java.util.StringTokenizer(token, " ", false);
						while (st2.hasMoreTokens())  {
							word = st2.nextToken();
							if ((stringWidth(line.toString()) + stringWidth(word)) > maxWidth)  {
								rows++;
								msg.append(line.toString() + "\n");
								line.setLength(0);
								line.append(word + " ");
							}  else  {
								line.append(word + " ");
							}
						}
						// after we've run out of words, add any remaining text to the buffer, too
						if (line.length() > 0)  {
								rows++;
								msg.append(line.toString() + "\n");
						}
					}
				}
				
				// once we're done, split the buffer into the lines[] array
				java.util.StringTokenizer st = new java.util.StringTokenizer(msg.toString(), "\n");
				lines = new String[rows];
				for (int i = 0; i < rows; i++)
					lines[i] = st.nextToken();
					
			}
			
			public void paint (Graphics g)  {
				// draw the actual label to the screen, with space around the edges
				// based on the margins we've specified (for some reason, we have to
				// call setLabel instead of just parseText here in order to get the size
				// right, which is a little redundant, but whatever works...)
				int y;
				setText(label);
				Dimension d = this.getSize();
				y = lineAscent + (d.height - (rows * rowHeight)) / 2;
				for (int i = 0; i < rows; i++, y += rowHeight)
					g.drawString(lines[i], margin, y);
			}
			
		}		// end of the MultiLineLabel class
		
	}

}

