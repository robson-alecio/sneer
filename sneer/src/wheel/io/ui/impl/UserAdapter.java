package wheel.io.ui.impl;

import java.awt.Font;
import java.io.File;

import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.FriendlyException;
import wheel.reactive.Signal;

public abstract class UserAdapter implements User {

	public Signal<Font> font() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public void fontChooser() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	public void saveAs(String title, String buttonTitle, String[] suffixes, String description, Omnivore<File> callback) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override 
	public void acknowledge(Throwable t) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void acknowledgeFriendlyException(FriendlyException e) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void acknowledgeNotification(String notification) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void acknowledgeNotification(String notification,
			String replacementForBoringOK) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void acknowledgeUnexpectedProblem(String description) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void acknowledgeUnexpectedProblem(String description, String help) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String answer(String prompt) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public String answer(String prompt, String defaultAnswer) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Omnivore<Notification> briefNotifier() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Catcher catcher() {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public Object choose(String proposition, Object... options) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public boolean confirm(String proposition) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public boolean confirmOrCancel(String proposition) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void confirmWithTimeout(String proposition, int timeout,
			Omnivore<Boolean> callback) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

	@Override
	public void modelessAcknowledge(String title, String message) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
	
	@Override
	public void chooseDirectory(String title, String buttonTitle, Omnivore<File> callback) {
		// Implement Auto-generated method stub
		throw new wheel.lang.exceptions.NotImplementedYet();
	}
}
