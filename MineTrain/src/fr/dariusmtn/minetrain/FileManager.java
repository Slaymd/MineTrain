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
    
    private ArrayList<Station> stationsCache = new ArrayList<Station>();
    private HashMap<Location,Station> stationsByLoc = new HashMap<Location,Station>();
    
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
    	//Stations starts cache reset
    	this.stationsByLoc.clear();
    	for(Station station : getStations()) {
    		for(Location start : station.getStarts().keySet()) {
    			this.stationsByLoc.put(start, station);
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", stationsconfig.get("Stations." + stationId + ".name"));
			map.put("city", stationsconfig.get("Stations." + stationId + ".city"));
			map.put("lineId", stationsconfig.get("Stations." + stationId + ".lineId"));
			map.put("starts.loc", stationsconfig.get("Stations." + stationId + ".starts.loc"));
			map.put("starts.dir", stationsconfig.get("Stations." + stationId + ".starts.dir"));
			map.put("buttons", stationsconfig.get("Stations." + stationId + ".buttons"));
			Station station = new Station(map, plugin);
			return station;
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
			this.updateStationCache();
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
			this.updateStationCache();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 
     * @return ArrayList<Line>
     */
    public ArrayList<Line> getLines() {
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
			return lines;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("longname", linesconfig.get("Lines." + lineId + ".longname"));
			map.put("smallname", linesconfig.get("Lines." + lineId + ".smallname"));
			map.put("lineId", linesconfig.get("Lines." + lineId + ".lineId"));
			map.put("linetype", linesconfig.get("Lines." + lineId + ".linetype"));
			Line line = new Line(map);
			return line;
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
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
