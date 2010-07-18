package se.bluebrim.maven.plugin.screenshot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXFrame;

import se.bluebrim.maven.plugin.screenshot.Screenshot;
import se.bluebrim.maven.plugin.screenshot.decorate.CalloutDecorator;
import se.bluebrim.maven.plugin.screenshot.decorate.Center;
import se.bluebrim.maven.plugin.screenshot.decorate.CompositeDecorator;
import se.bluebrim.maven.plugin.screenshot.decorate.DecoratorUtils;
import se.bluebrim.maven.plugin.screenshot.decorate.Emphasizer;
import se.bluebrim.maven.plugin.screenshot.decorate.FrameDecorator;
import se.bluebrim.maven.plugin.screenshot.decorate.ScreenshotDecorator;

public class ScreenshotTest {
	
	private JTextField textField;
	private JButton button;
	private JLabel label;
	
	public ScreenshotTest() 
	{
	}


	private void addComponents(JPanel panel) 
	{
		label = new JLabel("Hello world");
		textField = new JTextField();
		textField.setColumns(10);
		button = new JButton("Press me");

		panel.add(label);
		panel.add(textField);
		panel.add(button);
	}
	
	
	@Screenshot (targetClass=FrameDecorator.class)
	public JComponent createFrameDecoratorPanel()
	{
		JPanel panel = new DecoratedPanel();
		addComponents(panel);
		FrameDecorator frameDecorator = new FrameDecorator(Color.RED.darker(), 4, 8);
		frameDecorator.setInsets(new Insets(-5, -5, -5, -5));
		label.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, frameDecorator);
		button.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, new FrameDecorator(new Color(0xAFA313), 4, 8));
		return panel;
	}
	
	@Screenshot (targetClass=Emphasizer.class)
	public JComponent createEmphasizedComponent()
	{
		JPanel panel = new DecoratedPanel();
		addComponents(panel);
		button.putClientProperty(Emphasizer.CLIENT_PROPERTY_KEY, new Emphasizer());
		return panel;
	}

	@Screenshot (targetClass=Emphasizer.class, scene="deco")
	public JComponent createEmphasizedComponentAndCallout()
	{
		JPanel panel = new DecoratedPanel();
		addComponents(panel);
		button.putClientProperty(Emphasizer.CLIENT_PROPERTY_KEY, new Emphasizer());
		textField.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, new CalloutDecorator(1));
		button.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, new CalloutDecorator(2, new Center(new Point(0, 40))));
		return panel;
	}


	
	@Screenshot (targetClass=CalloutDecorator.class)
	public JPanel createCalloutDecoratorPanel()
	{
		JPanel panel = new DecoratedPanel();
		addComponents(panel);
		textField.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, new CalloutDecorator(8));
		return panel;
	}
	
	@Screenshot (targetClass=CalloutDecorator.class, scene="transparent")
	public JPanel createCalloutDecoratorTransparentPanel()
	{
		JPanel panel = createCalloutDecoratorPanel();
		panel.setOpaque(false);
		return panel;
	}
	
	@Screenshot (targetClass=CompositeDecorator.class)
	public JPanel createCompositeDecoratorPanel()
	{
		JPanel panel = new DecoratedPanel();
		addComponents(panel);
		CompositeDecorator composite = new CompositeDecorator();
		composite.add(new FrameDecorator(Color.RED.darker(), 4, 8));
		composite.add(new CalloutDecorator(1));
		button.putClientProperty(ScreenshotDecorator.CLIENT_PROPERTY_KEY, composite);
		return panel;
	}
	
	
	
	public static void main(String[] args) {
		final ScreenshotTest instance = new ScreenshotTest();
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				instance.openInWindow();
			}
		});
	}

	protected void openInWindow()
	{
		JXFrame window = new JXFrame(getClass().getSimpleName(), true);
		JPanel panel = new JPanel();
		panel.add(createFrameDecoratorPanel());
		panel.add(createCalloutDecoratorPanel());
		panel.add(createCompositeDecoratorPanel());
		panel.add(createEmphasizedComponent());
		panel.add(createEmphasizedComponentAndCallout());
		window.getContentPane().add(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);		
	}
	
	private static class DecoratedPanel extends JPanel
	{
		@Override
		public void paint(Graphics g) 
		{
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setClip(null);
			DecoratorUtils.decorateScreenshot(this, g2d);
		}
	}

}
