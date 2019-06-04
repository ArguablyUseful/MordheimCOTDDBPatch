import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Patcher {

	Connection conn;
	String dbPath;
	
	String filename;
	String formatFile = "UTF-8";
	
	FileReader reader;
	
	public Patcher(String filename, String dbPath) throws java.sql.SQLException, IOException
	{
		this.filename = filename;
		this.dbPath = dbPath;
		this.conn  = Common.Connect(dbPath);
		this.reader = new FileReader(filename);
	    BufferedReader br = new BufferedReader(reader);
	    String next_cmd;
		while ((next_cmd = br.readLine()) != null) {
			PreparedStatement pstmt = conn.prepareStatement(next_cmd) ;
			pstmt.executeUpdate();
		}
		this.reader.close();	
		this.conn.close();
	}
	public static void main(String []args)
	{
		EventQueue.invokeLater(() -> {
			try {
		        UI app = new UI();
		        app.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});		
		
	}
}
