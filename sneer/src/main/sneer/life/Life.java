//Copyright (C) 2004 Klaus Wuestefeld
//This is free software. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the license distributed along with this file for more details.

package sneer.life;


public interface Life extends LifeView {

	public void name(String newName);
	public void thoughtOfTheDay(String newName);
	public void picture(JpgImage _picture);

	public void giveSomebodyANickname(LifeView somebody, String nickname);
	public void changeNickname(String oldNickname, String newNickname);
	public void forgetNickname(String nickname);

    public void profile(String profile);
    public void contactInfo(String contactInfo);

    public void thing(String name, Object thing);

}

