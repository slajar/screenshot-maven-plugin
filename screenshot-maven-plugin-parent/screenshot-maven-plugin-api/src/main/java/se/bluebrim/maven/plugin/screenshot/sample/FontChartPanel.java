package se.bluebrim.maven.plugin.screenshot.sample;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang.builder.CompareToBuilder;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import se.bluebrim.maven.plugin.screenshot.sample.SampleUtil.StaticMethodVisitor;

/**
 * <p>
 * The {@code FontChartPanel} draw an example of each specified font using a pangram and a character sets. 
 * Various sizes of the same font are grouped together. The fonts are specified as a Map to enable each font
 * to be associated with a string that is drawn as the font source.
 * </p>
 * 
 * <img src="doc-files/FontChartPanel.png">
 *  
 * TODO: Add a font metric figure according to: http://www.pyglet.org/doc/programming_guide/font_metrics.png and
 * http://www.myfirstfont.com/glossary.html
 * 
 * @author Goran Stack
 *
 */
@SuppressWarnings("serial")
public class FontChartPanel extends JPanel
{
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle(FontChartPanel.class.getPackage().getName() + ".messages");
	private static final Font INFO_FONT = new Font("SansSerif", Font.PLAIN, 12);
	private static final Color INFO_COLOR = Color.BLACK;		

	public static class FontPanel extends JPanel implements Comparable<FontChartPanel.FontPanel>
	{
		private static final float FONT_SAMPLE_SIZE = 16f;		// The size of the character set and digits sample
		private Font font;
		
		public FontPanel(Font font, String fontSource) 
		{
			this.font = font;
			setBackground(Color.WHITE);
			setLayout(new MigLayout(new LC().wrapAfter(1).insets("10 0 10 10")));
			addInfoLine(BUNDLE.getString("font.label") + ": " + getFontName());
			if (fontSource != null)
				addInfoLine(BUNDLE.getString("font.source") + ": " + fontSource);
			addSampleLine(font, "font.characterset");
			addSampleLine(font, "font.digits");
		}
	
		/**
		 * This kind of font is always derived to an appropriate size
		 * and is to small to display as a sample
		 */
		public boolean isOnePointSize() 
		{
			return font.getSize() == 1;
		}
	
		private void addInfoLine(String info)
		{
			JLabel infoLine = new JLabel(info);
			infoLine.setFont(INFO_FONT);
			infoLine.setForeground(INFO_COLOR);
			add(infoLine, "growx");			
		}
			
		public String getFontName() {
			return font.getFontName();
		}
				
	
		public Font getFont() {
			return font;
		}
	
		private void addSampleLine(Font font, String key) {
			JLabel fontSample = new BestRenderQualityLabel(BUNDLE.getString(key), font.deriveFont(FONT_SAMPLE_SIZE));
			add(fontSample, "growx");
		}
		
		public static JLabel createPangramSample(Font font)
		{
			return new BestRenderQualityLabel(BUNDLE.getString("font.pangram"), font);
		}
		
		@Override
		public int compareTo(FontChartPanel.FontPanel panel) {
			
			CompareToBuilder compareToBuilder = new CompareToBuilder();
			compareToBuilder.append(getFontName(), panel.getFontName());
			compareToBuilder.append(font.getSize2D(), panel.font.getSize2D());
			return compareToBuilder.toComparison();
		}
	}

	public static class FontSizeLabel extends JLabel
	{
		private static final NumberFormat FONT_SIZE_FORMAT = NumberFormat.getNumberInstance();
		{
			FONT_SIZE_FORMAT.setMinimumFractionDigits(0);		// Avoid to display font size as 22.0 but enable display of font size 22.5
		}

		public FontSizeLabel(Font font) 
		{
			super(FONT_SIZE_FORMAT.format(font.getSize2D()));
			setOpaque(false);
			setFont(INFO_FONT);
			setForeground(INFO_COLOR);
		}		

	}

	public static class DividerPanel extends JPanel
	{
		public DividerPanel() 
		{
			setOpaque(false);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setClip(null);
			g.setColor(INFO_COLOR);
			g.drawLine(0, getHeight(), getWidth(), getHeight());
		}
		
		@Override
		public Dimension getPreferredSize() 
		{
			return new Dimension(1, 1);
		}
	}

	public static class BestRenderQualityLabel extends JLabel
	{
		private static HashMap<Font, FontMetrics> FONT_METRICS = new HashMap<Font, FontMetrics>();

		public BestRenderQualityLabel(String string, Font font) 
		{
			super(string);
			setFont(font);
		}
	
		@Override
		public void paint(Graphics g) 
		{
			Graphics2D g2d = (Graphics2D)g;
			setRenderingHints(g2d);
	        super.paint(g);
		}

		private void setRenderingHints(Graphics2D g2d) {
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
	                RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
	                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		
		@Override
		/**
		 * @see http://stackoverflow.com/questions/2753514/java-friendlier-way-to-get-an-instance-of-fontmetrics
		 */
		public FontMetrics getFontMetrics(Font font) 
		{
		    if (FONT_METRICS.containsKey(font))
		    {
		        return FONT_METRICS.get(font);
		    }
		    FontMetrics fm = createFontMetrics(font);
		    FONT_METRICS.put(font, fm);
		    return fm;
		}
		

		private FontMetrics createFontMetrics(Font font)
		{
		    BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		    Graphics g = bi.getGraphics();
		    setRenderingHints((Graphics2D) g);
		    FontMetrics fm = g.getFontMetrics(font);
		    g.dispose();
		    bi = null;
		    return fm;
		}

		
	}

	public static FontChartPanel createFromStaticFontMethods(final Class<?> inClass)
	{
		final Map<Font, String> fontsWithSource = new HashMap<Font, String>();
		SampleUtil.eachStaticMethod(inClass, Font.class, new StaticMethodVisitor() {
			
			@Override
			public void visit(Object returnValue, Method method) 
			{
				if (returnValue != null)
					fontsWithSource.put((Font)returnValue, inClass.getSimpleName() + "." + method.getName());
			}
		});
		return new FontChartPanel(fontsWithSource);
	}
	
	/**
	 * 
	 * @param fontsWithSource is a map containing the fonts to display in the font chart. Each font has an associated source.
	 * The source can for example specify the class and the static method that provides the font
	 */
	public FontChartPanel(Map<Font, String> fontsWithSource)
	{
		setLayout(new MigLayout(new LC().wrapAfter(2).alignX("left")));
		setBackground(Color.WHITE);
		final List<FontChartPanel.FontPanel> samples = new ArrayList<FontChartPanel.FontPanel>();
		
		Set<Entry<Font, String>> entrySet = fontsWithSource.entrySet();
		for (Entry<Font, String> entry : entrySet) {
			samples.add(new FontPanel( entry.getKey(), entry.getValue()));
		}
		
		Collections.sort(samples);
		String fontName = null;
		for (FontChartPanel.FontPanel fontPanel : samples) 
		{
			if (!fontPanel.getFontName().equals(fontName))
			{
				if (fontName != null)
					add(new DividerPanel(), "span 2, growx");
				add(fontPanel, "span 2");
				fontName = fontPanel.getFontName();
			} 
			if (!fontPanel.isOnePointSize())
			{
				add(new FontSizeLabel(fontPanel.font));
				add(FontPanel.createPangramSample(fontPanel.font));
			}
		}			
	}
}