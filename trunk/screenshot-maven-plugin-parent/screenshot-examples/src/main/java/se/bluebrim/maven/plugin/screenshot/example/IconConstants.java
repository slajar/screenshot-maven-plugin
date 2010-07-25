package se.bluebrim.maven.plugin.screenshot.example;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * <img src="doc-files/IconConstants.png">
 * <p>
 * This is an example of a resource class with visual samples included in the Javadoc.
 * The images for the Javadoc are created by a few lines of  code in a test class.
 * The test class is processed by the screenshot-maven-plugin in a Maven build to produce the images.
 * </p>
 * 
 * @author Goran Stack
 *
 */
public class IconConstants {
	
	/**
	 * <img src="doc-files/IconConstants-dialog_error.png">
	 */
    public final static Icon DIALOG_ERROR = loadIcon("dialog-error.png");
	
	/**
	 * <img src="doc-files/IconConstants-dialog_information.png">
	 */
    public final static Icon DIALOG_INFORMATION = loadIcon("dialog-information.png");
	
	/**
	 * <img src="doc-files/IconConstants-dialog_question.png">
	 */
    public final static Icon DIALOG_QUESTION = loadIcon("dialog-question.png");
	
	/**
	 * <img src="doc-files/IconConstants-dialog_warning.png">
	 */
    public final static Icon DIALOG_WARNING = loadIcon("dialog-warning.png");

    private static Icon loadIcon(String name)
    {
    	return new ImageIcon(IconConstants.class.getResource("/images/" + name));
    }

}
