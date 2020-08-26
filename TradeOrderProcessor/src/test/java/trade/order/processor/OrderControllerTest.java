package trade.order.processor;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.JsonPath;



@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    
	 @Autowired
	 private MockMvc mvc;
    
    @MockBean
    private TradeOrderRepository tradeOrderRepositoryMock;  
    
    @MockBean
    private OrderConsumer consumer;  

    /**
     *
     * @throws Exception
     *
     * It tests finding orders by acocuntId
     */
    @Test
    public void getSavedOrdersByAccountId_success() throws Exception {
	    TradeOrder order1 = createOrder("U1123","TSLA",500,3,new Date(),Statuses.ACCEPTED.name(),new Date(01,01,2020));
	    TradeOrder order2 = createOrder("U1123","AAPL",500,4,new Date(),Statuses.ACCEPTED.name(),new Date(02,02,2020));

	    Mockito.when(tradeOrderRepositoryMock.findAllByAccountId("U1123")).thenReturn(Arrays.asList(order1, order2)); 		
	    
	    MvcResult mvcResult = mvc.perform(get("/savedOrders/U1123")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();

	    String response = mvcResult.getResponse().getContentAsString();
	    assertEquals("U1123",JsonPath.parse(response).read("$[0].accountId"));

    }
    
    
	/**
    *
    * @throws Exception
    *
    * It tests finding orders for wrong accountId
    */
   @Test
   public void findSavedOrdersByNonExistingAccountId() throws Exception {
   	mvc.perform(get("/savedOrders/X123"))
               .andExpect(status().isNotFound());
   }
    
    @Test
    public void getStreamOrdersByAccountId_success() throws Exception {
	    TradeOrder order1 = createOrder("U1123","TSLA",500,3,new Date(),Statuses.ACCEPTED.name(), new Date());
	    TradeOrder order2 = createOrder("U1123","AAPL",500,4,new Date(),Statuses.ACCEPTED.name(),new Date());

	    Mockito.when(consumer.getIncomingOrderList()).thenReturn(Arrays.asList(order1, order2)); 		
	    
	    MvcResult mvcResult = mvc.perform(get("/streamOrdersByAccount/U1123")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();

	    String response = mvcResult.getResponse().getContentAsString();
	    assertEquals("U1123",JsonPath.parse(response).read("$[0].accountId"));
	    assertEquals("AAPL",JsonPath.parse(response).read("$[0].symbolId"));

    }
    
    public void findStreamOrdersByNonExistingAccountId() throws Exception {
       	mvc.perform(get("/streamOrdersByAccount/X123"))
                   .andExpect(status().isNotFound());
       }
        
    
    @Test
    public void getStreamOrdersBySymbolId_success() throws Exception {
	    TradeOrder order1 = createOrder("U1123","TSLA",500,3,new Date(),Statuses.ACCEPTED.name(), new Date());
	    TradeOrder order2 = createOrder("U1123","AAPL",500,4,new Date(),Statuses.ACCEPTED.name(),new Date());
	    TradeOrder order3 = createOrder("M1123","AAPL",500,4,new Date(),Statuses.ACCEPTED.name(),new Date());
	    
	    Mockito.when(consumer.getIncomingOrderList()).thenReturn(Arrays.asList(order1, order2, order3)); 		
	    
	    MvcResult mvcResult = mvc.perform(get("/streamOrdersBySymbol/AAPL")
			      .accept(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andReturn();

	    String response = mvcResult.getResponse().getContentAsString();
	    assertEquals("M1123",JsonPath.parse(response).read("$[0].accountId"));
	    assertEquals("AAPL",JsonPath.parse(response).read("$[0].symbolId"));

    }
    
    public void findStreamOrdersByNonExistingSymbolId() throws Exception {
       	mvc.perform(get("/streamOrdersBySymbol/X123"))
                   .andExpect(status().isNotFound());
       }



	private TradeOrder createOrder(String acctId, String symbolId, double price, int qty, Date date, String status, Date orderDate) {
	    	TradeOrder order1 =new TradeOrder();
			order1.setAccountId(acctId);
	    	order1.setSymbolId(symbolId);
	    	order1.setPrice(price);
	    	order1.setQuantity(qty);
	    	order1.setOrderDate(date);
	    	order1.setStatus(status);
	    	order1.setOrderDate(orderDate);
	    	return order1;
	}

}
