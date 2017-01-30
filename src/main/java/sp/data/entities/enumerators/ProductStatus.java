package sp.data.entities.enumerators;

public enum ProductStatus {
	AVAILABLE(0),
	DELETED(1),
	NOT_AVAILABLE(2);

	private final int id;

	private String name;

	ProductStatus(int id) {
		this.id = id;
	}

	public String getName() {
		name = getById(id).toString();
		return name;
	}

	public int getId() {
		return id;
	}

	public static ProductStatus getById(int id) {
		switch (id) {
			case 0: return AVAILABLE;
			case 1: return DELETED;
			case 2: return NOT_AVAILABLE;
			default: return null;
		}
	}

	public static ProductStatus getByName(String name){
		for (ProductStatus status : ProductStatus.values()) {
			if (status.toString().equals(name)) return status;
		}
		return null;
	}

	@Override
	public String toString(){
		switch (id) {
			case 0: return "В наличии";
			case 1: return "Удалён";
			case 2: return "Нет в наличии";
			default: return "NULL";
		}
	}
}
