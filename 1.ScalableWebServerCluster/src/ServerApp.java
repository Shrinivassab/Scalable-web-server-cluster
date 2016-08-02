/**
 *
 * @author Mohamed
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ServerApp extends Frame implements ActionListener , AdjustmentListener  , ItemListener, Runnable
{
	TextField sip, prt, size;
	Label lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8, lb9, lb10, lb11, lb12, cap, syswl, bck1, bck2, srblbl, ln1;
	CheckboxGroup cbg1, cbg2;
	Checkbox en, dis, lck, unlck;
	Button bt1, bt2, Frz , Rest , Hlp , Ext;
	Scrollbar psz;
	List acl, szl, grpl;
	TextArea stsrpt;
	ServThread listening = null;   
	Socket csk = null;
	public int gFlg = 0, flg = 0 , sflg = 0;
	int tSz = 0, tCap = 0 , qLen = 0;
	Thread td;


	public ServerApp(String title)throws UnknownHostException
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

		cap = new Label("  Thread Based Sclable Web Server Cluster Control Internet Traffic");
		
		lb1 = new Label("Server IP");
		lb2 = new Label("Port No.");
		lb12 = new Label("Group");
		ln1 = new Label("");

		sip = new TextField(25);

		bt1 = new Button("Start Server");
		bt2 = new Button("Monitor Client");
		Frz = new Button("Freeze");
		Rest = new Button("ReStart");
		Hlp = new Button("Help");
		Ext = new Button("Exit");

		Color b2 = new Color(150 , 230 , 150);
		bt1.setBackground(b2);
		bt1.setForeground(Color.black);

		bt2.setBackground(b2);
		bt2.setForeground(Color.black);

		Frz.setBackground(b2);
		Frz.setForeground(Color.black);

		Rest.setBackground(b2);
		Rest.setForeground(Color.black);

		Hlp.setBackground(b2);
		Hlp.setForeground(Color.black);

		Ext.setBackground(b2);
		Ext.setForeground(Color.black);
		
		prt = new TextField(5);
		lb3 = new Label("Active Client List");
		acl = new List(10);
		szl = new List(10);
		grpl = new List(10);
	
		srblbl = new Label("0     1     2     3");
		lb4 = new Label("Page Fragmentation");
		lb5 = new Label("Page Size");
		lb6 = new Label("Size");
		lb7 = new Label("Status Report");
		lb8 = new Label("KB(s)");
		lb9 = new Label("Pool Manager");
		lb10 = new Label("System WorkLoad");
		lb11 = new Label("Size");
		bck1 = new Label("");
		bck2 = new Label("");

		cbg1 = new CheckboxGroup();
		en = new Checkbox("Enable" , cbg1, false);
		dis = new Checkbox("Disable" , cbg1, true);

		cbg2 = new CheckboxGroup();
		lck = new Checkbox("UnLock" , cbg2 , true);
		unlck = new Checkbox("Lock" , cbg2 , false);

		size = new TextField(3);

		syswl = new Label("");

		stsrpt = new TextArea(10 , 50);

		psz = new Scrollbar(Scrollbar.HORIZONTAL, 0 , 1 , 0, 4);

		sip.setForeground(Color.black);					
		size.setForeground(Color.black);					
		acl.setForeground(Color.black);					
		stsrpt.setForeground(Color.black);	
		prt.setForeground(Color.black);									

		cap.setBounds(220 , 30, 380 ,30);
		Color b4 = new Color(255 , 125 , 125);
		cap.setBackground(b4);
		cap.setForeground(Color.black);
		lb1.setBounds(10 , 60 , 100 , 30);		
		sip.setBounds(10 , 95 , 200 , 23);
		lb2.setBounds(210, 60, 100, 30);
		prt.setBounds(210 , 95, 50 , 23);
		bt1.setBounds(270, 95, 100 , 25);
		lb3.setBounds(400 , 60, 150 , 30);
		acl.setBounds(400 , 95, 225 , 140);
		lb11.setBounds(630, 60 , 35 , 30);
		lb12.setBounds(675 , 60 , 150 , 30);
		grpl.setBounds(675 , 95 , 95 , 140);
		szl.setBounds(630 , 95 , 40, 140);
		szl.setForeground(Color.black);
		lb4.setBounds(10 , 140 , 150 , 30);
		bck1.setBounds(50 , 190 , 250 , 40);
		bck1.setBackground(Color.cyan);
		en.setBounds(65 , 194 , 100 , 30);
		dis.setBounds(180 , 194 , 100 , 30);
		en.setBackground(Color.cyan);
		dis.setBackground(Color.cyan);

		lb5.setBounds(10 , 280, 100 , 20);
		psz.setBounds(15 , 300 , 100 , 20);	
		srblbl.setBounds(25 , 320, 100, 20);
		Color b1 = new Color(200 , 100 , 100);
		psz.setBackground(b1);
		psz.setEnabled(false);		

		lb9.setBounds(10 , 360 , 100 , 30);
		bck2.setBounds(50 , 410 , 250 , 40);
		bck2.setBackground(Color.cyan);
		lck.setBounds(65 , 414 , 100 , 30);
		unlck.setBounds(180 , 414 , 100 , 30);
		lck.setBackground(Color.cyan);
		unlck.setBackground(Color.cyan);

		lb6.setBounds(250,  280, 50 , 20);
		size.setBounds(250 , 300 , 30 , 23);
		size.setText(String.valueOf(0));
		lb8.setBounds(282 , 300, 40 , 20);
		bt2.setBounds(530 , 240 , 110 , 25);
	
		lb7.setBounds(500 , 340 , 100 , 30);
		stsrpt.setBounds(400 , 380 , 350 , 150);

		lb10.setBounds(10, 460, 150 , 20);
		syswl.setBounds(10 , 490 , 300, 20);
		Color b3 = new Color(255, 250, 150);
		syswl.setBackground(b3);

		Frz.setBounds(20 , 520 , 70 , 25);
		Rest.setBounds(95 , 520 , 70 , 25);
		Hlp.setBounds(170 , 520 , 70 , 25);
		Ext.setBounds(244 , 520 , 70 , 25);

		ln1.setBackground(Color.black);
		ln1.setBounds(2 , 270 , 800 , 2);
			
		Font f2 = new Font("Arial" , Font.BOLD, 12);
		setFont(f2);
		add(lb1);	
                add(sip);		
                add(bt1);	
                add(prt);
		add(lb2);	
                add(bt1);	
                add(lb3);	
                add(acl);
		add(cap);	
                add(lb4);	
                add(en);	
                add(dis);
		add(bck1);	
                add(lb5);	
                add(lck);	
                add(unlck);
		add(bck2);	
                add(psz);	
                add(srblbl);	
                add(lb9);
		add(lb6);	
                add(size);	
                add(bt2);	
                add(lb7);
                add(stsrpt);	
                add(lb8);	
                add(lb10);	
                add(syswl);
		add(Frz);	
                //add(Rest);
                //add(Hlp);
                add(Ext);                		
		add(lb11);	
                add(szl);	
                add(ln1);	
                add(grpl);	
		add(lb12);
		Font f3 = new Font("Arial" , Font.BOLD, 12);
		setFont(f3);
		add(acl);
                add(szl);	
                add(stsrpt);		
		InetAddress iadd = InetAddress.getLocalHost();
		sip.setText(String.valueOf(iadd));

		bt1.addActionListener(this);
		bt2.addActionListener(this);
		Frz.addActionListener(this);
		Rest.addActionListener(this);
		Hlp.addActionListener(this);
		Ext.addActionListener(this);
		en.addItemListener(this);
                lck.addItemListener(this);
                unlck.addItemListener(this);
		dis.addItemListener(this);
		acl.addItemListener(this);
		psz.addAdjustmentListener(this);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				dispose();
				System.exit(0);
			}
		});
	}

	public void adjustmentValueChanged(AdjustmentEvent ae)
	{
		if(ae.getSource() == psz)
		{
			size.setText(String.valueOf(psz.getValue()));
		}
	}

	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getSource() == en)
		{
			psz.setEnabled(true);
		}
                
                if(ie.getSource() == lck)
                {
                    System.out.println("Unlock Started");
                    bt2.setEnabled(true);
                    
                }
                
                if(ie.getSource() == unlck)
                {
                    System.out.println("PMS Lockiing");
                    bt2.setEnabled(false);
                }

		if(ie.getSource() == dis)
		{
			System.out.println("Dis");
			psz.setEnabled(false);
		}
		if(ie.getSource() == acl)
		{
			System.out.println("List Event");
			/*td = new Thread();
			td.start();*/
		}
			
	}

	public void actionPerformed(ActionEvent ae)
	{	
		Color nb1 = new Color(250 , 150 , 150);
		String sr = ae.getActionCommand();
		if(ae.getSource() == Frz)
		{				
			if(sr.equals("Freeze"))
			{
				Frz.setBackground(nb1);
				en.setEnabled(false);
				dis.setEnabled(false);
				psz.setEnabled(false);
				lck.setEnabled(false);
				unlck.setEnabled(false);
				size.setEnabled(false);
				Frz.setLabel("UnFreeze");
			}
			if(sr.equals("UnFreeze"))
			{
				Color nb2 = new Color(150 , 230 , 150);
				Frz.setBackground(nb2);
				en.setEnabled(true);
				dis.setEnabled(true);
				psz.setEnabled(true);
				lck.setEnabled(true);
				unlck.setEnabled(true);
				size.setEnabled(true);
				Frz.setLabel("Freeze");
			}			
		}

		/**************************Server Invocation******************************/
		if(sr.equals("Start Server"))
		{
			stsrpt.setText("Please wait.... Server Initializing..... \n");
			startServer();	
			td = new Thread(this, "Control1");
			td.start();				
		}
		
		if(ae.getSource() == Ext)
		{
			Ext.setBackground(nb1);
			dispose();
			System.exit(0);	
		}

		if(ae.getSource() == bt2)
		{
			try	
			{
			System.out.println("PMS");
			gFlg = 1;
			for(int jk = 0 ; jk < grpl.getItemCount(); jk++)
				grpl.remove(jk);
				
			int cnt = szl.getItemCount();
			int totsz = 0, flg = 0;
			System.out.println("*************Web Server Monitor*************");
			/*for(int i = 0 ; i < cnt ; i++)
			{
				System.out.println(szl.getItem(i));
				totsz = totsz + Integer.parseInt(szl.getItem(i));
				System.out.println(totsz);
				if(totsz < Integer.parseInt(size.getText()))
				{
					if(flg == 0)
					{						
						grpl.add("Group A" , i);
					}
					
					else if(flg == 1)					
					{
		
						grpl.add("Group B" , i);
					}
					
					else if(flg == 2)
					{
						
						grpl.add("Group C" , i);
					}
					
					if(flg == 0 && totsz > 30)
					{
						flg = 1;
						totsz = 0;
					}
					else if(flg == 1 && totsz > 30)
					{					
						flg = 2;
						totsz = 0;
					}
					else if(flg == 2 && totsz > 30)						
					{
						flg = 0;
						totsz = 0;
					}
											
				}
				
				if(totsz > Integer.parseInt(size.getText()))
				{
					totsz = 0;
				}
				
			}*/
							
			td = new Thread(this, "Control1");
			td.start();							
			PMS ps = new PMS("Web Server Cluster Pool Monitoring System" , size , szl , grpl);
			}
                        catch(Exception e){}
		}			
	}

	public void startServer()
	{
		int portno = 0;
		sip.setEnabled(false);
		
		try
		{
			portno = Integer.parseInt(prt.getText());
		}catch(NumberFormatException e)
		{
			stsrpt.append("\n Invalid Port Number... Abnormal System Termination....\n Restart the Server");
			bt1.setEnabled(true);
		}

		listening = new ServThread(portno, stsrpt , acl , sip, bt1, szl , gFlg);
		listening.start();//Main Thread Starts Here...

	}

	public void run()
	{
		try
		{			
			while(true)
			{				
				tCap = Integer.parseInt(size.getText());
				tCap = tCap * 10;
				if(acl.getItemCount() != 0 && szl.getItemCount() != 0)
				{
					for(int sc = 0 ; sc < szl.getItemCount(); sc++)
					{
						System.out.println("Sc Value : " + sc);
						tSz = tSz + Integer.parseInt(szl.getItem(sc));
						if(tSz < tCap)
						{
							if(flg == 1)
								grpl.add("Group A" , sc);
							if(flg == 2)
								grpl.add("Group B" , sc);
							if(flg == 3)
								grpl.add("Group C" , sc);
						}
						
						if(tSz > tCap)
						{
							tCap = tCap + tSz;
							if(flg == 0)
								sflg = 1;
							if(flg == 1)
								sflg = 2;
							if(flg == 2)
								sflg = 3;
							if(flg == 3)
							{
								tCap = 0;
								tSz = 0;
							}
							flg = sflg;
						}
					}
					break;
				}
				Thread.sleep(500);
			}
		}catch(Exception e){}			
	}
			
//	public static void main(String ar[])throws UnknownHostException
//	{
//		ServerApp sa = new ServerApp("* * * * * * * * * * * *  Thread Based Sclable Web Server Cluster   * * * * * * * * * * * * * *");
//	}
}
