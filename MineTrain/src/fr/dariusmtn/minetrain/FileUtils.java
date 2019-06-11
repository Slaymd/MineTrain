package fr.dariusmtn.minetrain;

import org.bukkit.Location;

public class FileUtils {

	private Main plugin;
    public FileUtils(Main instance) {
          this.plugin = instance; 
    }
	
    /**
     * 
     * @param loc
     * @return
     */
    public String locationToString(Location loc) {
    	String locStr = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    	return locStr;
    }
    
    /**
     * 
     * @param locStr
     * @return
     */
    public Location stringToLocation(String locStr) {
    	String[] locChunks = locStr.split(",");
    	Location loc = new Location(plugin.getServer().getWorld(locChunks[0]),Double.valueOf(locChunks[1]),Double.valueOf(locChunks[2]),Double.valueOf(locChunks[3]));
    	return loc;
    }
}
