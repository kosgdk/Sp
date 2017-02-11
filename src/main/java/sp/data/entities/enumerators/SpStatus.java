package sp.data.entities.enumerators;

public enum SpStatus {
	COLLECTING(1),
	CHECKOUT(2),
	PACKING(3),
	PAID(4),
	SENT(5),
	ARRIVED(6),
	DISTRIBUTING(7),
	COMPLETED(8);

	private final int id;

	private String name;

	SpStatus(int id) {
		this.id = id;
		this.name = toString();
	}

	public String getName() {
		return name;
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
			case 6: return ARRIVED;
			case 7: return DISTRIBUTING;
			case 8: return COMPLETED;
			default: return null;
		}
	}

	public static SpStatus getByName(String name){
		for (SpStatus status : SpStatus.values()) {
			if (status.toString().equals(name)) return status;
		}
		return null;
	}

	@Override
	public String toString(){
		switch (id) {
			case 1: return "Сбор";
			case 2: return "Оплата";
			case 3: return "На комплектации";
			case 4: return "Оплачен";
			case 5: return "Отправлен";
			case 6: return "Прибыл";
			case 7: return "Раздаётся";
			case 8: return "Завершён";
			default: return "NULL";
		}
	}
}
