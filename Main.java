package dev;

public class Main {
	public static void main(String[] args) {
		RideBookingSystem system=new RideBookingSystem();

		User u1=new User("k@mail","khushi","pass",01);
		User u2=new User("t@mail","tulsi","pass1",07);
		User u3=new User("m@mail","manishka","pass2",67);
		
		system.Signup(u1);
		system.Signup(u2);
		system.Signup(u3);
		
		User loggedInUser = system.Login("k@mail", "pass");

        if (loggedInUser != null) {
        	Ride ride1=new Ride(1, "Delhi", "Greater Noida", 3, 250, u1);
            system.createRide(1, "Delhi", "Greater Noida", 3, 250, u1);
            system.createRide(2, "Jaipur", "Ajmer", 4, 420, u2);
            Ride ride2=new Ride(2, "Jaipur", "Ajmer", 4, 420, u2);
            System.out.println(system.showAllRides());

            system.bookRide(101, ride1, loggedInUser, 2);

            System.out.println("Rides created by Khushi: " + system.viewCreatedRides(u1));
            System.out.println("Rides booked by Aman: " + system.viewBookedRides(loggedInUser));

            system.updateBooking(101, 3);
            system.deleteBooking(101);
        }
	}

}
