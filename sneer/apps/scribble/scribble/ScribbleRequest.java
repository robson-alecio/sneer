package scribble;

import static wheel.i18n.Language.translate;
import sneer.apps.asker.packet.AskerRequestPayload;

public class ScribbleRequest extends AskerRequestPayload{
	
	@Override
	public String prompt() {
		return translate("Do you accept to scribble with me ?");
	}

	private static final long serialVersionUID = 1L;
}
