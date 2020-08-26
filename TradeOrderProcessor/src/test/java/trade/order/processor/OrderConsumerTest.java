package trade.order.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderConsumerTest {

   @Autowired
   @InjectMocks
   private OrderConsumer consumer; 
 
   @MockBean
   private TradeOrderRepository tradeOrderRepositoryMock;  
   
  
  @Test
  public void processValidOrders_invalidOrder_invalidAccountId() {
    TradeOrder order = new TradeOrder("N123", "AAPL", 3, 10.00);
    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(new ArrayList<TradeOrder>());
	consumer.processValidOrder(order);
	assertEquals(Statuses.DECLINED.name(), order.getStatus());
  }
  
  @Test
  public void processValidOrders_invalidOrder_invalidSymbolId() {
    TradeOrder order = new TradeOrder("M7123", "AA", 3, 10.00);
    TradeOrder exsitngOrder1 = new TradeOrder("M1123", "AAPL", 4, 100);
    TradeOrder exsitngOrder2 = new TradeOrder("M1123", "AAPL", 3, 80);
    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
    exsitngOrders.add(exsitngOrder1);
    exsitngOrders.add(exsitngOrder2);
    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(exsitngOrders);
    consumer.processValidOrder(order);
	assertEquals(Statuses.DECLINED.name(), order.getStatus());
  }
  
  @Test
  public void processValidOrders_validOrder() {
    TradeOrder order = new TradeOrder("U1123", "AAPL", 3, 10.00);
    TradeOrder exsitngOrder1 = new TradeOrder("U1123", "AAPL", 4, 100);
    TradeOrder exsitngOrder2 = new TradeOrder("U1123", "AAPL", 3, 80);
    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
    exsitngOrders.add(exsitngOrder1);
    exsitngOrders.add(exsitngOrder2);
    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(exsitngOrders);
    consumer.processValidOrder(order);
	assertEquals(Statuses.ACCEPTED.name(), order.getStatus());
  }
  
  @Test
  public void processValidOrders_validOrder_NegativeAccountBalance() {
    TradeOrder order = new TradeOrder("U1123", "AAPL", 3, 10.00);
    TradeOrder exsitngOrder1 = new TradeOrder("U1123", "AAPL", 4, 800);
    TradeOrder exsitngOrder2 = new TradeOrder("U1123", "AAPL", 3, 200);
    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
    exsitngOrders.add(exsitngOrder1);
    exsitngOrders.add(exsitngOrder2);
    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(exsitngOrders);
    consumer.processValidOrder(order);
	assertEquals(Statuses.DECLINED.name(), order.getStatus());
  }
  
  @Test
  public void processValidOrders_succesfullyProcessed_saveAtGivenPrice() {
    TradeOrder order = new TradeOrder("M1123", "AAPL", 3, 10.00);
    TradeOrder exsitngOrder1 = new TradeOrder("M1123", "AAPL", 4, 100);
    TradeOrder exsitngOrder2 = new TradeOrder("M1123", "AAPL", 3, 80);
    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
    exsitngOrders.add(exsitngOrder1);
    exsitngOrders.add(exsitngOrder2);
    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(exsitngOrders);
    when(tradeOrderRepositoryMock.findAllByAccountIdAndSymbolId(order.getAccountId(),order.getSymbolId())).thenReturn(exsitngOrders);
    consumer.processValidOrder(order);
	assertEquals(Statuses.ACCEPTED.name(), order.getStatus());
	assertEquals(10.00, order.getPrice());
	Mockito.verify(tradeOrderRepositoryMock).save(order);
  }
  
// Following test is written if avg price is determined by previously SAVED successful orders for an account and symbol id
  // Uncomment  getAvgPriceBasedOnPreviouslySavedOrders(order); methos in OrderConsumer class to test this , line #58
//  @Test
//  public void processValidOrders_succesfullyProcessed_avgPriceFromLast10SavedOrders() {
//    TradeOrder order = new TradeOrder("M1123", "AAPL", 3, 10.00);
//    TradeOrder exsitngOrder1 = new TradeOrder("M1123", "AAPL", 4, 100);
//    TradeOrder exsitngOrder2 = new TradeOrder("M1123", "AAPL", 3, 80);
//    TradeOrder exsitngOrder3 = new TradeOrder("M1123", "AAPL", 4, 100);
//    TradeOrder exsitngOrder4= new TradeOrder("M1123", "AAPL", 3, 80);
//    TradeOrder exsitngOrder5 = new TradeOrder("M1123", "AAPL", 4, 100);
//    TradeOrder exsitngOrder6= new TradeOrder("M1123", "AAPL", 3, 80);
//    TradeOrder exsitngOrder7 = new TradeOrder("M1123", "AAPL", 4, 100);
//    TradeOrder exsitngOrder8= new TradeOrder("M1123", "AAPL", 3, 80);
//    TradeOrder exsitngOrder9 = new TradeOrder("M1123", "AAPL", 4, 100);
//    TradeOrder exsitngOrder10= new TradeOrder("M1123", "AAPL", 3, 80);
//    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
//    exsitngOrders.add(exsitngOrder1);
//    exsitngOrders.add(exsitngOrder2);
//    exsitngOrders.add(exsitngOrder3);
//    exsitngOrders.add(exsitngOrder4);
//    exsitngOrders.add(exsitngOrder5);
//    exsitngOrders.add(exsitngOrder6);
//    exsitngOrders.add(exsitngOrder7);
//    exsitngOrders.add(exsitngOrder8);
//    exsitngOrders.add(exsitngOrder9);
//    exsitngOrders.add(exsitngOrder10);
//    when(tradeOrderRepositoryMock.getAccountBalance(order.getAccountId())).thenReturn(100);
//    when(tradeOrderRepositoryMock.findOrdersBySymbolId(order.getAccountId(),order.getSymbolId())).thenReturn(exsitngOrders);
//    consumer.processValidOrder(order);
//	assertEquals(Statuses.ACCEPTED.name(), order.getStatus());
//	assertEquals(90.00, order.getPrice());
//	Mockito.verify(tradeOrderRepositoryMock).save(order);
//  }
//  
  
  @Test
  public void processValidOrders_succesfullyProcessed_avgPriceFromLast10OrdersIncomingStream() {
    TradeOrder order = new TradeOrder("M1123", "AAPL", 3, 10.00);
    TradeOrder exsitngOrder1 = new TradeOrder("M1123", "AAPL", 4, 5);  
    TradeOrder exsitngOrder2 = new TradeOrder("M11233", "AAPL", 3, 40);   //exclude
    TradeOrder exsitngOrder3 = new TradeOrder("M1123", "AABL", 4, 100);  //exclude
    TradeOrder exsitngOrder4= new TradeOrder("M1123", "AAPL", 3, 10);
    TradeOrder exsitngOrder5 = new TradeOrder("M1123", "AAPL", 4, 11);
    TradeOrder exsitngOrder6= new TradeOrder("M1123", "AAPL", 3, 7);
    TradeOrder exsitngOrder7 = new TradeOrder("U1123", "AAPL", 4, 50); //exclude
    TradeOrder exsitngOrder8= new TradeOrder("M1123", "AA", 3, 30);  //exclude  
    TradeOrder exsitngOrder9 = new TradeOrder("M1123", "AAPL", 4, 6);
    TradeOrder exsitngOrder10= new TradeOrder("M1123", "AAPL", 3, 7);
    TradeOrder exsitngOrder11 = new TradeOrder("M1123", "AAPL", 4, 10);
    TradeOrder exsitngOrder12 = new TradeOrder("M1123", "AAPL", 3, 5);
    TradeOrder exsitngOrder13 = new TradeOrder("M3", "AAPL", 4, 100); //exclude
    TradeOrder exsitngOrder14= new TradeOrder("M1123", "X", 3, 80); //exclude
    TradeOrder exsitngOrder15 = new TradeOrder("M1123", "AAPL", 4, 7);
    TradeOrder exsitngOrder16= new TradeOrder("M1123", "AAPL", 3, 9);

    ArrayList<TradeOrder> exsitngOrders = new ArrayList<TradeOrder>();
    exsitngOrders.add(exsitngOrder1);
    exsitngOrders.add(exsitngOrder2);
    exsitngOrders.add(exsitngOrder3);
    exsitngOrders.add(exsitngOrder4);
    exsitngOrders.add(exsitngOrder5);
    exsitngOrders.add(exsitngOrder6);
    exsitngOrders.add(exsitngOrder7);
    exsitngOrders.add(exsitngOrder8);
    exsitngOrders.add(exsitngOrder9);
    exsitngOrders.add(exsitngOrder10);
    exsitngOrders.add(exsitngOrder11);
    exsitngOrders.add(exsitngOrder12);
    exsitngOrders.add(exsitngOrder13);
    exsitngOrders.add(exsitngOrder14);
    exsitngOrders.add(exsitngOrder15);
    exsitngOrders.add(exsitngOrder16);
    
    ArrayList<TradeOrder> exsitngOrdersForAccount = new ArrayList<TradeOrder>();
    exsitngOrdersForAccount.add(exsitngOrder1);
    exsitngOrdersForAccount.add(exsitngOrder4);
    exsitngOrdersForAccount.add(exsitngOrder5);
    exsitngOrdersForAccount.add(exsitngOrder6);
    exsitngOrdersForAccount.add(exsitngOrder9);
    exsitngOrdersForAccount.add(exsitngOrder10);
    exsitngOrdersForAccount.add(exsitngOrder11);
    exsitngOrdersForAccount.add(exsitngOrder12);
    exsitngOrdersForAccount.add(exsitngOrder15);
    exsitngOrdersForAccount.add(exsitngOrder16);

    when(tradeOrderRepositoryMock.findAllByAccountId(order.getAccountId())).thenReturn(exsitngOrdersForAccount);
    consumer.setIncomingOrderList(exsitngOrders);
    consumer.processValidOrder(order);
	assertEquals(Statuses.ACCEPTED.name(), order.getStatus());
	assertEquals(7.70, order.getPrice());
	Mockito.verify(tradeOrderRepositoryMock).save(order);
  }

}