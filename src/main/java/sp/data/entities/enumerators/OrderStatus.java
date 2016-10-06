package sp.data.entities.enumerators;

import java.util.LinkedHashMap;
import java.util.Map;

public enum OrderStatus {
	UNPAID(1),
	PAID(2),
	PACKING(3),
	SENT(4),
	RECEIVED(5),
	COMPLETED(6);

	private final int id;

	private String name;

	OrderStatus(int id) {
		this.id = id;
	}

	public String getName() {
		name = getById(id).toString();
		return name;
	}

	public int getId() {
		return id;
	}

	public static OrderStatus getById(int id) {
		switch (id) {
			case 1: return UNPAID;
			case 2: return PAID;
			case 3: return PACKING;
			case 4: return SENT;
			case 5: return RECEIVED;
			case 6: return COMPLETED;
			default: return null;
		}
	}

	public static OrderStatus getByName(String name){
		for (OrderStatus status : OrderStatus.values()) {
			if (status.toString().equals(name)) return status;
		}
		return null;
	}

	public static Map<Integer, String> getAllAsMap(){
		Map<Integer, String> statuses = new LinkedHashMap<>();
		for (OrderStatus status : OrderStatus.values()) {
			statuses.put(status.getId(), status.toString());
		}
		return statuses;
	}

	@Override
	public String toString(){
		switch (id) {
			case 1: return "Не оплачен";
			case 2: return "Оплачен";
			case 3: return "Комплектуется";
			case 4: return "Отправлен";
			case 5: return "Получен";
			case 6: return "Завершён";
			default: return "NULL";
		}
	}
}
