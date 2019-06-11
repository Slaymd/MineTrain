package fr.dariusmtn.minetrain;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.dariusmtn.minetrain.commands.MineTrainCommandExecutor;
import fr.dariusmtn.minetrain.commands.MineTrainConfigCommandExecutor;
import fr.dariusmtn.minetrain.commands.MineTrainTabCompleter;
import fr.dariusmtn.minetrain.listeners.AsyncPlayerChatListener;
import fr.dariusmtn.minetrain.listeners.PlayerInteractListener;
import fr.dariusmtn.minetrain.listeners.VehicleDestroyListener;
import fr.dariusmtn.minetrain.listeners.VehicleExitListener;
import fr.dariusmtn.minetrain.listeners.VehicleMoveListener;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;

public class Main extends JavaPlugin{
	
	private FileManager fileManager = new FileManager(this);
	private FileUtils fileUtils = new FileUtils(this);
	private LinesMap linesMap = new LinesMap(this);
	private EditorMessages editorMessages = new EditorMessages(this);
	private EntityPusher entityPusher = new EntityPusher();
	
	public HashMap<Player,PlayerEditor> editor = new HashMap<Player,PlayerEditor>();
	
	public HashMap<Player,Location> playerLastStation = new HashMap<Player,Location>();
	
	public HashMap<Player,ArrayList<Location>> stlocEditor = new HashMap<Player,ArrayList<Location>>();
	public String version;

	public void onEnable() {
		ConfigurationSerialization.registerClass(Line.class);
		//Commands executors
		this.getCommand("minetrain").setExecutor(new MineTrainCommandExecutor(this));
		this.getCommand("minetrain").setTabCompleter(new MineTrainTabCompleter());
		this.getCommand("minetrainconfig").setExecutor(new MineTrainConfigCommandExecutor(this));
		//Listeners
		PluginManager plugman = getServer().getPluginManager();
		plugman.registerEvents(new AsyncPlayerChatListener(this), this);
		plugman.registerEvents(new PlayerInteractListener(this), this);
		plugman.registerEvents(new VehicleMoveListener(this), this);
		plugman.registerEvents(new VehicleExitListener(this), this);
		plugman.registerEvents(new VehicleDestroyListener(this), this);
		//Config
		saveDefaultConfig();
		//Stats (bstats) https://bstats.org/plugin/bukkit/MineTrain
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);

		String ver = getServer().getClass().getPackage().getName();
		String sub = ver.substring(ver.lastIndexOf('.') + 1);
		version = sub;

	}


	public void onDisable() {
		Bukkit.getPluginManager().disablePlugin(this);
	}
	/**
	 * Lines map AI
	 * @return
	 */
	public LinesMap getLinesMap() {
		return linesMap;
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


	/**
	 * Returns server version
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Send editor message
	 * @return
	 */
	public EditorMessages getEditorMessages() {
		return editorMessages;
	}
	/**
	 * Push/kill entities
	 * @return
	 */
	public EntityPusher getEntityPusher() {
		return entityPusher;
	}

}