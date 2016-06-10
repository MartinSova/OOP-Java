package beanbags;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class TestApp
{
    public static void main(String[] args) throws IllegalNumberOfBeanBagsAddedException,BeanBagMismatchException,
    IllegalIDException, InvalidMonthException, InvalidPriceException, InsufficientStockException,
    IllegalNumberOfBeanBagsSoldException, BeanBagNotInStockException, BeanBagIDNotRecognisedException,
    ReservationNumberNotRecognisedException, IllegalNumberOfBeanBagsReservedException, PriceNotSetException,
    IOException, ClassNotFoundException {
    
     Store firstStore = new Store();
     
     firstStore.addBeanBags(10, "manufact1", "bag1", "aaaaaaaa", (short) 2016, (byte) 3);
     firstStore.setBeanBagPrice("aaaaaaaa", 100);
     
     firstStore.addBeanBags(10, "manufact2", "bag2", "bbbbbbbb", (short) 2016, (byte) 3);
     firstStore.setBeanBagPrice("bbbbbbbb", 200);
     
     firstStore.addBeanBags(10, "manufact3", "bag3", "cccccccc", (short) 2016, (byte) 3);
     firstStore.setBeanBagPrice("cccccccc", 300);
        
     firstStore.reserveBeanBags(5, "aaaaaaaa");
     firstStore.reserveBeanBags(3, "bbbbbbbb");
     
     firstStore.saveStoreContents("firstStore.ser");
     
     firstStore.loadStoreContents("firstStore.ser");
     
     System.out.println(firstStore.getNumberOfDifferentBeanBagsInStock());
    }
}
