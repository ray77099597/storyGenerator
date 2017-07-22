package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import generator.Parameter;

public class ConstraintModel extends ModelAbstract{

	
	@SuppressWarnings("unchecked")
	@Override
	public double getValue(Object... args) {
		if(args.length !=2) return 1.0;
		List<String> cList = (List<String>) args[0];
		int storyIndex = (Integer) args[1];
		LinkedList<String> aList;
		Set<String> pset = new HashSet<String>();
		Set<String> nset = new HashSet<String>();
		boolean isContain;
		
		for (String k : cList) {
			String[] sp = k.split("_");
			aList = new LinkedList<String>(Arrays.asList(sp));
			for(String term:Parameter.storyInformation.get(storyIndex).storyformat.positiveConstraint){
				String[] spTerm = term.split("_");
				isContain = true;
				for(String t: spTerm){
					if(!aList.contains(t)){
						isContain = false;
						break;
					}
				}
				if(isContain){
					pset.add(term);
				}
			}
			for(String term:Parameter.storyInformation.get(storyIndex).storyformat.negativeConstraint){
				String[] spTerm = term.split("_");
				isContain = true;
				for(String t: spTerm){
					if(!aList.contains(t)){
						isContain = false;
						break;
					}
				}
				if(isContain){
					nset.add(term);
				}
			}
		}
//		return (1-((double)nset.size()/Parameter.negtiveConstraint.length))*(1+((double)pset.size()/Parameter.positiveConstraint.length));
//		return Math.pow(((double)Parameter.negtiveConstraint.length+pset.size()-nset.size()+1)/(Parameter.negtiveConstraint.length+Parameter.positiveConstraint.length+1),2);
		return((double)Parameter.storyInformation.get(storyIndex).storyformat.positiveConstraint.length+pset.size()-nset.size()+1)/(Parameter.storyInformation.get(storyIndex).storyformat.negativeConstraint.length+Parameter.storyInformation.get(storyIndex).storyformat.positiveConstraint.length+1);
	
	}

}
