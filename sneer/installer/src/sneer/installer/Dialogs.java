package sneer.installer;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

import javax.swing.JOptionPane;

abstract class Dialogs {

	static void show(String title, String msg, Runnable toCallOnCancel, Object...options) {
		int dialogOptions;
		int dialogType;
		
		if(options.length==1){
			dialogOptions=OK_OPTION;
			dialogType=INFORMATION_MESSAGE;
		}else{
			dialogOptions=OK_CANCEL_OPTION;
			dialogType=QUESTION_MESSAGE;
		}
		
		int bnt = JOptionPane.showOptionDialog(null, msg, title, dialogOptions, dialogType, null,  options,  options[0]);
		if (bnt != OK_OPTION) toCallOnCancel.run();
	}
}