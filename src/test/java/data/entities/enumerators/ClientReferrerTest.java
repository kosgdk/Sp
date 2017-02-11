package data.entities.enumerators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.enumerators.ClientReferrer;

import static org.junit.Assert.*;


@RunWith(BlockJUnit4ClassRunner.class)
public class ClientReferrerTest {

    private final String [] names = {"Форум", "Вконтакте", "Прочее"};
    private final ClientReferrer[] clientReferrers = ClientReferrer.values();

    @Test
    public void quantityTest(){
        assertEquals(names.length, clientReferrers.length);
    }

    @Test
    public void getName_ShouldReturnAppropriateName(){
        for (int i = 0; i < clientReferrers.length; i++) {
            assertEquals("i = " + i, names[i], clientReferrers[i].getName());
        }
    }

    @Test
    public void getId_ShouldReturnAppropriateId(){
        for (int i = 0; i < clientReferrers.length; i++) {
            assertEquals("i = " + i, i+1, clientReferrers[i].getId());
        }
    }

    @Test
    public void getById_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < clientReferrers.length; i++) {
            assertEquals("i = " + i, ClientReferrer.getById(i+1), clientReferrers[i]);
        }
    }

    @Test
    public void getByName_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < clientReferrers.length; i++) {
            assertEquals("i = " + i, ClientReferrer.getByName(names[i]), clientReferrers[i]);
        }
    }

}