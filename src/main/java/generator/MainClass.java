package generator;

import connection.Myjdbc;
import database.ConceptDataConstructor;
import database.ConceptNetDatabaseConstructor;
import database.FabulaDatabasConstructor;
import ui.MainWindow;

public class MainClass {
	
	public static void init() {
		Parameter.con = Myjdbc.connect();
		Parameter.stm = Myjdbc.createStatement(Parameter.con);
	}
	
	public static void release() {
		Myjdbc.closeStatement(Parameter.stm);
		Myjdbc.disconnect(Parameter.con);
	}
	
	public static void main(String[] args) {
		
		init();
		switch (Parameter.mode) {
		//build concept database by extracting concept from conceptnet5 .csv file
		case CONSTRUCT_CONCEPTNET_DB:
			ConceptNetDatabaseConstructor ccc = new ConceptNetDatabaseConstructor();
			ccc.buildConceptNetDatabase();
			release();
			break;
		//build fabula links database by extracting concept from conceptnet database
		case CONSTRUCT_FABULA_DB:
			FabulaDatabasConstructor r = new FabulaDatabasConstructor();
			r.buildFabulaDatabase();
			release();
			break;
		//build similarity links database by extracting concept from conceptnet database
		case CONSTRUCT_CONCEPT_DB:
			ConceptDataConstructor s = new ConceptDataConstructor();
			s.consrtuctConceptTable();
			release();
			break;
		//generate story using MCTS
		case GENERATE_STORY:
			new MainWindow().openWindow();
			break; 
		}
		
	}
	
	
	
}
