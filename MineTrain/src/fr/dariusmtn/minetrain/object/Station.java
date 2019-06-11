package fr.dariusmtn.minetrain.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.dariusmtn.minetrain.Main;

public class Station{

	private UUID stationId = null;
	private String name = "";
	private String city = "";
	private Map<Location, ArrayList<Object>> starts = new HashMap<Location, ArrayList<Object>>();
	private ArrayList<Location> buttons = new ArrayList<Location>();
	
	public Station(UUID stationId, String name, Map<Location, ArrayList<Object>> starts, ArrayList<Location> buttons) {
		this.stationId = stationId;
		this.setName(name);
		this.starts = starts;
		this.setButtons(buttons);
	}
	
	public Station() {
		//empty
	}
	
	@SuppressWarnings("unchecked")
	public Station(Map<String, Object> map, HashMap<Location, ArrayList<Object>> starts, Main plugin) {
		this.stationId = UUID.fromString((String) map.get("id"));
		this.name = (String) map.get("name");
		this.city = (String) map.get("city");
		//Starts
		this.starts = starts;
		//Buttons
		ArrayList<String> btns = (ArrayList<String>) map.get("buttons");
		ArrayList<Location> buttons = new ArrayList<Location>();
		for(String loc : btns) {
			buttons.add(plugin.getFileUtils().stringToLocation(loc));
		}
		this.buttons = buttons;
	}
	
	/**
	 * Serialize station
	 * @param plugin
	 * @return
	 */
	public Map<String, Object> serialize(Main plugin) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", this.name);
		map.put("city", this.city);
		Map<String, ArrayList<String>> startsserialized = new HashMap<String, ArrayList<String>>();
		for(Location start : starts.keySet()) {
			String loc = plugin.getFileUtils().locationToString(start).replace(".", "*");
			String dir = plugin.getFileUtils().locationToString((Location) starts.get(start).get(0));
			String lineId = ((UUID) starts.get(start).get(1)).toString();
			ArrayList<String> startcontentseria = new ArrayList<String>();
			startcontentseria.add(0, dir);startcontentseria.add(1, lineId);
			startsserialized.put(loc, startcontentseria);
		}
		map.put("starts", startsserialized);
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
	 * @return the starts
	 */
	public HashMap<Location, ArrayList<Object>> getStarts() {
		return (HashMap<Location, ArrayList<Object>>) starts;
	}

	/**
	 * @param starts the starts to set
	 */
	public void setStarts(HashMap<Location, ArrayList<Object>> starts) {
		this.starts = starts;
	}
	
	/**
	 * List of start locations
	 * @return ArrayList<Location>
	 */
	public ArrayList<Location> getStartLocations() {
		ArrayList<Location> startsloc = new ArrayList<Location>();
		startsloc.addAll(this.starts.keySet());
		return startsloc;
	}
	
	/**
	 * 
	 * @param startloc
	 * @return Location
	 */
	public Location getStartDirectionLocation(Location startloc) {
		ArrayList<Object> startcontent = this.starts.get(startloc);
		return (Location) startcontent.get(0);
	}
	
	/**
	 * Getting cart launcher vector from a start location
	 * @param startloc
	 * @return
	 */
	public Vector getStartDirection(Location startloc) {
		Location dirloc = this.getStartDirectionLocation(startloc);
		if(dirloc.distance(startloc) == 0)
			return null;
		Vector dir = dirloc.toVector().subtract(startloc.toVector());
		return dir;
	}
	
	/**
	 * Getting line at a start location
	 * @param startloc
	 * @return
	 */
	public UUID getStartLineId(Location startloc) {
		ArrayList<Object> startcontent = this.starts.get(startloc);
		return (UUID) startcontent.get(1);
	}
	
	/**
	 * 
	 * @param loc
	 * @param dir
	 */
	public void addStarts(Location loc, Location dir, UUID lineId) {
		ArrayList<Object> startcontent = new ArrayList<Object>();
		startcontent.add(0, dir);
		startcontent.add(1, lineId);
		this.starts.put(loc, startcontent);
	}
	
	/**
	 * 
	 * @param loc
	 */
	public void removeStarts(Location loc) {
		this.starts.remove(loc);
	}
	
	/**
	 * Get lines at this station from all starts
	 * @return
	 */
	public ArrayList<UUID> getLinesId() {
		ArrayList<UUID> linesId = new ArrayList<UUID>();
		for(Location loc : this.getStartLocations()) {
			linesId.add(this.getStartLineId(loc));
		}
		return linesId;
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
