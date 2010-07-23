package se.bluebrim.maven.plugin.screenshot;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;


/**
 * Scans test classes for method annotated with the Screenshot annotation. Calls found methods and save a png-file with the same name as
 * return JComponent subclass. The png is saved in a doc-files directory alongside the source file for the JComponent subclass.
 * 
 * @author G Stack
 *
 */
public class JavaDocScreenshotScanner extends ScreenshotScanner 
{
	private File sourceDirectory;

	public JavaDocScreenshotScanner(AbstractMojo mojo, File testClassesDirectory, File classesDirectory, List<String> testClasspathElements, File sourceDirectory) 
	{
		super(mojo, testClassesDirectory, classesDirectory, testClasspathElements);
		this.sourceDirectory = sourceDirectory;
	}


	/**	
	 * Use the Javadoc convention to name the screen shot file. See:
	 * <a href="http://java.sun.com/j2se/javadoc/writingdoccomments/#images">Including images in Javadoc</a>
	 */
	@Override
	protected void handleFoundMethod(Class candidateClass, Method method) {
		Object screenshot = callScreenshotMethod(candidateClass, method);
		if (screenshot instanceof JComponent)
		{
			JComponent screenshotComponent = (JComponent)screenshot;
			Class javadocClass = getTargetClass(method, screenshotComponent);
			createJavadocScreenshot(createScreenshotName(javadocClass, method), screenshotComponent, javadocClass);
		} else
			if (screenshot instanceof Collection<?>)
			{
				int index = 0;
				Collection<ScreenshotDescriptor> screenShots = (Collection<ScreenshotDescriptor>) screenshot;
				for (ScreenshotDescriptor screenshotDescriptor : screenShots) {
					Class javadocClass = screenshotDescriptor.getTargetClass();
					String scene = StringUtils.isEmpty(screenshotDescriptor.getScene()) ? "" + index : screenshotDescriptor.getScene();
					createJavadocScreenshot(javadocClass.getSimpleName() + "-" + scene, screenshotDescriptor.getScreenshot(), javadocClass);
				}
			}
				
	}

	private void createJavadocScreenshot(String screenshotName, JComponent screenshotComponent, Class javadocClass) 
	{
		File docFilesDirectory = new File(sourceDirectory, org.springframework.util.ClassUtils.classPackageAsResourcePath(javadocClass) + "/doc-files");
		docFilesDirectory.mkdirs();
		createScreenshotFile(screenshotComponent, docFilesDirectory, screenshotName);
	}	

}
