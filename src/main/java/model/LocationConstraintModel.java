package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import generator.Parameter;

public class LocationConstraintModel extends ModelAbstract{
	int currentLocation;
	@SuppressWarnings("unchecked")
	@Override
	public double getValue(Object... args) {
		if(args.length != 1) return 0.0;
		List<String> cList = (List<String>) args[0];
		List<Integer> locationList = new LinkedList<Integer>();

		for(String c : cList){
			if(Parameter.locationMap.containsKey(c)){
				locationList.add(Parameter.locationMap.get(c));
			}else{
				locationList.add(0);
			}
		}
		//calculate location score
		currentLocation = 1;
		int acceptCount = 0;

		for(int loc : locationList){
			if(checkRule(loc)){
				acceptCount++;
			}
		}
		//return 1.0;
		return Math.pow((double)acceptCount/locationList.size(), 3.0);
	}
	
	private boolean checkRule(int location){
		//same location
		if(location == currentLocation) return true;
		//switch location
		if(isSwitchLocation(location)){
			currentLocation = -location;
			return true;
		}
		//check hierarchy rule
		//location switch from parent to child location
		if(currentLocation == 1){
			currentLocation = location;
			return true;
		}else if(currentLocation == 2 && isInside(location)){
			currentLocation = location;
			return true;
		}else if(currentLocation == 3 && isOutside(location)){
			currentLocation = location;
			return true;
		}
		//location propagate while face parent location
		if(isAnywhere(location)){
			return true;
		}else if(location == 2 && isInside(currentLocation)){
			return true;
		}else if(location == 3 && isOutside(currentLocation)){
			return true;
		}
		currentLocation = location;
		return false;
		
	}
	private boolean isAnywhere(int location){
		if(location == 1) return true;
		return false;
	}
	
	private boolean isSwitchLocation(int location){
		if(location < 0) return true;
		return false;
	}
	private boolean isInside(int location){
		if(Arrays.asList(Parameter.insideLocation).contains(location)) return true;
		return false;
	}
	private boolean isOutside(int location){
		if(Arrays.asList(Parameter.outsideLocation).contains(location)) return true;
		return false;
	}
}
