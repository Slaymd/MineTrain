package fr.dariusmtn.minetrain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.Station;

public class LinesMap {

	private Main plugin;
    public LinesMap(Main instance) {
          this.plugin = instance; 
    }
    
    /**
     * Add next stop
     * @param from
     * @param fromstart
     * @param to
     */
    public void setNextStop(Location fromstart, Station to) {
    	try {
    		if(this.getNextStop(fromstart) == null || this.getNextStop(fromstart) != to) {
		    	File linesmapfile = new File(plugin.getDataFolder(), "linesmap.yml");
				linesmapfile.createNewFile();
				FileConfiguration linesmapconfig = YamlConfiguration.loadConfiguration(linesmapfile);
				String loc = plugin.getFileUtils().locationToString(fromstart).replace(".", "*");
				linesmapconfig.set("NextStop." + loc + ".nextstop", to.getStationId().toString());
				linesmapconfig.save(linesmapfile);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public Station getNextStop(Location fromstart) {
    	try {
	    	File linesmapfile = new File(plugin.getDataFolder(), "linesmap.yml");
			linesmapfile.createNewFile();
			FileConfiguration linesmapconfig = YamlConfiguration.loadConfiguration(linesmapfile);
			String loc = plugin.getFileUtils().locationToString(fromstart).replace(".", "*");
			if(linesmapconfig.isSet("NextStop." + loc + ".nextstop")) {
				String stationId = linesmapconfig.getString("NextStop." + loc + ".nextstop");
				return plugin.getFileManager().getStation(UUID.fromString(stationId));
			}
			return null;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * set terminus
     * @param fromstart
     * @param term
     */
    public void setTerminus(Location fromstart, Station term) {
    	try {
	    	File linesmapfile = new File(plugin.getDataFolder(), "linesmap.yml");
			linesmapfile.createNewFile();
			FileConfiguration linesmapconfig = YamlConfiguration.loadConfiguration(linesmapfile);
			String loc = plugin.getFileUtils().locationToString(fromstart);
			linesmapconfig.set("NextStop." + loc + ".terminus", term.getStationId());
			linesmapconfig.save(linesmapfile);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Add a travel
     * @param from
     * @param to
     */
    public void addTravel(Station from, Station to) {
    	try {
	    	File linesmapfile = new File(plugin.getDataFolder(), "linesmap.yml");
			linesmapfile.createNewFile();
			FileConfiguration linesmapconfig = YamlConfiguration.loadConfiguration(linesmapfile);
			ArrayList<UUID> tos = new ArrayList<UUID>();
			if(linesmapconfig.isSet("Travels." + from.getStationId())) {
				for(String uuid : linesmapconfig.getStringList("Travels." + from.getStationId())){
					tos.add(UUID.fromString(uuid));
				}
			}
			tos.add(to.getStationId());
			linesmapconfig.set("Travels." + from.getStationId(), tos);
			linesmapconfig.save(linesmapfile);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Get saved travels
     * @return
     */
    public HashMap<Station,ArrayList<Station>> getTravels() {
    	HashMap<Station,ArrayList<Station>> travels = new HashMap<Station,ArrayList<Station>>();
    	try {
	    	File linesmapfile = new File(plugin.getDataFolder(), "linesmap.yml");
			linesmapfile.createNewFile();
			FileConfiguration linesmapconfig = YamlConfiguration.loadConfiguration(linesmapfile);
			for(String stationId : linesmapconfig.getConfigurationSection("Travels").getKeys(false)) {
				Station from = plugin.getFileManager().getStation(UUID.fromString(stationId));
				ArrayList<Station> tos = new ArrayList<Station>();
				if(linesmapconfig.isSet("Travels." + from.getStationId())) {
					for(String uuid : linesmapconfig.getStringList("Travels." + from.getStationId())){
						tos.add(plugin.getFileManager().getStation(UUID.fromString(uuid)));
					}
				}
				travels.put(from, tos);
			}
			
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return travels;
    }
    
    
    
    public ArrayList<Station> getDirections(Station from, Line line) {
    	ArrayList<Station> directions = new ArrayList<Station>();
    	directions.add(from);
    	HashMap<Station,ArrayList<Station>> travels = getTravels();
    	while(travels.containsKey(from)) {
    		if(from.getLinesId().contains(line.getLineId())) {
    			for(Station station : travels.get(from)) {
    				if(station.getLinesId().contains(line.getLineId())) {
    					if(!directions.contains(station)) {
    						directions.add(station);
    						from = station;
    					}
    				}
    			}
    		}
    	}
    	return directions;
    }
    
    
}
