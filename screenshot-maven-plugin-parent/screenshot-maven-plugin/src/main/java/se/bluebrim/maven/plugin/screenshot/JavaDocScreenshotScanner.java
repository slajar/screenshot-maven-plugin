package se.bluebrim.maven.plugin.screenshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.velocity.texen.util.FileUtil;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;


/**
 * Scans test classes for method annotated with the Screenshot annotation. Calls found methods and save a png-file with the same name as
 * return JComponent subclass. The png is saved in a doc-files directory alongside the source file for the JComponent subclass.
 * 
 * @author G Stack
 *
 */
public class JavaDocScreenshotScanner extends ScreenshotScanner 
{
	private static final String DOC_FILES = "doc-files";
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
			String screenshotName = createScreenshotName(javadocClass, method);
			createJavadocScreenshot(screenshotName, screenshotComponent, javadocClass);
			addImageTagToJavadoc(javadocClass, screenshotName);
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
		File docFilesDirectory = new File(sourceDirectory, org.springframework.util.ClassUtils.classPackageAsResourcePath(javadocClass) + "/" + DOC_FILES);
		docFilesDirectory.mkdirs();
		createScreenshotFile(screenshotComponent, docFilesDirectory, screenshotName);
	}

	/**
	 * Add missing image tag in class comment: The image tag look like this:
	 * <pre>
	 * &lt;img src="doc-files/ColorConstants.png"&gt;
	 * </pre>
	 * 
	 */
	private void addImageTagToJavadoc(Class javadocClass, String screenshotName)
	{
		final String srcPath = DOC_FILES + "/" + screenshotName + "." + FORMAT_PNG;
		File javaFile = new File(sourceDirectory, org.springframework.util.ClassUtils.convertClassNameToResourcePath(javadocClass.getName()) + ".java");
		boolean notAddedYet = true;
		if (!hasImageTag(getClassComment(javaFile), srcPath))
		{
			try {
				List lines = FileUtils.readLines(javaFile, "UTF-8");
				List<String> out = new ArrayList<String>();
				for (Object object : lines) {
					String line = (String)object;
					out.add(line);
					if (line.contains("/**") && notAddedYet)
					{
						out.add(" * <img src=\"" + srcPath + "\">");
						notAddedYet = false;
					}
				FileUtils.writeLines(javaFile, "UTF-8", out);		
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}		
	}
	
	private boolean hasImageTag(String comment, final String srcPath)
	{
		final MutableBoolean result = new MutableBoolean(false);
		
	    HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
			
			@Override
			public void handleSimpleTag(Tag tag, MutableAttributeSet a, int pos)
			{
				if (tag.equals(Tag.IMG))
				{
					Object attribute = a.getAttribute(HTML.Attribute.SRC);
					if (attribute instanceof String)
					{
						String srcAttribute = (String) attribute;
						if (srcPath.equals(srcAttribute))
							result.setValue(true);
					}
				}
			}
		};
	    try {
			new ParserDelegator().parse(new StringReader(comment), callback, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result.booleanValue();
	}


	private String getClassComment(File javaFile) {
		JavaDocBuilder builder = new JavaDocBuilder();
	    try {
			builder.addSource(javaFile);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}	    
	    JavaSource src = builder.getSources()[0];
	    JavaClass javaClass = src.getClasses()[0];
	    String comment = "<html>" + javaClass.getComment() + "</html>";
		return comment;
	}

}

