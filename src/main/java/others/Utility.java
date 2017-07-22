package others;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
	static private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	static public JSONObject readJsonFromUrl(String url) throws IOException,JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	static public List<String> readFile(String filename) {
		List<String> records = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				records.add(line);
			}
			reader.close();
			return records;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.",
					filename);
			e.printStackTrace();
			return null;
		}
	}

	static public int sampling(Random r, double weight[]) {
		// Random r = new Random(System.currentTimeMillis());
		double find = r.nextDouble();
		double total = 0.0;
		// System.out.println(find);
		for (int i = 0; i < weight.length; i++) {
			total += weight[i];
			if (total > find) {
				return i;
			}
		}
		return weight.length - 1;
	}

	static public int sampling(double weight[]) {
		Random r = new Random(System.currentTimeMillis());
		double find = r.nextDouble();
		double total = 0.0;
		for (int i = 0; i < weight.length; i++) {
			total += weight[i];
			if (total > find) {
				return i;
			}
		}
		return weight.length - 1;
	}

	static public HashMap<String, List<String>> initNextStates() {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> nextStates = new LinkedList<String>();

		nextStates.add("e");
		nextStates.add("p");
		map.put("e", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		map.put("p", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		nextStates.add("g");
		nextStates.add("a");
		nextStates.add("o");
		map.put("ie", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("p");
		nextStates.add("e");
		map.put("a", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("g");
		nextStates.add("a");
		map.put("g", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		nextStates.add("g");
		map.put("o", nextStates);
		return map;
	}
	
	static public HashMap<String, List<String>> initNextStatesInverse() {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> nextStates = new LinkedList<String>();

		nextStates.add("e");
		nextStates.add("a");
		map.put("e", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("e");
		nextStates.add("a");
		map.put("p", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("p");
		nextStates.add("ie");
		nextStates.add("o");
		map.put("ie", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		nextStates.add("g");
		map.put("a", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		nextStates.add("g");
		nextStates.add("o");
		map.put("g", nextStates);
		nextStates = new LinkedList<String>();
		nextStates.add("ie");
		map.put("o", nextStates);
		return map;
	}

	

}
