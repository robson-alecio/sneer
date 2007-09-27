package sneer.apps.talk;

import static wheel.i18n.Language.translate;
import sneer.apps.asker.packet.AskerRequestPayload;

public class AudioRequest extends AskerRequestPayload{
	
	@Override
	public String prompt() {
		return translate("Do you accept to talk to me ?");
	}

	private static final long serialVersionUID = 1L;
}
