package se.bluebrim.maven.plugin.screenshot;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXPanel;

/**
 * <p>
 * A small panel with a sample of something that can be drawn by {@link JComponent}
 * with a text label below the sample.
 * </p>
 * <img src="doc-files/NamedSamplePanel.png">
 * 
 * @author Goran Stack
 *
 */
@SuppressWarnings("serial")
public class NamedSamplePanel extends JXPanel {

	public NamedSamplePanel(JComponent samplePanel, String name) {
		setLayout(new MigLayout(new LC().flowY()));
		setOpaque(false);
		add(samplePanel, "center");
		JLabel label = new JLabel(name);
		label.setOpaque(false);
		add(label, "center");
	}
}