package fr.dariusmtn.minetrain;

import java.util.HashMap;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dariusmtn.minetrain.commands.MineTrainCommandExecutor;
import fr.dariusmtn.minetrain.commands.MineTrainConfigCommandExecutor;
import fr.dariusmtn.minetrain.listeners.AsyncPlayerChatListener;
import fr.dariusmtn.minetrain.listeners.PlayerInteractListener;
import fr.dariusmtn.minetrain.listeners.VehicleMoveListener;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;

public class Main extends JavaPlugin{
	
	private FileManager fileManager = new FileManager(this);
	private FileUtils fileUtils = new FileUtils(this);
	
	public HashMap<Player,PlayerEditor> editor = new HashMap<Player,PlayerEditor>();
	
	public void onEnable() {
		ConfigurationSerialization.registerClass(Line.class);
		//Commands executors
		this.getCommand("minetrain").setExecutor(new MineTrainCommandExecutor(this));
		this.getCommand("minetrainconfig").setExecutor(new MineTrainConfigCommandExecutor(this));
		//Listeners
		PluginManager plugman = getServer().getPluginManager();
		plugman.registerEvents(new AsyncPlayerChatListener(this), this);
		plugman.registerEvents(new PlayerInteractListener(this), this);
		plugman.registerEvents(new VehicleMoveListener(this), this);
		//Config
		this.saveDefaultConfig();
	}
	
	/**
	 * Get/Set/Add/Remove Line and Station
	 * @return FileManager
	 */
	public FileManager getFileManager() {
		return fileManager;
	}
	
	/**
	 * File utils
	 * @return
	 */
	public FileUtils getFileUtils() {
		return fileUtils;
	}

}