public class Main {
    public static void main(String[] args) {
        RideBookingSystemDB system = new RideBookingSystemDB();

        // Create users
        User u1 = new User("k@mail", "khushi", "pass", 1);
        User u2 = new User("t@mail", "tulsi", "pass1", 7);
        User u3 = new User("m@mail", "manishka", "pass2", 67);

        // Sign up users
        system.Signup(u1);
        system.Signup(u2);
        system.Signup(u3);

        // Login
        User loggedInUser = system.Login("k@mail", "pass");

        if (loggedInUser != null) {
            // Create rides
            system.createRide(1, "Delhi", "Greater Noida", 3, 250, u1);
            system.createRide(2, "Jaipur", "Ajmer", 4, 420, u2);

            // Show all rides
            System.out.println("\n=== All Available Rides ===");
            System.out.println(system.showAllRides());

            // Book a ride (use ride_id instead of Ride object)
            system.bookRide(101, 1, loggedInUser, 2);

            // View created rides
            System.out.println("\n=== Rides created by Khushi ===");
            System.out.println(system.viewCreatedRides(u1));

            // View booked rides
            System.out.println("\n=== Rides booked by Khushi ===");
            System.out.println(system.viewBookedRides(loggedInUser));

            // Update booking
            System.out.println("\n=== Updating booking ===");
            system.updateBooking(101, 3);

            // Delete booking
            System.out.println("\n=== Deleting booking ===");
            system.deleteBooking(101);
        }
    }
}
            system.updateBooking(101, 3);
            system.deleteBooking(101);
        }
	}

}

