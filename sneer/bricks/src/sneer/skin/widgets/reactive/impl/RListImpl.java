package sneer.skin.widgets.reactive.impl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import wheel.io.ui.impl.ListSignalModel;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;

class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {

	private static final long serialVersionUID = 1L;

	protected final ListSignal<ELEMENT> _source;
	protected LabelProvider<ELEMENT> _labelProvider;

	private void repaintList() {
		SwingUtilities.invokeLater(new Runnable() {@Override public void run() {
			revalidate();
			repaint();
		}});	
	}

	RListImpl(ListSignal<ELEMENT> source, LabelProvider<ELEMENT> labelProvider) {
		_source = source;
		_labelProvider = labelProvider;
		initModel();

		class DefaultListCellRenderer implements ListCellRenderer {

			@Override
			public Component getListCellRendererComponent(JList ignored, Object value, int ignored2, boolean isSelected, boolean cellHasFocus) {
				
				Signal<String> signalText = _labelProvider.labelFor(getElement(value));
				Signal<Image> signalImage = _labelProvider.imageFor(getElement(value));

				JPanel root = new JPanel();
				root.setLayout(new GridBagLayout());
				root.setOpaque(false);
				
				JLabel icon = new JLabel(new ImageIcon(signalImage.currentValue()));
				icon.setOpaque(false);
				
				JTextArea area = new JTextArea();
				area.setWrapStyleWord(true);
				area.setLineWrap(true);
				area.setText(signalText.currentValue());
				
				root.add(icon, new GridBagConstraints(0,0,1,1,0,0, 
						GridBagConstraints.NORTHWEST, 
						GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0));

				root.add(area, new GridBagConstraints(1,0,1,1,1.,1., 
						GridBagConstraints.NORTHWEST, 
						GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));;
						
				new Receiver<Object>() {@Override	public void consume(Object ignore) {
					repaintList();
				}};

				packLines(area, root);
				return root;
			}

			private void packLines(JTextArea area, JPanel root) { //Optimize - implement pack method without JWindow
				Dimension listSize = RListImpl.this.getSize();
				root.setPreferredSize(new Dimension(listSize.width-20, listSize.height*5));
				JWindow win = new JWindow();
				win.add(root);
				win.pack();
				root.setPreferredSize(new Dimension(listSize.width-20, area.getPreferredSize().height));
				win.remove(root);
			}

			private ELEMENT getElement(Object value) {
				return (ELEMENT)value;
			}
		}
		setCellRenderer(new DefaultListCellRenderer());

	}

	private void initModel() {
		setModel(new ListSignalModel<ELEMENT>(_source, new ListSignalModel.SignalChooser<ELEMENT>(){
		@Override
		public Signal<?>[] signalsToReceiveFrom(ELEMENT element) {
			return new Signal<?>[]{_labelProvider.imageFor(element), 
								   _labelProvider.labelFor(element)};
		}}));
	}

	@Override
	public JList getMainWidget() {
		return this;
	}

	@Override
	public JComponent getComponent() {
		return this;
	}

	@Override
	public void setLabelProvider(LabelProvider<ELEMENT> labelProvider) {
		_labelProvider = labelProvider;
	}

	@Override
	public ListSignal<ELEMENT> output() {
		return _source;
	}
}