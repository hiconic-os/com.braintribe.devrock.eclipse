package com.braintribe.devrock.zed.ui.comparison.renderer.data;

import java.util.ArrayList;
import java.util.List;

/**
 * a simple structure that contains all differences found within a compilation unit (class/interface/enum/annotation)
 * @author pit
 */
public class Differences {
	
	String relevantOwner;
	String rating;
	List<Difference> differences = new ArrayList<>();
	
	/**
	 * @return - the name of the compilation unit 
	 */
	public String getRelevantOwner() {
		return relevantOwner;
	}
	public void setRelevantOwner(String relevantOwner) {
		this.relevantOwner = relevantOwner;
	}
	
	/**
	 * @return - the *worst* rating found amongst all differences 
	 */
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	/**
	 * @return - a list of {@link Difference}s attached to the compilation unit
	 */
	public List<Difference> getDifferences() {
		return differences;
	}
	public void setDifferences(List<Difference> differences) {
		this.differences = differences;
	}
	
	
	
	
}
