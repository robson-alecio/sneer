package sneer.skin.widgets.reactive.impl;

import static wheel.lang.Types.cast;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.skin.widgets.reactive.ImageWidget;
import sneer.skin.widgets.reactive.LabelProvider;
import sneer.skin.widgets.reactive.ListWidget;
import sneer.skin.widgets.reactive.RFactory;
import sneer.skin.widgets.reactive.TextWidget;
import wheel.io.ui.impl.ListSignalModel;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.Receiver;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListValueChange;

public class RListImpl<ELEMENT> extends JList implements ListWidget<ELEMENT> {

	private static final long serialVersionUID = 1L;

	private LabelProvider<ELEMENT> _labelProvider;

	private final ListSignal<ELEMENT> _source;

	private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
			1);

	private final Omnivore<ListValueChange> _listReceiver = new Omnivore<ListValueChange>() {
		@Override
		public void consume(ListValueChange valueObject) {
			repaintList();
		}
	};

	private void repaintList() {
		revalidate();
		repaint();
	}

	RListImpl(ListSignal<ELEMENT> source, LabelProvider<ELEMENT> labelProvider) {
		_source = source;
		_labelProvider = labelProvider;
		initModel();
		addListReceiver();

		class DefaultListCellRenderer implements ListCellRenderer {

			Map<Object, JPanel> cacheLines = new HashMap<Object, JPanel>(); //Fix this might be a leak

			@SuppressWarnings("unused")
			Receiver<Object> _listRepainter = new Receiver<Object>() {
				@Override
				public void consume(Object ignore) {
					repaintList();
				}
			};

			private Border getNoFocusBorder() {
				if (System.getSecurityManager() != null) {
					return SAFE_NO_FOCUS_BORDER;
				}
				return noFocusBorder;
			}

			@Override
			public Component getListCellRendererComponent(JList ignored,
					Object value, int ignored2, boolean isSelected,
					boolean cellHasFocus) {
				JPanel panel;
				if (cacheLines.containsKey(value)) {
					panel = cacheLines.get(value);
				} else {
					Container container = ContainerUtils.getContainer();
					RFactory rfactory = container.produce(RFactory.class);

					panel = new JPanel();
					panel.setOpaque(false);
					panel.setLayout(new BorderLayout(5, 5));

					Signal<String> slabel = _labelProvider
							.labelFor(getElement(value));
					Signal<Image> sicon = _labelProvider
							.imageFor(getElement(value));

					TextWidget<JLabel> label = rfactory.newLabel(slabel);
					panel.add(label.getComponent(), BorderLayout.CENTER);

					ImageWidget image = rfactory.newImage(sicon);
					panel.add(image.getComponent(), BorderLayout.WEST);
					panel.setOpaque(false);

					_listRepainter.addToSignal(label.output());
					_listRepainter.addToSignal(image.output());

					cacheLines.put(value, panel);
				}

				Border border = null;
				if (cellHasFocus) {
					if (isSelected) {
						border = UIManager
								.getBorder("List.focusSelectedCellHighlightBorder");
					}
					if (border == null) {
						border = UIManager
								.getBorder("List.focusCellHighlightBorder");
					}
				} else {
					border = getNoFocusBorder();
				}
				panel.setBorder(border);
				return panel;
			}

			private ELEMENT getElement(Object value) {
				return cast(value);
			}
		}
		setCellRenderer(new DefaultListCellRenderer());

	}

	private void addListReceiver() {
		_source.addListReceiver(_listReceiver);
	}

	private void initModel() {

		//		setModel(new ListSignalModel<ELEMENT>(_source, new ListSignalModel.SignalChooser<ELEMENT>(){
		//			@Override
		//			public Signal<?>[] signalsToReceiveFrom(ELEMENT element) {
		//				return new Signal<?>[]{_labelProvider.imageFor(element), 
		//									   _labelProvider.labelFor(element)};
		//			}}));
		setModel(new ListSignalModel<ELEMENT>(_source));
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