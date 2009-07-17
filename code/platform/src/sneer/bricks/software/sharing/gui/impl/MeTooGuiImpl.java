package sneer.bricks.software.sharing.gui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import sneer.bricks.skin.main.dashboard.InstrumentPanel;
import sneer.bricks.skin.main.instrumentregistry.InstrumentRegistry;
import sneer.bricks.skin.main.synth.scroll.SynthScrolls;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanel;
import sneer.bricks.snapps.diff.text.gui.TextDiffPanels;
import sneer.bricks.software.sharing.gui.MeTooGui;

class MeTooGuiImpl implements MeTooGui{

	private InstrumentPanel _container;
	private final JTree _tree = new JTree();
	private final TextDiffPanel _diffPanel = my(TextDiffPanels.class).newPanel();
	
	@Override public int defaultHeight() { return 150;  }
	@Override public String title() { return "MeToo"; }

	MeTooGuiImpl(){
		my(InstrumentRegistry.class).registerInstrument(this);

		_tree.setModel(new DefaultTreeModel(FakeModel.root()));
	}
	
	@Override
	public void init(final InstrumentPanel container) {
		_container = container;
		Container contentPane = _container.contentPane();
		
		contentPane.setLayout(new BorderLayout());
		
		JScrollPane scrollTree = my(SynthScrolls.class).create();
		JScrollPane scrollFiles = my(SynthScrolls.class).create();
		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTree, scrollFiles);
		
		JScrollPane scrollDiff = my(SynthScrolls.class).create();
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, verticalSplit, scrollDiff);
		
		contentPane.add(horizontalSplit, BorderLayout.CENTER);
		
		verticalSplit.setDividerSize(2);
		horizontalSplit.setDividerSize(2);
		verticalSplit.setDividerLocation(100);
		horizontalSplit.setDividerLocation(100);
	
		scrollTree.getViewport().add(_tree);
		scrollDiff.getViewport().add(_diffPanel.component());
	}
}
