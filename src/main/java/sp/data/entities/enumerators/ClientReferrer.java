package sp.data.entities.enumerators;

public enum ClientReferrer {
	FORUM(1),
	VK(2),
	OTHER(3);

	private final int id;

	private String name;

	ClientReferrer(int id) {
		this.id = id;
		this.name = toString();
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public static ClientReferrer getById(int id) {
		switch (id) {
			case 1: return FORUM;
			case 2: return VK;
			case 3: return OTHER;
			default: return null;
		}
	}

	public static ClientReferrer getByName(String name){
		for (ClientReferrer clientReferrer : ClientReferrer.values()) {
			if (clientReferrer.toString().equals(name)) return clientReferrer;
		}
		return null;
	}

	@Override
	public String toString(){
		switch (id) {
			case 1: return "Форум";
			case 2: return "Вконтакте";
			case 3: return "Прочее";
			default: return "NULL";
		}
	}
}
