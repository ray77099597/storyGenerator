package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import generator.Parameter;
/**
 * csv file is split by tab
 * /a/[/r/IsA/,/c/en/bitter_odor/,/c/en/odor/]	/r/IsA	/c/en/bitter_odor	/c/en/odor	/ctx/all	1.0	/s/umbel/2013	/e/33b2a9d2b872c1e42fd7dce3372926156b6ffb46	/d/umbel	[[bitter odor]] is an instance of [[odor]]
 */
public class ConceptNetDatabaseConstructor {
	
	@SuppressWarnings("resource")
	public void buildConceptNetDatabase(){
		String SQL;
		
		// get all csv file in the directory
		File a = new File(Parameter.conceptNet5CSVfilePath);
	    String[] filenames = a.list();
	    
	    //read csv
		try {
			String start, end, relation;
			double weight;
			//truncate table, drop index
			try{
				SQL = "truncate table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table;
				Parameter.stm.executeUpdate(SQL);
				SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" drop index "+Parameter.conceptNet5_id;
				Parameter.stm.executeUpdate(SQL);
				SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" drop index "+Parameter.conceptNet5_start;
				Parameter.stm.executeUpdate(SQL);
				SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" drop index "+Parameter.conceptNet5_end;
				Parameter.stm.executeUpdate(SQL);
				SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" drop index "+Parameter.conceptNet5_relation;
				Parameter.stm.executeUpdate(SQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			for(String csvfile : filenames){
		    	FileReader fr = new FileReader(Parameter.conceptNet5CSVfilePath+"//"+csvfile);
		    	System.out.println("run "+csvfile);
				BufferedReader br = new BufferedReader(fr);
		    	String str;
				while((str = br.readLine())!= null){
					//if concept is english
					if(!str.split("	")[2].split("/")[2].equals("en")) continue;
					//check array of split string > 3
					if(str.split("	")[2].split("/").length<4 || str.split("	")[3].split("/").length<4) continue;
					relation = str.split("	")[1].split("/")[2].replace("'", "\\'");
					start = str.split("	")[2].split("/")[3].replace("'", "\\'");
					end = str.split("	")[3].split("/")[3].replace("'", "\\'");
					weight = Double.parseDouble(str.split("	")[5]);
					//check length of concept
					if(start.length() > 100 || end.length() > 100) continue;
					try{
						SQL = "insert into " + Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table +
								" ("+Parameter.conceptNet5_start+","+Parameter.conceptNet5_end+","+Parameter.conceptNet5_relation+","+Parameter.conceptNet5_weight+") " + 
								"values('"+start+"','"+end+"','"+relation+"',"+weight+")";
						Parameter.stm.executeUpdate(SQL);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
		    }
	    } catch (IOException e) {
			e.printStackTrace();
		}
		//add index
		try{
			SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" add index ("+Parameter.conceptNet5_id+")";
			Parameter.stm.executeUpdate(SQL);
			SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" add index ("+Parameter.conceptNet5_start+")";
			Parameter.stm.executeUpdate(SQL);
			SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" add index ("+Parameter.conceptNet5_end+")";
			Parameter.stm.executeUpdate(SQL);
			SQL = "alter table "+Parameter.conceptNet5_DBname +"."+Parameter.conceptNet5_table+" add index ("+Parameter.conceptNet5_relation+")";
			Parameter.stm.executeUpdate(SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Conceptnet Database Done!");
	}
}
