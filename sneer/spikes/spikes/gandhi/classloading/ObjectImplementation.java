package spikes.gandhi.classloading;

public class ObjectImplementation implements ObjectInterface{

	public String toLowercase(String text) {
		return text.toLowerCase();
	}

	public InnerClassInterface plus() {
		return new InnerClassInterface(){
			public int execute(int a, int b) {
				return a + b;
			}
		};
	}

	public InnerClassInterface minus() {
		return new InnerClassInterface(){
			public int execute(int a, int b) {
				return a - b;
			}
		};
	}

	public InnerClassInterface multiply() {
		return new InnerClassInterface(){
			public int execute(int a, int b) {
				return a * b;
			}
		};
	}	
	
}
