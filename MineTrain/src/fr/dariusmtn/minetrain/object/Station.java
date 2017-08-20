package fr.dariusmtn.minetrain.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;

import fr.dariusmtn.minetrain.Main;

public class Station{

	private UUID stationId = null;
	private String name = "";
	private String city = "";
	private ArrayList<String> lineId = new ArrayList<String>();
	private HashMap<Location,Location> starts = new HashMap<Location,Location>();
	private ArrayList<Location> buttons = new ArrayList<Location>();
	
	public Station(UUID stationId, String name, ArrayList<UUID> lineId, HashMap<Location,Location> starts, ArrayList<Location> buttons) {
		ArrayList<String> lineIdStr = new ArrayList<String>();
		for(UUID lid : lineId) {
			lineIdStr.add(lid.toString());
		}
		this.stationId = stationId;
		this.setName(name);
		this.lineId = lineIdStr;
		this.starts = starts;
		this.setButtons(buttons);
	}
	
	public Station() {
		//empty
	}
	
	@SuppressWarnings("unchecked")
	public Station(Map<String, Object> map, Main plugin) {
		this.name = (String) map.get("name");
		this.city = (String) map.get("city");
		this.lineId = (ArrayList<String>) map.get("lineId");
		ArrayList<String> starts_loc = (ArrayList<String>) map.get("starts.loc");
		ArrayList<String> starts_dir = (ArrayList<String>) map.get("starts.dir");
		HashMap<Location, Location> starts = new HashMap<Location, Location>();
		for(int i = 0; i < starts_loc.size();i++) {
			Location loc = plugin.getFileUtils().stringToLocation(starts_loc.get(i));
			Location dir = plugin.getFileUtils().stringToLocation(starts_dir.get(i));
			starts.put(loc, dir);
		}
		this.starts = starts;
		ArrayList<String> btns = (ArrayList<String>) map.get("buttons");
		ArrayList<Location> buttons = new ArrayList<Location>();
		for(String loc : btns) {
			buttons.add(plugin.getFileUtils().stringToLocation(loc));
		}
		this.buttons = buttons;
	}
	
	public Map<String, Object> serialize(Main plugin) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.name);
		map.put("city", this.city);
		map.put("lineId", this.lineId);
		ArrayList<String> starts_loc = new ArrayList<String>();
		ArrayList<String> starts_dir = new ArrayList<String>();
		for(Location loc : this.starts.keySet()) {
			starts_loc.add(plugin.getFileUtils().locationToString(loc));
			starts_dir.add(plugin.getFileUtils().locationToString(this.starts.get(loc)));
		}
		map.put("starts.loc", starts_loc);
		map.put("starts.dir", starts_dir);
		ArrayList<String> btns = new ArrayList<String>();
		for(Location loc : this.buttons) {
			btns.add(plugin.getFileUtils().locationToString(loc));
		}
		map.put("buttons", btns);
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
		this.name = name.replaceAll("&", "§");
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
	public ArrayList<UUID> getLinesId() {
		ArrayList<UUID> lineIdUUID = new ArrayList<UUID>();
		for(String strUUID : this.lineId) {
			lineIdUUID.add(UUID.fromString(strUUID));
		}
		return lineIdUUID;
	}

	/**
	 * @param lineid the lineid to set
	 */
	public void setLinesId(ArrayList<UUID> lineIdList) {
		ArrayList<String> lineIdStr = new ArrayList<String>();
		for(UUID lid : lineIdList) {
			lineIdStr.add(lid.toString());
		}
		this.lineId = lineIdStr;
	}
	
	/**
	 * 
	 * @param lineId
	 */
	public void removeLineId(UUID lineId) {
		this.lineId.remove(lineId.toString());
	}
	
	/**
	 * 
	 * @param lineId
	 */
	public void addLineId(UUID lineId) {
		this.lineId.add(lineId.toString());
	}

	/**
	 * @return the starts
	 */
	public HashMap<Location,Location> getStarts() {
		return starts;
	}

	/**
	 * @param starts the starts to set
	 */
	public void setStarts(HashMap<Location,Location> starts) {
		this.starts = starts;
	}
	
	/**
	 * 
	 * @param loc
	 * @param dir
	 */
	public void addStarts(Location loc, Location dir) {
		this.starts.put(loc, dir);
	}
	
	/**
	 * 
	 * @param loc
	 */
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

	/**
	 * @return the buttons
	 */
	public ArrayList<Location> getButtons() {
		return buttons;
	}

	/**
	 * @param buttons the buttons to set
	 */
	public void setButtons(ArrayList<Location> buttons) {
		this.buttons = buttons;
	}
	
	/**
	 * 
	 * @param loc
	 */
	public void removeButton(Location loc) {
		this.buttons.remove(loc);
	}
	
	/**
	 * 
	 * @param loc
	 */
	public void addButton(Location loc) {
		this.buttons.add(loc);
	}
	
}
