package sneer.tests;

public class StrapMock {

	public static void run() {
		try {
			BootTest._wasStrapRun = true;
			System.out.println(BootTest._wasStrapRun);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
