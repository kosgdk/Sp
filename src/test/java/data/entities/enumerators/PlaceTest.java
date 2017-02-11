package data.entities.enumerators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.enumerators.Place;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class PlaceTest {

    private final String [] names = {"Юмашева", "Океан"};
    private final Place[] places = Place.values();

    @Test
    public void quantityTest(){
        assertEquals(names.length, places.length);
    }

    @Test
    public void getName_ShouldReturnAppropriateName(){
        for (int i = 0; i < places.length; i++) {
            assertEquals("i = " + i, names[i], places[i].getName());
        }
    }

    @Test
    public void getId_ShouldReturnAppropriateId(){
        for (int i = 0; i < places.length; i++) {
            assertEquals("i = " + i, i+1, places[i].getId());
        }
    }

    @Test
    public void getById_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < places.length; i++) {
            assertEquals("i = " + i, Place.getById(i+1), places[i]);
        }
    }

    @Test
    public void getByName_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < places.length; i++) {
            assertEquals("i = " + i, Place.getByName(names[i]), places[i]);
        }
    }

}