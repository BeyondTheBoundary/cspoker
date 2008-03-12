package org.client.GUI.Window;

import org.client.ClientCore;
import org.client.GUI.ClientGUI;
import org.client.GUI.Images.ImageFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
/**
 * A superclass of all windows
 * @author Cedric
 */
public abstract class Window {

	/**********************************************************
	 * Variables
	 **********************************************************/
	
	protected final ImageFactory imageFactory=ImageFactory.getImageFactory();
	/**
	 * The display for this window
	 */
	protected final Display display;
	/**
	 * The gui that uses this window
	 */
	protected final ClientGUI gui;
	/**
	 * The shell used in this window
	 */
	protected Shell shell;
	/**
	 * The graphical context of this window
	 */
	protected final GC gc;
	/**
	 * The client core of this window
	 */
	protected final ClientCore clientCore;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new window with the given display and gui
	 * @param display
	 * 			the given display
	 * @param gui
	 * 			the given gui
	 */
	public Window(Display display,final ClientGUI gui,final ClientCore clientCore){
		this.display=display;
		this.shell = new Shell(display);
		this.gui = gui;
		this.gc=new GC(this.shell);
		this.clientCore=clientCore;
	}
	/**********************************************************
	 * Methods
	 **********************************************************/
	protected void setAsCurrentWindow(){
		gui.setAsCurrentWindow(this);
	}
	/**
	 * Returns the width of this window
	 */
	public int getWindowWidth(){
		return (int) (getShell().getBounds().width);
	}
	/**
	 * Returns the height of this window
	 */
	public int getWindowHeight(){
		return (int) (getShell().getBounds().height);
	}
	public void draw(){
		shell.open();
		while (!shell.isDisposed()){
			drawImages();
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
	}
	public abstract void drawImages();
	/**
	 * Returns the gui used in this window
	 */
	public ClientGUI getGui(){
		return gui;
	}
	/**
	 * Returns the shell used in this window
	 */
	public Shell getShell(){
		return shell;
	}
	/**
	 * Returns the display used in this window
	 */
	public Display getDisplay(){
		return display;
	}
	/**
	 * Returns the graphical context used in this window
	 */
	public GC getGC(){
		return gc;
	}
	/**
	 * Returns the client core of this window
	 */
	public ClientCore getClientCore(){
		return clientCore;
	}
	protected void createCloseListener() {
		getShell().addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(getShell(), style);
		        messageBox.setText("Information");
		        messageBox.setMessage("Are you sure you want to exit?");
		        event.doit = messageBox.open() == SWT.YES;
		      }
		    });
	}
}