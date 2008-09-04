package sneer.pulp.httpclient;


public interface HttpRequest {

	void setHeader(String name, String value);

	HttpResponse submit();

}
