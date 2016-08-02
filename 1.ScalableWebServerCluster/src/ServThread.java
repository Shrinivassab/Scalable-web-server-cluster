/**
 *
 * @
 */

import java.net.*;

import java.awt.*;

public final class ServThread extends Thread
{
	int port;
	TextField sip;
	List acl,szl;
	TextArea stsrpt;
	Button bt1;
	Thread t;
	int pt = 0, gFlg = 0;
	Runtime r = Runtime.getRuntime();
	long mem1 , mem2, mem3;


	ServThread(int port , TextArea stsrpt , List acl , TextField sip, Button bt1, List szl, int gFlg)
	{
		this.port = port;
		this.stsrpt = stsrpt;
		this.acl = acl;
		this.sip = sip;
		this.bt1 = bt1;
		this.szl = szl;
		this.gFlg = gFlg;
	}

	public void run()
	{
		ServerSocket listener = null;
		try
		{
			stsrpt.append("\n Starting Server on Port " + port + "\n");
			listener = new ServerSocket(port);
		}catch(Exception e){stsrpt.append("\n Malfunctioning on Network Terminal.... Please Try Again...");}

		while(true)
		{
			try
			{
			
			if(gFlg == 1)
			{
				System.out.println("PMS");
		
                             //PMS ps = new PMS("Thread Pool Monitoring System");
			}
			System.out.println("Total Memory is : " + r.totalMemory());
			
			mem1 = r.freeMemory();
			new Thread(new ServCon(listener.accept(), stsrpt , acl , sip , bt1, szl)).start();
			mem2 = r.freeMemory();
			if(mem1 > mem2)
				mem3 = ((mem1-mem2)/1024);
			else
				mem3 = ((mem2-mem1)/1024);	

			if(szl.getItemCount() == 0)
				szl.add(String.valueOf(mem3/10));
			else
			{
				int icnt = szl.getItemCount();
				int lsz = Integer.parseInt(szl.getItem(icnt-1));
				if(mem3 > lsz)
					mem3 = mem3 - lsz;
				else
					mem3 = lsz - mem3;	
				szl.add(String.valueOf(mem3/10));
			}
			
			pt++;	
			int icnt1 = szl.getItemCount();
			int min1 = Integer.parseInt(szl.getItem(0));

			int minidx1 = 0;
			for( int j1 = 0 ; j1 < icnt1; j1++)
			{
				if(Integer.parseInt(szl.getItem(j1)) < min1)
				{
					min1 = Integer.parseInt(szl.getItem(j1));
					minidx1 = j1;
				}
			}
			acl.select(minidx1);
			szl.select(minidx1);

			if(acl.getItemCount() > 7)
			{				
				stsrpt.setForeground(Color.red);
				stsrpt.append("\n\n Before Garbage collection" + r.freeMemory()+"\n");
				stsrpt.setForeground(Color.black);
				r.gc();
				stsrpt.setForeground(Color.red);
				stsrpt.append("\n\n After Garbage collection" + r.freeMemory()+"\n");
				//stsrpt.setForeground(Color.black);
				int icnt = szl.getItemCount();
				int min = Integer.parseInt(szl.getItem(0));
				int minidx = 0;
				for(int j = 0 ; j < icnt; j++)
				{
					if(Integer.parseInt(szl.getItem(j)) < min)
					{
						min = Integer.parseInt(szl.getItem(j));
						minidx = j;
					}
				}
				szl.remove(minidx);
				acl.remove(minidx);	
                                
				/*if(szl.getItemCount() != acl.getItemCount())
				{
					szl.remove(szl.getItemCount()-1);
				}*/
			}
			
			}catch(Exception e)
			{
				stsrpt.append("\n" + e + "\n");
				continue;
			}
		}	
	}	
}