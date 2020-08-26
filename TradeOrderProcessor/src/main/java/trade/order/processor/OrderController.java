package trade.order.processor;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
	
	 private static final int PAST_999_ORDERS = 999;
	 private static final int PAST_99_ORDERS = 99;

	@Autowired
	private TradeOrderRepository tradeOrderRepository;
	
	@Autowired
	private OrderConsumer consumer;

	@GetMapping("/savedOrders/{accountId}")
	public List<TradeOrder> getOrdersByAccountId(@PathVariable String accountId) {
		List<TradeOrder> orders = tradeOrderRepository.findAllByAccountId(accountId);

		if(orders==null || orders.size()==0) {
			throw new OrdersNotFoundException("Order not found for acocuntId ::" + accountId);
		}
		orders= orders.stream().limit(PAST_999_ORDERS)
				.collect(Collectors.toList());
      return orders;
	}
	
	
	@GetMapping("/streamOrdersByAccount/{accountId}")
	public List<TradeOrder> getStreamOrdersByAccountId(@PathVariable String accountId) {
		List<TradeOrder> orderList = consumer.getIncomingOrderList();
		if(orderList.size()==0) {
			throw new OrdersNotFoundException("Order not found for acocuntId ::" + accountId);
		}
		Collections.reverse(orderList);
		List<TradeOrder> orders = orderList.stream()
				.filter(p -> (p.getAccountId().equals(accountId)))
				.limit(PAST_999_ORDERS)
				.collect(Collectors.toList());
		
		
      return orders;
	}
	
	@GetMapping("/streamOrdersBySymbol/{symbolId}")
	public List<TradeOrder> getStreamOrdersBySymbolId(@PathVariable String symbolId) {
		List<TradeOrder> orderList = consumer.getIncomingOrderList();
		if(orderList.size()==0) {
			throw new OrdersNotFoundException("Order not found for symbolId ::" + symbolId);
		}
		orderList = consumer.getIncomingOrderList().stream()
				.filter(p -> (p.getSymbolId().equals(symbolId)))
				.collect(Collectors.toList());
		Collections.reverse(orderList);
		List<TradeOrder> orders = orderList.stream()
				.limit(PAST_99_ORDERS)
				.collect(Collectors.toList());
      return orders;
	}

}
