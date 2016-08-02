/**
 *
 * 
 */
import javax.swing.JWindow;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.net.UnknownHostException;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class Loading extends JWindow {

	private JPanel content = null;
	private JProgressBar progress = null;
	private JLabel l1 = null;

	public Loading() {
		super();
		initialize();             

		// TODO Auto-generated constructor stub
	}

	private void initialize() {
        this.setSize(new java.awt.Dimension(700,100));
        this.setLocation(200,200);
        this.setContentPane(getContent());
        this.setVisible(true);
        this.setProgress();


	}
	public void setProgress()
	{
		for(int i=0;i<=200;i++)
		{
			if(i==200)
			{
				this.dispose();
			}
			else
			{
				progress.setValue(i);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * This method initializes content
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getContent() {
		if (content == null) {
			l1 = new JLabel();
			l1.setText("Web Server Cluster Loading ........................");
			l1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			//l1.setIcon(new ImageIcon("D:/JAVA NETBEANS/COGNITION/Icon.jpg"));
                      	content = new JPanel();
			content.setLayout(new BorderLayout());
			content.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.activeCaptionText,2), "Internet Traffic Control Web Server Monitoring", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Courier New", java.awt.Font.BOLD | java.awt.Font.ITALIC, 24), java.awt.Color.black));
			content.add(getProgress(), java.awt.BorderLayout.SOUTH);
			content.add(l1, java.awt.BorderLayout.CENTER);
		}
		return content;
	}

	/**
	 * This method initializes progress
	 *
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgress() {
		if (progress == null) {
			progress = new JProgressBar();
			progress.setMaximum(200);
			progress.setForeground(new java.awt.Color(0,102,102));
			progress.setFont(new java.awt.Font("Courier New", java.awt.Font.BOLD | java.awt.Font.ITALIC, 10));
			progress.setStringPainted(true);
			progress.setMinimum(0);
		}
		return progress;
	}
        public static void main(String args[]) throws UnknownHostException
        {
          new Loading();
          ServerApp sa = new ServerApp("* * * * * * * * * * * *  Thread Based Sclable Web Server Cluster   * * * * * * * * * * * * * *");
          sa.setVisible(true);
        }
}