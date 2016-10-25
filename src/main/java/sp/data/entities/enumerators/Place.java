package sp.data.entities.enumerators;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Place {
	YUMASHEVA(1),
	OKEAN(2),
	OTHER(3);

	private final int id;

	private String name;

	Place(int id) {
		this.id = id;
	}

	public String getName() {
		name = getById(id).toString();
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

	public static Map<Integer, String> getAllAsMap(){
		Map<Integer, String> statuses = new LinkedHashMap<>();
		for (Place status : Place.values()) {
			statuses.put(status.getId(), status.toString());
		}
		return statuses;
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
