package e2ePojoRequest;

import java.util.List;

public class PlaceOrderPojo {
	
	private List<PlaceOrderDetailPojo> orders;

	public List<PlaceOrderDetailPojo> getOrders() {
		return orders;
	}

	public void setOrders(List<PlaceOrderDetailPojo> orders) {
		this.orders = orders;
	}

}
