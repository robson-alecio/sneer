package functional.freedom7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;

import testdashboard.TestDashboard;

import functional.SignalUtils;
import functional.SovereignFunctionalTest;
import functional.SovereignParty;

public abstract class Freedom7Test extends SovereignFunctionalTest {

	private static final String X = "sneer.bricks.x.X";
	private static final String Y = "sneer.bricks.y.Y";
	private static final String Z = "sneer.bricks.z.Z";
	
	
	@Test
	public void testPublish() throws Exception {
		
		if (!TestDashboard.newTestsShouldRun()) return;
		
		SovereignParty publisher = _a;
		SovereignParty receiver = _b;
		
		System.out.println("Ana Almeida "  + _a.ownPublicKey());
		System.out.println("Bruno Barros " + _b.ownPublicKey());
		
		//if(true) throw new NotImplementedYet(); //navigate from receiver to publisher
		SignalUtils.waitForValue("Bruno Barros", _a.navigateAndGetName("Bruno Barros"));
		SignalUtils.waitForValue("Ana Almeida", _b.navigateAndGetName("Ana Almeida"));
		
		File sourceFolder = askSourceFolder();
		
		publisher.publishBrick(new File(sourceFolder,"source2")); //deploy Y first
		Object y1 = publisher.produce(Y);
		assertNotNull(y1);
		
		publisher.publishBrick(new File(sourceFolder,"source")); //deploy X and Z

		//test Z
		Object z1 = publisher.produce(Z);
		ClassLoader cl1 = z1.getClass().getClassLoader();
		String logFactory1 = callMethod(z1, "logFactory").toString();
		assertTrue("wrong directory for brick class loader: "+cl1.toString(),cl1.toString().indexOf("sneer+AnaAlmeida") > 0);
		
		receiver.meToo(publisher, Z);
		Object z2 = receiver.produce(Z);
		ClassLoader cl2 = z2.getClass().getClassLoader();
		String logFactory2 = callMethod(z2, "logFactory").toString();
		assertTrue("wrong directory for brick class loader: "+cl2.toString(),cl2.toString().indexOf("sneer+BrunoBarros") > 0);
		
		assertFalse("LogFactory should have been loaded on a different class loader", logFactory1.equals(logFactory2));
		assertNotSame(z1, z2);
		assertNotSame(cl1, cl2);
		
		//test X, depends on Y and Z
		receiver.meToo(publisher, X);
		Object x1 = publisher.produce(X);
		Object x2 = receiver.produce(X);
		assertNotSame(x1, x2);
		
		Object y2 = receiver.produce(Y);
		assertNotSame(y1, y2);
		
		Object result = callMethod(x2, "callY");
		assertEquals(y2.toString(), result);
	}


	private Object callMethod(Object target, String methodName) throws Exception {
		Method m = target.getClass().getMethod(methodName, (Class<?>[]) null);
		m.setAccessible(true);
		Object result = m.invoke(target, (Object[]) null);
		return result;
	}

	protected abstract  File askSourceFolder();

}