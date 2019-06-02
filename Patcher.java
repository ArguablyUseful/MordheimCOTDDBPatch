import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Patcher {

	Connection conn;
	String dbPath = "jdbc:sqlite:mordheim";
	
	public Patcher()
	{
		this.conn  = Common.Connect(dbPath);
		try {
			this.patch_doc_00();
			this.patch_doc_01();
			this.patch_doc_02();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void ReplaceField(String tableName, String columnName, int value, int key) throws SQLException
	{
		String []keyCols = new String[1];
		keyCols[0] = "id";
		int [] keys = new int[1];
		keys[0] = key;
		ReplaceField(tableName, columnName, value, keyCols, keys);
	}
	private void ReplaceField(String tableName, String colunmName, int value, String []keyCols, int[] key) throws SQLException
	{
		//ContentValues cv = new ContentValues();
		//cv.put(ColumnName, newValue);
		
		//String sql = "UPDATE "+TABLE_NAME +" SET " + ColumnName+ " = '"+newValue+"' WHERE "+Column+ " = "+rowId;
		
		String sql = "UPDATE " + tableName + " SET " + colunmName + " = ? WHERE";
		for(int index = 0; index < keyCols.length; index++)
		{
			sql += " " + keyCols[index] + " = ?";
			if ( index != keyCols.length - 1)
				sql += " AND";
		}
		PreparedStatement pstmt = conn.prepareStatement(sql) ;
        // set the corresponding param
        //pstmt.setString(1, tableName);
		int idx = 1;
        pstmt.setInt(idx, value);
        for(int index = 0; index < keyCols.length; index++)
        {
        	idx++;
        	pstmt.setInt(idx, key[index]);	
        }
        
        
        // update 
        pstmt.executeUpdate();		
	}
	
	 private void AddEntry_enchantment_join_attribute(int fk_ench_id, int fk_attr_id, int modifier) throws SQLException {
	        String sql = "INSERT INTO enchantment_join_attribute(fk_enchantment_id,fk_attribute_id, modifier) VALUES(?,?,?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, fk_ench_id);
	        pstmt.setInt(2, fk_attr_id);
	        pstmt.setInt(3, modifier);
	        pstmt.executeUpdate();
	   }
	private void AddEntry_attribute_attribute(int id, int fk_attr_id_base, int fk_attr_id, int modifier) throws SQLException
	{
		String sql = "INSERT INTO attribute_attribute (id, fk_attribute_id, fk_attribute_id_base, modifier) VALUES(?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setInt(2, fk_attr_id);
        pstmt.setInt(3, fk_attr_id_base);
        pstmt.setInt(4, modifier);
        pstmt.executeUpdate();
	}
	private void RemoveEntry_attribute_attribute(int id_key) throws SQLException
	{
		String sql = "DELETE FROM attribute_attribute WHERE ID = " + id_key;
		PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();	
	}
	private void patch_doc_00() throws SQLException
	{
		//perception buff
		ReplaceField("enchantment", "duration", 3, 219);
		AddEntry_enchantment_join_attribute(219, 74, 15);
		//adds ambush melee resistance buff
		
		//reducing climb by 20%
		ReplaceField("attribute", "base_roll", 40, 16);
		ReplaceField("attribute", "base_roll", 30, 109);
		ReplaceField("attribute", "base_roll", 20, 110);
		
		//ReplaceField("attribute", "base_roll", 30, 17); //leap
		
		//ReplaceField("attribute", "base_roll", 40, 18); //jump
		//ReplaceField("attribute", "base_roll", 30, 111); //jump 
		//ReplaceField("attribute", "base_roll", 20, 112); //jump
		
		//climb always get buffed when failed.
		ReplaceField("enchantment_effect_enchantment", "fk_enchantment_trigger_id", 0, 216); //try out
		ReplaceField("enchantment_effect_enchantment", "fk_enchantment_trigger_id", 0, 217); //try out
		ReplaceField("enchantment_effect_enchantment", "fk_enchantment_trigger_id", 0, 218); //try out
		
		//so that the buff stays for 3 turns
		ReplaceField("enchantment", "duration", 3, 1069); //seems a bit high to me.
		
		//--------------changing effects for allocating points --------------
		//removing melee res from WS
		RemoveEntry_attribute_attribute(26);
		//adding melee res to ALERTNESS
		AddEntry_attribute_attribute(26, 14,75,1); //using the old attribute id 
		
		//modify parry chance WS from 4 to 5
		{
			String[] colKeys = {"fk_attribute_id_base", "fk_attribute_id"};
			int[] keys = {2, 40};
			ReplaceField("attribute_attribute", "modifier", 5, colKeys, keys);
		}
		//remove ranged resist from alertness
		RemoveEntry_attribute_attribute(24);
		//add ranged resist to intelligence
		AddEntry_attribute_attribute(24, 10,74,1);
		
		//--------------------ACTIVE SKILLS (STRENGTH)----------------
		
		//might charge
		{
			String [] colKeys = {"id", "fk_skill_id"};
			int [] keys = {91, 138};
			ReplaceField("skill_attribute", "modifier", -20, colKeys, keys);
		}
		//swift charge
		ReplaceField("skill", "points", 2, 131);
		{
			String [] colKeys = {"id", "fk_skill_id"};
			int [] keys = {81, 131};
			ReplaceField("skill_attribute", "modifier", 30, colKeys, keys);
		}
		//kidney strike TODO
		//strong blow
		{
			String []colKeys = {"id", "fk_skill_id"};
			int [] keys = {340,541};
			ReplaceField("skill_attribute", "modifier", 25, colKeys, keys);
		}
		//daredevil TODO
		
		
	}
	
	private void patch_doc_01() throws SQLException
	{
		//intensity 
		//trivial to train
		ReplaceField("skill","points", 0,547);
		//buff
		ReplaceField("enchantment", "duration", 2, 489);
		//wound
		ReplaceField("enchantment", "damage_min", 5, 491);
		ReplaceField("enchantment", "damage_max", 5, 491);
		//easy to master
		ReplaceField("skill", "points", 2, 548);
		//buff
		ReplaceField("enchantment", "duration", 3, 490); 
		
		//cauterize 
		ReplaceField("skill","stat_value", 3, 345);
		ReplaceField("skill","stat_value", 9, 346);
		
		//frenzy
		ReplaceField("skill","stat_value", 6, 14 );
		ReplaceField("skill","stat_value", 12, 15);
		//buff magic res to 15%
		{
			String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int [] keys = {118, 53, 5};
			ReplaceField("enchantment_join_attribute", "modifier", 15, colKeys, keys);	
		}
		//buff mastery magic res to 30%
		{
			String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int [] keys = {119, 53, 10};
			ReplaceField("enchantment_join_attribute", "modifier", 30, colKeys, keys);
		}
		
		//skill ignore pain
		ReplaceField("skill","stat_value", 6, 20 );
		ReplaceField("skill","stat_value", 12, 21);
		
		ReplaceField("enchantment", "duration", 2, 478);
		ReplaceField("enchantment", "damage_min", 10, 480);
		ReplaceField("enchantment", "damage_max", 10, 480);
		{
			String [] colKeys = { "fk_enchantment_id", "fk_attribute_id", "modifier" };
			int [] keys = {478, 130, 10};
			ReplaceField("enchantment_join_attribute", "modifier", 5, colKeys, keys); 		
		}
		ReplaceField("enchantment", "duration", 3, 479);
		ReplaceField("enchantment", "damage_min", 10, 811);
		ReplaceField("enchantment", "damage_max", 10, 811);
		//already 10% for armor absorbption
		//already not stackable

		//adrenaline rush
		ReplaceField("skill","stat_value", 9, 146);
		ReplaceField("skill","stat_value", 15, 147);
		//exhaustion
		ReplaceField("skill","stat_value", 9, 351 );
		ReplaceField("skill","stat_value", 15, 352);
		
		
		//agility skills
		
		//quick leap
		ReplaceField("skill","points", 0,357); //trivial to learn
		ReplaceField("skill","points", 2,358); // easy to master
		
		//swift jump
		ReplaceField("skill","points", 0,3); // trivial to learn
		ReplaceField("skill","points", 2,4); //easy to master
		//TODO -45% chances
		
		//wal runner
		ReplaceField("skill","points", 2,2); //easy to master
		//TODO -30%
		
		//careful approach
		//TODO
		
		//prowl
		ReplaceField("skill","points", 2,360); //easy to master
		ReplaceField("skill","strategy_points", 1, 360); // 1 more SP
		ReplaceField("skill","offense_points",2,360); // 1 less OP
		
	}
	
	private void patch_doc_02() throws SQLException
	{
		//Courage
		ReplaceField("enchantment", "duration", 2, 123); 
		ReplaceField("enchantment", "duration", 3, 124);
		
		//rallying cry
		ReplaceField("enchantment", "duration", 3, 420);
		
		//taunt
		ReplaceField("enchantment", "duration", 2, 277);
		ReplaceField("enchantment", "duration", 3, 279);
		//threaten
		ReplaceField("skill","points", 0, 36); //trivial to train
		ReplaceField("skill","points", 2, 37); //easy to master
		
		{
			String colKeys[] = { "fk_enchantment_id", "fk_attribute_id", "modifier" };
			int keys[] = {509, 114, -10};
			ReplaceField("enchantment_join_attribute","modifier", -5, colKeys, keys);
		}
		
		{
			String colKeys[] = { "fk_enchantment_id", "fk_attribute_id", "modifier" };
			int keys[] = {510, 114, -25};
			ReplaceField("enchantment_join_attribute","modifier", -35, colKeys, keys);
		}
		
		//coordination

		ReplaceField("enchantment", "duration", 1, 418);
		
		{
			String colKeys[] = { "fk_enchantment_id", "fk_attribute_id", "modifier" };
			int keys[] = {418, 76, 5};
			ReplaceField("enchantment_join_attribute","modifier", 10, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 419);
		{
			String colKeys[] = { "fk_enchantment_id", "fk_attribute_id", "modifier" };
			int keys[] = {419, 76, 10};
			ReplaceField("enchantment_join_attribute","modifier", 20, colKeys, keys);
		}
		//guidance 
		
		ReplaceField("skill", "points", 0, 305);
		ReplaceField("skill", "points", 2, 306);
		
		
		
	}
	
	public static void main(String []args)
	{
		new Patcher();
	}
}
