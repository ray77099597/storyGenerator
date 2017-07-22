/**
 * Parameter of story generator
 */

package generator;

import java.io.BufferedWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.analysis.function.Gaussian;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import model.ConstraintModel;
import model.LocationConstraintModel;
import model.LengthModel;
import others.StoryStructure;
import others.Timer;
import others.Character;
import others.StoryInformation;
import storyFormat.StoryFormat;
import ui.MainWindow;

public class Parameter {
	
	/**
	 * Database Connection Parameter
	 * jdbcUser		Database user name
	 * jdbcPasd		Database user password
	 * jdbcIp		Database IP
	 * con			Connection
	 * stm			Statement
	 */
	public static final String jdbcUser = "root";
	public static final String jdbcPasd = "kimo810422";
	public static final String jdbcIp = "localhost";
	public static Connection con = null;
	public static Statement stm = null;
	
	/**
	 * ConceptNet5 database
	 * database 					ConceptNet5
	 * table 						data
	 * scheme 						id, rel, start, end, weight
	 */
	public static final String conceptNet5_DBname = "ConceptNet5";
	public static final String conceptNet5_table = "data";
	public static final String conceptNet5_id = "id";
	public static final String conceptNet5_relation = "rel";
	public static final String conceptNet5_start = "start";
	public static final String conceptNet5_end = "end";
	public static final String conceptNet5_weight = "weight";
	
	/**
	 * StoryTeller database
	 * database 				StoryTeller2
	 * fabula table 			a_e, a_p, e_e, e_p, g_a, g_g, ie_a, ie_g, ie_ie, ie_o, o_g, o_ie, p_ie
	 * scheme 					id, rel, start, end, weight
	 * concept table 			concept
	 * scheme 					id, concept, simset, feature, similarity
	 * action table				action
	 * scheme					id, concept, type, indegree, outdegree, location
	 */
	public static final String storyTeller_DBname = "StoryTeller2";
	public static final String[] storyTeller_fabula_table = {"a_e", "a_p", "e_e", "e_p", "g_a", "g_g", "ie_a", "ie_g", "ie_ie", "p_ie"};
	public static final String storyTeller_fabula_id = "id";
	public static final String storyTeller_fabula_relation = "rel";
	public static final String storyTeller_fabula_start = "start";
	public static final String storyTeller_fabula_end = "end";
	public static final String storyTeller_fabula_weight = "weight";
	/* --------------------------------------------------------------------------------------------------- */
	public static final String storyTeller_concept_table = "concept";
	public static final String storyTeller_concept_id = "id";
	public static final String storyTeller_concept_concept = "concept";
	public static final String storyTeller_concept_simset = "simset";
	public static final String storyTeller_concept_feature = "feature";
	public static final String storyTeller_concept_similarity = "similarity";
	/* --------------------------------------------------------------------------------------------------- */
	public static final String storyTeller_action_table = "action";
	public static final String storyTeller_action_id = "id";
	public static final String storyTeller_action_concept = "concept";
	public static final String storyTeller_action_type = "type";
	public static final String storyTeller_action_indegree = "indegree";
	public static final String storyTeller_action_outdegree = "outdegree";
	public static final String storyTeller_action_location = "location";
	
	/**
	 * ConceptNet5 Database Constructor Parameter
	 * conceptNet5CSVfilePath 		Location of CSV file of all conceptnet5 data
	 */
	public static final String conceptNet5CSVfilePath = "data/assertions";
	
	/**
	 * Fabula Database Constructor Parameter
	 * relationCounter			StoryTeller database counter to count number of each fabula link and element
	 * similarityFolder			Folder to store similarity file (Automatically generated)
	 * similarityFile			file to store similarity data of each concept (Automatically generated)
	 * ------------------------------- data form -------------------------------
	 * concept					concept1 concept2 concept3 ... (split by ' ')
	 * ------------------------------- data form -------------------------------
	 */
	public static final HashMap <String, Integer> relationCounter = new HashMap<String, Integer>();
	public static final String similarityFolder = "similarity";
	public static final String similarityFile = "similarity";
	public static final int similarityLimited = 200;
	public static final double similarityThreshold = 0.85;
	
	/**
	 * Wikitionary Parameter
	 * wiktionaryXML			Wiktionary XML 
	 * wiktionaryPath			Wiktionary dump path
	 * 
	 * Download wikitionary from wiktionaryXML to wiktionaryPath
	 * ---------------------Code---------------------
	 * File dumpFile = new File(Parameter.wiktionaryXML);	
	 * File outputDirectory = new File(Parameter.wiktionaryPath);
	 * boolean overwriteExisting = Boolean.valueOf(true);
	 * JWKTL.parseWiktionaryDump(dumpFile, outputDirectory, overwriteExisting);
	 * ---------------------Code---------------------
	 */
	public static final String wiktionaryXML = "enwiktionary-20161201-pages-articles.xml";
	public static final String wiktionaryPath = "WiktionaryDump";
	
	/**
	 * Add data or generate story
	 * CONSTRUCT_CONCEPTNET_DB		construct conceptnet5 database from csv file
	 * CONSTRUCT_FABULA_DB    		construct fabula database from conceptnet5 database
	 * CONSTRUCT_CONCEPT_TB    		construct concept data database from fabula database
	 * GENERATE_STORY 	 			open story generator window
	 */
	public enum Mode {
		CONSTRUCT_CONCEPTNET_DB,
		CONSTRUCT_FABULA_DB,
		CONSTRUCT_CONCEPT_DB,
		GENERATE_STORY
	}
	public static final Mode mode = Mode.GENERATE_STORY;
	
	/** TODO
	 *  add to Jswing
	 */
	public static final double constraintRatio = 1.5;
	public static final LinkedList<String> dynamicConstraint = new LinkedList<String>();
	
	/**
	 * Story generator Constant
	 * START_TO_GOAL_STORY			story from start concept to goal concept
	 * UNLIMITED_STORY				story from start concept to any goal concept
	 * UNLIMITED_INVERSE_STORY		story from any start concept to goal concept
	 * LONG_STORY					longer story than START_TO_GOAL_STORY, from start concept to goal concept
	 * HUMOR_STORY 					not yet complete
	 * TWO_CHARACTER_STORY			story from start concept to goal concept, with two characters
	 */
	public enum StoryType {
		START_TO_GOAL_STORY,
		UNLIMITED_STORY,
		UNLIMITED_INVERSE_STORY,
		LONG_STORY,
		HUMOR_STORY,
		TWO_CHARACTER_STORY,
		TWO_CHARACTER_STORY2,
		TWO_CHARACTER_STORY3
	}
	
	/**
	 * Story generator Parameter
	 * 
	 */
	public static final double MaxScore = 7.127;
	public static final double epsilon = 1e-6;
	public static final int ScoreConstant = 1000;
	/**
	 * Story generator Parameter
	 * 
	 * resultFolder				Folder to store result of generated story (Automatically generated)
	 */
	public static BufferedWriter fwriter;
	public static final String resultFolder = "result";
	public static double alpha;
	public static int storyCnt = 0;
	public static List<StoryStructure> storyList;
	public static List<StoryStructure> storyList2;
	public static List<StoryInformation> storyInformation = new ArrayList<StoryInformation>();
	public static final HashMap <String, Integer> actionMap = new HashMap<String, Integer>();
	public static final Set <String> actionHighOutdegreeSet = new HashSet<String>();
	public static final HashMap <String, Integer> locationMap = new HashMap<String, Integer>();
	public static final int[] insideLocation = {4,5,6,7,8,9,10,11,12,13,14,15,16,17,22,23,24,25,26,27};
	public static final int[] outsideLocation = {18,19,20};
	/**
	 * public static final StoryFormat[] story = { 

			new StoryFormat(Parameter.StartToGoalStory, 1000000, 12, "join_army", "e", "fight_war"),
			new StoryFormat(Parameter.StoryType.START_TO_GOAL_STORY, 1000000, 12, "join_army", "e", "have_peace"),
			new StoryFormat(Parameter.StoryType.START_TO_GOAL_STORY, 1000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StoryType.LONG_STORY, 5000000, 15, "wake_up", "e", "hot"),
			new StoryFormat(Parameter.StoryType.LONG_STORY, 5000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StoryType.START_TO_GOAL_STORY, 200000, 8, "wake_up", "e", "get_hot"),
			new StoryFormat(Parameter.StoryType.START_TO_GOAL_STORY, 200000, 8, "wake_up", "e", "get_cold"),
			new StoryFormat(Parameter.StartToGoalStory, 2000000, 50, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 1000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 1000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 2000000, 40, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 3000000, 10, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 3000000, 20, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 3000000, 30, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 3000000, 40, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 3000000, 50, "go_to_school", "e", "go_home"),
			
			new StoryFormat(Parameter.StartToGoalStory, 600000, 30, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 700000, 30, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 800000, 30, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 900000, 30, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 1000000, 30, "go_to_school", "e", "go_home"),	
			
			new StoryFormat(Parameter.UnlimitedStory, 1000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 2000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.StartToGoalStory, 5000000, 40, "exercise", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 4000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 5000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 4000000, 12, "wake_up", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 5000000, 12, "wake_up", "e", "go_home"),
			
			new StoryFormat(Parameter.UnlimitedStoryInverse, 2000000, 12, "go_home", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 12, "cry", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 12, "cry", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 12, "cry", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 12, "cry", "e", "go_home"),
			
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "be_sick", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "be_lonely", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 2000000, 10, "go_home", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 10, "get_healthy", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 10, "make_friend", "e", "go_home"),
			
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "be_sick", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 3000000, 10, "be_lonely", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 10, "go_home", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 10, "get_healthy", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 10, "make_friend", "e", "go_home"),
			
			
			new StoryFormat(Parameter.UnlimitedStoryInverse, 1000000, 10, "cry", "e", "go_to_school"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 20, "get_marry", "e", "go_to_school"),
			new StoryFormat(Parameter.UnlimitedStoryInverse, 3000000, 20, "get_rich", "e", "go_to_school"),
			new StoryFormat(Parameter.UnlimitedStory, 2000000, 50, "go_to_school", "e", "go_home"),
			new StoryFormat(Parameter.UnlimitedStory, 200000, 15, "go_to_school", "e", "go_home"),
			new StoryFormat(1000000, 50, "go_to_school", "e", "go_home"),
			new StoryFormat(1000000, 70, "go_to_school", "e", "go_home"),
			
			new StoryFormat(1000000, 30, "wake_up", "e", "go_to_work"),
			new StoryFormat(1000000, 50, "wake_up", "e", "go_to_work"),
			new StoryFormat(1000000, 70, "wake_up", "e", "go_to_work"),
			
			new StoryFormat(1000000, 12, "go_to_school", "e", "go_home"),
			new StoryFormat(1000000, 12, "go_to_work", "e", "be_fire"),
			new StoryFormat(1000000, 12, "find_job", "e", "get_marry"),
			new StoryFormat(1000000, 12, "be_lonely", "e", "make_friend"),
			new StoryFormat(1000000, 12, "have_no_money", "e", "get_rich"),
			new StoryFormat(1000000, 12, "bore", "e", "visit_other_country"),
			
			new StoryFormat(Parameter.UnlimitedStory, 500000, 12, "wake_up", "e", "go_to_work"),
			new StoryFormat(750000, 12, "wake_up", "e", "go_to_work"),
	};
	 */
	
	/**
	 * Story Generator Parameter
	 * 
	 */
	public static String curStartConcept;
	public static String curStartState;
	public static String curGoalConcept;
	public static int curStoryLength;
	public static StoryType curStorytype;
	public static boolean forwardDirection;
	public static Gaussian gaussianDistribution = null;
	public static HashMap <String, List<String>> nextStates = null;
	public static Set<String> examine;
	public static Set<Integer> indexExamine;
	public static Set<Integer> locationSet;
	public static int currentlocation;
	/**
	 * Story Generator Evaluation Model
	 * lengthModel				: Model to evaluate score by length of story
	 * constraintModel			: Model to evaluate score by constraint satisfaction of story
	 * dynamicConstraintModel	: Model to evaluate score by dynamic constraint satisfaction of story (not yet done)
	 */
	public static LengthModel lengthModel = new LengthModel();
	public static ConstraintModel constraintModel = new ConstraintModel();
	public static LocationConstraintModel locationConstraintModel = new LocationConstraintModel();
	/**
	 * User Define Parameter
	 */
	public static int fabulaLimit = 100;
	public static int actionNum = 1000;
	public static final int shortestStorylength = 5;
	public static final int generateStoryNum = 500;
	
	/**
	 * Essay Generator Character Information
	 */
	public static EssayGenerator essayGenerator = null;
	
	public static final DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
	
	public static final Character character1 = new Character("Jason", "male");
	public static final Character character2 = new Character("Lisa", "female");
	
	public static final String subjectPronoun_male = "he";
	public static final String objectPronoun_male = "him";
	public static final String possessivePronouns_male = "his";
	public static final String reflexivePronoun_male = "himself";
	
	public static final String subjectPronoun_female = "she";
	public static final String objectPronoun_female = "her";
	public static final String possessivePronouns_female = "hers";
	public static final String reflexivePronoun_female = "herself";
	
	/**
	 * Window Parameter
	 * Main window : main window
	 * stopRunning : stop MCTS
	 * isAddWindow : check the window is adding window or revising window
	 * reviseStoryIndex : the index of choosing story in the story jlist 
	 * selectStoryResultIndex : the index of choosing story result in the result jlist 
	 */
	public static MainWindow mainWindow;
	public static boolean stopRunning;
	public static boolean isAddWindow;
	public static int reviseStoryIndex;
	public static int selectStoryIndex;
	public static int selectStoryResultIndex;
	
	/**
	 * Timer 
	 * calculate time on each steps of MCTS
	 */
	public static Timer mainTimer = new Timer();
	public static Timer simulTimer = new Timer();
	public static Timer expandTimer = new Timer();
	public static Timer selectTimer = new Timer();
	
	/**
	 * Fabula Information
	 * causalRelation 			potential relation that could be fabula link
	 * backwardCausalRelation	potential relation that could be fabula link (from end to start)
	 * fabulaLink				fabula links
	 * fabulaElement			fabula elements
	 */
	public static final String[] causalRelation = new String[] { "capableof", "causes", "causesdesire", "haslastsubevent", "hasfirstsubevent", "hassubevent"};
	public static final String[] backwardCausalRelation = new String[] {"hasprerequisite", "motivatedbygoal", "usedfor"};
	public static final String[] fabulaLink = new String[] {"a_e", "a_p", "e_e", "e_p", "g_a", "g_g", "ie_a", "ie_g", "ie_ie", "p_ie", "ie_o", "o_g", "o_ie", "a_ie", "e_ie"};
	public static final String[] fabulaElement = {"e", "p", "ie", "g", "a", "o"};
	
}
