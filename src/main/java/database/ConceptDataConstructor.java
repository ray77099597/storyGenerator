package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import generator.Parameter;
import others.Tagger;
import others.Utility;

public class ConceptDataConstructor {
	
	public void consrtuctConceptTable(){
		Tagger tagger = new Tagger();
		try {
			constructConceptID();
			constructSimilarity();
			constructSimSet();
//			constructLocationfeature();
//			constructPersonfeature();
//			constructTimefeature();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		tagger.taggerRelease();
	}
	
	/**
	 * collect all concept and construct id
	 * @throws SQLException 
	 */
	private void constructConceptID() throws SQLException{
		
		String SQL;
		ResultSet rs;
		
		//truncated concept table
		SQL = "truncate table "+Parameter.storyTeller_DBname +"."+Parameter.storyTeller_concept_table;
		Parameter.stm.executeUpdate(SQL);
		
		//collect all indifferent concept appeared in fabula link database
		Set <String>conceptSet = new HashSet<String>();
		
		for(String linktTable : Parameter.fabulaLink){
			SQL = "select * from " + Parameter.storyTeller_DBname + "." + linktTable;
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				conceptSet.add(rs.getString(Parameter.storyTeller_fabula_start));
				conceptSet.add(rs.getString(Parameter.storyTeller_fabula_end));
			}
		}
		System.out.println("concept set done!");
		
		//store all indifferent concept into concept table with unique ID
		for(String concept:conceptSet){
			SQL = "insert into " + Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table + " ("+ Parameter.storyTeller_concept_concept +") " + "values('" + concept.replace("'", "\\'") + "')";
			Parameter.stm.executeUpdate(SQL);
		}
		System.out.println("concept db id and concept done!");
	}
	
	/**
	 * read similarity concept using association api of conceptnet 5.4
	 * build the similarity set by parsing JSON using from conceptnet5 api
	 * store the similarity set into file and database
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void constructSimilarity() throws IOException, SQLException{
		
		Set<String> conceptSet_db = new HashSet <String> ();
		Set<String> conceptSet_file = new HashSet <String> ();
		Set<String> conceptSet_storeToDB;
		Set<String> conceptSet_findByConceptNet5;
		HashMap<String, String> conceptSimilarityMap = new HashMap<String, String>();
		HashMap<String, Integer> conceptIdMap = new HashMap<String, Integer>();
		ResultSet rs;
		String SQL;
		
		// open folder and file
		if (!(new File(Parameter.similarityFolder)).mkdirs()) System.out.println(Parameter.similarityFolder + " folder already open!");
		File varTmpDir = new File(Parameter.similarityFolder+"//"+Parameter.similarityFile + (int)(100*Parameter.similarityThreshold)+".txt");
		if(!varTmpDir.exists()) varTmpDir.createNewFile();
		
		// read file
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(Parameter.similarityFolder+"//"+Parameter.similarityFile + (int)(100*Parameter.similarityThreshold)+".txt") );
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			String[] sp = sCurrentLine.split(" ");
			if(sp.length > 1) conceptSimilarityMap.put(sp[0], sp[1]);
			else conceptSimilarityMap.put(sp[0], "");
		}
		conceptSet_file = conceptSimilarityMap.keySet();
		
		
		// find all concept in database
		SQL = "select * from " + Parameter.storyTeller_DBname + "." + Parameter.storyTeller_concept_table;
		rs = Parameter.stm.executeQuery(SQL);
		rs.beforeFirst();
		while(rs.next()){
			conceptSet_db.add(rs.getString(Parameter.storyTeller_concept_concept));
			conceptIdMap.put(rs.getString(Parameter.storyTeller_concept_concept), rs.getInt(Parameter.storyTeller_concept_id));
		}
		
		// concept that similarity is going to store directly to database
		conceptSet_storeToDB = new HashSet<String>(conceptSet_db);
		conceptSet_storeToDB.retainAll(new HashSet<String>(conceptSet_file));
		
		// concept that similarity is going to find by connecting to conceptnet5
		conceptSet_findByConceptNet5 = new HashSet<String>(conceptSet_db);
		conceptSet_findByConceptNet5.removeAll(new HashSet<String>(conceptSet_file));
		
		// store similarity directly to database that are originally stored in file
		for(String cpt : conceptSet_storeToDB){
			String simsetNumber = "";
			String[] sim = conceptSimilarityMap.get(cpt).split("#");
			for(int i=1; i<sim.length; i++){
				if(conceptIdMap.get(sim[i]) == null) continue;
				if(simsetNumber.equals("")) simsetNumber = conceptIdMap.get(sim[i])+"";
				else simsetNumber = simsetNumber + " " + conceptIdMap.get(sim[i]);
			}
			SQL = "update "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" set "+Parameter.storyTeller_concept_similarity+" = '"+simsetNumber+"' where "+Parameter.storyTeller_concept_concept+" = '"+cpt.replace("'", "\\'")+"'";	
			Parameter.stm.executeUpdate(SQL);
		}
		
		System.out.println("similarity file add to db!");
		
		// find similarity by connecting to conceptnet5 and store into database and file
		BufferedWriter bw = new BufferedWriter(new FileWriter(Parameter.similarityFolder+"//"+Parameter.similarityFile + (int)(100*Parameter.similarityThreshold)+".txt", true)); 
		for(String cpt : conceptSet_findByConceptNet5){
			try {
				JSONObject json = Utility.readJsonFromUrl("http://conceptnet5.media.mit.edu/data/5.4/assoc/list/en/"+cpt+"?limit="+Parameter.similarityLimited+"&filter=/c/en");
				JSONArray similarArray =  json.getJSONArray("similar");
				String similarity_db = "";
				String similarity_file = "";
				if(!similarArray.toString().equals("[]")){
					for(int i = 0;i < similarArray.length() ;i++){
						// if similarity weight < threshold then stop find (JSONArray is sorted by weight)
						if((Double)json.getJSONArray("similar").getJSONArray(i).get(1) < Parameter.similarityThreshold) break;
						String tempc = json.getJSONArray("similar").getJSONArray(i).get(0).toString().substring(6).split("/")[0];
						similarity_file = similarity_file+"#"+tempc;
						if(conceptIdMap.get(tempc) == null) continue;
						if(similarity_db.equals("")) similarity_db = conceptIdMap.get(tempc)+"";
						else similarity_db = similarity_db + " " + conceptIdMap.get(tempc);
					}
				}
				System.out.println(cpt + " " + similarity_file);
				//store to database
				SQL = "update "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" set "+Parameter.storyTeller_concept_similarity+" = '"+similarity_db+"' where "+Parameter.storyTeller_concept_concept+" = '"+cpt.replace("'", "\\'")+"'";	
				Parameter.stm.executeUpdate(SQL);
				//store to file
				bw.write(cpt + " " + similarity_file);
				bw.newLine();
				bw.flush();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		bw.close();
		
		System.out.println("similarity db done!");
	}
	
	/**
	 * find all similarity concept with the given concept id in the fabula concept, then store into concept table
	 * @throws IOException
	 * @throws SQLException
	 */
	private void constructSimSet() throws IOException, SQLException{
		String SQL;
		ResultSet rs;
		Map<Integer, List<Integer>> idToSimMap = new HashMap<Integer, List<Integer>>();
		
		//find similarity
		SQL = "select * from "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table;	
		rs = Parameter.stm.executeQuery(SQL);
		rs.beforeFirst();
		while(rs.next()){
			String[] simsp = rs.getString(Parameter.storyTeller_concept_similarity).split(" ");
			List<Integer> simList = new LinkedList<Integer>();
			for(String sim : simsp) {
				//"" is the condition that id has no similarity
				if(sim != "") simList.add(Integer.parseInt(sim));
			}
			idToSimMap.put(rs.getInt(Parameter.storyTeller_concept_id), simList);
		}
		rs.close();

		Set<Integer> idSet = new HashSet<Integer>(idToSimMap.keySet());
		
		//find simset
		while(!idSet.isEmpty()){
			int id = idSet.iterator().next();
			System.out.println("find "+ id);
			Set<Integer> simSet = new HashSet <Integer> ();
			simSet.add(id);
			//two-way find high confident concept id
			List<Integer> simList = idToSimMap.get(id);
			for(int simid : simList){
				if(idToSimMap.get(simid) == null) System.out.println(id +" "+simid);
				else if(idToSimMap.get(simid).contains(id)){
					simSet.add(simid);
				}
			}
			String simsetString = "";
			for(int simid : simSet) {
				if(simsetString.equals("")) simsetString = simid+"";
				else simsetString = simsetString + " " + simid;
			}
			//store to database
			SQL = "update "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" set "+Parameter.storyTeller_concept_simset+" = '"+simsetString+"' where "+Parameter.storyTeller_concept_id+" = '"+id+"'";	
			Parameter.stm.executeUpdate(SQL);
			//remove id from idset
			idSet.remove(id);
		}
		
		System.out.println("sim set done");
		
	}
	
	@SuppressWarnings("unused")
	private void constructSimSetOld() throws IOException, SQLException{
		
		Set<String> similaritySet;
		Set<String> findQueue;
		String SQL;
		ResultSet rs;
		Double similarity;
		Set<String> conceptSet = new HashSet <String> ();
		
		SQL = "select "+Parameter.storyTeller_concept_concept+" from "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table;	
		rs = Parameter.stm.executeQuery(SQL);
		rs.beforeFirst();
		while(rs.next()){
			conceptSet.add(rs.getString(Parameter.storyTeller_concept_concept));
		}
		
		while(!conceptSet.isEmpty()){
			String concept = conceptSet.iterator().next();
			System.out.println(concept);
			// BFS to find simset
			similaritySet = new HashSet<String>();
			findQueue = new HashSet<String>();
			if(!similaritySet.contains(concept)) findQueue.add(concept);
			similaritySet.add(concept);
			while(!findQueue.isEmpty()){
				String findConcept = findQueue.iterator().next();
				JSONObject json = Utility.readJsonFromUrl("http://conceptnet5.media.mit.edu/data/5.4/assoc/list/en/"+concept+"?limit=200&filter=/c/en");
				JSONArray similarArray =  json.getJSONArray("similar");
				if(!similarArray.toString().equals("[]")){
					for(int i = 0;i < similarArray.length() ;i++){
						similarity = (Double)json.getJSONArray("similar").getJSONArray(i).get(1);
						if(similarity<Parameter.similarityThreshold) break;
						String tempc = json.getJSONArray("similar").getJSONArray(i).get(0).toString().substring(6).split("/")[0];
						if(!similaritySet.contains(tempc)) findQueue.add(tempc);
						similaritySet.add(tempc);
					}
				}
				findQueue.remove(findConcept);
			}
			//Store into db
			String simsetNumber = "";
			for(String cpt:similaritySet){
				SQL = "select id from "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" where concept = '"+cpt.replace("'", "\\'") + "'";	
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				if(rs.next()){
					simsetNumber = simsetNumber + " " + rs.getInt("id");
				}
			}
			for(String cpt:similaritySet){
				SQL = "update "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" set simset = '"+simsetNumber+"' where concept = '"+cpt.replace("'", "\\'")+"'";	
				Parameter.stm.executeUpdate(SQL);
			}
			conceptSet.removeAll(similaritySet);
		}
		System.out.println("concept db simset done!");
		
	}

	@SuppressWarnings("unused")
	private void constructLocationfeature(){
		
	}
	
	@SuppressWarnings("unused")
	private void constructPersonfeature(){
		String SQL;
		ResultSet rs;
		Set<String> personlist;
		Set<String> conceptSet = new HashSet <String> ();
		
		try {
			for(String cpt : conceptSet){
				personlist = new HashSet<String>();
				//find person concept by relation
				
				SQL = "select * from "+Parameter.conceptNet5_DBname+"."+Parameter.conceptNet5_table+" where ( end = '"+cpt.replace("'", "\\'")+"' or start = '"+cpt.replace("'", "\\'")+"' ) and weight > 1.0";	
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				while(rs.next()){
					if(Arrays.asList(Parameter.causalRelation).contains(rs.getString("rel").toLowerCase())) continue;
					if(rs.getString("start").equals(cpt)) personlist.add(rs.getString("end"));
					else personlist.add(rs.getString("start"));
				}
				
				String person = "";
				for(String p : personlist){
					//check person concept is a name by tagger
					person = person + " " + p;
				}
				SQL = "update "+Parameter.storyTeller_DBname+"."+Parameter.storyTeller_concept_table+" set feature = '"+person.replace("'", "\\'")+"' where concept = '"+cpt.replace("'", "\\'")+"'";	
				Parameter.stm.executeUpdate(SQL);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unused")
	private void constructTimefeature(){
		
	}
	
	
}
