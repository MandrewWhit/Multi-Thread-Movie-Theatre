/* MULTITHREADING <MyClass.java>

 * EE422C Project 6 submission by
  * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Michael Whitaker
 * maw5299
 * 75535
 * Slip days used: <0>
 * Summer 2020
 */
package assignment6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;



import assignment6.Theater.Seat;

public class Theater {
	
	
	private int numRows;
	private int seatsPerRow;
	private String show;
	private int currentClient = 0;
	private boolean ticketsRemaining = true;
	private ArrayList<Ticket> ticketList = new ArrayList();
	private HashMap<Seat, Boolean> seats = new HashMap<Seat, Boolean>(); 
	private boolean printed = true;
	
	public boolean getTR() {
		return ticketsRemaining;
	}
	public void setTR(boolean tr) {
		ticketsRemaining = tr;
	}
	public boolean getPrinted() {
		return printed;
	}
	public void setPrinted(boolean tf) {
		printed = tf;
	}
	public synchronized int getCurrentClient() {
		
		return currentClient;
	}
	public synchronized void setCurrentClient(int cc) {
		currentClient = cc;
	}
	public ArrayList<Ticket> getTickets() {
		return ticketList;
	}
	public HashMap<Seat, Boolean> getSeats(){
		return seats;
	}

    /**
     * the delay time you will use when print tickets
     */
    private int printDelay = 1000; // 50 ms.  Use it in your Thread.sleep()

    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Represents a seat in the theater
     * A1, A2, A3, ... B1, B2, B3 ...
     */
    static class Seat {
        private int rowNum;
        private int seatNum;

        public Seat(int rowNum, int seatNum) {
            this.rowNum = rowNum;
            this.seatNum = seatNum;
        }

        public int getSeatNum() {
            return seatNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        @Override
        public String toString() {
            String result = "";
            int tempRowNumber = rowNum + 1;
            do {
                tempRowNumber--;
                result = ((char) ('A' + tempRowNumber % 26)) + result;
                tempRowNumber = tempRowNumber / 26;
            } while (tempRowNumber > 0);
            result += seatNum;
            return result;
        }
    }

    /**
     * Represents a ticket purchased by a client
     */
    static class Ticket {
        private String show;
        private String boxOfficeId;
        private Seat seat;
        private int client;
        public static final int ticketStringRowLength = 31;


        public Ticket(String show, String boxOfficeId, Seat seat, int client) {
            this.show = show;
            this.boxOfficeId = boxOfficeId;
            this.seat = seat;
            this.client = client;
        }

        public Seat getSeat() {
            return seat;
        }

        public String getShow() {
            return show;
        }

        public String getBoxOfficeId() {
            return boxOfficeId;
        }

        public int getClient() {
            return client;
        }

        @Override
        public String toString() {
            String result, dashLine, showLine, boxLine, seatLine, clientLine, eol;

            eol = System.getProperty("line.separator");

            dashLine = new String(new char[ticketStringRowLength]).replace('\0', '-');

            showLine = "| Show: " + show;
            for (int i = showLine.length(); i < ticketStringRowLength - 1; ++i) {
                showLine += " ";
            }
            showLine += "|";

            boxLine = "| Box Office ID: " + boxOfficeId;
            for (int i = boxLine.length(); i < ticketStringRowLength - 1; ++i) {
                boxLine += " ";
            }
            boxLine += "|";

            seatLine = "| Seat: " + seat.toString();
            for (int i = seatLine.length(); i < ticketStringRowLength - 1; ++i) {
                seatLine += " ";
            }
            seatLine += "|";

            clientLine = "| Client: " + client;
            for (int i = clientLine.length(); i < ticketStringRowLength - 1; ++i) {
                clientLine += " ";
            }
            clientLine += "|";

            result = dashLine + eol +
                    showLine + eol +
                    boxLine + eol +
                    seatLine + eol +
                    clientLine + eol +
                    dashLine;

            return result;
        }
    }

    public Theater(int numRows, int seatsPerRow, String show) {
        // TODO: Implement this constructor
    	this.numRows = numRows;
    	this.seatsPerRow = seatsPerRow;
    	this.show = show;
    	
    	for(int i=0;i<numRows;i++) {
    		for(int j=1;j<=seatsPerRow;j++) {
    			Seat s = new Seat(i, j);
    			seats.put(s, true);
    		}
    	}
    	
    }

    /**
     * Calculates the best seat not yet reserved
     *
     * @return the best seat or null if theater is full
     */
    public synchronized Seat bestAvailableSeat() {
        // TODO: Implement this method\
    	
    	ArrayList<String> keys = new ArrayList<String>();
    	Set<Entry<Seat,Boolean>> s = this.getSeats().entrySet();
    	Iterator<Entry<Seat,Boolean>> itr = s.iterator();
    	while(itr.hasNext()) {
    		Entry<Seat,Boolean> seat = itr.next();
    		if(seat.getValue()) {
    			keys.add(seat.getKey().toString());
    		}
    	}
    	if(keys.size()==0) {
    		return null;
    	}
    	int smallestLength = keys.get(0).length();
    	for(int i=0;i<keys.size();i++) {
    		if(keys.get(i).length()<smallestLength) {
    			smallestLength = keys.get(i).length();
    		}
    	}
    	for(int i=0;i<keys.size();i++) {
    		if(keys.get(i).length()!=smallestLength) {
    			keys.remove(i);
    			i--;
    		}
    	}
    	
    	char smallest = '@';
    	int index = 0;
    	while(keys.size()>1) {
	    	for(int i=0;i<keys.size();i++) {
	    		if(smallest=='@') {
	    			smallest = keys.get(i).charAt(index);
	    		}else if(smallest>keys.get(i).charAt(index)) {
	    			smallest = keys.get(i).charAt(index);
	    		}
	    	}
	    	for(int i=0;i<keys.size();i++) {
	    		if(keys.get(i).charAt(index)!=smallest) {
	    			keys.remove(i);
	    			i--;
	    		}
	    	}
	    	smallest = '@';
	    	index++;
    	}
    	if(keys.size()>0) {
    		Seat best = new Seat(-1,-1);
    		Iterator<Entry<Seat,Boolean>> it = s.iterator();
        	while(it.hasNext()) {
        		Entry<Seat,Boolean> seat = it.next();
        		if(seat.getValue()) {
        			if(seat.getKey().toString().equals(keys.get(0))) {
        				best = seat.getKey();
        				if(best.toString().equals("AA1")) {
        					int a = 0;
        				}
        			}
        		}
        	}
    		seats.put(best, false);
    		//currentClient++;
    		return best;
    	}else {
    		return null;
    	}
    	
    	
    	
    	
        
    }

    /**
     * Prints a ticket for the client after they reserve a seat
     * Also prints the ticket to the console
     *
     * @param seat a particular seat in the theater
     * @return a ticket or null if a box office failed to reserve the seat
     */
    public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
        // TODO: Implement this method
    	if(boxOfficeId==null) {
    		return null;
    	}
    	Ticket t = new Ticket(this.show, boxOfficeId, seat, client);
    	System.out.println(t.toString());
    	
    	try {
    		Thread.sleep(printDelay);
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
        return t;
    }

    /**
     * Lists all tickets sold for this theater in order of purchase
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        // TODO: Implement this method
        return ticketList;
    }
}
