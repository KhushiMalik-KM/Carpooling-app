package dev;

public class Ride {
	int ride_id;
	String source;
	String destination;
	int seats;
	double fare;
	int available_seats;
	int total_seats;
	User user;
	public Ride(int ride_id, String source, String destination, int seats, double fare, User user) {
		this.ride_id = ride_id;
		this.source = source;
		this.destination = destination;
		this.seats = seats;
		this.fare = fare;
		this.available_seats = seats;
		this.total_seats = seats;
	}
	@Override
	public String toString() {
		return "Ride [ride_id=" + ride_id + ", source=" + source + ", destination=" + destination + ", seats=" + seats + ", fare="
				+ fare + ", available_seats=" + available_seats + ", total_seats=" + total_seats + ", user=" + user + "]";
	}
	
	
}