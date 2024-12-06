package org.ofb.utils;

public class ModelUtils 
{	
	public static boolean isNumeric(String str) {
        return (str.matches("[+-]?\\d*(\\.\\d+)?") && str.equals("")==false);
    }
}
