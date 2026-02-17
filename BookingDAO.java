import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) for Booking - handles all database operations for bookings
 */
public class BookingDAO {

    /**
     * Add a new booking to the database
     */
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (booking_id, ride_id, user_id, seats_booked, total_fare) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, booking.booking_id);
            pstmt.setInt(2, booking.ride.ride_id);
            pstmt.setInt(3, booking.user.user_id);
            pstmt.setInt(4, booking.seats_booked);
            pstmt.setDouble(5, booking.total_fare);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error adding booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all bookings made by a specific user
     */
    public List<Booking> getBookingsByUser(int user_id) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "r.ride_id, r.source, r.destination, r.total_seats, r.available_seats, r.fare, r.user_id as ride_user_id, " +
                "u.email, u.name, u.password, " +
                "ru.email as ride_user_email, ru.name as ride_user_name, ru.password as ride_user_password " +
                "FROM bookings b " +
                "JOIN rides r ON b.ride_id = r.ride_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "JOIN users ru ON r.user_id = ru.user_id " +
                "WHERE b.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create the user who booked
                User bookingUser = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                // Create the user who created the ride
                User rideOwner = new User(
                        rs.getString("ride_user_email"),
                        rs.getString("ride_user_name"),
                        rs.getString("ride_user_password"),
                        rs.getInt("ride_user_id")
                );

                // Create the ride
                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        rideOwner
                );
                ride.available_seats = rs.getInt("available_seats");

                // Create the booking
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        ride,
                        bookingUser,
                        rs.getInt("seats_booked")
                );

                bookings.add(booking);
            }

        } catch (Exception e) {
            System.err.println("Error getting bookings by user: " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }

    /**
     * Get a specific booking by booking_id
     */
    public Booking getBookingById(int booking_id) {
        String sql = "SELECT b.*, " +
                "r.ride_id, r.source, r.destination, r.total_seats, r.available_seats, r.fare, r.user_id as ride_user_id, " +
                "u.email, u.name, u.password, " +
                "ru.email as ride_user_email, ru.name as ride_user_name, ru.password as ride_user_password " +
                "FROM bookings b " +
                "JOIN rides r ON b.ride_id = r.ride_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "JOIN users ru ON r.user_id = ru.user_id " +
                "WHERE b.booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, booking_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User bookingUser = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                User rideOwner = new User(
                        rs.getString("ride_user_email"),
                        rs.getString("ride_user_name"),
                        rs.getString("ride_user_password"),
                        rs.getInt("ride_user_id")
                );

                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        rideOwner
                );
                ride.available_seats = rs.getInt("available_seats");

                return new Booking(
                        rs.getInt("booking_id"),
                        ride,
                        bookingUser,
                        rs.getInt("seats_booked")
                );
            }

        } catch (Exception e) {
            System.err.println("Error getting booking by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Update a booking (change number of seats)
     */
    public boolean updateBooking(int booking_id, int new_seats, double new_fare) {
        String sql = "UPDATE bookings SET seats_booked = ?, total_fare = ? WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, new_seats);
            pstmt.setDouble(2, new_fare);
            pstmt.setInt(3, booking_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a booking
     */
    public boolean deleteBooking(int booking_id) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, booking_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all bookings (for admin purposes)
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "r.ride_id, r.source, r.destination, r.total_seats, r.available_seats, r.fare, r.user_id as ride_user_id, " +
                "u.email, u.name, u.password, " +
                "ru.email as ride_user_email, ru.name as ride_user_name, ru.password as ride_user_password " +
                "FROM bookings b " +
                "JOIN rides r ON b.ride_id = r.ride_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "JOIN users ru ON r.user_id = ru.user_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User bookingUser = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                User rideOwner = new User(
                        rs.getString("ride_user_email"),
                        rs.getString("ride_user_name"),
                        rs.getString("ride_user_password"),
                        rs.getInt("ride_user_id")
                );

                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        rideOwner
                );
                ride.available_seats = rs.getInt("available_seats");

                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        ride,
                        bookingUser,
                        rs.getInt("seats_booked")
                );

                bookings.add(booking);
            }

        } catch (Exception e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }
}
