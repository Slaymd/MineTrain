package fr.dariusmtn.minetrain.object;

public enum LineType {
	
	TRAMWAY("TRAMWAY", "Tramway/Streetcar"),
	REGIONAL_TRAIN("REGIONAL_TRAIN", "Regional Train"),
	INTERCITY("INTERCITY", "Intercity"),
	HIGH_SPEED_TRAIN("HIGH_SPEED_TRAIN", "High-Speed Train"),
	ROLLER_COASTER("ROLLER_COASTER", "Roller Coaster"),
	TRAIN("TRAIN", "Train"),
	METRO("METRO", "Metro/Subway");
	
	private String typeId = "";
	private String typename = "";
	
	LineType(String typeId, String typename){
		this.typeId = typeId;
		this.typename = typename;
	}
	
	/**
	 * Get line type name
	 * @return String
	 */
	public String getName() {
		return this.typename;
	}
	
	/**
	 * Get line type name
	 * @return String
	 */
	public String toString() {
		return this.typeId;
	}

	/**
	 * @return the typeId
	 */
	public String getTypeId() {
		return this.typeId;
	}

}
