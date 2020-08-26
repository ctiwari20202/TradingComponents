package trade.order.processor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;


public interface TradeOrderRepository extends CrudRepository<TradeOrder, Long>,
QueryByExampleExecutor<TradeOrder> {
	
	@Query("select o from TradeOrder o where o.accountId = :accountId order by o.orderDate desc ")
	List<TradeOrder> findAllByAccountId(@Param("accountId") String accountId);
	
	@Query("select o from TradeOrder o where o.accountId = :accountId and o.symbolId = :symbolId order by o.orderDate desc ")
	List<TradeOrder> findAllByAccountIdAndSymbolId(@Param("accountId") String accountId, @Param("symbolId") String symbolId);
	
}
