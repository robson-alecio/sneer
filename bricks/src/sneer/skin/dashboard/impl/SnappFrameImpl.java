package sneer.skin.dashboard.impl;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JInternalFrame;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import sneer.skin.dashboard.SnappFrame;

public class SnappFrameImpl extends JInternalFrame implements SnappFrame {

	private static final long serialVersionUID = 1L;
	private static byte[] prototypeBorder;

	public SnappFrameImpl() {
		this("");
	}

	public SnappFrameImpl(String _title) {
		super(_title, true, false, true, true);
		setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {

		int width = getParent().getWidth()-10;
		if(width<0)
			width = 0;
		
		int height = (int) super.getPreferredSize().getHeight();
		if(height<0) height = 30;
		
		Dimension dim = new Dimension(width,height);
		return dim;
	}
		
	@Override
	public void setBorder(Border border) {
		
		if(border==null){
			super.setBorder(border);
		}else{
			if(prototypeBorder==null){
				super.setBorder(border);
			}else{
				super.setBorder(cloneBorder());
			}
		}
	}

	private Border cloneBorder() {
		ByteArrayInputStream bais = new ByteArrayInputStream(prototypeBorder);
		ObjectInputStream in = null;
		
		try {
			in = new ObjectInputStream(bais);
			return (Border) in.readObject();
		} catch (Exception ex) {
			return new LineBorder(Color.LIGHT_GRAY,2,true);
		} finally {
			try{ in.close(); }catch (Exception e) { /*ignore*/}			
		}
	}

	public static boolean hasDefaultWindowBorder() {
		return prototypeBorder!=null;
	}
	
	public static void setDefaultWindowBorder(Border border) {
		if(border==null){
			prototypeBorder = null;
			return;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(border);
		} catch (Exception ex) {
			try {
				out = new ObjectOutputStream(baos);
				out.writeObject(new LineBorder(Color.LIGHT_GRAY,2,true));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} finally {
			prototypeBorder = baos.toByteArray();
			try{ out.close(); }catch (Exception e) { /*ignore*/}
		}
	}

	@Override
	public Dimension getSize() {
		int width = getParent().getWidth()-10;
		if(width<0) width = 0;
		
		int height = (int) super.getSize().getHeight();
		
		Dimension dim = new Dimension(width,height);
		return dim;		
	}
	
	@Override
	public Rectangle getBounds() {
		Rectangle bounds = super.getBounds();
		bounds.setLocation(10, (int) bounds.getY());
		return bounds;
	}
	
	@Override
	public Container getContainer() {
		return this;
	}
}
