
package assignment6;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import assignment6.Theater.Seat;
import assignment6.Theater.Ticket;

public class BoxOffice implements Runnable{

	
	Theater t;
	String name;
	int clientNum;
	
	
	public BoxOffice(Theater t, String name, int clientNum) {
		
		this.t = t;
		this.name = name;
		this.clientNum = clientNum;
		
	}
	
	@Override
	public void run() {
		
		while(t.getTR() && (clientNum>0)) {
			//System.out.println(name);
			try {
				
				clientNum--;
				Theater.Seat s = new Seat(-1, -1);
				int cc = -1;
				synchronized(t) {
					//System.out.println(Thread.currentThread().getName() + " is in block");
					s = t.bestAvailableSeat();
					if(s!=null) {
						cc = t.getCurrentClient();
						cc++;
						t.setCurrentClient(cc);
					}
				}
				//System.out.println(Thread.currentThread().getName() + " out");
				if(s!=null) {
					
					
				
					Ticket tick = t.printTicket(name, s, cc);
					t.getTickets().add(tick);
					
				
					
					
					
					/*
					Iterator it = t.getSeats().entrySet().iterator();
					int lowestSum = -1;
					Seat seat = new Seat(0,0);
					while(it.hasNext()) {
						Map.Entry<Seat, Boolean> entry = (Entry<Seat, Boolean>) it.next();
						if(entry.getValue()==true) {
							String ascii = entry.getKey().toString();
							int currentSum = 0;
							for(int i=0;i<ascii.length();i++) {
								currentSum += ascii.charAt(i);
							}
							if(lowestSum==-1 || currentSum<lowestSum) {
								lowestSum = currentSum;
								seat = entry.getKey();
							}
						}
					}
					if(lowestSum==-1) {
						
					}
					*/
				}else {
					synchronized(t) {
						if(t.getPrinted()) {
							System.out.println("Sorry, we are sold out!");
							t.setPrinted(false);
						}
						t.setTR(false);
					}
				}
				
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		synchronized(t) {
			if(t.getPrinted()) {
				System.out.println("Sorry, we are sold out!");
				t.setPrinted(false);
			}
		}
		
		
	}


	
	
}
