package se.bluebrim.maven.plugin.screenshot.sample;

import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.apache.commons.lang.ClassUtils;

import se.bluebrim.maven.plugin.screenshot.ScreenshotDescriptor;

/**
 * 
 * @author Goran Stack
 *
 */
public class SampleUtil {

	/**
	 * 
	 * @return A Collection of ScreenshotDescriptor's for each static field returning
	 * a Paint typed object.
	 */
	public static Collection<ScreenshotDescriptor> createStaticPaintFieldScreenshots(Class ofClass)
	{
		List<ScreenshotDescriptor> paintSamples = new ArrayList<ScreenshotDescriptor>();
		Field[] fields = ofClass.getDeclaredFields();
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
				paintSamples.add(new ScreenshotDescriptor(new PaintSamplePanel(paint), ofClass, field.getName().toLowerCase()));
			}
		}
		return paintSamples;
	}
	
	public static Collection<ScreenshotDescriptor> createStaticIconFieldScreenshots(Class ofClass)
	{
		List<ScreenshotDescriptor> icontSamples = new ArrayList<ScreenshotDescriptor>();
		Field[] fields = ofClass.getDeclaredFields();
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
				icontSamples.add(new ScreenshotDescriptor(new JLabel(icon), ofClass, field.getName().toLowerCase()));
			}
		}
		return icontSamples;
	}

}
