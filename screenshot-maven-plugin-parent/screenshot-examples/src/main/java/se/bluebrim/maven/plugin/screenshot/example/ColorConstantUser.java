package se.bluebrim.maven.plugin.screenshot.example;

import java.awt.Color;

/**
 * <p>
 * A class to demonstrate the visual experience of images included in the Javadoc
 * when activating code completion in your IDE.
 * </p>
 * In Eclipse IDE (works probably in other IDE's as well) try the following:
 * <ul>
 * <li>Hower the mouse over a ColorConstants class reference and see how the Javadoc pop ups</li>
 * <li>Try to assign {@code yourColor} with a color from {@code ColorConstants}</li>
 * </ul>
 * 
 * @author Goran Stack
 *
 */
public class ColorConstantUser {
	
	private Color myColor = ColorConstants.TRANSPARENT_BLUE;
	private Color yourColor;

}
