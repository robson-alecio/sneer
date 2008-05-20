package sneer.lego.tests;

import sneer.lego.Inject;

public class Static {

	@Inject
	public static Sample sample;

	static {
		System.out.println("static block on Static.java");
	}

}
