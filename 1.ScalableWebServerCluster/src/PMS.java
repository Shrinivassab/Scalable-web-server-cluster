/****** PMS - Pool Monitoring System ******/
/**
 *
 * @author Mohamed
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class PMS extends Frame implements ActionListener 
{
	Label cap, lb1, lb2, lb3, lb4, lb5, lb6, brd, ln1, ln2;
	Button bt1,bt2;
	CheckboxGroup cbg1;
	Checkbox chn , indx;
	Label lbarr[] = new Label[30];

	TextField size;
	List szl, grpl;
	int idx = 5, xpos = 1 , ypos = 220, totcnt , gCount;

	public PMS(String title, TextField size , List szl , List grpl)throws Exception
	{
		super(title);
		setLayout(null);
		setVisible(true);
		setSize(800,570);
		setLocation(100,100);
		setVisible(true);
		Color c1 = new Color(200, 200, 200);
		setBackground(c1);	
		setForeground(Color.blue);
		
		this.size = size;
		this.szl = szl;
		this.grpl = grpl;

		cap = new Label("Web Server Cluster - Thread Pool Monitoring System");
		lb1 = new Label("Allocation Policy");
		brd = new Label("");
		ln1 = new Label("");
		ln2 = new Label("");


		lbarr[0] = new Label("Pool Name");
		lbarr[1] = new Label("Actual Size");
		lbarr[2] = new Label("Available Size");
		lbarr[3] = new Label("Status");
		lbarr[4] = new Label("Allocation Policy");

		for(int j = 5; j < 20 ; j++)
		lbarr[j] = new Label("");
		
		bt1 = new Button("CLOSE");
                bt2 = new Button("Pool Details");

		cbg1 = new CheckboxGroup();
		chn = new Checkbox("Chaining" , cbg1 , false);
		indx = new Checkbox("Indexing" , cbg1 , true);

		Color b1 = new Color(255 , 125 , 125);
		cap.setBackground(b1);
		cap.setForeground(Color.black);
		
		ln1.setBackground(Color.black);

		brd.setBackground(Color.cyan);		
		chn.setBackground(Color.cyan);
		indx.setBackground(Color.cyan);
		
		cap.setBounds(220 , 30, 380 ,30);
		lb1.setBounds(20 , 80 , 150 , 30);

		brd.setBounds(40 , 120 , 250 , 40);
		chn.setBounds(70 , 123 , 80 , 30);
		indx.setBounds(170, 123 , 80 , 30);

		bt1.setBounds(550 , 123 , 120 , 25);
                bt2.setBounds(400 , 123 , 120 , 25);

		ln1.setBounds(2 , 180 , 800 , 2);

		Font f1 = new Font("Arial" , Font.BOLD, 15);
		setFont(f1);
		add(cap);	
                add(lb1);	
                add(chn);	
                add(indx);		
		add(brd);	
                add(bt1);
                add(bt2);
                add(ln1);		
		
		bt1.addActionListener(this);
                bt2.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				dispose();
				System.exit(0);
			}
		});
	}

	public void actionPerformed(ActionEvent ae)
	{
		String sr = ae.getActionCommand();
		int xpos1 = 1;
		if(sr.equals("Pool Details"))
		{
			System.out.println("Entered");
			
			Color c1 = new Color(255 , 100 , 100);
			for(int i = 0 ; i < 5; i++)
			{				
				lbarr[i].setBackground(c1);
				lbarr[i].setForeground(Color.black);
				lbarr[i].setAlignment(Label.CENTER);
				lbarr[i].setBounds(xpos1 , 185 , 150 , 20);
				xpos1 += 160;
				add(lbarr[i]);
			}
			ln2.setBounds(2, 210 , 800 , 2);
			ln2.setBackground(Color.black);
			add(ln2);
			totcnt = 2;
                        gCount = 3;		
			
			Color c2 = new Color(190, 130 , 130);
			int gi= 0, gasz = 0 , gbsz = 0, gcsz = 0;
			int flg = 0;
			for(int k = 0 ; k < gCount; k++)
			{
				for(int j = 0; j < 5; j++)
				{					
					lbarr[idx].setBounds(xpos , ypos , 150 , 20);
					lbarr[idx].setAlignment(Label.CENTER);
					lbarr[idx].setBackground(c2);
					xpos += 160;
					add(lbarr[idx]);
					
					if(j == 0)
					{
						if(flg == 0)
						{						
							lbarr[idx].setText("Group A");
							
							for(int ga = 0 ; ga < grpl.getItemCount() ; ga++)
							{
								if(grpl.getItem(ga).equals("Group A"))
								{
									gasz = gasz + Integer.parseInt(szl.getItem(ga));
								}							
							}
							System.out.println(gasz);
							lbarr[idx+2].setText(String.valueOf(gasz));
							if(gasz == 0)
								lbarr[idx+3].setText("Free");
							else									
								lbarr[idx+3].setText(String.valueOf(gasz)+ "KB(s) Occupied");		
								
							lbarr[idx+4].setText("Indexing");					
							flg = 1;
						}
						
						else if(flg == 1)
						{						
							lbarr[idx].setText("Group B");
							for(int ga  = 0 ; ga < grpl.getItemCount() ; ga++)
							{
								if(grpl.getItem(ga).equals("Group B"))
								{
									gbsz = gbsz + Integer.parseInt(szl.getItem(ga));
								}
							}
							System.out.println(gbsz);	
							lbarr[idx+2].setText(String.valueOf(gbsz));
							
							if(gbsz == 0)
								lbarr[idx+3].setText("Free");
							else
								lbarr[idx+3].setText(String.valueOf(gbsz)+ "KB(s) Occupied");
							lbarr[idx+4].setText("Indexing");					
							flg = 2;
						}
						
						else if(flg == 2)
						{						
							lbarr[idx].setText("Group C");
							for(int ga  = 0; ga < grpl.getItemCount() ; ga++)
							{
								if(grpl.getItem(ga).equals("Group C"))
								{
									gcsz = gcsz + Integer.parseInt(szl.getItem(ga));
								}
							}
							System.out.println(gcsz);
							lbarr[idx+2].setText(String.valueOf(gcsz));
							
							if(gcsz == 0)
								lbarr[idx+3].setText("Free");
							else							
								lbarr[idx+3].setText(String.valueOf(gcsz) + "KB(s) Occupied");													
							lbarr[idx+4].setText("Indexing");					
							flg = 0;
						}		
					}
					
					if(j == 1)
						lbarr[idx].setText(String.valueOf(Integer.parseInt(size.getText()) * 10));
					idx++;
				gi++;
				}
				ypos += 30;
				xpos = 1;
			}
		}
                
                else if(sr.equals("CLOSE"))
                {
                    dispose();
                }
	}
	
	/*public static void main(String ar[])throws Exception
	{
		PMS pm = new PMS("Thread Pool Monitoring System");
	}*/
}		
