package sneer.pulp.serialization;

import sneer.brickness.Brick;

@Brick
public interface DeepCopier {

	<T> T deepCopy(T original);

	<T> T deepCopy(T original, Serializer serializer);

	Object pipedDeepCopy(Object original, Serializer serializer);

}