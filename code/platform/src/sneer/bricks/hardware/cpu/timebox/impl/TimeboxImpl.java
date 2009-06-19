package sneer.bricks.hardware.cpu.timebox.impl;

import sneer.bricks.hardware.cpu.timebox.Timebox;

class TimeboxImpl implements Timebox {

	@Override
	public Runnable prepare(final int durationInMillis, final Runnable toRun, final Runnable toCallWhenBlocked) {
		return new Runnable() { @Override public void run() {
			TimeboxImpl.this.run(durationInMillis, toRun, toCallWhenBlocked);
		}};
	}

	@Override
	public void run(int durationInMillis, final Runnable toRun, final Runnable toCallWhenBlocked) {
		new TimeboxRunner(durationInMillis, toRun, toCallWhenBlocked);
	}

}
