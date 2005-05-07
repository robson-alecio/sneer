//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sovereign;


public interface Life extends LifeView {

	public void changeName(String newName);

	public void giveSomebodyANickname(LifeView somebody, String nickname);
	public void changeNickname(String oldNickname, String newNickname);
	public void forgetNickname(String nickname);

    public void profile(String profile);
    public void contactInfo(String contactInfo);

    public void send(String message, String to);

}

