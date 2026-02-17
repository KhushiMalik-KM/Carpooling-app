import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) for Ride - handles all database operations for rides
 */
public class RideDAO {

    /**
     * Add a new ride to the database
     */
    public boolean addRide(Ride ride) {
        String sql = "INSERT INTO rides (ride_id, source, destination, total_seats, available_seats, fare, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ride.ride_id);
            pstmt.setString(2, ride.source);
            pstmt.setString(3, ride.destination);
            pstmt.setInt(4, ride.total_seats);
            pstmt.setInt(5, ride.available_seats);
            pstmt.setDouble(6, ride.fare);
            pstmt.setInt(7, ride.user.user_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error adding ride: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all available rides from database
     */
    public List<Ride> getAllRides() {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT r.*, u.email, u.name, u.password " +
                "FROM rides r JOIN users u ON r.user_id = u.user_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        user
                );
                ride.available_seats = rs.getInt("available_seats");

                rides.add(ride);
            }

        } catch (Exception e) {
            System.err.println("Error getting all rides: " + e.getMessage());
            e.printStackTrace();
        }

        return rides;
    }

    /**
     * Get rides created by a specific user
     */
    public List<Ride> getRidesByUser(int user_id) {
        List<Ride> rides = new ArrayList<>();
        String sql = "SELECT r.*, u.email, u.name, u.password " +
                "FROM rides r JOIN users u ON r.user_id = u.user_id " +
                "WHERE r.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        user
                );
                ride.available_seats = rs.getInt("available_seats");

                rides.add(ride);
            }

        } catch (Exception e) {
            System.err.println("Error getting rides by user: " + e.getMessage());
            e.printStackTrace();
        }

        return rides;
    }

    /**
     * Update available seats for a ride
     */
    public boolean updateAvailableSeats(int ride_id, int available_seats) {
        String sql = "UPDATE rides SET available_seats = ? WHERE ride_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, available_seats);
            pstmt.setInt(2, ride_id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error updating available seats: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get a specific ride by ride_id
     */
    public Ride getRideById(int ride_id) {
        String sql = "SELECT r.*, u.email, u.name, u.password " +
                "FROM rides r JOIN users u ON r.user_id = u.user_id " +
                "WHERE r.ride_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ride_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_id")
                );

                Ride ride = new Ride(
                        rs.getInt("ride_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("total_seats"),
                        rs.getDouble("fare"),
                        user
                );
                ride.available_seats = rs.getInt("available_seats");

                return ride;
            }

        } catch (Exception e) {
            System.err.println("Error getting ride by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if a ride_id already exists
     */
    public boolean rideExists(int ride_id) {
        String sql = "SELECT COUNT(*) FROM rides WHERE ride_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ride_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.err.println("Error checking ride existence: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}