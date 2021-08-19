/* MULTITHREADING <MyClass.java>

 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Michael Whitaker
 * maw5299
 * 75535
 * Slip days used: <0>
 * Summer 2020
 */
package assignment6;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import assignment6.Theater.Seat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

	
	private Theater theater;
	private Map<String, Integer> office;
	private List<Thread> tickets;
    /**
     * @param office  maps box office id to number of customers in line
     * @param theater the theater where the show is playing
     */
    public BookingClient(Map<String, Integer> office, Theater theater) {
        // TODO: Implement this constructor
    	this.theater = theater;
    	this.office = office;
    }

    /**
     * Starts the box office simulation by creating (and starting) threads
     * for each box office to sell tickets for the given theater
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are box offices
     */
    public List<Thread> simulate() {
        // TODO: Implement this method
    	Set<Entry<String, Integer>> e = office.entrySet();
    	Iterator<Entry<String,Integer>> it = e.iterator();
    	tickets = new ArrayList<Thread>();
    	
    	
    	while(it.hasNext()) {
    		Entry<String, Integer> entry = it.next();
    		Thread thread = new Thread(new BoxOffice(theater, entry.getKey(), entry.getValue()));
    		tickets.add(thread);
    	}
    	
    	for(int i=0;i<tickets.size();i++) {
    		tickets.get(i).start();
    	}
    	
        return tickets;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
    	Map<String, Integer> boxOff = new HashMap<String, Integer>();
    	boxOff.put("BX1", 15);
    	boxOff.put("BX3", 15);
    	boxOff.put("BX2", 2);
    	boxOff.put("BX5", 15);
    	boxOff.put("BX4", 15);
    	/*
    	boxOff.put("BX1", 3);
    	boxOff.put("BX3", 3);
    	boxOff.put("BX2", 4);
    	boxOff.put("BX5", 3);
    	boxOff.put("BX4", 3);
    	*/

    	
    	//Theater t = new Theater(3, 5, "Ouija");
    	Theater t = new Theater(1, 50, "Ouija");
    	BookingClient book = new BookingClient(boxOff, t);
    	book.simulate();
    }
}
