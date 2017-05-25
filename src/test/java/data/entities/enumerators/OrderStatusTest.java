package data.entities.enumerators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.enumerators.OrderStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(BlockJUnit4ClassRunner.class)
public class OrderStatusTest {

    private final String [] names = {"Не оплачен", "Оплачен", "Комплектуется", "Отправлен", "Прибыл", "Завершён"};
    private final OrderStatus [] orderStatuses = OrderStatus.values();

    @Test
    public void quantityTest(){
        assertEquals(names.length, orderStatuses.length);
    }

    @Test
    public void getName_ShouldReturnAppropriateName(){
        for (int i = 0; i < orderStatuses.length; i++) {
            assertEquals("i = " + i, names[i], orderStatuses[i].getName());
        }
    }

    @Test
    public void getId_ShouldReturnAppropriateId(){
        for (int i = 0; i < orderStatuses.length; i++) {
            assertEquals("i = " + i, i+1, orderStatuses[i].getId());
        }
    }

    @Test
    public void getById_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < orderStatuses.length; i++) {
            assertEquals("i = " + i, OrderStatus.getById(i+1), orderStatuses[i]);
        }
    }

    @Test
    public void getById_NonexistentId_ShouldReturnNull(){
        OrderStatus orderStatus = OrderStatus.getById(20);
        assertNull(orderStatus);
    }

    @Test
    public void getByName_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < orderStatuses.length; i++) {
            assertEquals("i = " + i, OrderStatus.getByName(names[i]), orderStatuses[i]);
        }
    }

    @Test
    public void getByName_NullName_ShouldReturnNull(){
        OrderStatus orderStatus = OrderStatus.getByName(null);
        assertNull(orderStatus);
    }

    @Test
    public void getByName_NonexistentName_ShouldReturnNull(){
        OrderStatus orderStatus = OrderStatus.getByName("InvalidName");
        assertNull(orderStatus);
    }


}