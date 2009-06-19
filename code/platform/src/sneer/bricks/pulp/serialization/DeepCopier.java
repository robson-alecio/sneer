package sneer.bricks.pulp.serialization;

import sneer.foundation.brickness.Brick;

@Brick
public interface DeepCopier {

	<T> T deepCopy(T original);

	<T> T deepCopy(T original, Serializer serializer);

	Object pipedDeepCopy(Object original, Serializer serializer);

}