package org.sneer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sneer.compiler.tests.CompilerTest;
import sneer.deployer.test.DeployerTest;
import sneer.lego.tests.ContainerTest;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ContainerTest.class,
	CompilerTest.class,
	DeployerTest.class
})
public class SmokeTest {
	
}
