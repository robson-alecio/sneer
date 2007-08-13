package sneer.kernel.gui.contacts;

import sneer.kernel.pointofview.Party;

public class MeNode extends PartyNode {

	public MeNode(Party party){
		super(party);
	}

	@Override
	public Party party() {
		return (Party)getUserObject();
	}

	private static final long serialVersionUID = 1L;
}
