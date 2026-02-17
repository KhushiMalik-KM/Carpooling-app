import java.util.List;

/**
 * Main business logic class for the Ride Booking System
 * Now uses DAO classes to persist data in PostgreSQL database
 */
public class RideBookingSystemDB {

    // DAO objects to interact with database
    private UserDAO userDAO;
    private RideDAO rideDAO;
    private BookingDAO bookingDAO;

    public RideBookingSystemDB() {
        this.userDAO = new UserDAO();
        this.rideDAO = new RideDAO();
        this.bookingDAO = new BookingDAO();
    }

    /**
     * Sign up a new user
     */
    public void Signup(User user) {
        // Check if user already exists
        if (userDAO.userExists(user.user_id)) {
            System.out.println("User id already exists");
            return;
        }

        // Add user to database
        if (userDAO.addUser(user)) {
            System.out.println("User has successfully signed up!");
        } else {
            System.out.println("Error signing up user");
        }
    }

    /**
     * Login a user
     */
    public User Login(String email, String password) {
        User user = userDAO.getUserByEmailAndPassword(email, password);

        if (user != null) {
            System.out.println("Successfully logged in!");
            return user;
        } else {
            System.out.println("Invalid email or password!");
            return null;
        }
    }

    /**
     * Show all available rides
     */
    public List<Ride> showAllRides() {
        return rideDAO.getAllRides();
    }

    /**
     * Create a new ride
     */
    public void createRide(int ride_id, String source, String destination, int seats, double fare, User user) {
        // Check if ride already exists
        if (rideDAO.rideExists(ride_id)) {
            System.out.println("Ride already exists!");
            return;
        }

        // Create new ride object
        Ride ride = new Ride(ride_id, source, destination, seats, fare, user);

        // Add to database
        if (rideDAO.addRide(ride)) {
            System.out.println("Ride has been successfully created!!");
        } else {
            System.out.println("Error creating ride");
        }
    }

    /**
     * View all rides created by a specific user
     */
    public List<Ride> viewCreatedRides(User user) {
        List<Ride> rides = rideDAO.getRidesByUser(user.user_id);

        if (rides.isEmpty()) {
            System.out.println("No rides found");
            return null;
        }

        return rides;
    }

    /**
     * Book a ride
     */
    public void bookRide(int booking_id, int ride_id, User user, int seats_booked) {
        // Get the ride from database
        Ride ride = rideDAO.getRideById(ride_id);

        if (ride == null) {
            System.out.println("The ride is not available");
            return;
        }

        // Check if enough seats are available
        if (ride.available_seats >= seats_booked) {
            // Create booking
            Booking booking = new Booking(booking_id, ride, user, seats_booked);

            // Add booking to database
            if (bookingDAO.addBooking(booking)) {
                // Update available seats in the ride
                ride.available_seats -= seats_booked;
                rideDAO.updateAvailableSeats(ride_id, ride.available_seats);

                System.out.println("The ride has been booked and the total fare is " + booking.total_fare);
            } else {
                System.out.println("Error booking ride");
            }
        } else {
            System.out.println("The desired number of seats are not available");
        }
    }

    /**
     * View all bookings made by a user
     */
    public List<Booking> viewBookedRides(User user) {
        List<Booking> bookings = bookingDAO.getBookingsByUser(user.user_id);

        if (bookings.isEmpty()) {
            System.out.println("No bookings found");
            return null;
        }

        return bookings;
    }

    /**
     * Update a booking (change number of seats)
     */
    public void updateBooking(int booking_id, int new_seats) {
        // Get the booking from database
        Booking booking = bookingDAO.getBookingById(booking_id);

        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }

        // Get the associated ride
        Ride ride = rideDAO.getRideById(booking.ride.ride_id);

        if (ride == null) {
            System.out.println("Ride not found!");
            return;
        }

        // Calculate the difference in seats
        int seat_difference = new_seats - booking.seats_booked;

        // Check if we have enough seats for the increase
        if (seat_difference > 0 && ride.available_seats < seat_difference) {
            System.out.println("Not enough seats available to update!");
            return;
        }

        // Update available seats
        ride.available_seats -= seat_difference;
        rideDAO.updateAvailableSeats(ride.ride_id, ride.available_seats);

        // Update booking
        double new_fare = new_seats * ride.fare;
        if (bookingDAO.updateBooking(booking_id, new_seats, new_fare)) {
            System.out.println("Booking updated!");
        } else {
            System.out.println("Error updating booking");
        }
    }

    /**
     * Delete a booking
     */
    public void deleteBooking(int booking_id) {
        // Get the booking from database
        Booking booking = bookingDAO.getBookingById(booking_id);

        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }

        // Get the associated ride
        Ride ride = rideDAO.getRideById(booking.ride.ride_id);

        if (ride != null) {
            // Return the seats back to available pool
            ride.available_seats += booking.seats_booked;
            rideDAO.updateAvailableSeats(ride.ride_id, ride.available_seats);
        }

        // Delete the booking
        if (bookingDAO.deleteBooking(booking_id)) {
            System.out.println("Booking deleted");
        } else {
            System.out.println("Error deleting booking");
        }
    }
}
