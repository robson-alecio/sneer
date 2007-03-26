package wheel.io.ui;


public interface TrayIcon {

	public interface Action {
		String caption();
		void run();
	}

	void addAction(Action action);

	void seeReminder(String reminder);

}