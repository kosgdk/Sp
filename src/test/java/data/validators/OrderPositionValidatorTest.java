package data.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.Errors;
import sp.data.entities.*;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.PropertiesService;
import sp.data.validators.OrderPositionValidator;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class OrderPositionValidatorTest extends AbstractJUnit4SpringContextTests {

	@Mock OrderPositionService orderPositionService;
	@Mock OrderPosition changedOrderPosition;
	@Mock OrderPosition persistedOrderPosition;
	@Mock Errors errors;

	@InjectMocks
	@Resource(name = "OrderPositionValidator")
	OrderPositionValidator orderPositionValidator;

	@Before
	public void setUp() throws Exception {
		TestContextManager testContextManager = new TestContextManager(getClass());
		testContextManager.prepareTestInstance(this);
		MockitoAnnotations.initMocks(this);
	}

	@RunWith(BlockJUnit4ClassRunner.class)
	public static class OtherTests extends OrderPositionValidatorTest{

		@Spy
		@InjectMocks
		@Resource(name = "OrderPositionValidator")
		OrderPositionValidator orderPositionValidator;

		@Test
		public void validate()  {
			doNothing().when(orderPositionValidator).validatePriceOrdered(changedOrderPosition, persistedOrderPosition, errors);
			doNothing().when(orderPositionValidator).validatePriceVendor(changedOrderPosition, persistedOrderPosition, errors);
			doNothing().when(orderPositionValidator).validatePriceSp(changedOrderPosition, persistedOrderPosition, errors);
			Long id = 1L;
			when(changedOrderPosition.getId()).thenReturn(id);
			when(orderPositionService.getById(id)).thenReturn(persistedOrderPosition);
			orderPositionValidator.validate(changedOrderPosition, errors);

			verify(orderPositionService, times(1)).getById(id);
			verify(orderPositionValidator, times(1)).validatePriceOrdered(changedOrderPosition, persistedOrderPosition, errors);
			verify(orderPositionValidator, times(1)).validatePriceVendor(changedOrderPosition, persistedOrderPosition, errors);
			verify(orderPositionValidator, times(1)).validatePriceSp(changedOrderPosition, persistedOrderPosition, errors);
		}

		@Test
		public void supports_UnsupportedClass_ShouldReturnFalse() throws Exception {
			assertFalse(orderPositionValidator.supports(Product.class));
		}

		@Test
		public void supports_null_ShouldReturnFalse() throws Exception {
			assertFalse(orderPositionValidator.supports(null));
		}

		@Test
		public void supports_SupportedClass_ShouldReturnTrue() throws Exception {
			assertTrue(orderPositionValidator.supports(OrderPosition.class));
		}
	}

	@RunWith(BlockJUnit4ClassRunner.class)
	public static class ValidatePriceOrderedTests extends OrderPositionValidatorTest {

		@Mock Product product;

		@Before
		public void setUp() throws Exception {
			super.setUp();
			when(changedOrderPosition.getProduct()).thenReturn(product);
		}

		@Test
		public void validatePriceOrdered_newPriceOrderedDoesNotChanged_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceOrdered()).thenReturn(BigDecimal.ONE);
			when(persistedOrderPosition.getPriceOrdered()).thenReturn(BigDecimal.ONE);

			orderPositionValidator.validatePriceOrdered(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceOrdered", "orderPosition.priceOrdered.InvalidValue");
		}

		@Test
		public void validatePriceOrdered_newPriceOrderedHasChanged_AndEqualsToProductPrice_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(2));
			when(persistedOrderPosition.getPriceOrdered()).thenReturn(BigDecimal.ONE);
			when(product.getPrice()).thenReturn(new BigDecimal(2));

			orderPositionValidator.validatePriceOrdered(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceOrdered", "orderPosition.priceOrdered.InvalidValue");
		}

		@Test
		public void validatePriceOrdered_newPriceOrderedHasChanged_AndNotEqualsToProductPrice_ShouldCauseErrors() {
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(2));
			when(persistedOrderPosition.getPriceOrdered()).thenReturn(BigDecimal.ONE);
			when(product.getPrice()).thenReturn(new BigDecimal(3));

			orderPositionValidator.validatePriceOrdered(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, times(1)).rejectValue("priceOrdered", "orderPosition.priceOrdered.InvalidValue");
		}

	}

	@RunWith(BlockJUnit4ClassRunner.class)
	public static class ValidatePriceVendorTests extends OrderPositionValidatorTest {

		@Mock PropertiesService propertiesService;
		@Mock Properties properties;

		@Before
		public void setUp() throws Exception {
			super.setUp();
			when(propertiesService.getProperties()).thenReturn(properties);
		}

		@Test
		public void validatePriceVendor_PriceVendorHasNotChanged_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE);
			when(persistedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE);

			orderPositionValidator.validatePriceVendor(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceVendor", "orderPosition.priceVendor.InvalidValue");
		}

		@Test
		public void validatePriceVendor_PriceVendorHasChanged_ValidValue_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceVendor()).thenReturn(new BigDecimal(97).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(persistedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(properties.getPercentDiscount()).thenReturn(new BigDecimal(0.03).setScale(2, BigDecimal.ROUND_HALF_DOWN));

			orderPositionValidator.validatePriceVendor(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceVendor", "orderPosition.priceVendor.InvalidValue");
		}

		@Test
		public void validatePriceVendor_PriceVendorHasChanged_InvalidValue_ShouldCauseErrors() {
			when(changedOrderPosition.getPriceVendor()).thenReturn(new BigDecimal(50).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(persistedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(properties.getPercentDiscount()).thenReturn(new BigDecimal(0.03).setScale(2, BigDecimal.ROUND_HALF_DOWN));

			orderPositionValidator.validatePriceVendor(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, times(1)).rejectValue("priceVendor", "orderPosition.priceVendor.InvalidValue");
		}

	}

	@RunWith(BlockJUnit4ClassRunner.class)
	public static class ValidatePriceSpTests extends OrderPositionValidatorTest {

		@Mock Order order;
		@Mock Sp sp;

		@Before
		public void setUp() throws Exception {
			super.setUp();
			when(changedOrderPosition.getOrder()).thenReturn(order);
			when(order.getSp()).thenReturn(sp);
		}

		@Test
		public void validatePriceSp_PriceSpHasNotChanged_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceSp()).thenReturn(BigDecimal.ONE);
			when(persistedOrderPosition.getPriceSp()).thenReturn(BigDecimal.ONE);

			orderPositionValidator.validatePriceSp(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceSp", "orderPosition.priceSp.InvalidValue");
		}

		@Test
		public void validatePriceSp_PriceSpHasChanged_ValidValue_ShouldNotCauseErrors() {
			when(changedOrderPosition.getPriceSp()).thenReturn(new BigDecimal(115).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(persistedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(sp.getPercent()).thenReturn(new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_DOWN));

			orderPositionValidator.validatePriceSp(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, never()).rejectValue("priceSp", "orderPosition.priceSp.InvalidValue");
		}

		@Test
		public void validatePriceSp_PriceSpHasChanged_InvalidValue_ShouldCauseErrors() {
			when(changedOrderPosition.getPriceSp()).thenReturn(new BigDecimal(50).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(changedOrderPosition.getPriceOrdered()).thenReturn(new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(persistedOrderPosition.getPriceVendor()).thenReturn(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_DOWN));
			when(sp.getPercent()).thenReturn(new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_DOWN));

			orderPositionValidator.validatePriceSp(changedOrderPosition, persistedOrderPosition, errors);

			verify(errors, times(1)).rejectValue("priceSp", "orderPosition.priceSp.InvalidValue");
		}
	}
}