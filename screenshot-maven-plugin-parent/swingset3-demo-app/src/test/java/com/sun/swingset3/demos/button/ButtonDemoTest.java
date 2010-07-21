package com.sun.swingset3.demos.button;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import se.bluebrim.maven.plugin.screenshot.Screenshot;

import com.sun.swingset3.demos.button.ButtonDemo;

/**
 * Demonstrates how to create various screen shots of components in the SwingSet3 application
 * 
 * @author Goran Stack
 *
 */
public class ButtonDemoTest 
{
	@Screenshot
	public JComponent createButtonDemoScreenShot()
	{
		return new ButtonDemo();
	}
	
	@Screenshot (scene = "laf-nimbus")
	public JComponent createButtonDemoScreenShot2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		return new ButtonDemo();
	}
}
