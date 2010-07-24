package se.bluebrim.maven.plugin.screenshot.sample;

import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
			if (ClassUtils.getAllInterfaces(field.getType()).contains(Paint.class) && Modifier.isStatic(field.getModifiers()))
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

}
