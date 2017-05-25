package data.entities.enumerators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.enumerators.SpStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(BlockJUnit4ClassRunner.class)
public class SpStatusTest {

    private final String [] names = {"Сбор", "Оплата", "На комплектации", "Оплачен", "Отправлен", "Прибыл", "Раздаётся", "Завершён"};
    private final SpStatus[] spStatuses = SpStatus.values();

    @Test
    public void quantityTest(){
        assertEquals(names.length, spStatuses.length);
    }

    @Test
    public void getName_ShouldReturnAppropriateName(){
        for (int i = 0; i < spStatuses.length; i++) {
            assertEquals("i = " + i, names[i], spStatuses[i].getName());
        }
    }

    @Test
    public void getId_ShouldReturnAppropriateId(){
        for (int i = 0; i < spStatuses.length; i++) {
            assertEquals("i = " + i, i+1, spStatuses[i].getId());
        }
    }

    @Test
    public void getById_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < spStatuses.length; i++) {
            assertEquals("i = " + i, SpStatus.getById(i+1), spStatuses[i]);
        }
    }

    @Test
    public void getById_NonexistentId_ShouldReturnNull(){
        SpStatus spStatus = SpStatus.getById(20);
        assertNull(spStatus);
    }

    @Test
    public void getByName_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < spStatuses.length; i++) {
            assertEquals("i = " + i, SpStatus.getByName(names[i]), spStatuses[i]);
        }
    }

    @Test
    public void getByName_NullName_ShouldReturnNull(){
        SpStatus spStatus = SpStatus.getByName(null);
        assertNull(spStatus);
    }

    @Test
    public void getByName_NonexistentName_ShouldReturnNull(){
        SpStatus spStatus = SpStatus.getByName("InvalidName");
        assertNull(spStatus);
    }
}