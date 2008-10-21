package snapps.wind.gui.impl;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import snapps.wind.Shout;
import snapps.wind.Wind;
import snapps.wind.gui.WindGui;
import sneer.kernel.container.Inject;
import sneer.skin.snappmanager.InstrumentManager;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.ReactiveWidgetFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.Logger;
import wheel.io.ui.graphics.Images;
import wheel.reactive.Signal;
import wheel.reactive.impl.Constant;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListValueChange;

class WindGuiImpl implements WindGui {
	
	@Inject
	static private InstrumentManager _instruments;
	
	@Inject
	static private Wind _wind;

	@Inject
	static private ReactiveWidgetFactory _rfactory;

	private final static Signal<Image> _meImage;
	
	private ListWidget<Shout> _shoutsList;
	private TextWidget<JTextField> _myShout;

	private Container _container;

	private JToggleButton _lock;

	private Receiver<ListValueChange> _shoutReceiverToAvoidGc;

	private JScrollPane _scrollPane;

	static {
		_meImage = new Constant<Image>(loadImage("me.png"));
	}
	
	public WindGuiImpl() {
		_instruments.registerInstrument(this);
	} 
	
	private static Image loadImage(String fileName) {
		return Images.getImage(WindGuiImpl.class.getResource(fileName));
	}

	private Signal<Image> image(Shout shout) {
		if (!ShoutUtils.isMyOwnShout(shout))
			return new Constant<Image>(null);
		return _meImage;
	}	

	@Override
	public void init(Container container) {
		_container = container;
		ShoutLabelProvider labelProvider = new ShoutLabelProvider();
		_shoutsList = _rfactory.newList(_wind.shoutsHeard(), labelProvider, new WindListCellRenderer(labelProvider));
		_myShout = _rfactory.newTextField(new Constant<String>(""), _wind.megaphone(), true);
		_container.setBackground(_shoutsList.getComponent().getBackground());
		iniGui();
		initShoutReceiver();
		new WindClipboardSupport();
	}

	private void iniGui() {
		createScrollPane();
		_scrollPane.getViewport().add(_shoutsList.getComponent());
		
		_container.setLayout(new GridBagLayout());
		_container.add(_scrollPane, new GridBagConstraints(0, 0, 2, 1, 1., 1.,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		_container.add(_myShout.getComponent(), new GridBagConstraints(0, 1, 1, 1, 1., 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 0), 0, 0));

		_container.add(_lock, new GridBagConstraints(1, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 5), 0, 0));		
		
		_shoutsList.getComponent().setBorder(new EmptyBorder(0,0,0,0));
	}
	
	private void createScrollPane() {
		_scrollPane = new JScrollPane();
		_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_scrollPane.setMinimumSize(size(_container));
		_scrollPane.setPreferredSize(size(_container));
		_scrollPane.setBorder(new TitledBorder(new EmptyBorder(5,5,2,2), getName()));
		_scrollPane.setOpaque(false);
		new WindAutoscrollSupport();
	}

	private void initShoutReceiver() {
		_shoutReceiverToAvoidGc = new Receiver<ListValueChange>(){ @Override public void consume(ListValueChange value) {
			shoutAlert();
		}};
		_wind.shoutsHeard().addListReceiver(_shoutReceiverToAvoidGc);
	}
	
	private void shoutAlert() {
		Window window = SwingUtilities.windowForComponent(_container);
		_myShout.getMainWidget().requestFocus();
		
		if(window.hasFocus()) return;
		alertUser(window);
	}

	private void alertUser(Window window) {
		window.toFront();
	}

	private Dimension size(Container container) {
		return new Dimension(container.getSize().width, 248 );
	}
	
	private String getName() {
		return "Wind";
	}

	
	private final class ShoutLabelProvider implements LabelProvider<Shout> {
		@Override
		public Signal<String> labelFor(Shout shout) {
			return new Constant<String>(shout.phrase);
		}

		@Override
		public Signal<Image> imageFor(Shout shout) {
			return image(shout);
		}
	}

	
	private final class WindAutoscrollSupport{
		
		private final Image LOCK_ICON;
		private final Image UNLOCK_ICON;

		private WindAutoscrollSupport() {
			LOCK_ICON = loadImage("lock.png");
			UNLOCK_ICON = loadImage("unlock.png");
			addAutoScrollToggleButton();
			addAutoScrollListener();
		}

		private void addAutoScrollToggleButton() {
			_lock = new JToggleButton();
			_lock.setPreferredSize(new Dimension(20,20));
			_lock.setBorder(new EmptyBorder(0,0,0,0));
			
			_lock.getModel().addChangeListener(new ChangeListener(){
				{ _lock.setIcon(new ImageIcon(UNLOCK_ICON)); }
				@Override public void stateChanged(ChangeEvent e) {
					if(_lock.isSelected())
						_lock.setIcon(new ImageIcon(LOCK_ICON));
					else
						_lock.setIcon(new ImageIcon(UNLOCK_ICON));
				}});
		}

		private void addAutoScrollListener() {//Optimize better auto-scroll
			final BoundedRangeModel model = _scrollPane.getVerticalScrollBar().getModel();
			model.addChangeListener(new ChangeListener(){@Override	public void stateChanged(ChangeEvent e) {
				if(!_lock.isSelected())
					model.setValue(model.getMaximum());
			}});
		}
	}
	
	
	private final class WindClipboardSupport implements ClipboardOwner{
		
		private WindClipboardSupport(){
			addSelectionChangeListener();
			addKeyStrokeListener();
		}

		private void addKeyStrokeListener() {
			JList list = _shoutsList.getMainWidget();
			int modifiers = getPortableSoModifiers();
			final KeyStroke ctrlc = KeyStroke.getKeyStroke(KeyEvent.VK_C, modifiers);
			list.getInputMap().put(ctrlc,  "ctrlc");
			list.getActionMap().put("ctrlc",  new AbstractAction(){@Override public void actionPerformed(ActionEvent e) {
				copySelectedShoutToClipboard();
			}});
		}

		private void addSelectionChangeListener() {
			JList list = _shoutsList.getMainWidget();
			list.getSelectionModel().addListSelectionListener(new ListSelectionListener(){ @Override public void valueChanged(ListSelectionEvent e) {
				copySelectedShoutToClipboard();
			}});
		}
		
		@Override
		public void lostOwnership(Clipboard arg0, Transferable arg1) {
			Logger.log("Lost Clipboard Ownership.");
		}
		
		private int getPortableSoModifiers() {
			return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		}
		
		private void copySelectedShoutToClipboard() {
			JList list = _shoutsList.getMainWidget();
			Object values[] = list.getSelectedValues();
			if(values == null || values.length == 0) return;
			
			StringBuilder builder = new StringBuilder();
			for (Object value : values) {
				Shout shout = (Shout) value;
				if(values.length>1)
					builder	.append(ShoutUtils.publisherNick(shout)).append(" - ")
					.append(ShoutUtils.getFormatedShoutTime(shout)).append("\n");
				builder.append(shout.toString()).append("\n\n");
			}
			
			String out = builder.toString().substring(0, builder.length()-2);
			StringSelection fieldContent = new StringSelection(out);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fieldContent, this);	
		}
	}
}