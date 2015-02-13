package test.hic;

import javax.swing.JTextArea;

import hic.IOPanelInterface;

public class IOPanelProxy implements IOPanelInterface {

	@Override
	public void displayText(String msg) {
		System.out.println("Hello World!");
		
	}

	@Override
	public void displayErrorText(String msg) {		
		System.out.println("I am a test object");
	}

	@Override
	public JTextArea getOutputArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOnTown() {
		// TODO Auto-generated method stub
		return true;
	}

}
