import java.util.ArrayList;
import java.util.List;
public class RideBookingSystem {
    List<User> users = new ArrayList<>();
    List<Ride> rides=new ArrayList<>();
    List<Booking> bookings= new ArrayList<>();

    public void Signup (User user) {
        for(User user1:users) {
            if(user1.user_id==user.user_id) {
                System.out.println("User id already exists");
                return;
            }
        }
        users.add(user);
        System.out.println("User has successfully signedin! ");
    }
    public User Login(String email,String password) {
        for(User user :users) {
            if(user.email.equals(email)&&user.password.equals(password)) {
                System.out.println("Successfully loged in! ");
                return user;
            }
        }
        System.out.println("Invalid email or password! ");
        return null;
    }
    public List<Ride> showAllRides() {
        return rides;
    }
    public void createRide(int ride_id,String source, String destination,int seats,double fare,User user){
        Ride ride =new Ride( ride_id, source,destination,seats, fare,user );
        for(Ride ride1:rides) {
            if (ride1.ride_id==ride_id) {
                System.out.println("Ride already exists!");
                return;
            }
        }
        rides.add(ride);
        System.out.println("Ride has been successfully created!! ");
    }
    public List<Ride> viewCreatedRides(User user){
        List<Ride> result= new ArrayList<>();
        for (Ride ride:rides) {
            if(ride.user.user_id==user.user_id) {
                result.add(ride);
            }
        }
        if(result.isEmpty()) {
            System.out.println("No rides found");
            return null;
        }
        return result;
    }
    public void bookRide(int booking_id, Ride ride, User user, int seats_booked){
        for(Ride ride1:rides) {
            if (ride.ride_id==ride1.ride_id) {
                if(ride1.available_seats>=seats_booked) {
                    Booking booking=new Booking(booking_id, ride,user,  seats_booked);
                    booking.total_fare=seats_booked*ride1.fare;
                    ride1.available_seats-= seats_booked;
                    bookings.add(booking);
                    System.out.println("The ride has been booked and the total fare is "+booking.total_fare);
                }
                else {
                    System.out.println("The desired number of seats are not available");
                }
                return;  // FIX: Return should be here, after processing the matching ride
            }
        }
        System.out.println("The ride is not available");
    }
    public List<Booking> viewBookedRides(User user){
        List<Booking> result= new ArrayList<>();
        for(Booking booking:bookings) {
            if(booking.user.user_id==user.user_id) {
                result.add(booking);
            }
        }
        if(result.isEmpty()) {
            System.out.println("User not found");
            return null;
        }
        return result;

    }
    public void updateBooking(int booking_id, int new_seats) {
        for(Booking booking:bookings ) {
            if(booking.booking_id==booking_id) {
                booking.ride.available_seats+=booking.seats_booked;
                if(booking.ride.available_seats>=new_seats) {
                    booking.ride.available_seats-=new_seats;
                    System.out.println("Booking updated! ");
                }
                else {
                    System.out.println("Not enough seats available to updtae!");
                }
                return;
            }
        }
        System.out.println("Booking not found!");
    }
    public void deleteBooking(int booking_id) {
        for(Booking booking:bookings) {
            if(booking.booking_id==booking_id) {
                booking.ride.available_seats+=booking.seats_booked;
                bookings.remove(booking);
                System.out.println("Booking deleted");
                return;
            }
        }
        System.out.println("Booking not found! ");
    }
}
//future Scope
//time
//authentication
//authorization
//admin
//Mul location
//price bargain
//ai chatbot support
//reviews
//chat
