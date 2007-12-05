package sneer.kernel.gui.contacts;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeCellRenderer;

import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.widgets.ReactiveLabel;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;

public class ContactTreeCellRenderer extends DefaultTreeCellRenderer{

	final static String IMAGE_PATH = "/sneer/kernel/gui/contacts/images/";

	final static ImageIcon CROWN = new ImageIcon(ContactTreeCellRenderer.class.getResource(IMAGE_PATH + "crown.jpg"));

	final static ImageIcon ONLINE_ICON = new ImageIcon(ContactTreeCellRenderer.class.getResource(IMAGE_PATH + "operator.gif"));
	final static ImageIcon OFFLINE_ICON = new ImageIcon(ContactTreeCellRenderer.class.getResource(IMAGE_PATH + "operator_disabled.gif"));

	final static ImageIcon UNCONFIRMED_ICON = new ImageIcon(ContactTreeCellRenderer.class.getResource(IMAGE_PATH + "yellowled.gif"));
	final static ImageIcon CONFIRMED_ICON = new ImageIcon(ContactTreeCellRenderer.class.getResource(IMAGE_PATH + "greenled.gif"));
	
	final static Color selectedColor = new Color(230,240,255);

	private final Signal<Font> _font;

	public ContactTreeCellRenderer(Signal<Font> font){
		_font = font;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object treeNode, boolean isSelected, boolean expanded, boolean leaf, int row, boolean focus) {
		JPanel result = new JPanel(new FlowLayout(FlowLayout.LEFT));

		result.setBackground(isSelected ? selectedColor : Color.white);
		Color borderColor = isSelected ? Color.GRAY : Color.white;
		result.setBorder(new LineBorder(borderColor, 1));

		//if (treeNode == ContactTreeNode.NO_CONTACTS)
		//return super.getTreeCellRendererComponent(tree,treeNode,isSelected, expanded, leaf, row, focus);

		if (treeNode instanceof ContactNode)
			renderContact(((ContactNode)treeNode).contact(), result);
		else
			renderMe(((MeNode)treeNode).party(), result);

		return result;
	}

	private void renderMe(Party me, JPanel panel) {
		panel.add(new JLabel(CROWN));
		panel.add(new ReactiveLabel(me.name(),_font));
		panel.add(new ReactiveLabel(signal(me.isOnlineOnMsn().currentValue() ? " MSN On!!!" : " MSN Off"),_font));
	}

	private void renderContact(Contact contact, JPanel panel) {
		Party party = contact.party();
		
		String nick = contact.nick().currentValue();
		String name = party.name().currentValue();
		
		panel.add(new JLabel(onlineIconFor(party)));
		panel.add(new JLabel(stateIconFor(party)));
		panel.add(new ReactiveLabel(signal(nick + " - " + name),_font));
	}
	
	private Signal<String> signal(String text){
		return new SourceImpl<String>(text).output();
	}

	Signal<?>[] signalsToReceiveFrom(PartyNode node) {
		return node instanceof ContactNode
			? signalsToReceiveFromContact(((ContactNode)node).contact())
			: signalsToReceiveFromMe(((MeNode)node).party());
	}

	private Signal<?>[] signalsToReceiveFromMe(Party me) {
		return new Signal<?>[] {
			me.name(),
			me.isOnlineOnMsn()
		};
	}

	private Signal<?>[] signalsToReceiveFromContact(Contact contact) {
		return new Signal<?>[] {
			contact.party().isOnline(),
			contact.party().isOnlineOnMsn(),
			contact.nick(),
			contact.party().name(),
		};
	}

	private ImageIcon onlineIconFor(Party party) {
		return party.isOnline().currentValue()
			? ONLINE_ICON
			: OFFLINE_ICON;
	}

	private ImageIcon stateIconFor(Party party) {
		return party.isOnlineOnMsn().currentValue()
			? CONFIRMED_ICON
			: UNCONFIRMED_ICON;
	}

	private static final long serialVersionUID = 1L;

}

