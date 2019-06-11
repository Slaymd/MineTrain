package fr.dariusmtn.minetrain.object;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Line implements ConfigurationSerializable {

	private String longname = "";
	private String smallname = "";
	private UUID lineId = null;
	private LineType lineType = null;
	
	public Line(UUID lineId, String longname, String smallname, LineType lineType) {
		this.setLineId(lineId);
		this.setName(longname);
		this.setAcronym(smallname);
		this.setLineType(lineType);
	}
	
	public Line(Map<String, Object> map) {
		this.setName((String) map.get("longname"));
		this.setAcronym((String) map.get("smallname"));
		this.setLineId((UUID) map.get("lineId"));
		this.setLineType(LineType.valueOf((String) map.get("linetype")));
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("longname", this.longname);
		map.put("smallname", this.smallname);
		map.put("linetype", this.lineType.toString());
		return map;
	}
	
	public Line() {
		//Empty
	}

	/**
	 * @return the lineId
	 */
	public UUID getLineId() {
		return lineId;
	}

	/**
	 * @param lineId the lineId to set
	 */
	public void setLineId(UUID lineId) {
		this.lineId = lineId;
	}

	/**
	 * @return the acronym
	 */
	public String getAcronym() {
		if(this.smallname != "") {
			return smallname;
		} else {
			return this.getName();
		}
	}

	/**
	 * @param smallname the acronym to set
	 */
	public void setAcronym(String smallname) {
		this.smallname = smallname.replaceAll("&", "§");
	}

	/**
	 * @return the line name
	 */
	public String getName() {
		return longname;
	}

	/**
	 * @param longname the name to set
	 */
	public void setName(String longname) {
		this.longname = longname.replaceAll("&", "§");
	}

	/**
	 * @return the lineType
	 */
	public LineType getLineType() {
		return lineType;
	}

	/**
	 * @param lineType the lineType to set
	 */
	public void setLineType(LineType lineType) {
		this.lineType = lineType;
	}
	
	
}
