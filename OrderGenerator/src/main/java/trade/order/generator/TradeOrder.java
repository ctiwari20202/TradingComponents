package trade.order.generator;


import java.util.Objects;


/**
 * This class defines a loan. It is associated with an applicant, has an amount, and a status.
 */
public class TradeOrder {

  private String accountId;
  private String symbolId;
  private int quantity;
  private double price;

  public TradeOrder() {
  }

  public TradeOrder(String accountId, String symbolId, int quantity, int price) {
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
