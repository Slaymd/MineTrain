package fr.dariusmtn.minetrain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Line implements ConfigurationSerializable{

	private String longname = "";
	private String smallname = "";
	private ChatColor color = null;
	private UUID lineId = null;
	
	public Line(UUID lineId, String longname, String smallname, ChatColor color) {
		this.setLineId(lineId);
		this.setLongname(longname);
		this.setSmallname(smallname);
		this.setColor(color);
	}
	
	public Line(Map<String, Object> map) {
		this.setLongname((String) map.get("longname"));
		this.setSmallname((String) map.get("smallname"));
		this.setColor(ChatColor.valueOf((String) map.get("color")));
		this.setLineId(UUID.fromString((String) map.get("lineId")));
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("longname", this.longname);
		map.put("smallname", this.smallname);
		map.put("lineId", this.lineId.toString());
		map.put("color", this.color.toString());
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
	 * @return the color
	 */
	public ChatColor getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(ChatColor color) {
		this.color = color;
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
		this.smallname = smallname;
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
		this.longname = longname;
	}
	
	
}
