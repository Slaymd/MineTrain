package fr.dariusmtn.minetrain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.Station;

public class FileManager {
	
	private Main plugin;
    public FileManager(Main instance) {
          this.plugin = instance; 
    }
    
    //Caches (prevent file load/unload every seconds...)
    private ArrayList<Station> stationsCache = new ArrayList<Station>();
    private ArrayList<Line> linesCache = new ArrayList<Line>();
    
    private HashMap<Location,Station> stationsByLoc = new HashMap<Location,Station>();
    private HashMap<Location,Station> stationByButton = new HashMap<Location,Station>();
    
    /**
     *  
     * @return ArrayList<Station>
     */
    public ArrayList<Station> getStations() {
    	if(stationsCache.size() == 0)
    		this.updateStationCache();
    	return stationsCache;
    }
    
    /**
     * Getting all stations starts
     * @return HashMap<Location,Station>
     */
    public HashMap<Location,Station> getStationsStarts() {
    	if(stationsCache.size() == 0)
    		this.updateStationCache();
    	return stationsByLoc;
    }
    
    /**
     * Getting station by button location
     * @param loc
     * @return
     */
    public Station getStationFromButton(Location loc) {
    	if(stationsCache.size() == 0)
    		this.updateStationCache();
    	return this.stationByButton.get(loc);
    }
    
    /**
     * Check if location is a station button
     * @param loc
     * @return
     */
    public boolean isStationButton(Location loc) {
    	if(stationsCache.size() == 0)
    		this.updateStationCache();
    	if(this.stationByButton.containsKey(loc))
    		return true;
    	return false;
    }
    
    /**
     * update station cache from file
     */
    public void updateStationCache() {
    	ArrayList<Station> stations = new ArrayList<Station>();
    	//Stations from file
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			if(stationsconfig.isSet("Stations")) {
				for(String stationId : stationsconfig.getConfigurationSection("Stations").getKeys(false)) {
					stations.add(getStation(UUID.fromString(stationId)));
				}
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	stationsCache = stations;
    	if(stationsCache.size() > 0) {
	    	//Stations starts cache reset
	    	this.stationsByLoc.clear();
	    	for(Station station : getStations()) {
	    		for(Location start : station.getStarts().keySet()) {
	    			this.stationsByLoc.put(start, station);
	    		}
	    		for(Location button : station.getButtons()) {
	    			this.stationByButton.put(button, station);
	    		}
	    	}
    	}
    }
    
    
    /**
     * 
     * @param stationId
     * @return Station
     */
	public Station getStation(UUID stationId) {
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			if(stationsconfig.isSet("Stations." + stationId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", stationId.toString());
				map.put("name", stationsconfig.get("Stations." + stationId + ".name"));
				map.put("city", stationsconfig.get("Stations." + stationId + ".city"));
				//Starts
				HashMap<Location, ArrayList<Object>> starts = new HashMap<Location, ArrayList<Object>>();
				for(String loc : stationsconfig.getConfigurationSection("Stations." + stationId + ".starts").getKeys(false)) {
					ArrayList<String> startsserialized = (ArrayList<String>) stationsconfig.getStringList("Stations." + stationId + ".starts." + loc);
					Location startdir = plugin.getFileUtils().stringToLocation(startsserialized.get(0));
					UUID startlineid = UUID.fromString(startsserialized.get(1));
					ArrayList<Object> startcontent = new ArrayList<Object>();
					startcontent.add(0, startdir);startcontent.add(1, startlineid);
					Location startloc = plugin.getFileUtils().stringToLocation(loc.replace("*", "."));
					starts.put(startloc, startcontent);
				}
				map.put("buttons", stationsconfig.get("Stations." + stationId + ".buttons"));
				Station station = new Station(map, starts, plugin);
				return station;
			}
			return null;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 
     * @param station
     */
    public void removeStation(Station station) {
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			stationsconfig.set("Stations." + station.getStationId().toString(), null);
			stationsconfig.save(stationsfile);
			//update cache
			this.clearStationCache();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @param station
     */
    public void addStation(Station station) {
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			stationsconfig.set("Stations." + station.getStationId().toString(), station.serialize(plugin));
			stationsconfig.save(stationsfile);
			//update cache
			this.clearStationCache();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void clearStationCache() {
    	this.stationsByLoc.clear();
    	this.stationsCache.clear();
    	this.stationByButton.clear();
    }
    
    /**
     * 
     * @return ArrayList<Line>
     */
    public ArrayList<Line> getLines() {
    	if(linesCache.size() == 0)
    		this.updateLineCache();
    	return linesCache;
    }
    
    /**
     * 
     * @param lineId
     * @return Line
     */
	public Line getLine(UUID lineId) {
    	try {
	    	File linesfile = new File(plugin.getDataFolder(), "lines.yml");
			linesfile.createNewFile();
			FileConfiguration linesconfig = YamlConfiguration.loadConfiguration(linesfile);
			if(linesconfig.isSet("Lines." + lineId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("longname", linesconfig.get("Lines." + lineId + ".longname"));
				map.put("smallname", linesconfig.get("Lines." + lineId + ".smallname"));
				map.put("lineId", lineId);
				map.put("linetype", linesconfig.get("Lines." + lineId + ".linetype"));
				Line line = new Line(map);
				return line;
			}
			return null;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 
     * @param line
     */
    public void removeLine(Line line) {
    	try {
	    	File linesfile = new File(plugin.getDataFolder(), "lines.yml");
			linesfile.createNewFile();
			FileConfiguration linesconfig = YamlConfiguration.loadConfiguration(linesfile);
			linesconfig.set("Lines." + line.getLineId().toString(), null);
			linesconfig.save(linesfile);
			//update cache
			this.clearLineCache();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @param line
     */
    public void addLine(Line line) {
    	try {
	    	File linesfile = new File(plugin.getDataFolder(), "lines.yml");
			linesfile.createNewFile();
			FileConfiguration linesconfig = YamlConfiguration.loadConfiguration(linesfile);
			linesconfig.set("Lines." + line.getLineId().toString(), line.serialize());
			linesconfig.save(linesfile);
			//update cache
			this.clearLineCache();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * update line cache from file
     */
    public void updateLineCache() {
    	ArrayList<Line> lines = new ArrayList<Line>();
    	try {
	    	File linesfile = new File(plugin.getDataFolder(), "lines.yml");
			linesfile.createNewFile();
			FileConfiguration linesconfig = YamlConfiguration.loadConfiguration(linesfile);
			if(linesconfig.isSet("Lines")) {
				for(String lineId : linesconfig.getConfigurationSection("Lines").getKeys(false)) {
					lines.add(getLine(UUID.fromString(lineId)));
				}
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	this.linesCache = lines;
    }
    
    private void clearLineCache() {
    	this.linesCache.clear();
    }

}
