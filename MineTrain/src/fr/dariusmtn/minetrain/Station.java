package fr.dariusmtn.minetrain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

public class Station implements ConfigurationSerializable {

	private UUID stationId = null;
	private String name = "";
	private String city = "";
	private UUID lineId = null;
	private HashMap<Location,Vector> starts = new HashMap<Location,Vector>();
	
	public Station(UUID stationId, String name, UUID lineId, HashMap<Location,Vector> starts) {
		this.stationId = stationId;
		this.name = name;
		this.lineId = lineId;
		this.starts = starts;
	}
	
	public Station() {
		//empty
	}
	
	@SuppressWarnings("unchecked")
	public Station(Map<String, Object> map) {
		this.name = (String) map.get("name");
		this.city = (String) map.get("city");
		this.lineId = UUID.fromString((String) map.get("lineid"));
		this.starts = (HashMap<Location, Vector>) map.get("starts");
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.name);
		map.put("city", this.city);
		map.put("lineId", this.lineId.toString());
		map.put("starts", this.starts);
		return map;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the lineid
	 */
	public UUID getLineId() {
		return lineId;
	}

	/**
	 * @param lineid the lineid to set
	 */
	public void setLineId(UUID lineId) {
		this.lineId = lineId;
	}

	/**
	 * @return the starts
	 */
	public HashMap<Location,Vector> getStarts() {
		return starts;
	}

	/**
	 * @param starts the starts to set
	 */
	public void setStarts(HashMap<Location,Vector> starts) {
		this.starts = starts;
	}
	
	/**
	 * 
	 * @param loc
	 * @param dir
	 */
	public void addStarts(Location loc, Vector dir) {
		this.starts.put(loc, dir);
	}
	
	public void removeStarts(Location loc) {
		this.starts.remove(loc);
	}

	/**
	 * @return the stationId
	 */
	public UUID getStationId() {
		return stationId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(UUID stationId) {
		this.stationId = stationId;
	}
	
}
