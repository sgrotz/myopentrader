

 /*
  * Copyright (C) 2015 Stephan Grotz - stephan@myopentrader.org
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *
  */
  
  
  package org.mot.common.tools;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Map;
import java.util.Queue;

import com.hazelcast.core.*;
import com.hazelcast.config.*;

public class GridFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main (String[] args) {
		
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        
        Map<String, Integer> myMap = instance.getMap("stephan");
        
        System.out.println(myMap.size());
        
        myMap.put("123", 1);
        
        Queue<String> queueCustomers = instance.getQueue("customers");
        queueCustomers.offer("Tom");
        queueCustomers.offer("Mary");
        queueCustomers.offer("Jane");
        System.out.println("First customer: " + queueCustomers.poll());
        System.out.println("Second customer: "+ queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());
                
        instance.shutdown();
		
	}
	
	
	private static GridFactory gf;
	private static HazelcastInstance instance;
	
	private GridFactory() {
		
		PropertiesFactory pf = PropertiesFactory.getInstance();
		Config cfg;
		try {
			cfg = new FileSystemXmlConfig(pf.getConfigDir() + "/hazelcast.xml");
			this.instance = Hazelcast.newHazelcastInstance(cfg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public synchronized static GridFactory getInstance() {
		if (instance == null) {
			gf = new GridFactory();
		} 
		return gf;
	}
	
	public HazelcastInstance getHazelCastInstance() {
		return instance;
	}
	
	
	public Map<String, String> getStringStringMap(String name) {
		return instance.getMap(name);
	}
	
	
	public Map<String, Integer> getMapAsString(String name) {
		return instance.getMap(name);
	}
	

	public Map<Double, Integer> getMapAsDouble(String name) {
		return instance.getMap(name);
	}
	
	public MultiMap<String, Double> getMultiMap(String name) {
		return instance.getMultiMap(name);
	}

}
