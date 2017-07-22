package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import generator.Parameter;

public class FabulaDatabasConstructor {
	String SQL;
	ResultSet rs;
	public void buildFabulaDatabase(){
		
		ConceptToFabulaElementFilter flt = new ConceptToFabulaElementFilter();
		Set<Integer> idSet = new HashSet<Integer>();
		String start, end, relation;
		double weight;
		List<String> fabulaLinkList = Arrays.asList(Parameter.fabulaLink);
		List<String> causalRelationList = Arrays.asList(Parameter.causalRelation);
		List<String> backwardCausalRelationList = Arrays.asList(Parameter.backwardCausalRelation);
		Set <String> startState, endState;
		
		flt.filterInit();
		
		try {
			//truncate fabula link table
			for(String table : Parameter.fabulaLink){
				SQL = "truncate table "+Parameter.storyTeller_DBname +"."+table;
				Parameter.stm.executeUpdate(SQL);
			}
			//collect the id all tuple that is causal relation
			SQL = "select "+Parameter.conceptNet5_id+" from " + Parameter.conceptNet5_DBname + "." + Parameter.conceptNet5_table +" where ";
			for(String rel : causalRelationList){
				SQL = SQL + Parameter.conceptNet5_relation + " = '" + rel + "'";
				if(causalRelationList.lastIndexOf(rel) != causalRelationList.size()-1){
					SQL = SQL + " or ";
				}
			}
			for(String rel : backwardCausalRelationList){
				SQL = SQL + " or " + Parameter.conceptNet5_relation + " = '" + rel + "'";
			}
			System.out.println(SQL);
			rs = Parameter.stm.executeQuery(SQL);
			rs.beforeFirst();
			while(rs.next()){
				idSet.add(rs.getInt(1));
			}
			System.out.println(idSet.size());
			//filter and insert into storyteller database
			for(int id : idSet){
				SQL = "select * from " + Parameter.conceptNet5_DBname + "." + Parameter.conceptNet5_table +" where " + Parameter.storyTeller_fabula_id + " = " + id;
				rs = Parameter.stm.executeQuery(SQL);
				rs.beforeFirst();
				if(rs.next()){
					//check concept belongs to which fabula element
					relation = rs.getString(Parameter.conceptNet5_relation);
					if(causalRelationList.contains(relation.toLowerCase())){
						start = rs.getString(Parameter.conceptNet5_start);
						end = rs.getString(Parameter.conceptNet5_end);
					}else{
						end = rs.getString(Parameter.conceptNet5_start);
						start = rs.getString(Parameter.conceptNet5_end);
					}
					weight = rs.getDouble(Parameter.conceptNet5_weight);
					startState = flt.filter(start);
					endState = flt.filter(end);
					for(String ss : startState){
						for(String es : endState){
								String linkTable = ss+"_"+es;
								//System.out.println("Insert "+start+" "+relation+" "+end+" into table "+linkTable);
								if(fabulaLinkList.contains(linkTable)){
									SQL = "insert into " + Parameter.storyTeller_DBname +"."+linkTable +
											" ("+Parameter.storyTeller_fabula_start+","+Parameter.storyTeller_fabula_end+","+Parameter.storyTeller_fabula_relation+","+Parameter.storyTeller_fabula_weight+") " + 
											"values ('"+start.replace("'", "\\'")+"','"+end.replace("'", "\\'")+"','"+relation+"',"+weight+")";
									System.out.println(id+" "+SQL);
									Parameter.stm.executeUpdate(SQL);
								}
						}
					}
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		//print fabula link table and element information
		for (Map.Entry<String, Integer> m : Parameter.relationCounter.entrySet()) {
			System.out.println(m.getKey() + " : " + m.getValue());
		}
		
		flt.filterRelease();
		
		System.out.println("fabula database done!");
	}
}
