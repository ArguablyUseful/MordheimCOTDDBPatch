import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class UI extends JFrame implements ActionListener
{

	JButton patchBtn;

	public UI()
	{
		
		patchBtn = new JButton("PATCH");
		patchBtn.addActionListener(this);
		this.getContentPane().add(patchBtn);
		
		setTitle("Mordheim patch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //center the jframe on the screen
        this.setSize(new Dimension(100,100));
		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if ( ae.getSource() == this.patchBtn)
		{
			this.patchBtn.setEnabled(false);
			this.patchBtn.setText("Patching...");
			try {
				new Patcher("sqlCommands.txt", "jdbc:sqlite:mordheim");
				this.patchBtn.setText("Done !");
			} catch (SQLException | IOException e) {
				this.patchBtn.setText("Error.");
				//e.printStackTrace();
				//System.out.println(e.getMessage());
				
			}
		}
	}
}
