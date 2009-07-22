package spikes.sneer.bricks.skin.screenshotter.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import sneer.bricks.hardware.cpu.exceptions.Hiccup;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.foundation.lang.exceptions.FriendlyException;
import spikes.sneer.bricks.skin.screenshotter.Screenshotter;

class ScreenshotterImpl implements Screenshotter {

	private final BlinkingLights _lights = my(BlinkingLights.class);
	
	private final GraphicsDevice _device = defaultDevice();
	private final Robot _robot = createRobot();

	/** @throws Hiccup */
	@Override
	public BufferedImage takeScreenshot() throws FriendlyException, Hiccup {
		if (_robot == null) throw new FriendlyException("Unable to take a snapshot of your screen.", "Get an expert sovereign friend to help you.");
		return _robot.createScreenCapture(fullScreenSize(_device));
	}

	private Robot createRobot() {
		try {
			return new Robot(_device);
		} catch (AWTException e) {
			_lights.turnOn(LightType.ERROR, "Screenshots wont work",  "Get an expert sovereign friend to help you.", e);
			return null;
		}
	}

	private Rectangle fullScreenSize(GraphicsDevice device) {
		DisplayMode mode = device.getDisplayMode();
		return new Rectangle(mode.getWidth(), mode.getHeight());
	}

	private GraphicsDevice defaultDevice() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	}

}