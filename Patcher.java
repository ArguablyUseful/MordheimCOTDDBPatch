import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Patcher {

	Connection conn;
	String dbPath = "jdbc:sqlite:mordheim";
	
	public Patcher() throws SQLException
	{
		this.conn  = Common.Connect(dbPath);

		this.patch_doc_00();
		this.patch_doc_01();
		this.patch_doc_02();
		this.patch_doc_03();
		this.patch_doc_04();
		this.patch_doc_05();
		this.patch_doc_06();
		this.patch_doc_07();
		this.patch_doc_08();
		this.patch_doc_09();
		
		this.conn.close();
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
		String sql = "UPDATE " + tableName + " SET " + colunmName + " = ? WHERE";
		for(int index = 0; index < keyCols.length; index++)
		{
			sql += " " + keyCols[index] + " = ?";
			if ( index != keyCols.length - 1)
				sql += " AND";
		}
		PreparedStatement pstmt = conn.prepareStatement(sql) ;
		int idx = 1;
        pstmt.setInt(idx, value);
        for(int index = 0; index < keyCols.length; index++)
        {
        	idx++;
        	pstmt.setInt(idx, key[index]);	
        }
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
	private void RemoveEntry_skill_perform_skill(int id_key) throws SQLException
	{
		String sql = "DELETE FROM skill_perform_skill WHERE ID = " + id_key;
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
		
		//careful approach TODO
		
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
		
		ReplaceField("skill", "points", 0, 305); //trivial to learn
		ReplaceField("skill", "points", 2, 306); //easy to master
		
		ReplaceField("enchantment", "duration", 1, 125);
		
		//hold ground
		
		ReplaceField("enchantment", "duration", 2, 687);

		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {687,40,5};
			ReplaceField("enchantment_join_attribute", "modifier",10, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {687,41,5};
			ReplaceField("enchantment_join_attribute", "modifier",10, colKeys, keys);
		}
		
		ReplaceField("enchantment", "duration", 3, 688);
		
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {688,40,10};
			ReplaceField("enchantment_join_attribute", "modifier",15, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {688,41,10};
			ReplaceField("enchantment_join_attribute", "modifier",15, colKeys, keys);
		}
		
		//insult
		
		ReplaceField("enchantment", "duration", 2, 802);
		ReplaceField("enchantment", "duration", 3, 803);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {803,75,-20};
			ReplaceField("enchantment_join_attribute", "modifier",-15, colKeys, keys);
		}
		
		//intimidate
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {536,114,5};
			ReplaceField("enchantment_join_attribute", "modifier",-5, colKeys, keys);
		}
		//retreat
		ReplaceField("skill","points", 0, 158); //trivial to learn
		ReplaceField("skill","points", 2, 159); //easy to master
		
		//war cry
		ReplaceField("enchantment", "duration", 2, 168);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {168,61,10};
			ReplaceField("enchantment_join_attribute", "modifier",5, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 169);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {169,61,20};
			ReplaceField("enchantment_join_attribute", "modifier",10, colKeys, keys);
		}
		
		//combat savy
		ReplaceField("enchantment", "duration", 2, 438);
		ReplaceField("enchantment", "duration", 3, 439);
		
		//Wild casting TODO
		
		//quick casting TODO
		
		//exploit positioning
		ReplaceField("enchantment", "duration", 2, 436);
		
		{//sets melee resistance instead of ranged resistance
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {436,74,-10};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id",75, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 437);
		{//sets melee resistance instead of ranged resistance
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {437,74,-20};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id",75, colKeys, keys);
		}
		{//sets to -15 instead of -20
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {437,75,-20};
			ReplaceField("enchantment_join_attribute", "modifier",-15, colKeys, keys);
		}
		
		//study
		ReplaceField("enchantment", "duration", 2, 180);
		ReplaceField("enchantment", "duration", 3, 181);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {181,40,20};
			ReplaceField("enchantment_join_attribute", "modifier",15, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {181,41,20};
			ReplaceField("enchantment_join_attribute", "modifier",15, colKeys, keys);
		}
		
		//introspection TODO
		
		//meditation TODO
	}
	private void patch_doc_03() throws SQLException
	{
		//staggering blow 458 457
		ReplaceField("enchantment", "duration", 2, 458);
		ReplaceField("enchantment", "duration", 3, 457);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {457,76,-30};
			ReplaceField("enchantment_join_attribute", "modifier",-20, colKeys, keys);
		}
		
		//ready stance
		ReplaceField("skill","points",0, 60); //trivial to learn
		ReplaceField("skill","points", 2, 61); //easy to master
		ReplaceField("skill","stat_value", 3, 60); //requiremnt
		ReplaceField("skill","stat_value", 9, 61); //requirement
		
		
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {172,76,10};
			ReplaceField("enchantment_join_attribute", "modifier",15, colKeys, keys);
		}
		
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {173,76,20};
			ReplaceField("enchantment_join_attribute", "modifier",45, colKeys, keys);
		}
		
		//stimulus
		ReplaceField("skill","stat_value", 6, 537); //requiremnt
		ReplaceField("skill","stat_value", 12, 538); //requirement
		ReplaceField("enchantment", "duration", 2, 461);
		ReplaceField("enchantment", "duration", 3, 462);
		
		//scout's advice
		ReplaceField("enchantment", "duration", 2, 463);
		ReplaceField("enchantment", "duration", 3, 464);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {463,74,10};
			ReplaceField("enchantment_join_attribute", "modifier", 15, colKeys, keys);
		}
		
		//deft stance
		ReplaceField("skill","points",0, 58); //trivial to learn
		ReplaceField("skill","points", 2, 59); //easy to master
		
		//initiative is 76
		this.AddEntry_enchantment_join_attribute(170, 76, -20);
		this.AddEntry_enchantment_join_attribute(171, 76, -40);
		
		//safe stance
		ReplaceField("skill","points", 2, 215); // easy to master
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {175,51,30};
			ReplaceField("enchantment_join_attribute", "modifier", 45, colKeys, keys);
		}
		
		//combat focus TODO (because I'll need to check wheter "only next action" skills have special field -which they probably do have-
		
		//jaw strike
		//everything goes to 20 for 2 turns
		ReplaceField("enchantment", "duration", 2, 443);
		
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {443,28,15};
			ReplaceField("enchantment_join_attribute", "modifier", 20, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {443,65,15};
			ReplaceField("enchantment_join_attribute", "modifier", 20, colKeys, keys);
		}
		
		ReplaceField("enchantment", "duration", 3, 444);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {444,30,-40};
			ReplaceField("enchantment_join_attribute", "modifier", -30, colKeys, keys);
		}
		
		//hamstring
		this.RemoveEntry_skill_perform_skill(13);
		this.RemoveEntry_skill_perform_skill(14);
		ReplaceField("enchantment", "duration", 2, 527);
		ReplaceField("enchantment", "duration", 3, 528);

		//onslaught
		ReplaceField("skill", "strategy_points", 1, 560);
		ReplaceField("skill","offense_points", 2, 560);
		ReplaceField("skill","points", 2, 560);
		
	}
	
	private void patch_doc_04() throws SQLException
	{
		//hand shot
		ReplaceField("enchantment", "duration", 2, 492);
		ReplaceField("enchantment", "duration", 3, 494);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {494,33,-20};
			ReplaceField("enchantment_join_attribute", "modifier", -15, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {494,35,-20};
			ReplaceField("enchantment_join_attribute", "modifier", -15, colKeys, keys);
		}
		
		
		//knee shot
		ReplaceField("enchantment", "duration", 2, 128);
		ReplaceField("enchantment", "duration", 3, 129);
		
		//Crippling shot
		ReplaceField("enchantment", "duration", 2, 500);
		ReplaceField("enchantment", "duration", 3, 501);
		
		//pinning shot
		ReplaceField("enchantment", "duration", 3, 499);
		
		//entrenched
		
		ReplaceField("skill", "points", 0, 72); //trivial to learn
		ReplaceField("skill", "points", 2, 73); // easy to master
		
		/* NOTE : skill_enchantment for the mastery (skill 73) has ID 424 & 425 identical. entrenched is buggy ? */
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {546,35,10};
			ReplaceField("enchantment_join_attribute", "modifier", 15, colKeys, keys);
		}
		
		//nerve shot TODO
		
		//feint TODO (can't find it)
		
		//precise strike
		ReplaceField("skill","points", 2, 264);
		
		//armor break TODO can't find it
		
		//head shot
		ReplaceField("skill_attribute", "modifier",-40, 344);
		ReplaceField("skill_attribute", "modifier",-10, 345);
	}
	private void patch_doc_05() throws SQLException
	{
		//blitz
		ReplaceField("skill","points", 0,16 );
		ReplaceField("skill","points", 2, 17);
		
		//bull charge
		ReplaceField("skill","points", 0,18 );
		ReplaceField("skill","points", 2, 19);
		
		//strider
		ReplaceField("skill","points", 2, 350);
		
		//surprise
		ReplaceField("skill","stat_value", 6, 134);
		ReplaceField("skill","stat_value", 12, 135);
		ReplaceField("skill","points", 2, 135);
		
		//armor proficient
		ReplaceField("skill","stat_value", 9, 563);
		ReplaceField("skill","stat_value", 15, 564);
		
		//thougness
		ReplaceField("skill_attribute", "modifier", 20, 95);
		ReplaceField("skill_attribute", "modifier", 60, 96);
		
		//veteran
		ReplaceField("skill","stat_value", 3, 26);
		ReplaceField("skill","stat_value", 9, 27);
		ReplaceField("skill","points", 0, 26);
		ReplaceField("skill","points", 2, 27);
		//TODO major revision
		
		//quick recovery
		ReplaceField("skill", "points", 0, 24);
		ReplaceField("skill", "points", 2, 25);

		//unstoppable
		ReplaceField("skill","stat_value", 6, 529);
		ReplaceField("skill","stat_value", 12, 530);
		ReplaceField("skill_attribute", "modifier", 30, 302);
		
		//hardy
		ReplaceField("skill_attribute", "modifier", 60, 154);
		//resilient
		ReplaceField("skill_attribute", "modifier", 30, 304);
		
		//ambuscade
		ReplaceField("skill", "points", 2, 239); //easy to master
		ReplaceField("skill_attribute", "modifier", 30, 112); 
		
		//athletic expert TODO (not found)
		
		//acrobatic
		ReplaceField("skill", "points", 0, 7);
		ReplaceField("skill", "points", 2, 9);
		
		//counterblow
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {637,33,10};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 36, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {638,33,30};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 36, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {638,36,30};
			ReplaceField("enchantment_join_attribute", "modifier", 20, colKeys, keys);
		}
		
		//quick draw
		ReplaceField("skill","points",2, 11); //easy to master
	}
	
	private void patch_doc_06() throws SQLException
	{
		//nerves of steel
		ReplaceField("skill","stat_value", 3, 38);
		ReplaceField("skill","stat_value", 9, 39);
		
		//commander
		ReplaceField("skill_attribute", "modifier",8,25);
		ReplaceField("skill_attribute", "modifier",16,26);
		
		//inspiring
		ReplaceField("skill_attribute", "modifier",4,99);
		ReplaceField("skill_attribute", "modifier",12,100);
		
		//demoralize
		ReplaceField("enchantment", "duration", 2, 422);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {422,23,-5};
			ReplaceField("enchantment_join_attribute", "modifier", -4, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 423);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {423,23,-10};
			ReplaceField("enchantment_join_attribute", "modifier", -6, colKeys, keys);
		}
		
		//battle tongue
		ReplaceField("skill", "points", 0, 166); //trivial to train
		
		ReplaceField("skill_attribute", "modifier", 60, 358);
		
		//serenity
		ReplaceField("enchantment", "duration", 2, 424);
		
		ReplaceField("enchantment", "duration", 3, 425);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {425,75,20};
			ReplaceField("enchantment_join_attribute", "modifier", 15, colKeys, keys);
		}
		
		//heroic presence TODO (not found)
		
		//devotion channeling TODO spell casting
		
		//tracker
		ReplaceField("skill", "points", 0, 375);
		ReplaceField("skill", "points", 2, 376);
		ReplaceField("skill", "stat_value", 3, 375);
		ReplaceField("skill", "stat_value", 9, 376);
		//TODO
		
		//resist corruption
		ReplaceField("skill", "points", 0, 202);
		ReplaceField("skill", "points", 2, 203);
		ReplaceField("skill", "stat_value", 3,202);
		ReplaceField("skill", "stat_value", 9, 203);
		
		ReplaceField("skill_attribute", "modifier", 20, 103);
		ReplaceField("skill_attribute", "modifier", 60, 104);
		
		
		//divine & arcane study
		ReplaceField("skill", "points", 0, 531);
		ReplaceField("skill", "points", 2, 532);
		ReplaceField("skill", "points", 0, 533);
		ReplaceField("skill", "points", 2, 534);

		//resist magic
		ReplaceField("skill", "stat_value", 6, 200);
		ReplaceField("skill", "stat_value", 12, 201);
		ReplaceField("skill_attribute", "modifier", 20, 101);
		ReplaceField("skill_attribute", "modifier", 40, 102);
		
		//knowledge : ambush
		ReplaceField("skill", "stat_value", 6, 552);
		ReplaceField("skill", "stat_value", 12, 553);
		ReplaceField("skill", "points", 2, 553);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {512,75,40};
			ReplaceField("enchantment_join_attribute", "modifier", 60, colKeys, keys);
		}
		
		//piety expert casting TODO spell casting

		//knowledge tactic
		ReplaceField("skill", "stat_value", 9, 206);
		ReplaceField("skill", "stat_value", 15, 207);
		
		ReplaceField("skill_attribute", "modifier", 10, 330);
		ReplaceField("skill_attribute", "modifier", 10, 331);
		ReplaceField("skill_attribute", "modifier", 30, 332);
		ReplaceField("skill_attribute", "modifier", 30, 333);
		
	}
	private void patch_doc_07() throws SQLException
	{
		//sixth sense
		ReplaceField("skill", "points", 0, 5);
		ReplaceField("skill", "points", 2, 6);
		
		//TODO remove cost & change stats ?
		
		//vigilance
		ReplaceField("skill", "points", 2, 229);
		ReplaceField("skill_attribute", "modifier", 20, 109);
		ReplaceField("skill_attribute", "modifier", 60, 110);
		
		//combat movement
		ReplaceField("skill", "points", 2, 63);
		
		//concealement
		ReplaceField("skill_attribute","modifier", 10  ,65);
		ReplaceField("skill_attribute", "modifier",20 ,66);
		
		//nimble TODO (the spec says to decrease the stats...wtf ?
		
		
		//flash parry
		ReplaceField("skill_attribute", "modifier", 30, 114);
		
		//shield specialist TODO 
		
		//retaliation TODO instruction unclear
		
		//swift counter TODO instruction unclear
		
	}
	private void patch_doc_08() throws SQLException
	{
		//overhead
		ReplaceField("skill_attribute", "modifier", 10, 353);
		ReplaceField("skill_attribute", "modifier", 20, 354);
		
		//bulls eye
		ReplaceField("skill", "stat_value", 6, 255);
		ReplaceField("skill", "stat_value", 12, 256);
		
		//quick reload
		ReplaceField("skill", "stat_value", 9, 76);
		ReplaceField("skill", "stat_value", 15, 77);
		
		//sharpshooter
		ReplaceField("enchantment", "duration", 2, 476);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {476,37,2};
			ReplaceField("enchantment_join_attribute", "modifier", 1, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 477);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {477,37,4};
			ReplaceField("enchantment_join_attribute", "modifier", 2, colKeys, keys);
		}
		
		//quick incision
		//to range resistance
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {451,75,-3};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 74, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 2, 451);
		ReplaceField("enchantment", "duration", 3, 452);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {452,75,-6};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 74, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {452,74,-6};
			ReplaceField("enchantment_join_attribute", "modifier", -4, colKeys, keys);
		}
		
		//weak spot
		ReplaceField("skill_attribute", "modifier", -10, 51);
		ReplaceField("skill_attribute", "modifier", -10, 52);
		ReplaceField("skill","points", 2, 85); // easy to master
		
		//fatality : increase both melee & ranged crit (but only proc on melee damage)
		ReplaceField("enchantment", "duration", 2, 455);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {455,36,5};
			ReplaceField("enchantment_join_attribute", "modifier", 3, colKeys, keys);
			keys[1] = 37;
			ReplaceField("enchantment_join_attribute", "modifier", 3, colKeys, keys);
		}
		ReplaceField("enchantment", "duration", 3, 456);
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {456,36,10};
			ReplaceField("enchantment_join_attribute", "modifier", 4, colKeys, keys);
			keys[1] = 37;
			ReplaceField("enchantment_join_attribute", "modifier", 4, colKeys, keys);
		}
		
		//underdog
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {552,36,8};
			ReplaceField("enchantment_join_attribute", "modifier", 10, colKeys, keys);
		}
		{
			String colKeys[] = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int keys[] = {553,36,15};
			ReplaceField("enchantment_join_attribute", "modifier", 20, colKeys, keys);
		}
		
		//break defense TODO not found
		
		
		
	}
	
	private void patch_doc_09() throws SQLException
	{
		
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
