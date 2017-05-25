package sp.data.entities.enumerators;

public enum Place {
	YUMASHEVA(1),
	OKEAN(2);

	private final int id;

	private String name;

	Place(int id) {
		this.id = id;
		this.name = toString();
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public static Place getById(int id) {
		switch (id) {
			case 1: return YUMASHEVA;
			case 2: return OKEAN;
			default: return null;
		}
	}

	public static Place getByName(String name){
		for (Place status : Place.values()) {
			if (status.toString().equals(name)) return status;
		}
		return null;
	}

	@Override
	public String toString(){
		switch (id) {
			case 1: return "Юмашева";
			case 2: return "Океан";
			default: return "NULL";
		}
	}
}
