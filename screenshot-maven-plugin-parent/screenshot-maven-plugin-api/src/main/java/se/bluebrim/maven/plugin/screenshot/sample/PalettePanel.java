package se.bluebrim.maven.plugin.screenshot.sample;

import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.ClassUtils;

/**
 * Arrange added samples in rows with the specified number of columns.
 * The name of each sample is drawn below the sample.
 * 
 * @author Goran Stack
 *
 */
@SuppressWarnings("serial")
public class PalettePanel extends JPanel
{
	public static PalettePanel createFromStaticPaintFields(Class<?> ofClass, int noOfColumns)
	{
		Field[] fields = ofClass.getDeclaredFields();
		PalettePanel panel = new PalettePanel(noOfColumns);
		for (Field field : fields) {
			if (Paint.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()))
			{
				Paint paint;
				try {
					paint = (Paint) field.get(null);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				panel.addSample(paint, field.getName());
			}
		}
		return panel;
	}

	public static PalettePanel createFromStaticIconFields(Class<?> ofClass, int noOfColumns)
	{
		Field[] fields = ofClass.getDeclaredFields();
		PalettePanel panel = new PalettePanel(noOfColumns);
		for (Field field : fields) {
			if (Icon.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()))
			{
				Icon icon;
				try {
					icon = (Icon) field.get(null);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				panel.addSample(new JLabel(icon), field.getName());
			}
		}
		return panel;
	}

	public PalettePanel(int noOfColumns) 
	{
		setLayout(new MigLayout(new LC().wrapAfter(noOfColumns)));
		setOpaque(false);
	}
	
	public void addSample(JComponent sample, String name)
	{
		JPanel samplePanel = new NamedSamplePanel(sample, name);
		add(samplePanel, "center");			
	}
	
	public void addSample(Paint paint, String name)
	{
		addSample(new PaintSamplePanel(paint), name);
	}
}