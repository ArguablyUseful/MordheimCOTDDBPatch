import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ProduceSqlCommands {

	PrintWriter writer;
	String filename;
	String formatFile = "UTF-8";
	
	int NEXT_FRESH_UID_TABLE_SKILL_ATTRIBUTE = 666;
	int NEXT_FRESH_UID_TABLE_ENCHANTMENT = 1324;
	int NEXT_FRESH_UID_TABLE_SKILL_ENCHANTMENT = 933;
	public static void main(String []args)
	{
		try {
			new ProduceSqlCommands("sqlCommands.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ProduceSqlCommands(String filename) throws IOException
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
		
		this.patch_doc_11();
		this.patch_doc_12();
		this.patch_doc_13();
		this.patch_doc_14();
		
		this.patch_doc_19();
		
		writer.close();
	}
	
	private void WriteToFile(String sqlStatement)
	{
		writer.print(sqlStatement += "\n");
	}
	private void ReplaceField(String tableName, String columnName, int value, int key) 
	{
		String []keyCols = new String[1];
		keyCols[0] = "id";
		int [] keys = new int[1];
		keys[0] = key;
		ReplaceField(tableName, columnName, value, keyCols, keys);
	}
	private void ReplaceField(String tableName, String colunmName, int value, String []keyCols, int[] key) 
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
	private void AddEntry_enchantment_join_attribute(int fk_ench_id, int fk_attr_id, int modifier) {
	        String sql = "INSERT INTO enchantment_join_attribute(fk_enchantment_id,fk_attribute_id, modifier) VALUES(?,?,?)";
	        String sql2 = new String(sql);
			String regex = "\\?";
			sql2 = sql2.replaceFirst(regex, Integer.toString(fk_ench_id));
			sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attr_id));
			sql2 = sql2.replaceFirst(regex, Integer.toString(modifier));
			this.WriteToFile(sql2);

	}
	private void AddEntry_skill_enchantment(
			int id,
			int fk_skill_id,
			int fk_enchantment_id,
			int fk_enchantment_trigger_id,
			int fk_unit_action_id_trigger,
			int fk_skill_id_trigger,
			int ratio,
			int self,
			int target_self,
			int target_ally,
			int target_enemy
			)
	{
		String sql = "INSERT INTO skill_enchantment(id,fk_skill_id, fk_enchantment_id,fk_enchantment_trigger_id,fk_unit_action_id_trigger,fk_skill_id_trigger,ratio,self,target_self,target_ally,target_enemy) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_skill_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_trigger_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_unit_action_id_trigger));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_skill_id_trigger));
		sql2 = sql2.replaceFirst(regex, Integer.toString(ratio));
		sql2 = sql2.replaceFirst(regex, Integer.toString(self));
		sql2 = sql2.replaceFirst(regex, Integer.toString(target_self));
		sql2 = sql2.replaceFirst(regex, Integer.toString(target_ally));
		sql2 = sql2.replaceFirst(regex, Integer.toString(target_enemy));
		this.WriteToFile(sql2);
	}
	private void AddEntry_enchantment(
			int id, 
			String name,
			int fk_effect_type_id,
			int fk_enchantment_type_id,
			int fk_enchantment_quality_id,
			int fk_enchantment_consume_id,
			int fk_enchantment_trigger_id_destroy,
			int damage_min,
			int damage_max,
			int fk_enchantment_dmg_trigger_id,
			int fk_attribute_id_dmg_resist_roll,
			int duration,
			int valid_next_action,
			int indestructible,
			int require_unit_state,
			int change_unit_state,
			int fk_unit_state_id_required,
			int fk_unit_state_id_next,
			int stackable,
			int destroy_on_apply,
			int keep_on_death,
			int no_display,
			int make_unit_visible,
			int fk_enchantment_id_on_turn_start)
	{
		String sql = "INSERT INTO enchantment(id, name, fk_effect_type_id,fk_enchantment_type_id,fk_enchantment_quality_id,fk_enchantment_consume_id,"
				+ "fk_enchantment_trigger_id_destroy,damage_min,damage_max,fk_enchantment_dmg_trigger_id,fk_attribute_id_dmg_resist_roll,"
				+ "duration,valid_next_action,indestructible,require_unit_state,change_unit_state,fk_unit_state_id_required,fk_unit_state_id_next,"
				+ "stackable,destroy_on_apply,keep_on_death,no_display,make_unit_visible,fk_enchantment_id_on_turn_start"
				+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id));
		sql2 = sql2.replaceFirst(regex, "\'" + name + "\'");
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_effect_type_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_type_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_quality_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_consume_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_trigger_id_destroy));
		sql2 = sql2.replaceFirst(regex, Integer.toString(damage_min));
		sql2 = sql2.replaceFirst(regex, Integer.toString(damage_max));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_dmg_trigger_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attribute_id_dmg_resist_roll));
		sql2 = sql2.replaceFirst(regex, Integer.toString(duration));
		sql2 = sql2.replaceFirst(regex, Integer.toString(valid_next_action));
		sql2 = sql2.replaceFirst(regex, Integer.toString(indestructible));
		sql2 = sql2.replaceFirst(regex, Integer.toString(require_unit_state));
		sql2 = sql2.replaceFirst(regex, Integer.toString(change_unit_state));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_unit_state_id_required));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_unit_state_id_next));
		sql2 = sql2.replaceFirst(regex, Integer.toString(stackable));
		sql2 = sql2.replaceFirst(regex, Integer.toString(destroy_on_apply));
		sql2 = sql2.replaceFirst(regex, Integer.toString(keep_on_death));
		sql2 = sql2.replaceFirst(regex, Integer.toString(no_display));
		sql2 = sql2.replaceFirst(regex, Integer.toString(make_unit_visible));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_enchantment_id_on_turn_start));
		
		this.WriteToFile(sql2);
	}
	private void AddEntry_skill_attribute(int id, int fk_skill_id, int fk_attr_id, int fk_unit_action_id_trigger, int fk_skill_id_trigger, int modifier)
	{
	    String sql = "INSERT INTO skill_attribute(id, fk_skill_id, fk_attribute_id, fk_unit_action_id_trigger, fk_skill_id_trigger, modifier) VALUES(?,?,?,?,?,?)";
        String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_skill_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_attr_id));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_unit_action_id_trigger));
		sql2 = sql2.replaceFirst(regex, Integer.toString(fk_skill_id_trigger));
		sql2 = sql2.replaceFirst(regex, Integer.toString(modifier));
		this.WriteToFile(sql2);
	}
	private void AddEntry_attribute_attribute(int id, int fk_attr_id_base, int fk_attr_id, int modifier) 
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
	private void RemoveEntry_attribute_attribute(int id_key) 
	{
		String sql = "DELETE FROM attribute_attribute WHERE ID = " + id_key;
		String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id_key));
		this.WriteToFile(sql2);
	}
	private void RemoveEntry_skill_perform_skill(int id_key) 
	{
		String sql = "DELETE FROM skill_perform_skill WHERE ID = " + id_key;
		
		String sql2 = new String(sql);
		String regex = "\\?";
		sql2 = sql2.replaceFirst(regex, Integer.toString(id_key));
		this.WriteToFile(sql2);
		
	}
	private void Replace_enchantment_join_attribute(int fk_ench_id, int fk_attr_id, int mod, int newMod) 
	{
		String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
		int [] keys = {fk_ench_id, fk_attr_id, mod};
		ReplaceField("enchantment_join_attribute", "modifier", newMod, colKeys, keys);
	}
	private void patch_doc_00() 
	{
		//perception buff
		//ReplaceField("enchantment", "duration", 3, 219); --> REMOVED FROM SPEC.
		AddEntry_enchantment_join_attribute(219, 74, 15);
		//TODO adds ambush melee resistance buff --> REMOVED FROM SPEC.

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
		ReplaceField("enchantment", "duration", 3, 1069); 
		
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
		AddEntry_attribute_attribute(24, 10,74,1); //using old attribute id
		
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
		//kidney strike (name is puncture & puncture_mstr)
		this.ReplaceField("enchantment", "duration", 2, 384);
		this.ReplaceField("enchantment", "duration", 3, 385);
		//ranged resistance = 74
		{
			String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int [] keys = {384, 75, -15};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 74, colKeys, keys);
		}
		this.Replace_enchantment_join_attribute(384, 74, -15, -20);
		{
			String [] colKeys = {"fk_enchantment_id", "fk_attribute_id", "modifier"};
			int [] keys = {385, 75, -30};
			ReplaceField("enchantment_join_attribute", "fk_attribute_id", 74, colKeys, keys);
		}
		//strong blow
		{
			String []colKeys = {"id", "fk_skill_id"};
			int [] keys = {340,541};
			ReplaceField("skill_attribute", "modifier", 25, colKeys, keys);
		}
		//daredevil ( known as "careless strike")
		this.ReplaceField("enchantment", "duration", 2, 164);
		this.Replace_enchantment_join_attribute(164, 61, 75, 35);
		this.Replace_enchantment_join_attribute(164, 75, -20, -10);
		this.ReplaceField("enchantment", "duration", 3, 165);
		this.Replace_enchantment_join_attribute(165, 61, 125, 50);
		this.Replace_enchantment_join_attribute(165, 75, -10, -5);
		
		
	}
		
	
	private void patch_doc_01() 
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
		ReplaceField("skill_attribute","modifier",-45,7);
		ReplaceField("skill_attribute","modifier",-90,8);
		ReplaceField("skill_attribute","modifier",-135,9);
		
		//wall runner
		ReplaceField("skill","points", 2,2); //easy to master
		ReplaceField("skill_attribute","modifier",-30, 1);
		ReplaceField("skill_attribute","modifier",-60, 2);
		ReplaceField("skill_attribute","modifier",-90, 3);
		
		//careful approach named "balance"
		this.ReplaceField("enchantment","duration",2, 382);
		this.ReplaceField("enchantment","duration",2, 383);
		this.Replace_enchantment_join_attribute(382, 1, -2, -1);
		this.Replace_enchantment_join_attribute(383, 1, -2, -1);
		
		//prowl
		ReplaceField("skill","points", 2,360); //easy to master
		ReplaceField("skill","strategy_points", 1, 360); // 1 more SP
		ReplaceField("skill","offense_points",2,360); // 1 less OP
		
	}
	
	private void patch_doc_02() 
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
		
		//Wild casting NOTE : no major revision
		this.Replace_enchantment_join_attribute(211, 28, 10, 5);
		//plea NOTE : no major revision
		this.Replace_enchantment_join_attribute(287, 65, 10, 5);

		//quick casting NOTE : no major revision
		this.ReplaceField("skill", "strategy_points", 1, 42);
		this.ReplaceField("skill", "strategy_points", 1, 43);
		this.Replace_enchantment_join_attribute(281, 30, -15, -5);
		this.Replace_enchantment_join_attribute(280, 30, -30, -15);
		//quick prayer NOTE : no major revision
		this.ReplaceField("skill", "strategy_points", 1, 48);
		this.ReplaceField("skill", "strategy_points", 1, 49);
		this.Replace_enchantment_join_attribute(282, 30, -30, -15);
		this.Replace_enchantment_join_attribute(283, 30, -15, -5);
		
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
		
		//introspection TODO --> REMOVED FROM SPEC
		
		//meditation TODO --> REMOVED FROM SPEC
	}
	private void patch_doc_03() 
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
		
		//combat focus 
		this.ReplaceField("enchantment", "valid_next_action", 0, 525);
		this.ReplaceField("enchantment", "valid_next_action", 0, 526);
		
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
	
	private void patch_doc_04()
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
		
		//nerve shot TODO --> REMOVED FROM SPECS
		
		//feint (name is parade) 
		this.ReplaceField("skill", "points", 2, 267);
		this.Replace_enchantment_join_attribute(448, 33, -50, -75);
		//precise strike
		ReplaceField("skill","points", 2, 264);
		
		//armor break (name is execution) 
		this.ReplaceField("enchantment", "duration", 2, 449);
		this.ReplaceField("enchantment", "duration", 3, 450);
		this.Replace_enchantment_join_attribute(449, 130, -10, -15);
		this.Replace_enchantment_join_attribute(450, 130, -25, -20);
		
		//head shot
		ReplaceField("skill_attribute", "modifier",-40, 344);
		ReplaceField("skill_attribute", "modifier",-10, 345);
	}
	private void patch_doc_05() 
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
		
		//athletic expert called blessing of speed
		this.ReplaceField("skill", "points", 0, 405);
		this.ReplaceField("skill", "points", 2, 406);
		
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
	
	private void patch_doc_06()
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
	private void patch_doc_07()
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
	private void patch_doc_08() 
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
	
	private void patch_doc_11() 
	{
		//combat expertise
		ReplaceField("skill", "points", 0, 291);
		ReplaceField("skill","points", 2, 292);
		
		Replace_enchantment_join_attribute(551,40,20,30);
		Replace_enchantment_join_attribute(551,41,20,30);
		
		//flawless positioning
		ReplaceField("skill_attribute", "modifier", 10, 129);
		ReplaceField("skill_attribute", "modifier", 30, 130);
		
		//hunter
		ReplaceField("skill_enchantment", "ratio", 40, 380);
		ReplaceField("skill_enchantment", "ratio", 40, 382);
		
		ReplaceField("skill_enchantment", "ratio", 80, 381);
		ReplaceField("skill_enchantment", "ratio", 80, 383);
		
		//prayer of swiftness
		ReplaceField("enchantment", "duration", 2, 188);
		ReplaceField("enchantment", "duration", 3, 189);
		Replace_enchantment_join_attribute(189,41,30,20);
		
		//righteous fury
		ReplaceField("skill_attribute", "modifier", -30, 57);
		ReplaceField("skill_attribute", "modifier", -5, 58);
		
		ReplaceField("enchantment", "duration", 2, 232);
		ReplaceField("enchantment", "duration", 3, 233);
		
		//sign of sigmar
		ReplaceField("enchantment", "duration", 2, 406);
		ReplaceField("enchantment", "duration", 3, 407);

		//divine aegis
		ReplaceField("skill_attribute", "modifier", 15, 59);
		ReplaceField("skill_attribute", "modifier", 30, 60);
		
		//protection of sigmar
		ReplaceField("skill_attribute", "modifier", 25, 133);
		ReplaceField("skill_attribute", "modifier", 50, 134);
		
		//trial by pain
		ReplaceField("enchantment", "duration", 2, 1098);
		ReplaceField("enchantment", "duration", 3, 1099);
		
		//burn the witch
		ReplaceField("enchantment", "duration", 2, 1102);
		ReplaceField("enchantment", "duration", 3, 1103);
		
		//Fanatical zeal
		
		//TODO NOTE would need to add fresh lines to make the mastery last 3 turns
		ReplaceField("enchantment", "duration", 2, 1104);
		ReplaceField("enchantment", "duration", 3, 1105);
		
		//sigil of sigmar
		ReplaceField("enchantment", "duration", 2, 1115);
		ReplaceField("enchantment", "duration", 3, 1116);
		this.Replace_enchantment_join_attribute(1115, 61, 20, 15);
		this.Replace_enchantment_join_attribute(1116, 61, 40, 20);
		
		//witchfinder ward

		int magic_res_attr = 53;
		AddEntry_skill_attribute(this.NEXT_FRESH_UID_TABLE_SKILL_ATTRIBUTE++, 811, magic_res_attr, 0, 0, 10 );	
		AddEntry_skill_attribute(this.NEXT_FRESH_UID_TABLE_SKILL_ATTRIBUTE++, 812, magic_res_attr, 0, 0, 20 );
	}
	private void patch_doc_12()
	{
		//black hunger
		this.ReplaceField("enchantment",  "duration",  2, 120);
		this.Replace_enchantment_join_attribute(120,61,20,10);
		this.Replace_enchantment_join_attribute(120,72,-20,-10);
		this.Replace_enchantment_join_attribute(120,73,-20,-10);
		
		this.ReplaceField("enchantment",  "duration",  3, 121);
		this.Replace_enchantment_join_attribute(120,61,40,20);
		this.Replace_enchantment_join_attribute(120,72,-40,-20);
		this.Replace_enchantment_join_attribute(120,73,-40,-20);
		
		//numbing poison
		this.ReplaceField("enchantment",  "duration",  2, 400);
		this.ReplaceField("enchantment",  "duration",  2, 397);
		this.Replace_enchantment_join_attribute(397, 12, -2, -1);
		
		this.ReplaceField("enchantment",  "duration",  3, 399);
		this.ReplaceField("enchantment",  "duration",  3, 398);
		
		//warp poison
		//TODO not found
		
		//poison expert
		this.ReplaceField("skill_attribute", "modifier", -15, 97);
		this.ReplaceField("skill_attribute", "modifier", -30, 98);
		
		//attracting lure
		this.ReplaceField("skill",  "points",  0, 106);
		this.ReplaceField("skill",  "points",  2, 107);
		
		this.Replace_enchantment_join_attribute(799, 114, -15, -5);
		int tmp_ench_id = this.NEXT_FRESH_UID_TABLE_ENCHANTMENT++;
		this.AddEntry_enchantment(tmp_ench_id,"skill_attracting_lure",3,0,2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0);
		this.AddEntry_enchantment_join_attribute(tmp_ench_id, 114, 25);
		
		
		//blood offering
		ReplaceField("skill","points", 0, 301);
		ReplaceField("skill","points", 2, 302);
		
		ReplaceField("enchantment","duration",2, 190);
		ReplaceField("enchantment","duration",3, 191);
		
		this.Replace_enchantment_join_attribute(190, 61, 25, 5);
		this.Replace_enchantment_join_attribute(191, 61, 50, 10);
		
		//touch of palsy
		ReplaceField("skill","points",0,299);
		ReplaceField("skill","points",2,300);
		ReplaceField("enchantment", "duration", 2, 408);
		this.Replace_enchantment_join_attribute(408, 1, -2, -1);
		ReplaceField("enchantment", "duration", 3, 409);
		this.Replace_enchantment_join_attribute(409, 1, -4, -2);
		this.Replace_enchantment_join_attribute(409, 16, -40, -30);
		this.Replace_enchantment_join_attribute(409, 17, -40, -30);
		this.Replace_enchantment_join_attribute(409, 18, -40, -30);
		this.Replace_enchantment_join_attribute(409, 16, -109, -30);
		this.Replace_enchantment_join_attribute(409, 16, -110, -30);
		this.Replace_enchantment_join_attribute(409, 16, -111, -30);
		this.Replace_enchantment_join_attribute(409, 16, -112, -30);
		
		//chaos evolution
		ReplaceField("skill","points",0, 528);
		ReplaceField("skill","points",2, 554);
		
		//chaotic advantage
		ReplaceField("skill","points",0, 303);
		ReplaceField("skill","points", 2, 304);
		
		this.Replace_enchantment_join_attribute(523, 123, 1, 2);
		this.Replace_enchantment_join_attribute(524, 123, 1, 2);
		
		//tzeentch warding
		ReplaceField("skill","points",0,108);
		ReplaceField("skill","points",2,109);
		
		//death stench
		//already 2 turns
		this.Replace_enchantment_join_attribute(1270, 40, -5, -10);
		this.Replace_enchantment_join_attribute(1270, 41, -5, -10);
		this.Replace_enchantment_join_attribute(1270, 75, -5, -10);
		
		this.ReplaceField("enchantment", "duration", 3, 1271);
		this.Replace_enchantment_join_attribute(1271, 40, -10, -15);
		this.Replace_enchantment_join_attribute(1271, 41, -10, -15);
		this.Replace_enchantment_join_attribute(1271, 75, -10, -15);
		
		//death trap
		//TODO
		//eye trap
		//TODO
		
		
		//body reconstruction
		ReplaceField("skill_attribute", "modifier", 30, 629);
		
		//embedded wyrdstone
		ReplaceField("skill","points",0,904);
		ReplaceField("skill","points",2,905);
		
		//find the breach
		this.Replace_enchantment_join_attribute(1268, 131, 5, 10);
		this.Replace_enchantment_join_attribute(1269, 131, 10, 20);
	}
	private void patch_doc_13()
	{
		//liquid courage
		this.ReplaceField("enchantment", "stackable", 1, 1049);
		this.ReplaceField("enchantment", "stackable", 1, 1050);
		this.ReplaceField("enchantment",  "duration", 3, 1050);
		
		this.Replace_enchantment_join_attribute(1050, 23, 30, 20);
		
		//ripost stance
		//TODO cannot change the OP reduction to more than 1 (it uses a base enchantment that is used elsewhere), would need time to see how to mimic it

		//two parry
		this.AddEntry_skill_enchantment(this.NEXT_FRESH_UID_TABLE_SKILL_ENCHANTMENT++, 769, 1054, 0, 0, 0, 0, 1, 0, 0, 0);
		//three parry
		this.AddEntry_skill_enchantment(this.NEXT_FRESH_UID_TABLE_SKILL_ENCHANTMENT++, 770, 1055, 0, 0, 0, 0, 1, 0, 0, 0);
		
		//captain speech
		ReplaceField("enchantment", "duration", 2, 1052);
		ReplaceField("enchantment", "duration", 3, 1053);
		this.Replace_enchantment_join_attribute(1053, 23, 40, 30);
		this.Replace_enchantment_join_attribute(1053, 24, 40, 30);
		this.Replace_enchantment_join_attribute(1053, 26, 40, 30);
		
		//white powder trap
		//TODO
		
		//captain order
		ReplaceField("enchantment", "duration", 2, 1056);
		this.Replace_enchantment_join_attribute(1056, 61, 15, 5);
		this.Replace_enchantment_join_attribute(1056, 62, 15, 5);
		
		ReplaceField("enchantment", "duration", 3, 1057);
		this.Replace_enchantment_join_attribute(1057, 61, 30, 10);
		this.Replace_enchantment_join_attribute(1057, 62, 30, 10);
		
		//hand bomb
		//6 12 ws instead of 9 15 bs
		
		ReplaceField("skill","stat_value",6,763);
		ReplaceField("skill","stat_value",12,764);
		ReplaceField("skill","fk_attribute_id_stat",2,763);
		ReplaceField("skill","fk_attribute_id_stat",2,764);
		
		//RADIUS TODO
		//ReplaceField("skill","radius",5,763); MUST CHECK
		//ReplaceField("skill","radius",5,764); MUST CHECK
		
		//in and out
		ReplaceField("enchantment","duration",3,1060);
		ReplaceField("enchantment","duration",3,1061);
		this.Replace_enchantment_join_attribute(1060, 76, 5, 10);
		this.Replace_enchantment_join_attribute(1061, 76, 10, 30);
		
		//expert fencer
		this.ReplaceField("skill_attribute", "modifier", 10, 475);
		this.ReplaceField("skill_attribute", "modifier", 10, 476);
		this.ReplaceField("skill_attribute", "modifier", 10, 622);
		
		this.ReplaceField("skill_attribute", "modifier", 20, 477);
		this.ReplaceField("skill_attribute", "modifier", 20, 478);
		this.ReplaceField("skill_attribute", "modifier", 20, 623);
		
		//tight crew
		this.ReplaceField("skill", "points", 2, 776);
		
		//walk the topsail
		this.ReplaceField("skill", "points", 0, 781);
		this.ReplaceField("skill", "points", 2, 782);
		
		this.ReplaceField("skill_attribute", "modifier", 20, 481);
		this.ReplaceField("skill_attribute", "modifier", 20, 482);
		this.ReplaceField("skill_attribute", "modifier", 20, 483);
		this.ReplaceField("skill_attribute", "modifier", 20, 484);
		this.ReplaceField("skill_attribute", "modifier", 20, 485);
		this.ReplaceField("skill_attribute", "modifier", 20, 486);
		this.ReplaceField("skill_attribute", "modifier", 20, 487);
		
		this.ReplaceField("skill_attribute", "modifier", 60, 488);
		this.ReplaceField("skill_attribute", "modifier", 60, 489);
		this.ReplaceField("skill_attribute", "modifier", 60, 490);
		this.ReplaceField("skill_attribute", "modifier", 60, 491);
		this.ReplaceField("skill_attribute", "modifier", 60, 492);
		this.ReplaceField("skill_attribute", "modifier", 60, 493);
		this.ReplaceField("skill_attribute", "modifier", 60, 494);
		
		//survival instinct
		
		this.Replace_enchantment_join_attribute(1062, 36, 5, 15);
		this.Replace_enchantment_join_attribute(1063, 36, 10, 30);
		
		//alpha howl
		this.ReplaceField("enchantment", "duration", 2, 1120);
		this.ReplaceField("enchantment", "duration", 3, 1121);
		
		this.Replace_enchantment_join_attribute(1121, 69, 4, 3);
		this.Replace_enchantment_join_attribute(1121, 70, 4, 3);
		
		//crush the weak
		this.ReplaceField("enchantment", "duration", 2, 1122);
		this.Replace_enchantment_join_attribute(1122, 51, -15, -10);
		this.ReplaceField("enchantment", "duration", 3, 1123);
		this.Replace_enchantment_join_attribute(1123, 51, -30, -20);
		
		//ulric's fury
		//TODO
		
		//northern tenacity
		this.ReplaceField("enchantment", "duration", 2, 1126);
		this.ReplaceField("enchantment", "duration", 3, 1127);
	}
	
	private void patch_doc_14()
	{
		//vent
		this.ReplaceField("enchantment", "duration", 2, 1031);
		this.ReplaceField("enchantment", "duration", 3, 1032);
		this.Replace_enchantment_join_attribute(1032, 33, -20, -15);
		this.Replace_enchantment_join_attribute(1032, 35, -20, -15);
		
		
		//warp fumes
		this.ReplaceField("enchantment", "duration", 2, 1033);
		this.ReplaceField("enchantment", "duration", 2, 1034);
		
		this.ReplaceField("enchantment", "duration", 2, 1035);
		this.ReplaceField("enchantment", "duration", 2, 1036);
		
		this.Replace_enchantment_join_attribute(1035, 33, 20, 15);
		this.Replace_enchantment_join_attribute(1035, 41, 20, 15);
		
		//warp globe
		
		
		//melee & poison resistance : attr = 75 & 21
		this.ReplaceField("enchantment", "duration", 2, 1243); // magic & poison resist
		//ranged & critical resistance: attr = 74& 51
		this.ReplaceField("enchantment", "duration", 2, 1245); // ranged & crit resis
		//magic & stun resistance : attr = 53 & 127
		this.ReplaceField("enchantment", "duration", 2, 1247); // magic & stun resist
		
		this.Replace_enchantment_join_attribute(1243, 75, -15, -10);
		this.Replace_enchantment_join_attribute(1243, 21, -15, -10);
		
		this.Replace_enchantment_join_attribute(1245, 74, -15, -10);
		this.Replace_enchantment_join_attribute(1245, 51, -15, -10);
		
		this.Replace_enchantment_join_attribute(1247, 53, -15, -10);
		this.Replace_enchantment_join_attribute(1247, 127, -15, -10);
		
		this.ReplaceField("enchantment", "duration", 3, 1244);
		this.ReplaceField("enchantment", "duration", 3, 1246);
		this.ReplaceField("enchantment", "duration", 3, 1248);
		
		//enriched globe
		//1249, 1251, 1253
		//& 1250, 1252, 1254 ?
		this.ReplaceField("enchantment", "duration", 2, 1249);
		this.ReplaceField("enchantment", "duration", 2, 1251);
		this.ReplaceField("enchantment", "duration", 2, 1253);
		
		this.ReplaceField("enchantment", "duration", 3, 1250);
		this.ReplaceField("enchantment", "duration", 3, 1252);
		this.ReplaceField("enchantment", "duration", 3, 1254);
		
		this.Replace_enchantment_join_attribute(1249, 75, 15, 10);
		this.Replace_enchantment_join_attribute(1249, 21, 15, 10);
		
		this.Replace_enchantment_join_attribute(1251, 74, 15, 10);
		this.Replace_enchantment_join_attribute(1251, 51, 15, 10);
		
		this.Replace_enchantment_join_attribute(1253, 127, 15, 10);
		this.Replace_enchantment_join_attribute(1253, 53, 15, 10);
		
		//wyrdstone lure
		//TODO the base game doesnre-use the same enchantment for both nrmal & mastery making a "new" effect that lasts 3 turns require moretime
		
		//infused globe
		//TODO trap
		
		//strangling globe
		this.ReplaceField("enchantment", "duration", 2, 1064);
		this.ReplaceField("enchantment", "duration", 3, 1065);
		//warp resistance
		this.ReplaceField("skill", "points", 0, 754);
		this.ReplaceField("skill", "points", 2, 755);
		
		//warp rush
		this.ReplaceField("enchantment", "duration", 3, 1040);
		this.ReplaceField("enchantment", "duration", 3, 1041);
		
		this.Replace_enchantment_join_attribute(1040, 76, 10, 15);
		this.Replace_enchantment_join_attribute(1041, 76, 20, 45);
		
		//dagger specialist
		this.ReplaceField("skill_attribute", "modifier", -10, 467);
		this.ReplaceField("skill_attribute", "modifier", -10, 468);
		this.ReplaceField("skill_attribute", "modifier", 10, 624);
		
		this.ReplaceField("skill_attribute", "modifier", -20, 469);
		this.ReplaceField("skill_attribute", "modifier", -20, 470);
		this.ReplaceField("skill_attribute", "modifier", 20, 625);
		
		//invigorating fumes
		this.ReplaceField("skill_attribute", "modifier", 15, 455);
		this.ReplaceField("skill_attribute", "modifier", 15, 456);
		
		this.ReplaceField("skill_attribute", "modifier", 30, 465);
		this.ReplaceField("skill_attribute", "modifier", 30, 466);
		
		//paralysing discharge
		this.ReplaceField("enchantment", "duration", 2, 1038);
		this.ReplaceField("enchantment", "duration", 3, 1039);
		
		//potent globe
		this.ReplaceField("enchantment", "duration", 2, 1046);
		this.ReplaceField("enchantment", "duration", 3, 1047);
		
		this.Replace_enchantment_join_attribute(1047, 21, -20, -15);
		
		//ritual of defiance
		this.ReplaceField("enchantment", "duration", 2, 1137);
		this.Replace_enchantment_join_attribute(1138, 1, -2, -1); //-1 meters
		
		
		this.ReplaceField("enchantment", "duration", 3, 1139);
		this.ReplaceField("enchantment", "duration", 3, 1140);
		this.Replace_enchantment_join_attribute(1140, 1, -4, -2);
		
		//ritual of suffering
		this.ReplaceField("enchantment", "duration", 2, 1133);
		this.ReplaceField("enchantment", "duration", 3, 1135);
		
		//ritual of scorn
		this.ReplaceField("enchantment", "duration", 2, 1130);
		
		this.ReplaceField("enchantment", "duration", 3, 1131);
		this.ReplaceField("enchantment", "duration", 3, 1132);
		
		//daub of change
		this.ReplaceField("skill", "points", 0,  851);
		this.ReplaceField("skill", "points", 2,  852);
		
		//daub of hatred
		this.ReplaceField("skill", "points", 0,  853);
		this.ReplaceField("skill", "points", 2,  854);
	}
	
	private void patch_doc_19()
	{
		//squire curse
		this.ReplaceField("enchantment", "duration", 3, 306);
		
		//adaptable defense
		//TODO need to modify 
		/* must A) create a new enchantment that is linked to the skill, with a line in enchantment_effect_enchantment where trigger id is 4 (on fail) and attribute is 33 (melee hit roll) 
		 * the idea is that passive becomes an ehcnatment that produce an enchantment when a melee hit roll fails (4 trigger & 33). The produced enchantment is the effects (aka the current enchantment when hit damage succeed) 
		 */
		//chain shot
		//TODO same
		//momentum 
		//TODO same
		//defense breach
		
		//defense breach (called night master)
		this.ReplaceField("skill_attribute", "modifier", -25, 261);
		this.ReplaceField("skill_attribute", "modifier", -25, 404);
		
		//fleet footed
		this.ReplaceField("enchantment", "duration", 3, 301);
		this.Replace_enchantment_join_attribute(301, 1, 3, 2);
		
		//warp resonance
		this.ReplaceField("enchantment", "duration", 3, 345);
		
		//warp immunity
		this.ReplaceField("skill_attribute", "modifier", 5, 405);
		
		//warp rage
		this.ReplaceField("enchantment", "duration", 3, 346);
		this.Replace_enchantment_join_attribute(346, 41, 10, 15);
		
		//for sigmar
		this.ReplaceField("enchantment", "duration", 2, 314);
		this.Replace_enchantment_join_attribute(314, 1, 3, 2);
		
		//unsettling charge
		//TODO
		
		//repentance
		this.ReplaceField("enchantment", "duration", 3, 347);
		
		//military training
		//TODO (missing attack)
		
		//sigmar's chosen
		//TODO
		
		//purge the heretic
		//TODO
		
		
		//vengeance
		this.ReplaceField("enchantment", "duration", 3, 1095);
		//stat 62
		this.AddEntry_enchantment_join_attribute(1095, 62, 10);
		
		//pyre of the righteous
		//TODO
		
		//blood sacrifice
		//TODO
		
		//shadow lord touch
		this.ReplaceField("enchantment", "duration", 3, 348);
		
		//norse charge
		this.ReplaceField("enchantment", "duration", 3, 318);
		this.Replace_enchantment_join_attribute(318, 61, 20, 10);
		
		//lurker
		this.ReplaceField("skill_attribute", "fk_attribute_id", 37, 266);
		
		//humble servant
		//TODO
		
		//disease carrier
		this.ReplaceField("enchantment", "duration", 3, 1263);
		this.ReplaceField("enchantment", "duration", 3, 1264);
		this.ReplaceField("enchantment", "duration", 3, 1265);
		this.Replace_enchantment_join_attribute(1263, 76, -5, -10);
		this.Replace_enchantment_join_attribute(1264, 51, -3, -5);
		this.Replace_enchantment_join_attribute(1265, 40, -3, -5);
		this.Replace_enchantment_join_attribute(1265, 41, -3, -5);
		
	}
}
