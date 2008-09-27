package spikes.sandro.old.gui.community.share;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import spikes.sandro.old.swing.panel.GradientPanel;

public class SharePanel extends GradientPanel {

	private static final long serialVersionUID = 1L;
	private JLabel _title = new JLabel("T�tulo");
	private JTextArea _description = new JTextArea("bl�, bl�, bl�, bl�, bl�, bl�, bl�, " +
											      "bl�, bl�, bl�, bl�, bl�, bl�, bl�, bl�...");	
	private JLabel sizeLabel = new JLabel("Quantidade:");
	private JLabel sizeText = new JLabel("XX Arquivos, 30MG");
	
	private JLabel updateDateLabel = new JLabel("Alterado em:");
	private JLabel updateDateText = new JLabel("10/10/2006");

	public SharePanel(String title, String description) {
		super(new Color(230,255,230,0), 
			  new Color(230,255,230,255),GradientPanel._TOP_DOWN);
		initialize();
		setTitle(title);
		setDescription(description);
	}
	
	public void setTitle(String txt){_title.setText(txt);}
	public void setDescription(String txt){_description.setText(txt);}
	public void setSizeLabel(String txt) { sizeLabel.setText(txt);}
	public void setSizeText(String txt) { this.sizeText.setText(txt);}
	public void setUpdateDateLabel(String txt) { this.updateDateLabel.setText(txt); }
	public void setUpdateDateText(String txt) { this.updateDateText.setText(txt); }

	private void initialize() {
		_description.setEditable(false);
		_description.setOpaque(false);
		_description.setLineWrap(true);
		_description.setWrapStyleWord(true);
		
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);

		GridBagConstraints c00 = newConstraints(0,0, 1.0,1.0, 5,15,0,10);
		c00.gridwidth = 2;
		c00.anchor = GridBagConstraints.SOUTHWEST;
		this.add(_title, c00);
		
		GridBagConstraints c01 = newConstraints(0,1, 1.0,3.0, 0,15,5,10);
		c01.gridwidth = 2;
		c01.anchor = GridBagConstraints.SOUTHWEST;
		c01.fill = GridBagConstraints.BOTH;
		this.add(_description, c01);

		//Fontes
		Font font12Plain = new Font("Dialog", Font.PLAIN, 12);
		Font font14Bold = new Font("Dialog", Font.BOLD, 14);
		
		_title.setFont(font14Bold);
		_title.setForeground(Color.DARK_GRAY);
		updateDateText.setFont(font12Plain);
		sizeText.setFont(font12Plain);
		_description.setFont(font12Plain);

		//Dados
		GridBagConstraints c1 = newConstraints(0, 2, 0.0, 0.0, 0,15,5,10);
		GridBagConstraints c2 = newConstraints(1, 2, 1.0, 0.0, 0,15,5,10);
		c1.anchor = GridBagConstraints.NORTHEAST;
		c2.anchor = GridBagConstraints.NORTHEAST;	
		c2.fill = GridBagConstraints.HORIZONTAL;
		this.add(sizeLabel, c1);
		this.add(sizeText, c2);		
		
		c1 = newConstraints(0, 2, 0.0, 0.0, 0,10,5,10);
		c2 = newConstraints(1, 2, 1.0, 0.0, 0,10,5,10);
		c1.anchor = GridBagConstraints.NORTHEAST;
		c2.anchor = GridBagConstraints.NORTHEAST;	
		c2.fill = GridBagConstraints.HORIZONTAL;
		this.add(updateDateLabel, c1);
		this.add(updateDateText, c2);		
		
		prepareLabels(sizeLabel, sizeText, 2, 0.0, 0);
		prepareLabels(updateDateLabel, updateDateText, 3, 1.0, 5);
	}	
	
	private GridBagConstraints newConstraints(int gridX, int gridY, 
											  double weightX, double weightY,
											  int bTop, int bLeft, int bBotton, int bRight){
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridX;
		c.gridy = gridY;
		c.weightx = weightX;
		c.weighty = weightY;
		c.insets = new Insets(bTop, bLeft, bBotton, bRight);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		return c;
	}
	
	private void prepareLabels(JLabel lb, JLabel txt,
							  int gridY, double weightY, int botton){
		
		GridBagConstraints c1 = newConstraints(0, gridY, 0.0, weightY, 0,15,botton,10);
		GridBagConstraints c2 = newConstraints(1, gridY, 1.0, weightY, 0,15,botton,10);
		c1.anchor = GridBagConstraints.NORTHEAST;
		c2.anchor = GridBagConstraints.NORTHEAST;	
		c2.fill = GridBagConstraints.HORIZONTAL;
		this.add(lb, c1);
		this.add(txt, c2);
	}
}
