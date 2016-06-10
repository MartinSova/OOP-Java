package beanbags;
import java.io.Serializable;


/**
 * Write a description of class BeanBag here.
 * 
 * @authors 650020356, 650046063
 * @version 1.0
 */
public class BeanBag implements Serializable
{
    //instance variables
    private int num; //number of that kind of BeanBag in stock
    private String manufacturer;
    private String name;
    private String id;
    private short year;
    private byte month;
    private String information;
    private int currentPriceInPence; //max possible price £655.35
    private int totalNumberSold;
    private int totalRevenueFromBag;
    private int totalReserved;
    
    public BeanBag(int num, String manufacturer, String name, String id, short year, byte month) throws
    InvalidMonthException{
        if (month < 1 || month > 12){
            throw new InvalidMonthException("Month entered is not valid.");
        }
        
        this.num = num;
        this.manufacturer = manufacturer;
        this.name = name;
        this.id = id;
        this.year = year;
        this.month = month;
        this.information = "";
        this.totalNumberSold = 0;
        this.totalRevenueFromBag = 0;
        this.totalReserved = 0;
    }
    
    public BeanBag(int num, String manufacturer, String name, String id, short year, byte month, String information)
    throws InvalidMonthException{
        if (month < 1 || month > 12){
            throw new InvalidMonthException("Month entered is not valid.");
        }
        
        this.num = num;
        this.manufacturer = manufacturer;
        this.name = name;
        this.id = id;
        this.year = year;
        this.month = month;
        this.information = information;
        this.totalNumberSold = 0;
        this.totalRevenueFromBag = 0;
        this.totalReserved = 0;
    }
   
    public void increaseNumber(int num){
        //to be used when BeanBags are added to stock
        this.num += num;
    }
    
    public void decreaseNumber(int num){
        //to be used when BeanBags are sold
        this.num -= num;
    }
    
    public void setPrice(int priceInPence){
        this.currentPriceInPence = priceInPence;
    }
    
    public String getID(){
        return this.id;
    }
    
    public int getNum(){
        return this.num;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getManufacturer(){
        return this.manufacturer;
    }
    
    public byte getMonth(){
        return this.month;
    }
    
    public short getYear(){
        return this.year;
    }
    
    public String getInfo(){
        return this.information;
    }
    
    public int getCurrentPrice(){
        return this.currentPriceInPence;
    }
    
    public int getNumberSold(){
        return this.totalNumberSold;
    }
    
    public int getTotalRevenue(){
        return this.totalRevenueFromBag; 
    }
    
    public void increaseReservationCount(int numReserved){
        this.totalReserved += numReserved;
    }
    
    public void decreaseReservationCount(int numUnreserved){
        this.totalReserved -= numUnreserved;
    }
    
    public int getTotalReserved(){
        return this.totalReserved;
    }
    
    public void increaseNumberSold(int numSold){
        totalNumberSold +=numSold;
    }
    
    public void addRevenue(int numSold){
        totalRevenueFromBag += (numSold * currentPriceInPence);
    }
    
    public void addRevenue(int numSold, int reservationPrice){
        totalRevenueFromBag += (numSold * reservationPrice);
    }
    
    public void changeID(String newID){
        this.id = newID;
    }
    
    public boolean equals(Object obj){
        //just uses the attributes that are relevant for determining equality
        if (obj instanceof BeanBag)
            {
             if (id.equals(((BeanBag) obj).id) &&
                 name.equals(((BeanBag) obj).name) &&
                 manufacturer.equals((((BeanBag) obj).manufacturer)) &&
                 information.equals(((BeanBag) obj).information))
                 return true;
            }
            /**else if(name.equals(((BeanBag) obj).name) &&
                 manufacturer.equals((((BeanBag) obj).manufacturer)) &&
                 information.equals(((BeanBag) obj).information) &&
                 !(id.equals(((BeanBag) obj).id)))
                 throw new BeanBagMismatchException("Two Beanbags with similar information have different IDs!");*/
        return false;
    }
}
