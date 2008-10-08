package wheel.io.codegeneration.tests;

import org.junit.Assert;
import org.junit.Test;

import wheel.io.codegeneration.Template;
import wheel.lang.Functor;

public class TemplateTest {
	
	static public class My  {
		private final String _name;

		public My(String name) {
			_name = name;
		}
		
		public String name() {
			return _name;
		}
	}
	
	@Test
	public void testCompile() {
		
		final Functor<My, String> template = Template.compile("Hello, <%=value.name()%>!", My.class);
		final String output = template.evaluate(new My("sneer"));
		Assert.assertEquals("Hello, sneer!", output);
	}
	
	@Test
	public void testEval() {
		final String code = "<%=value.name()%> is cool";
		assertEvaluation("sneer is cool", code, new My("sneer"));
	}
	
	@Test
	public void testUnicode() {
		assertEvaluation("façade", "façade", null);
	}
	
	@Test
	public void testLoop() {
		final String code = "before\n<% for (int i=0; i<value; ++i) { %>i: <%=i%>\n<% } %>after";
		assertEvaluation("before\ni: 0\ni: 1\ni: 2\nafter", code, 3);
	}

	private <ARG> void assertEvaluation(final String expected, final String code, final ARG value) {
		final String output = Template.evaluate(code, value);
		Assert.assertEquals(expected, output);
	}
	

}
