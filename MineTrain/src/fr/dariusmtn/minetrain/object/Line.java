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
		this.setLongname(longname);
		this.setSmallname(smallname);
		this.setLineType(lineType);
	}
	
	public Line(Map<String, Object> map) {
		this.setLongname((String) map.get("longname"));
		this.setSmallname((String) map.get("smallname"));
		this.setLineId(UUID.fromString((String) map.get("lineId")));
		this.setLineType(LineType.valueOf((String) map.get("linetype")));
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("longname", this.longname);
		map.put("smallname", this.smallname);
		map.put("lineId", this.lineId.toString());
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
	 * @return the smallname
	 */
	public String getSmallname() {
		return smallname;
	}

	/**
	 * @param smallname the smallname to set
	 */
	public void setSmallname(String smallname) {
		this.smallname = smallname.replaceAll("&", "§");
	}

	/**
	 * @return the longname
	 */
	public String getLongname() {
		return longname;
	}

	/**
	 * @param longname the longname to set
	 */
	public void setLongname(String longname) {
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
