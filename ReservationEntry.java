package beanbags;
import java.io.Serializable;


/**
 * Write a description of class PriceEntry here.
 * 
 * @authors 650020356, 650046063 
 * @version 1.0
 */
public class ReservationEntry implements Serializable
{ 
    private int reservationID;
    private String bagID;
    private int numberReserved;
    private int reservationPrice;
    
    public ReservationEntry(int reservationID, String bagID, int reservationPrice, int numberReserved){
        this.reservationID = reservationID;
        this.bagID = bagID;
        this.numberReserved = numberReserved;
        this.reservationPrice = reservationPrice;
    }
    
    public String getBagID(){
        return this.bagID;
    }
    
    public void setBagID(String newID){
        this.bagID = newID;
    }
    
    public int getReservationPrice(){
        return this.reservationPrice;
    }
    
    public void setReservationPrice(int newPrice){
        reservationPrice = newPrice;
    }
    
    public int getReservationID(){
        return this.reservationID;
    }
    
    public int getNumberReserved(){
        return this.numberReserved;
    }
}
