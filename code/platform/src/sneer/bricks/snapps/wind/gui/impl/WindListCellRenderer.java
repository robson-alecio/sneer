package sneer.bricks.snapps.wind.gui.impl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

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

import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.skin.widgets.reactive.LabelProvider;
import sneer.bricks.snapps.wind.Shout;

class WindListCellRenderer implements ListCellRenderer {

	private static final int MIN_SHOUT_WIDTH = 200;
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
		JComponent root = createRootPanel( nick, shoutTime, shoutText, isSelected, jList);

		addLineSpace(root);
		return root;
	}
	
	private JComponent createNick(Shout shout) {
		if (ShoutUtils.isMyOwnShout(shout))
			return getNickAsIcon(shout);
		return getNickAsText(shout);
	}
	
	private JComponent getNickAsIcon(Shout shout) {
		Signal<? extends Image> signalImage = _labelProvider.imageFor(shout);
		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()), SwingConstants.LEFT);
		icon.setOpaque(false);
		return icon;
	}

	private JComponent getNickAsText(Shout shout) {
		String nick = ShoutUtils.publisherNick(shout);
		JLabel labelNick = new JLabel(nick,  SwingConstants.LEFT);
		labelNick.setFont(new Font(labelNick.getFont().getFontName() , Font.BOLD, 11));
		labelNick.setOpaque(false);
		return labelNick;
	}

	private JComponent createShoutTime(Shout shout, @SuppressWarnings("unused") boolean isSelected) {
		JLabel label = new JLabel(ShoutUtils.getFormatedShoutTime(shout) + " ",  SwingConstants.RIGHT);
		label.setFont(new Font(label.getFont().getFontName() , 0, 11));
		label.setOpaque(false);
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
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	private void initDocumentStyles(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle( StyleContext.DEFAULT_STYLE );

	    Style sender = doc.addStyle( SHOUTERS_NICK, def );
	    StyleConstants.setFontSize( sender, 11 );
	    StyleConstants.setBold(sender, true);
	    
	    doc.addStyle( SHOUT, def );
	}

	private JComponent createRootPanel(JComponent nick, JComponent time, JComponent shout, @SuppressWarnings("unused") boolean isSelected, final JList list) {
		JPanel root = new JPanel();
		root.setLayout(new BorderLayout());
		root.setOpaque(true);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.add(nick, BorderLayout.CENTER);
		top.add(time, BorderLayout.EAST);
		
		root.add(top, BorderLayout.NORTH);

		JPanel horizontalLimit = new JPanel();
		horizontalLimit.setLayout(new BorderLayout());
		horizontalLimit.add(shout);
		horizontalLimit.setOpaque(false);
		int width = getShoutLimitWidth(list);
		
		Resizer.pack(shout, width - SCROLL_WIDTH, 0);
		root.add(horizontalLimit, BorderLayout.CENTER);

		return root;
	}

	private int getShoutLimitWidth(final JList list) {
		int width = list.getSize().width;
		if(width<MIN_SHOUT_WIDTH) 
			width=MIN_SHOUT_WIDTH;
		return width;
	}

	private void addLineSpace(JComponent root) {
		Dimension psize = root.getPreferredSize();
		root.setPreferredSize(new Dimension(psize.width, psize.height + SPACE_BETWEEN_LINES));
	}
}