package trade.order.generator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Supplier;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeGeneratorApplicationTest {

  @Test
  public void supplyLoan() {
	TradeGeneratorApplication app = new TradeGeneratorApplication();
    Supplier<TradeOrder> order = app.supplyOrder();
    assertNotNull(order);
    assertNotNull(order.get());
    assertNotNull(order.get().getAccountId());
    assertNotNull(order.get().getSymbolId());
    assertNotNull(order.get().getPrice());
    assertNotNull(order.get().getQuantity());
  }
}