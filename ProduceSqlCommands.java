import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ProduceSqlCommands {

	PrintWriter writer;
	String filename;
	String formatFile = "UTF-8";
	
	public static void main(String []args)
	{
		try {
			new ProduceSqlCommands("sqlCommands.txt");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ProduceSqlCommands(String filename) throws java.sql.SQLException, IOException
	{
		this.filename = filename;

		this.writer = new PrintWriter("sqlCommands.txt", "UTF-8");
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
		
		writer.close();
	}
	
	private void WriteToFile(String sqlStatement)
	{
		writer.print(sqlStatement += "\n");
	}
	private void ReplaceField(String tableName, String columnName, int value, int key) throws java.sql.SQLException
	{
		String []keyCols = new String[1];
		keyCols[0] = "id";
		int [] keys = new int[1];
		keys[0] = key;
		ReplaceField(tableName, columnName, value, keyCols, keys);
	}
	private void ReplaceField(String tableName, String colunmName, int value, String []keyCols, int[] key) throws java.sql.SQLException
	{
		String sql = "UPDATE " + tableName + " SET " + colunmName + " = ? WHERE";
		for(int index = 0; index < keyCols.length; index++)
		{
			sql += " " + keyCols[index] + " = ?";
			if ( index != keyCols.length - 1)
				sql += " AND";
		}		
		String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(value));
        for(int index = 0; index < keyCols.length; index++)
        {
        	sql2 = sql2.replaceFirst(regex, Integer.toString(key[index]));
        }
        this.WriteToFile(sql2);

	}
	private void AddEntry_enchantment_join_attribute(int fk_ench_id, int fk_attr_id, int modifier) throws java.sql.SQLException {
	        String sql = "INSERT INTO enchantment_join_attribute(fk_enchantment_id,fk_attribute_id, modifier) VALUES(?,?,?)";
	        String sql2 = new String(sql);
			String regex = "\\?";
			sql2 = sql2.replaceFirst(regex, Integer.toString(fk_ench_id));
			sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attr_id));
			sql2 = sql2.replaceFirst(regex, Integer.toString(modifier));
			this.WriteToFile(sql2);

	   }
	private void AddEntry_attribute_attribute(int id, int fk_attr_id_base, int fk_attr_id, int modifier) throws java.sql.SQLException
	{
		String sql = "INSERT INTO attribute_attribute (id, fk_attribute_id, fk_attribute_id_base, modifier) VALUES(?,?,?,?)";
        
        String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attr_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attr_id_base));
		sql2 = sql2.replaceFirst(regex, Integer.toString(modifier));
		this.WriteToFile(sql2);        
	}
	private void RemoveEntry_attribute_attribute(int id_key) throws java.sql.SQLException
	{
		String sql = "DELETE FROM attribute_attribute WHERE ID = " + id_key;
		String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id_key));
		this.WriteToFile(sql2);
	}
	private void RemoveEntry_skill_perform_skill(int id_key) throws java.sql.SQLException
	{
		String sql = "DELETE FROM skill_perform_skill WHERE ID = " + id_key;
		
		String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id_key));
		this.WriteToFile(sql2);
		
	}
	private void patch_doc_00() throws java.sql.SQLException
	{
		//perception buff
		ReplaceField("enchantment", "duration", 3, 219);
		AddEntry_enchantment_join_attribute(219, 74, 15);
		//TODO adds ambush melee resistance buff
		
		//TODO spellcasting stuff
		
		//reducing climb by 20%
		ReplaceField("attribute", "base_roll", 40, 16);
		ReplaceField("attribute", "base_roll", 30, 109);
		ReplaceField("attribute", "base_roll", 20, 110);
		
		ReplaceField("attribute", "base_roll", 30, 17); //leap
		
		ReplaceField("attribute", "base_roll", 40, 18); //jump
		ReplaceField("attribute", "base_roll", 30, 111); //jump 
		ReplaceField("attribute", "base_roll", 20, 112); //jump
		
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
		
	private void Replace_enchantment_join_attribute(int fk_ench_id, int fk_attr_id, int mod, int newMod) throws java.sql.SQLException
	{
		String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
		int [] keys = {fk_ench_id, fk_attr_id, mod};
		ReplaceField("enchantment_join_attribute", "modifier", newMod, colKeys, keys);
	}
	private void patch_doc_01() throws java.sql.SQLException
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
		Replace_enchantment_join_attribute(118,53,5,15);
		
		//buff mastery magic res to 30%
		Replace_enchantment_join_attribute(119,53,10,30);
		
		//skill ignore pain
		ReplaceField("skill","stat_value", 6, 20 );
		ReplaceField("skill","stat_value", 12, 21);
		
		ReplaceField("enchantment", "duration", 2, 478);
		ReplaceField("enchantment", "damage_min", 10, 480);
		ReplaceField("enchantment", "damage_max", 10, 480);
		Replace_enchantment_join_attribute(478,130,10,5);
		
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
	
	private void patch_doc_02() throws java.sql.SQLException
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
		
		Replace_enchantment_join_attribute(509,114,-10,-5);
		Replace_enchantment_join_attribute(510,114,-25,-35);
		
		
		//coordination

		ReplaceField("enchantment", "duration", 1, 418);
		Replace_enchantment_join_attribute(418,76,5,10);
		ReplaceField("enchantment", "duration", 3, 419);
		Replace_enchantment_join_attribute(419,76,10,20);
		//guidance 
		
		ReplaceField("skill", "points", 0, 305); //trivial to learn
		ReplaceField("skill", "points", 2, 306); //easy to master
		
		ReplaceField("enchantment", "duration", 1, 125);
		
		//hold ground
		
		ReplaceField("enchantment", "duration", 2, 687);

		Replace_enchantment_join_attribute(687,40,5,10);
		Replace_enchantment_join_attribute(687,41,5,10);
		
		ReplaceField("enchantment", "duration", 3, 688);
		Replace_enchantment_join_attribute(688,40,10,15);
		Replace_enchantment_join_attribute(688,41,10,15);
		
		//insult
		
		ReplaceField("enchantment", "duration", 2, 802);
		ReplaceField("enchantment", "duration", 3, 803);
		Replace_enchantment_join_attribute(803,75,-20,-15);
		
		//intimidate
		Replace_enchantment_join_attribute(536,114,5,-5);
		//retreat
		ReplaceField("skill","points", 0, 158); //trivial to learn
		ReplaceField("skill","points", 2, 159); //easy to master
		
		//war cry
		ReplaceField("enchantment", "duration", 2, 168);
		Replace_enchantment_join_attribute(168,61,10,5);
		ReplaceField("enchantment", "duration", 3, 169);
		Replace_enchantment_join_attribute(169,61,20,10);
		
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
		
		Replace_enchantment_join_attribute(437,75,-20,-15);
		
		//study
		ReplaceField("enchantment", "duration", 2, 180);
		ReplaceField("enchantment", "duration", 3, 181);
		Replace_enchantment_join_attribute(181,40,20,15);
		Replace_enchantment_join_attribute(181,41,20,15);
		
		//introspection TODO
		
		//meditation TODO
	}
	private void patch_doc_03() throws java.sql.SQLException
	{
		//staggering blow 458 457
		ReplaceField("enchantment", "duration", 2, 458);
		ReplaceField("enchantment", "duration", 3, 457);
		Replace_enchantment_join_attribute(457,76,-30,-20);
		
		//ready stance
		ReplaceField("skill","points",0, 60); //trivial to learn
		ReplaceField("skill","points", 2, 61); //easy to master
		ReplaceField("skill","stat_value", 3, 60); //requiremnt
		ReplaceField("skill","stat_value", 9, 61); //requirement
		
		Replace_enchantment_join_attribute(172,76,10,15);
		Replace_enchantment_join_attribute(173,76,20,45);
		
		//stimulus
		ReplaceField("skill","stat_value", 6, 537); //requiremnt
		ReplaceField("skill","stat_value", 12, 538); //requirement
		ReplaceField("enchantment", "duration", 2, 461);
		ReplaceField("enchantment", "duration", 3, 462);
		
		//scout's advice
		ReplaceField("enchantment", "duration", 2, 463);
		ReplaceField("enchantment", "duration", 3, 464);
		Replace_enchantment_join_attribute(463,74,10,15);
		
		
		//deft stance
		ReplaceField("skill","points",0, 58); //trivial to learn
		ReplaceField("skill","points", 2, 59); //easy to master
		
		//initiative is 76
		this.AddEntry_enchantment_join_attribute(170, 76, -20);
		this.AddEntry_enchantment_join_attribute(171, 76, -40);
		
		//safe stance
		ReplaceField("skill","points", 2, 215); // easy to master
		Replace_enchantment_join_attribute(175,51,30,45);
		
		//combat focus TODO (because I'll need to check wheter "only next action" skills have special field -which they probably do have-
		
		//jaw strike
		//everything goes to 20 for 2 turns
		ReplaceField("enchantment", "duration", 2, 443);
		Replace_enchantment_join_attribute(443,28,15,20);
		Replace_enchantment_join_attribute(443,65,15,20);
		
				
		ReplaceField("enchantment", "duration", 3, 444);
		Replace_enchantment_join_attribute(444,30,-40,-30);
		
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
	
	private void patch_doc_04() throws java.sql.SQLException
	{
		//hand shot
		ReplaceField("enchantment", "duration", 2, 492);
		ReplaceField("enchantment", "duration", 3, 494);
		Replace_enchantment_join_attribute(494,33,-20,-15);
		Replace_enchantment_join_attribute(494,35,-20,-15);
		
		
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
		Replace_enchantment_join_attribute(546,35,10,15);
		
		//nerve shot TODO
		
		//feint TODO (can't find it)
		
		//precise strike
		ReplaceField("skill","points", 2, 264);
		
		//armor break TODO can't find it
		
		//head shot
		ReplaceField("skill_attribute", "modifier",-40, 344);
		ReplaceField("skill_attribute", "modifier",-10, 345);
	}
	private void patch_doc_05() throws java.sql.SQLException
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
		Replace_enchantment_join_attribute(638,36,30,20);
		
		//quick draw
		ReplaceField("skill","points",2, 11); //easy to master
	}
	
	private void patch_doc_06() throws java.sql.SQLException
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
		Replace_enchantment_join_attribute(422,23,-5,-4);
		Replace_enchantment_join_attribute(423,23,-10,-6);
		ReplaceField("enchantment", "duration", 3, 423);
		
		
		//battle tongue
		ReplaceField("skill", "points", 0, 166); //trivial to train
		
		ReplaceField("skill_attribute", "modifier", 60, 358);
		
		//serenity
		ReplaceField("enchantment", "duration", 2, 424);
		
		ReplaceField("enchantment", "duration", 3, 425);
		Replace_enchantment_join_attribute(425,75,20,15);
		
		
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
		Replace_enchantment_join_attribute(512,75,40,60);
		
		//piety expert casting TODO spell casting

		//knowledge tactic
		ReplaceField("skill", "stat_value", 9, 206);
		ReplaceField("skill", "stat_value", 15, 207);
		
		ReplaceField("skill_attribute", "modifier", 10, 330);
		ReplaceField("skill_attribute", "modifier", 10, 331);
		ReplaceField("skill_attribute", "modifier", 30, 332);
		ReplaceField("skill_attribute", "modifier", 30, 333);
		
	}
	private void patch_doc_07() throws java.sql.SQLException
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
	private void patch_doc_08() throws java.sql.SQLException
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
		Replace_enchantment_join_attribute(476,37,2,1);
		ReplaceField("enchantment", "duration", 3, 477);
		Replace_enchantment_join_attribute(477,37,4,2);
		
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
		Replace_enchantment_join_attribute(452,74,-6,-4);
		
		//weak spot
		ReplaceField("skill_attribute", "modifier", -10, 51);
		ReplaceField("skill_attribute", "modifier", -10, 52);
		ReplaceField("skill","points", 2, 85); // easy to master
		
		//fatality : increase both melee & ranged crit (but only proc on melee damage)
		ReplaceField("enchantment", "duration", 2, 455);
		Replace_enchantment_join_attribute(455,36,5,3);
		Replace_enchantment_join_attribute(455,37,5,3);
		ReplaceField("enchantment", "duration", 3, 456);
		Replace_enchantment_join_attribute(456,36,10,4);
		Replace_enchantment_join_attribute(456,37,10,4);
		
		//underdog
		Replace_enchantment_join_attribute(552,36,8,10);
		Replace_enchantment_join_attribute(553,36,15,20);
		
		//break defense TODO not found
	}
	
	private void patch_doc_09() throws java.sql.SQLException
	{
		
	}

}
