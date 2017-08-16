package fr.dariusmtn.minetrain;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {
	
	private Main plugin;
    public FileManager(Main instance) {
          this.plugin = instance; 
    }
    
    /**
     * 
     * @return ArrayList<Station>
     */
    public ArrayList<Station> getStations() {
    	ArrayList<Station> stations = new ArrayList<Station>();
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			for(String stationId : stationsconfig.getConfigurationSection("Stations").getKeys(false)) {
				stations.add(getStation(UUID.fromString(stationId)));
			}
			return stations;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 
     * @param stationId
     * @return Station
     */
	@SuppressWarnings("unchecked")
	public Station getStation(UUID stationId) {
    	try {
	    	File stationsfile = new File(plugin.getDataFolder(), "stations.yml");
			stationsfile.createNewFile();
			FileConfiguration stationsconfig = YamlConfiguration.loadConfiguration(stationsfile);
			Map<String, Object> stationMap = (Map<String, Object>) stationsconfig.get("Stations." + stationId.toString());
			Station station = new Station(stationMap);
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
			stationsconfig.set("Stations." + station.getStationId().toString(), station.serialize());
			stationsconfig.save(stationsfile);
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
			for(String lineId : linesconfig.getConfigurationSection("Lines").getKeys(false)) {
				lines.add(getLine(UUID.fromString(lineId)));
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
    @SuppressWarnings("unchecked")
	public Line getLine(UUID lineId) {
    	try {
	    	File linesfile = new File(plugin.getDataFolder(), "lines.yml");
			linesfile.createNewFile();
			FileConfiguration linesconfig = YamlConfiguration.loadConfiguration(linesfile);
			Map<String, Object> lineMap = (Map<String, Object>) linesconfig.get("Lines." + lineId.toString());
			Line line = new Line(lineMap);
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
