package sp.data.entities.enumerators;

public enum OrderStatus {
	UNPAID(1),
	PAID(2),
	PACKING(3),
	SENT(4),
	ARRIVED(5),
	COMPLETED(6);

	private final int id;

	private String name;

	OrderStatus(int id) {
		this.id = id;
		this.name = toString();
	}

	public String getName() {
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
			case 5: return ARRIVED;
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

	@Override
	public String toString(){
		switch (id) {
			case 1: return "Не оплачен";
			case 2: return "Оплачен";
			case 3: return "Комплектуется";
			case 4: return "Отправлен";
			case 5: return "Прибыл";
			case 6: return "Завершён";
			default: return "NULL";
		}
	}
}
