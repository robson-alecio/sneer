package snapps.wind.gui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import snapps.wind.Shout;
import sneer.kernel.container.Inject;
import sneer.pulp.contacts.Contact;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.skin.widgets.reactive.LabelProvider;
import wheel.reactive.Signal;

class RListStyledCellRenderer implements ListCellRenderer {

	@Inject
	private static KeyManager _keys;

	@Inject
	private static OwnNameKeeper _ownName;
	
	private static final String SHOUT = "shout";
	private static final String SHOUT_TIME = "shoutTime";
	private static final String SHOUTERS_NICK = "shoutersNick";
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm");
	private static final int SCROLL_WIDTH = 10;
	
	private static final int SPACE_BETWEEN_LINES = 5;
	
	private final LabelProvider<Shout> _labelProvider;
	
	RListStyledCellRenderer(LabelProvider<Shout> labelProvider) {
		_labelProvider = labelProvider;
	}

	@Override
	public Component getListCellRendererComponent(JList jList, Object element, int ignored2, boolean isSelected, boolean cellHasFocus) {
		Shout shout = (Shout)element;
		JComponent icon = createIcon(shout);
		JComponent text = createText(shout);
		JComponent root = createRootPanel(icon, text);

		Resizer.pack(text, jList.getWidth() - SCROLL_WIDTH);
		addLineSpace(root);
		return root;
	}
	
	private JComponent createIcon(Shout shout) {
		Signal<Image> signalImage = _labelProvider.imageFor(shout);
		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()));
		icon.setOpaque(false);
		return icon;
	}
	
	private JComponent createText(Shout shout) {
		StyledDocument doc = new DefaultStyledDocument();
	    initDocumentStyles(doc);
	    
		appendStyledText(doc, getShoutersNick(shout), SHOUTERS_NICK);
		appendStyledText(doc, getFormatedShoutTime(shout) + " ", SHOUT_TIME);
		Signal<String> signalText = _labelProvider.labelFor(shout);
		appendStyledText(doc, signalText.currentValue(), SHOUT);

		JTextPane result = new JTextPane();
		result.setDocument(doc);
		return result;
	}

	private void appendStyledText(StyledDocument doc, String msg, String style) {
		try {
			doc.insertString(doc.getLength(), msg, doc.getStyle(style));
		} catch (BadLocationException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private String getFormatedShoutTime(Shout shout) {
		return FORMAT.format(new Date(shout.publicationTime));
	}

	private String getShoutersNick(Shout shout) {
		if (isMyOwnShout(shout))
			return "";
		
		Contact contact = _keys.contactGiven(shout.publisher);
		return contact == null ? "<Unknown> " : contact.nickname().currentValue() + " ";
	}

	private boolean isMyOwnShout(Shout shout) {
		return _keys.ownPublicKey().equals(shout.publisher);
	}

	private void initDocumentStyles(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );

	    Style sender = doc.addStyle( SHOUTERS_NICK, def );
	    StyleConstants.setForeground(sender, Color.DARK_GRAY);
	    StyleConstants.setFontSize( sender, 11 );
	    StyleConstants.setBold(sender, true);
	    
	    Style time = doc.addStyle( SHOUT_TIME, def );
	    StyleConstants.setFontSize( time, 10 );
	    StyleConstants.setForeground(time, Color.LIGHT_GRAY);

	    doc.addStyle( SHOUT, def );
	}

	private JComponent createRootPanel(JComponent icon, JComponent area) {
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());
		root.setOpaque(false);

		root.add(icon, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		root.add(area, new GridBagConstraints(1, 0, 1, 1, 1., 1.,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		return root;
	}

	private void addLineSpace(JComponent root) {
		Dimension psize = root.getPreferredSize();
		root.setPreferredSize(new Dimension(psize.width, psize.height + SPACE_BETWEEN_LINES));
	}
	
}