package se.bluebrim.maven.plugin.screenshot.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;

import se.bluebrim.maven.plugin.screenshot.Screenshot;
import se.bluebrim.maven.plugin.screenshot.ScreenshotDescriptor;

/**
 * Demonstrates how to create screenshots for a constant class containing visualizable resources.
 * In this case we are working with Colors but anything that can be painted by a Swing component will do.
 * {@code ColorConstantsTest} creates one gallery screenshot for the Javadoc of the ColorConstant class
 * and one screenshot to be included in the Javadoc for each static field declaration. The {@code @Screenshot}
 * annotaded methods are processed by the screenshot:javadoc goal and stores the images files in a "doc-files" directory
 * following Javadoc conventions.
 * 
 * @author Goran Stack
 *
 */
public class ColorConstantsTest {


	@Screenshot(oneForEachLocale = false, targetClass = ColorConstants.class)
	public JComponent createPaletteScreenshot() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Field[] fields = ColorConstants.class.getDeclaredFields();
		JPanel panel = new JPanel(new MigLayout(new LC().wrapAfter(7)));
		for (Field field : fields) {
			if (field.getType() == Color.class && Modifier.isStatic(field.getModifiers()))
			{
				Color color = (Color) field.get(null);
				JPanel sample = new NamedSamplePanel(new PaintSamplePanel(color), field.getName());
				panel.add(sample, "center");
			}
		}
		panel.setOpaque(false);
		return panel;
	}

	
	/**
	 * Creates a screen shot for each method in the PaintFactory 
	 */
	@Screenshot
	public Collection<ScreenshotDescriptor> createFieldScreenshots() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		List<ScreenshotDescriptor> paintSamples = new ArrayList<ScreenshotDescriptor>();
		Field[] fields = ColorConstants.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType() == Color.class && Modifier.isStatic(field.getModifiers()))
			{
				Color color = (Color) field.get(null);
				paintSamples.add(new ScreenshotDescriptor(new PaintSamplePanel(color), ColorConstants.class, field.getName().toLowerCase()));
			}
		}
		return paintSamples;
	}

	
	@SuppressWarnings("serial")
	public static class PaintSamplePanel extends JXPanel
	{
		private Dimension size;
		
		public PaintSamplePanel(Paint paint) 
		{
			this(paint, new Dimension(80, 80));
		}

		public PaintSamplePanel(Paint paint, Dimension size) 
		{
			this.size = size;
			CompoundPainter<PaintSamplePanel> painter = new CompoundPainter<PaintSamplePanel>(new CheckerboardPainter(), new MattePainter(paint, true));
			setBackgroundPainter(painter);
			setPaintBorderInsets(false);
			setBorder(new DropShadowBorder());
		}
		
		@Override
		public Dimension getPreferredSize() 
		{
			return size;
		}
	}
	
	@SuppressWarnings("serial")
	public static class NamedSamplePanel extends JXPanel
	{
		
		public NamedSamplePanel(JComponent samplePanel, String name) 
		{
			setLayout(new MigLayout(new LC().flowY()));
			setOpaque(false);
			add(samplePanel, "center");	
			JLabel label = new JLabel(name);
			label.setOpaque(false);
//			label.setFont(label.getFont().deriveFont(label.getFont().getSize2D() * 1.3f));	// Increase font size
			add(label, "center");
		
		}		
	}

}
