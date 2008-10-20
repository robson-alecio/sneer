package snapps.wind.gui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import snapps.wind.Shout;
import sneer.skin.widgets.reactive.LabelProvider;
import wheel.reactive.Signal;

class WindListCellRenderer implements ListCellRenderer {

	private static final String SHOUT = "shout";
	private static final String SHOUTERS_NICK = "shoutersNick";
	private static final int SCROLL_WIDTH = 10;
	
	private static final int SPACE_BETWEEN_LINES = 0;
	
	private final LabelProvider<Shout> _labelProvider;
	
	WindListCellRenderer(LabelProvider<Shout> labelProvider) {
		_labelProvider = labelProvider;
	}

	@Override
	public Component getListCellRendererComponent(JList jList, Object element, int ignored2, boolean isSelected, boolean cellHasFocus) {
		Shout shout = (Shout)element;
		JComponent nick = createNick(shout);
		JComponent shoutTime = createShoutTime(shout, isSelected);
		JComponent shoutText = createShoutText(shout);
		JComponent root = createRootPanel(nick, shoutTime, shoutText, isSelected);

		Resizer.pack(shoutText, jList.getWidth() - SCROLL_WIDTH, 25);
		
		addLineSpace(root);
		return root;
	}
	
	private JComponent createNick(Shout shout) {
		if (ShoutUtils.isMyOwnShout(shout))
			return getNickAsIcon(shout);
		return getNickAsText(shout);
	}
	
	private JComponent getNickAsIcon(Shout shout) {
		Signal<Image> signalImage = _labelProvider.imageFor(shout);
		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()), SwingConstants.LEFT);
		icon.setOpaque(false);
		return icon;
	}

	private JComponent getNickAsText(Shout shout) {
		String nick = ShoutUtils.publisherNick(shout);
		JLabel labelNick = new JLabel(nick,  SwingConstants.LEFT);
		labelNick.setFont(new Font(labelNick.getFont().getFontName() , Font.BOLD, 11));
		labelNick.setForeground(Color.DARK_GRAY);
		labelNick.setOpaque(false);
		return labelNick;
	}

	private JComponent createShoutTime(Shout shout, boolean isSelected) {
		JLabel label = new JLabel(ShoutUtils.getFormatedShoutTime(shout) + " ",  SwingConstants.RIGHT);
		label.setFont(new Font(label.getFont().getFontName() , 0, 11));
		label.setOpaque(false);
		if(isSelected) label.setForeground(Color.WHITE);
		else label.setForeground(Color.LIGHT_GRAY);
		return label;
	}

	private JComponent createShoutText(Shout shout) {
		Signal<String> signalText = _labelProvider.labelFor(shout);
		return createTextComponent(signalText.currentValue(), SHOUT);
	}	
	
	private JComponent createTextComponent(String msg, String style) {
		StyledDocument doc = new DefaultStyledDocument();
	    initDocumentStyles(doc);
	    appendStyledText(doc, msg, style);
		JTextPane result = new JTextPane();
		result.setDocument(doc);
		result.setOpaque(false);
		return result;
	}	
	
	private void appendStyledText(StyledDocument doc, String msg, String style) {
		try {
			doc.insertString(doc.getLength(), msg, doc.getStyle(style));
		} catch (BadLocationException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void initDocumentStyles(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );

	    Style sender = doc.addStyle( SHOUTERS_NICK, def );
	    StyleConstants.setForeground(sender, Color.DARK_GRAY);
	    StyleConstants.setFontSize( sender, 11 );
	    StyleConstants.setBold(sender, true);
	    
	    doc.addStyle( SHOUT, def );
	}

	private JComponent createRootPanel(JComponent nick, JComponent time, JComponent shout, boolean isSelected) {
		JPanel root = new JPanel();
		root.setLayout(new GridBagLayout());
		root.setOpaque(true);

		if(isSelected) root.setBackground(Color.LIGHT_GRAY);
		else root.setBackground(Color.WHITE);
		
		root.add(nick, new GridBagConstraints(0, 0, 1, 1, 1., 0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		root.add(time, new GridBagConstraints(1, 0, 1, 1, 1., 0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		root.add(shout, new GridBagConstraints(0, 1, 2, 1, 1., 1.,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
//		nick.setBorder(new LineBorder(Color.BLACK));
//		time.setBorder(new LineBorder(Color.BLUE));
//		shout.setBorder(new LineBorder(Color.RED));
		return root;
	}

	private void addLineSpace(JComponent root) {
		Dimension psize = root.getPreferredSize();
		root.setPreferredSize(new Dimension(psize.width, psize.height + SPACE_BETWEEN_LINES));
	}
}