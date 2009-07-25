package sneer.bricks.hardware.cpu.lang.contracts;

import sneer.foundation.brickness.Brick;

@Brick
public interface Contracts {

	WeakContract newContractFor(Disposable service, Object annex);

}
