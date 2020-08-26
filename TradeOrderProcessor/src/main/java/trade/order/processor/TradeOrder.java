package trade.order.processor;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * This class defines an Order. It is associated with a trader, has an amount, and a quantity.
 */
@Entity
@JsonPropertyOrder({ "account_id", "symbol_id", "quantity", "price", "status", "order_date"})
public class TradeOrder {

  @Id
  @GeneratedValue
  @JsonIgnore
  private long id;
  
//  @JsonProperty("account_id")
  @Column(name = "account_id")
  @Pattern(regexp = "[UMD]\\d{4,7}")
  private String accountId;
  
//  @JsonProperty("symbol_id")
  @Column(name = "symbol_id")
  @Pattern(regexp = "([\\w]{3,7})|([\\w]{3}/[\\w]{3})")
  private String symbolId;  
  private int quantity;
  private double price;
  private String status;

  @Column(name = "order_date")
  @Temporal(TemporalType.TIMESTAMP)
  private java.util.Date orderDate;


  public TradeOrder() {
  }

  public TradeOrder(String accountId, String symbolId, int quantity, double price) {
    this.accountId = accountId;
    this.symbolId = symbolId;
    this.quantity = quantity;
    this.price= price;
  }
  
  public String getAccountId() {
		return accountId;
	}

	public String getSymbolId() {
		return symbolId;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
    }
	
   public void setPrice(double price) {
		this.price = price;
   }
   
   public void isValidOrder() {
	   
   }
   public java.util.Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setSymbolId(String symbolId) {
		this.symbolId = symbolId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TradeOrder that = (TradeOrder) o;
    return quantity == that.quantity &&
    		symbolId.equals(that.symbolId) &&
    		accountId.equals(that.accountId) &&
    		Double.compare(price, that.price) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, symbolId, quantity,price);
  }

  @Override
  public String toString() {
    return "TradeOrder{" +
            "accountId='" + accountId + '\'' +
            ", symbolId='" + symbolId + '\'' +
            ", quantity='" + quantity + '\'' +
             ", price='" + price + '\'' +
            '}';
  }
}
