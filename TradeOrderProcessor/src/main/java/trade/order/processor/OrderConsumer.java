package trade.order.processor;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

 private static final int PAST_TEN_ORDERS = 10;
 
 private static final double MAX_BALANCE = 1000;


public static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);
  
  private MessageProcessor processor;

  private Validator validator;
  
  private List<TradeOrder> incomingOrderList = new ArrayList<TradeOrder>();

  @Autowired
  private TradeOrderRepository tradeOrderRepository;

  @Autowired
  public OrderConsumer(MessageProcessor processor, Validator validator) {
    this.processor = processor;
    this.validator=validator;
  }

  
  @StreamListener(MessageProcessor.APPLICATIONS_IN)
  public void processOrders(TradeOrder order) {
    log.info("{} {} quantity {} for ${} each", order.getAccountId(), order.getSymbolId(), order.getQuantity(), order.getPrice());
    processValidOrder(order);
  }

 protected void processValidOrder(TradeOrder order) {
	
	 if (invalidOrder(order)) {
	      order.setStatus(Statuses.DECLINED.name());
	      processor.declined().send(message(order));
	      incomingOrderList.add(order);
	  }else {
		 Double accountBalance = getAvailableAccountBalance(order);
	     if (accountBalance.compareTo(0.0)<=0 ) {
	      order.setStatus(Statuses.DECLINED.name());
	      processor.declined().send(message(order));
	      incomingOrderList.add(order);
	    } else {
	//      getAvgPriceBasedOnPreviouslySavedOrders(order);
	     
	      List<TradeOrder> previousOrders = incomingOrderList.stream()
	    		 .filter(p -> (p.getAccountId().equals(order.getAccountId()) && p.getSymbolId().equals(order.getSymbolId())))
	    		 .collect(Collectors.toList());
	      Collections.reverse(previousOrders);
	      List<TradeOrder>  last10Orders=previousOrders.stream()	 
	    		   .limit(PAST_TEN_ORDERS)
	               .collect(Collectors.toList());
	       if(last10Orders.size()>=PAST_TEN_ORDERS) {
	      	 double newPrice =  (last10Orders.stream().mapToDouble(i-> i.getPrice()).sum())/10;
	      	 order.setPrice(newPrice);
	        }
	         order.setOrderDate(new Date());
	    	 order.setStatus(Statuses.ACCEPTED.name());
	    	 tradeOrderRepository.save(order);
	    	 incomingOrderList.add(order);
	    	 processor.accepted().send(message(order));
	    }
	  }
}
 



  private void getAvgPriceBasedOnPreviouslySavedOrders(TradeOrder order) {
	List<TradeOrder> existingOrders = tradeOrderRepository.findAllByAccountIdAndSymbolId(order.getAccountId(), order.getSymbolId());
      if(existingOrders.size()>=PAST_TEN_ORDERS) {
    	 double newPrice =  (existingOrders.stream().limit(10).mapToDouble(i-> i.getPrice()).sum())/10;
    	 order.setPrice(newPrice);
      }
  }
  
  private Double getAvailableAccountBalance(TradeOrder order) {
		List<TradeOrder> existingOrders =tradeOrderRepository.findAllByAccountId(order.getAccountId());
		Double accountBalance =MAX_BALANCE;
		if(existingOrders==null) return accountBalance;
	    for(TradeOrder o :existingOrders) {
	    	 BigDecimal usedBalance = new BigDecimal(o.getPrice()).multiply(BigDecimal.valueOf(o.getQuantity()));	    	 
	    	 accountBalance= new BigDecimal(accountBalance).subtract(usedBalance).doubleValue();
	    }
	    return accountBalance;
	  }

  private static final <T> Message<T> message(T val) {
    return MessageBuilder.withPayload(val).build();
  }
  
  public void setIncomingOrderList(List<TradeOrder> incomingOrderList) {
	this.incomingOrderList = incomingOrderList;
  }
  
  public List<TradeOrder> getIncomingOrderList() {
	return incomingOrderList;
}

  
  private boolean invalidOrder(TradeOrder order) {
	  Set<ConstraintViolation<TradeOrder>> violations = validator.validate(order);
	  if(!violations.isEmpty()) {
		  return true;
	  }
	  return false;
  }

}
