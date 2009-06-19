package spikes.sandro.cellrenderer;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import sneer.bricks.hardware.gui.images.Images;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.skin.widgets.reactive.LabelProvider;
import spikes.wheel.testutil.MemorySentinel;

class WindListCellRendererSample implements ListCellRenderer {

	static final Image _me = getImage();
	private static Image getImage(){
		return my(Images.class).getImage(ComplexRenderingSample.class.getResource("me.png"));
	}	
	
	public static void main(String args[]) {
		MemorySentinel.startLoggingSignificantMemoryUsageChanges();
		initGui();
	}

	private static void initGui() {
		JFrame frame = new JFrame("WindListCellRendererSample");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();

		final DefaultListModel model = new DefaultListModel();
		JList jlist = new JList(model);
		ListCellRenderer renderer = new WindListCellRendererSample(new LabelProvider<String>(){

			@Override
			public Signal<Image> imageFor(String element) {
				return my(Signals.class).constant(_me);
			}

			@Override
			public Signal<String> labelFor(String element) {
				return my(Signals.class).constant("bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, " +
						"bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla, bla");
			}
		});
		jlist.setCellRenderer(renderer);
		JScrollPane scrollPane = new JScrollPane(jlist);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JButton btn = new JButton("Add...");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.addElement(new Date().toString());
			}
		});

		contentPane.add(btn, BorderLayout.SOUTH);
		frame.setSize(300, 300);
		frame.setVisible(true);
	}	
	
	
	private static final String SHOUT = "shout";
	private static final String SHOUTERS_NICK = "shoutersNick";
	
	private static final int SPACE_BETWEEN_LINES = 0;
	private static final int SCROLL_WIDTH = 10;
	
	private final LabelProvider<String> _labelProvider;
	private boolean _flag;
	
	WindListCellRendererSample(LabelProvider<String> labelProvider) {
		_labelProvider = labelProvider;
	}

	@Override
	public Component getListCellRendererComponent(JList jList, Object element, int ignored2, boolean isSelected, boolean cellHasFocus) {
		String shout = (String)element;
		JComponent nick = createNick(shout);
		JComponent shoutTime = createShoutTime(isSelected);
		JComponent shoutText = createShoutText(shout);
		JComponent root = createRootPanel(nick, shoutTime, shoutText, isSelected);
		
		FixedResizerCopy.pack(shoutText, jList.getWidth() - SCROLL_WIDTH, 25);
		
		addLineSpace(root);
		return root;
	}
	
	private JComponent createNick(String shout) {
		_flag = !_flag;
		if (_flag)
			return getNickAsIcon(shout);
		return getNickAsText();
	}
	
	private JComponent getNickAsIcon(String shout) {
		Signal<? extends Image> signalImage = _labelProvider.imageFor(shout);
		JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()), SwingConstants.LEFT);
		icon.setOpaque(false);
		return icon;
	}

	private JComponent getNickAsText() {
		String nick = "NickName";
		JLabel labelNick = new JLabel(nick,  SwingConstants.LEFT);
		labelNick.setFont(new Font(labelNick.getFont().getFontName() , Font.BOLD, 11));
		labelNick.setForeground(Color.DARK_GRAY);
		labelNick.setOpaque(false);
		return labelNick;
	}

	private JComponent createShoutTime(boolean isSelected) {
		JLabel label = new JLabel(" ",  SwingConstants.RIGHT);
		label.setFont(new Font(label.getFont().getFontName() , 0, 11));
		label.setOpaque(false);
		if(isSelected) label.setForeground(Color.WHITE);
		else label.setForeground(Color.LIGHT_GRAY);
		return label;
	}

	private JComponent createShoutText(String shout) {
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