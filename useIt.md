# Introduction #

We assume that you have a multi module project. Otherwise you put all configuration in your single pom file. This is a minimal configuration that let you run the javadoc goal only. The other goals need some more configuration.


## Configuration in the parent pom ##

Specify a property for the version number:

```
<properties>
    <screenshot-maven-plugin-version>0.0.4</screenshot-maven-plugin-version>
</properties>
```

Since the screenshot-maven-plugin artifacts do not appear in Maven Central Repository you have to add a repository and a pluginRepository element:

```
<repositories>
    <repository>
        <id>googlecode-screenshot-maven-plugin</id>
        <name>Screenshot Maven Plugin Repository</name>
        <url>http://screenshot-maven-plugin.googlecode.com/svn/mavenrepo</url>
     </repository>
</repositories>
```

```
<pluginRepositories>
    <pluginRepository>
        <id>googlecode-screenshot-maven-plugin</id>
        <name>Screenshot Maven Plugin Repository</name>
        <url>http://screenshot-maven-plugin.googlecode.com/svn/mavenrepo</url>
    </pluginRepository>
</pluginRepositories>
```

Add a plugin element to the pluginManagement part

```
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>se.bluebrim.maven.plugin</groupId>
            <artifactId>screenshot-maven-plugin</artifactId>
            <version>${screenshot-maven-plugin-version}</version>
            <configuration>
                <goalPrefix>screenshot</goalPrefix>
            </configuration>
         </plugin>
         .
         .
         .
     </plugins>
</pluginManagement>
```

Add a dependency element to the dependencyManagement part
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>se.bluebrim.maven.plugin</groupId>
            <artifactId>screenshot-maven-plugin-api</artifactId>
            <version>${screenshot-maven-plugin-version}</version>
            <scope>test</scope>
         </dependency>
         .
         .
         .
         </dependencies>
</dependencyManagement>
```

## Configuration in module pom ##

Add the following to the pom files of modules containing Swing classes that you like to generate screen shots of.

```
<dependencies>
    <dependency>
        <groupId>se.bluebrim.maven.plugin</groupId>
        <artifactId>screenshot-maven-plugin-api</artifactId>
    </dependency>
          .
          .
          .
</dependencies>
```

Verify the configuration by adding the following classes to one of those modules:

In `src/main/java` add:
```
package se.bluebrim.maven.plugin.screenshot.example;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The classic "Hello World" used to demonstrate the Screen Shot Maven Plugin.
 * The camera image was found at: http://wefunction.com/2008/07/function-free-icon-set
 * 
 * @author Goran Stack
 *
 */
@SuppressWarnings("serial")
public class HelloWorldPanel extends JPanel {

	public HelloWorldPanel() 
	{
		JLabel label = new JLabel("Hello World from Screen Shot Maven Plugin");
		ImageIcon icon;
		try {
			icon = new ImageIcon(new URL("http://screenshot-maven-plugin.googlecode.com/svn/site/images/camera.png"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		label.setIcon(icon);
		label.setFont(label.getFont().deriveFont(32f));
		label.setForeground(Color.DARK_GRAY);
		add(label);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED.darker(), 4), BorderFactory.createEmptyBorder(60, 20, 60, 20)));		
	}
}
```

In `src/test/java` add:
```
package se.bluebrim.maven.plugin.screenshot.example;

import javax.swing.JComponent;

import se.bluebrim.maven.plugin.screenshot.Screenshot;

/**
 * 
 * @author Goran Stack
 *
 */
public class HelloWorldPanelTest {

	@Screenshot
	public JComponent createScreenShot()
	{
		return new HelloWorldPanel();
	}
}
```

If you are using Eclipse adding these classes are very simple. Just copy the source from this page and paste it into the src/main or src/test folder in the Package Explorer view. Eclipse will create the package and the class for you.

From the command line run: `mvn screenshot:javadoc`

A `doc-files` folder containing a `HelloWorldPanel.png` is created at the same location as the source code for `HelloWorldPanel` class

![http://screenshot-maven-plugin.googlecode.com/svn/site/images/HelloWorldPanel.png](http://screenshot-maven-plugin.googlecode.com/svn/site/images/HelloWorldPanel.png)

The screenshot plugin detect the missing img tag in the Javadoc of `HelloWorldPanel` class and writes to the console.

`[INFO] Missing "<img src="doc-files/HelloWorldPanel.png">" in class:se.bluebrim.maven.plugin.screenshot.example.HelloWorldPanel`


To include the screen shot in the Javadoc of the `HelloWorldPanel` class copy the image tag from Maven console and paste into the Javadoc. You should now be able to see the image as part of the Javadoc.


