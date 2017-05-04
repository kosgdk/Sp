package sp.data.entities.formscontainers;

import sp.data.entities.OrderPosition;
import java.util.List;

public class ZeroWeightOrderPositionsForm {

	private List<OrderPosition> orderPositions;

	public ZeroWeightOrderPositionsForm() {}

	public ZeroWeightOrderPositionsForm(List<OrderPosition> orderPositions) {
		this.orderPositions = orderPositions;
	}

	public List<OrderPosition> getOrderPositions() {
		return orderPositions;
	}

	public void setOrderPositions(List<OrderPosition> orderPositions) {
		this.orderPositions = orderPositions;
	}
}
