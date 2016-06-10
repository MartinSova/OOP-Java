package beanbags; 
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;

/**
 * Store is one possible implementation of the BeanBagStore interface. It relies on the BeanBag and ReservationEntry class
 * to provide its full functionality. 
 * 
 * @authors 650020356, 650046063
 * @version 1.0
 */
public class Store implements BeanBagStore, Serializable
{
    private ObjectArrayList beanBags = new ObjectArrayList();
    private ObjectArrayList reservedBags = new ObjectArrayList();
    private Integer totalSold = 0;
    private int totalRevenue = 0;
    private int reservationCount = 0;
    
    private void checkID(String id) throws IllegalIDException {
        try{
            if (id.length() != 8){
                throw new IllegalIDException("The ID " + id + " is not a legal ID.");
            }
            Long.parseLong(id, 16); // PARSE LONG??
        }
        catch (NumberFormatException){
            throw new IllegalIDException("The ID " + id + " is not a legal ID.");
        }
    }
    
    public void addBeanBags(int num, String manufacturer, String name, 
    String id, short year, byte month)
    throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
    IllegalIDException, InvalidMonthException {
    
        checkID(id);
        
        if (num < 1){
            throw new IllegalNumberOfBeanBagsAddedException("Number of Beanbags added less than 1!");
        }
        
        BeanBag addedBag = new BeanBag(num, manufacturer, name, id, year, month);
        
        if (beanBags.size() == 0){      //if the stock is empty, just add the new beanbag to the stock
            beanBags.add(addedBag);
        }
        else{                           //if not, iterate over the stock list to find out if there is a beanbag with the same ID in the stock already
            for (int i=0; i < beanBags.size(); i++){            
                if (addedBag.equals(beanBags.get(i))){           //compare addedBag instance with each beanbag instance in stock
                BeanBag matchedBag = (BeanBag) beanBags.get(i); //if bags matched, get corresponding beanbag instance
                matchedBag.increaseNumber(num);                 //to increase its stock count
                beanBags.replace(matchedBag, i);                //replace the matched instance with the updated version
                break;
                }
                else {
                beanBags.add(addedBag);
                break;
                }
            }
        }
    }

    public void addBeanBags(int num, String manufacturer, String name, 
    String id, short year, byte month, String information)
    throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
    IllegalIDException, InvalidMonthException {
        checkID(id);
        
        if (num < 1){
            throw new IllegalNumberOfBeanBagsAddedException("Number of Beanbags added less than 1!");
        }
       
        BeanBag addedBag = new BeanBag(num, manufacturer, name, id, year, month, information);
        
        if (beanBags.size() == 0){  //if the stock is empty, just add the new beanbag to the stock
            beanBags.add(addedBag); 
        }
        else{                       //if not, iterate over the stock list to find out if there is a beanbag with the same ID in the stock already
            for (int i=0; i < beanBags.size(); i++){
                if (addedBag.equals(beanBags.get(i))){           //compare addedBag instance with each beanbag instance in stock 
                BeanBag matchedBag = (BeanBag) beanBags.get(i); //if bags matched, get corresponding beanbag instance
                matchedBag.increaseNumber(num);                 //to increase its stock count
                beanBags.replace(matchedBag, i);                //replace the matched instance with the updated version
                break;
                }
                else {
                beanBags.add(addedBag);                          //if addedBag can't be matched with any stock entry, add new entry to stock
                break;                       
                }
            }
        } 
    }

    public void setBeanBagPrice(String id, int priceInPence) 
    throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException { 
        checkID(id);
        
        if (priceInPence < 1){
                    throw new InvalidPriceException("The price in pence is less than 1!");
                }     
        else if (beanBags.size() == 0){
                    throw new BeanBagIDNotRecognisedException("There are no beanbags in the stock!");
                    //wouldn't it make more sense to have the for loop print something if the stock for a particular beanbag is empty?
                }       
        else{
                for (int i=0; i < beanBags.size(); i++){
                    if (( (BeanBag) beanBags.get(i)).getID().equals(id)){
                        BeanBag matchedBag = (BeanBag) beanBags.get(i);
                        matchedBag.setPrice(priceInPence);
                        beanBags.replace(matchedBag, i);
                        for (int j=0; j < reservedBags.size(); j++){
                            if(matchedBag.getID().equals(((ReservationEntry)reservedBags.get(j)).getBagID()) && priceInPence < (((ReservationEntry)reservedBags.get(j)).getReservationPrice())){
                                ((ReservationEntry)reservedBags.get(j)).setReservationPrice(priceInPence);
                            }
                        }
                        return;
                    }
                }
                //if no beanbag with the ID passed as an argument can be found, throw a BeanBagIDNotRecognisedException
                throw new BeanBagIDNotRecognisedException("There is no Beanbag with the ID " + id + " in stock");
        }
    }

    public void sellBeanBags(int num, String id) throws BeanBagNotInStockException,
    InsufficientStockException, IllegalNumberOfBeanBagsSoldException,
    PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        checkID(id);
        
        if (num<1){
            throw new IllegalNumberOfBeanBagsSoldException("Invalid number of bean bags sold!");    
        }
    
        for (int i=0; i < beanBags.size(); i++){
                if (( (BeanBag) beanBags.get(i)).getID().equals(id)) {
                    BeanBag matchedBag = (BeanBag) beanBags.get(i);
                    if(matchedBag.getNum() < (num + matchedBag.getTotalReserved())){
                        throw new InsufficientStockException("Not enough bean bags! Current available stock of the beanbag with the ID " + matchedBag.getID() + 
                        " is " + (matchedBag.getNum() - matchedBag.getTotalReserved()));
                    }
                    else if(matchedBag.getCurrentPrice() == 0){
                       throw new PriceNotSetException("The price for the bag with the ID " + id + " has not been set yet."); 
                    }
                    else{
                        matchedBag.decreaseNumber(num);
                        matchedBag.increaseNumberSold(num);
                        matchedBag.addRevenue(num);
                        totalRevenue += (num * matchedBag.getCurrentPrice());
                        totalSold += num;
                        beanBags.replace(matchedBag, i);
                        return;
                    }    
        }
        throw new BeanBagIDNotRecognisedException("There is no Beanbag with the ID " + id + " in stock.");
        }
    }
            
    public int reserveBeanBags(int num, String id) throws BeanBagNotInStockException,
    InsufficientStockException, IllegalNumberOfBeanBagsReservedException,
    PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {   
        checkID(id);
        
        if (num < 1){
            throw new IllegalNumberOfBeanBagsReservedException("You can't reserve " + num + " beanbags.");
        }
        
        for (int j=0; j<beanBags.size(); j++){
            if (((BeanBag)beanBags.get(j)).getID().equals(id)){ //find the bean bag with the same id
                if (((BeanBag)beanBags.get(j)).getNum() == 0){
                    throw new BeanBagNotInStockException("The stock of this bag is currently 0.");
                }
                else if (((BeanBag)beanBags.get(j)).getNum() < (((BeanBag)beanBags.get(j)).getTotalReserved() + num)){ //if total number reserved and new reservation is 
                                                                                                //bigger than stock, throw exception
                    throw new InsufficientStockException("The stock of this bag is insufficient to make the reservation.");
                }
                else if (((BeanBag)beanBags.get(j)).getCurrentPrice() == 0){
                    throw new PriceNotSetException("The price for this beanbag hasn't been set yet.");
                }
                int price = ((BeanBag)beanBags.get(j)).getCurrentPrice(); //set the price in the reservation to the price of the bean bag found
                int reservationID = ++reservationCount;
                ReservationEntry newEntry = new ReservationEntry(reservationID, id, price, num);
                reservedBags.add(newEntry);
                ((BeanBag)beanBags.get(j)).increaseReservationCount(num);
                return reservationID;
            }
        }
        
        throw new BeanBagIDNotRecognisedException("There is no beanbag with the ID " + id + " in the stock list.");
    }

    public void unreserveBeanBags(int reservationNumber)
    throws ReservationNumberNotRecognisedException { 
        for (int j=0; j < reservedBags.size(); j++){
            if (( (ReservationEntry) reservedBags.get(j)).getReservationID() == reservationNumber){
                for (int i=0; i < beanBags.size(); i++){
                    if (( (BeanBag) beanBags.get(i)).getID().equals(((ReservationEntry)reservedBags.get(j)).getBagID())){
                        BeanBag matchedBag = (BeanBag) beanBags.get(i);
                        matchedBag.decreaseReservationCount(((ReservationEntry)reservedBags.get(j)).getNumberReserved());
                        beanBags.replace(matchedBag, i);
                        reservedBags.remove(j);
                        return;
                    }
                } 
            }
        }
        
        throw new ReservationNumberNotRecognisedException("The reservation number " + reservationNumber + " cannot be recognised.");
    }

    public void sellBeanBags(int reservationNumber)
    throws ReservationNumberNotRecognisedException { 
        for (int j=0; j < reservedBags.size(); j++){    //iterate over reservation list to find entry with the right reservation ID
            if (( (ReservationEntry) reservedBags.get(j)).getReservationID() == reservationNumber){ //test for reservation ID
               for (int i=0; i < beanBags.size(); i++){     //if reservation has been found, go trough the stock list
                   if (( (BeanBag) beanBags.get(i)).getID().equals(( (ReservationEntry) reservedBags.get(j)).getBagID())) { //find stock entry with reservation's bag ID
                       BeanBag matchedBag = (BeanBag) beanBags.get(i); //get the matching bag from the stock to work on it
                       
                       matchedBag.decreaseNumber(( (ReservationEntry) reservedBags.get(j)).getNumberReserved());
                       matchedBag.increaseNumberSold(( (ReservationEntry) reservedBags.get(j)).getNumberReserved());
                       matchedBag.addRevenue(( (ReservationEntry) reservedBags.get(j)).getNumberReserved(), ( (ReservationEntry) reservedBags.get(j)).getReservationPrice());
                       matchedBag.decreaseReservationCount(((ReservationEntry)reservedBags.get(j)).getNumberReserved());
                       
                       totalRevenue += (( (ReservationEntry) reservedBags.get(j)).getNumberReserved() * ( (ReservationEntry) reservedBags.get(j)).getReservationPrice());
                       totalSold += ( (ReservationEntry) reservedBags.get(j)).getNumberReserved();
                       
                       beanBags.replace(matchedBag, i); //"put the bag back to the stock"
                       reservedBags.remove(j); //remove the reservation entry
                       
                       return;
                    }
               }
            } 
        }
        
        throw new ReservationNumberNotRecognisedException("The reservation ID " + reservationNumber + " cannot be recognised.");
    }
    
    public int beanBagsInStock() {
        int stockCount = 0;
        
        for (int i=0; i < beanBags.size(); i++){
            stockCount += ((BeanBag)beanBags.get(i)).getNum();
        }
        
        return stockCount; }

    public int reservedBeanBagsInStock() { 
        int reservedCount = 0;
        
        for (int i=0; i < beanBags.size(); i++){
            reservedCount += ((BeanBag)beanBags.get(i)).getTotalReserved();
        }
        
        return reservedCount; }

    public int beanBagsInStock(String id) throws BeanBagIDNotRecognisedException,
    IllegalIDException { 
        checkID(id);
        
        for (int i=0; i < beanBags.size(); i++){     //if reservation has been found, go trough the stock list
                   if (( (BeanBag) beanBags.get(i)).getID().equals(id)){
                       return ((BeanBag) beanBags.get(i)).getNum();
                   }
        }
        
        throw new BeanBagIDNotRecognisedException("The ID " + id + " cannot be recognised.");
    }
    
    public void saveStoreContents(String filename) throws IOException {
        
        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;
        try{
            fileOutput = new FileOutputStream(filename);
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(this);
            objectOutput.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        
    }

    public void loadStoreContents(String filename) throws IOException,
    ClassNotFoundException { 
        System.out.print("I've made it this far");
        Store loadedStore = null;
        FileInputStream fileInput = null;
        ObjectInputStream objectInput = null;
        try{
            fileInput = new FileInputStream(filename);
            objectInput = new ObjectInputStream(fileInput);
            loadedStore = (Store) objectInput.readObject();
            System.out.print("I've made it this far");
            
            objectInput.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        
        
    }

    public int getNumberOfDifferentBeanBagsInStock() { 
        return beanBags.size();
    }

    public int getNumberOfSoldBeanBags() { 
        return totalSold; 
    }

    public int getNumberOfSoldBeanBags(String id) throws
    BeanBagIDNotRecognisedException, IllegalIDException { 
    
        checkID(id);
        
        for (int i=0; i < beanBags.size(); i++){     //if reservation has been found, go trough the stock list
                   if (( (BeanBag) beanBags.get(i)).getID().equals(id)){
                       return ((BeanBag) beanBags.get(i)).getNumberSold();
                   }
        }
        
        throw new BeanBagIDNotRecognisedException("The ID " + id + " cannot be recognised.");
    }

    public int getTotalPriceOfSoldBeanBags() { 
        return totalRevenue; 
    }

    public int getTotalPriceOfSoldBeabBags(String id) throws
    BeanBagIDNotRecognisedException, IllegalIDException { 
        checkID(id);
        
            for (int i=0; i < beanBags.size(); i++){     //if reservation has been found, go trough the stock list
                   if (( (BeanBag) beanBags.get(i)).getID().equals(id)){
                       return ((BeanBag) beanBags.get(i)).getTotalRevenue();
                   }
            }
        
        throw new BeanBagIDNotRecognisedException("The ID " + id + " cannot be recognised.");
    }

    public int getTotalPriceOfReservedBeanBags() { 
        int valueOfReservations = 0;
        
        for (int j=0; j<reservedBags.size(); j++){
            valueOfReservations += ((ReservationEntry)reservedBags.get(j)).getReservationPrice() * ((ReservationEntry)reservedBags.get(j)).getNumberReserved();
        }
       
        return valueOfReservations; }

    public String getBeanBagDetails(String id) throws
    BeanBagIDNotRecognisedException, IllegalIDException { 
        checkID(id);
        
        for (int i=0; i < beanBags.size(); i++){
                    if (( (BeanBag) beanBags.get(i)).getID().equals(id)){
                        BeanBag matchedBag = (BeanBag) beanBags.get(i);
                        
                        //do we have to return all details or just the information from the text field? 
                        String beanBagDetails = new String(); //instantiate a new string to hold information
                        beanBagDetails = "The bag's details are: " + matchedBag.getInfo();
                        return beanBagDetails;
                    }
        }
        
        throw new BeanBagIDNotRecognisedException("The ID " + id + " cannot be recognised.");
    }

    public void empty() { 
        totalRevenue = 0;
        totalSold = 0;
        reservationCount = 0;
        beanBags = new ObjectArrayList();
        reservedBags = new ObjectArrayList();
    }
     
    public void resetSaleAndCostTracking() {
        totalRevenue = 0;
        totalSold = 0;
    }
     
    public void replace(String oldId, String replacementId) 
    throws BeanBagIDNotRecognisedException, IllegalIDException { 
    
        int changeCount = 0;
        
        checkID(oldId);
        checkID(replacementId);
        
        for (int i=0; i < beanBags.size(); i++){
               if (( (BeanBag) beanBags.get(i)).getID().equals(oldId)){
                        BeanBag matchedBag = (BeanBag) beanBags.get(i);
                        matchedBag.changeID(replacementId);
                        beanBags.replace(matchedBag, i);
                        
                        for (int j=0; j < reservedBags.size(); j++){
                            if(( (ReservationEntry) reservedBags.get(j)).getBagID().equals(oldId)){
                                ((ReservationEntry) reservedBags.get(j)).setBagID(replacementId);
                            }
                        }
                        return;
               }
        }
        
        throw new BeanBagIDNotRecognisedException("The ID " + oldId + " cannot be recognised.");
    }
}
