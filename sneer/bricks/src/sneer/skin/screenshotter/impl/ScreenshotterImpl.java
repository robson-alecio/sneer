package sneer.skin.screenshotter.impl;

import static sneer.commons.environments.Environments.my;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.screenshotter.Screenshotter;
import sneer.software.exceptions.FriendlyException;
import wheel.lang.exceptions.Hiccup;

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