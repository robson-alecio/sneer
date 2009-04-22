package sneer.skin.main.synth.impl;

import static sneer.commons.environments.Environments.my;

import java.io.InputStream;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

import sneer.hardware.gui.guithread.GuiThread;
import sneer.skin.main.synth.Synth;

class SynthImpl implements Synth {
	
	private final SynthLookAndFeel _synth = new SynthLookAndFeel();
	private final MetalLookAndFeel _default = new MetalLookAndFeel();
	
	SynthImpl(){
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
			try {
				UIManager.setLookAndFeel(_synth);
				load(SynthImpl.class);
				UIManager.setLookAndFeel(_default);
			} catch (Exception e) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
		}});
	}

	@Override
	public void load(Object resourceBase) {
		load(resourceBase.getClass());
	}
	
	@Override
	public void load(final Class<?> resourceBase){
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
			InputStream is = null;
			try {
				is = resourceBase.getResourceAsStream("synth.xml");
				_synth.load(is, resourceBase);
			} catch (Exception e) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			} finally {
				try { is.close(); } catch (Exception e2) { /* ignore */ }
			}
		}});		
	}

	@Override
	public void attach(final JComponent component) {
		my(GuiThread.class).invokeAndWait(new Runnable(){ @Override public void run() {
			try {
				UIManager.setLookAndFeel(_synth);
				SwingUtilities.updateComponentTreeUI(component);
				UIManager.setLookAndFeel(_default);
			} catch (UnsupportedLookAndFeelException e) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}			
		}});
	}
}