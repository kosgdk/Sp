package sp.data.entities.enumerators;

public enum SpStatus {
	COLLECTING(1),
	CHECKOUT(2),
	PACKING(3),
	PAID(4),
	SENT(5),
	RECEIVED(6),
	DISTRIBUTING(7),
	COMPLETED(8);

	private final int id;

	SpStatus(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static SpStatus getById(int id) {
		switch (id) {
			case 1: return COLLECTING;
			case 2: return CHECKOUT;
			case 3: return PACKING;
			case 4: return PAID;
			case 5: return SENT;
			case 6: return RECEIVED;
			case 7: return DISTRIBUTING;
			case 8: return COMPLETED;
			default: return null;
		}
	}
	@Override
	public String toString(){
		switch (id) {
			case 1: return "Сбор";
			case 2: return "Оплата";
			case 3: return "Комплектуется";
			case 4: return "Оплачен";
			case 5: return "Отправлен";
			case 6: return "Получен";
			case 7: return "Раздаётся";
			case 8: return "Завершён";
			default: return "NULL";
		}
	}
}
