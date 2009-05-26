package sneer.installer;

import javax.swing.JFrame;

public class OlaMundo extends JFrame{

	{
			setTitle("Olá Mundo!");
			setBounds(10,10,200,200);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new OlaMundo();
	}
}
