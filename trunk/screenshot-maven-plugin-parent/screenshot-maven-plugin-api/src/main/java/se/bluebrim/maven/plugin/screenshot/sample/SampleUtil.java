package se.bluebrim.maven.plugin.screenshot.sample;

import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;

import se.bluebrim.maven.plugin.screenshot.ScreenshotDescriptor;

/**
 * 
 * @author Goran Stack
 *
 */
public class SampleUtil {
	
	public interface StaticMethodVisitor
	{
		public void visit(Object returnValue, Method method);
	}
	
	public interface StaticFieldVisitor
	{
		public void visit(Object value, Field field);
	}
	
	
	public static void eachStaticField(Class<?> ofClass, Class<?> fieldType, StaticFieldVisitor visitor)
	{
		Field[] fields = ofClass.getDeclaredFields();
		for (Field field : fields) {
			if (fieldType.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers()))
			{
				Object value;
				try {
					value = field.get(null);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				visitor.visit(value, field);
			}
		}
	}
	
	public static void eachStaticMethod(Class<?> ofClass, Class<?> returnType, StaticMethodVisitor visitor)
	{
		Method[] methods = ofClass.getMethods();
		for (Method method : methods) {
			if (returnType.isAssignableFrom(method.getReturnType()) && Modifier.isStatic(method.getModifiers()))
			{
				Object returnValue;
					try {
						returnValue = method.invoke(null, new Object[]{});
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				visitor.visit(returnValue, method);
			}
		}
	}

	
	/**
	 * 
	 * @return A Collection of ScreenshotDescriptor's for each static field returning
	 * a Paint typed object.
	 */
	public static Collection<ScreenshotDescriptor> createStaticPaintFieldScreenshots(final Class<?> ofClass)
	{
		final List<ScreenshotDescriptor> paintSamples = new ArrayList<ScreenshotDescriptor>();
		eachStaticField(ofClass, Paint.class, new StaticFieldVisitor() {
			
			@Override
			public void visit(Object value, Field field) {
				paintSamples.add(new ScreenshotDescriptor(new PaintSamplePanel( (Paint)value ), ofClass, field.getName().toLowerCase()));
				
			}
		});
		return paintSamples;
	}
	
	/**
	 * 
	 * @return A Collection of ScreenshotDescriptor's for each static field returning
	 * a Paint typed object.
	 */
	public static Collection<ScreenshotDescriptor> createStaticIconFieldScreenshots(final Class<?> ofClass)
	{
		final List<ScreenshotDescriptor> icontSamples = new ArrayList<ScreenshotDescriptor>();
		eachStaticField(ofClass, Paint.class, new StaticFieldVisitor() {
			
			@Override
			public void visit(Object value, Field field) {
				icontSamples.add(new ScreenshotDescriptor(new JLabel( (Icon)value ), ofClass, field.getName().toLowerCase()));
				
			}
		});
		return icontSamples;
	}
	
	/**
	 * 
	 * @return A Collection of ScreenshotDescriptor's for each static field returning
	 * a Paint typed object.
	 */
	public static Collection<ScreenshotDescriptor> createStaticPaintMethodScreenshots(final Class<?> ofClass)
	{
		final List<ScreenshotDescriptor> paintSamples = new ArrayList<ScreenshotDescriptor>();
		eachStaticMethod(ofClass, Paint.class, new StaticMethodVisitor() {
			
			@Override
			public void visit(Object returnValue, Method method) {
				paintSamples.add(new ScreenshotDescriptor(new PaintSamplePanel( (Paint)returnValue ), ofClass,method.getName().toLowerCase()));
				
			}
		});
		return paintSamples;
	}
	
	/**
	 * 
	 * @return A Collection of ScreenshotDescriptor's for each static method returning
	 * a Paint typed object.
	 */
	public static Collection<ScreenshotDescriptor> createStaticIconMethodScreenshots(final Class<?> ofClass)
	{
		final List<ScreenshotDescriptor> icontSamples = new ArrayList<ScreenshotDescriptor>();
		eachStaticMethod(ofClass, Paint.class, new StaticMethodVisitor() {
			
			@Override
			public void visit(Object returnValue, Method method) {
				icontSamples.add(new ScreenshotDescriptor(new JLabel( (Icon)returnValue ), ofClass, method.getName().toLowerCase()));
				
			}
		});
		return icontSamples;
	}


}
