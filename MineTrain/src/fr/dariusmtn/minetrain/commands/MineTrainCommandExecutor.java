package fr.dariusmtn.minetrain.commands;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.LineType;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class MineTrainCommandExecutor implements CommandExecutor{

	private Main plugin;
    public MineTrainCommandExecutor(Main instance) {
          this.plugin = instance; 
    }
    
    void createLine(Player player, String headmessage){
    	player.sendMessage("§d§l➤ " + headmessage);
		plugin.editor.put(player, new PlayerEditor(0,new Line()));
		//Generate Line ID
		plugin.editor.get(player).getLine().setLineId(UUID.randomUUID());
		//LineType
		player.sendMessage("§6➤ Select line type:");
		for(LineType lt : EnumSet.allOf(LineType.class)) {
			new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then(lt.getName())
			.color(ChatColor.AQUA).tooltip("§7Select §f" + lt.getName() + "§7 line type.")
			.command("/minetrainconfig setlinetype " + lt.toString()).send(player);
		}
		new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
		player.sendMessage("§7§l§m-----");
    }
    
    void createStation(Player player) {
    	player.sendMessage("§d§l➤ Make a new station.");
    	plugin.editor.put(player, new PlayerEditor(10,new Station()));
    	//Generate Station ID
    	plugin.editor.get(player).getStation().setStationId(UUID.randomUUID());
    	//Which line
    	player.sendMessage("§6➤ To which line this station will be added?");
    	for(Line ln : plugin.getFileManager().getLines()) {
    		new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then(ln.getLongname() + "§o " + ln.getLineType().getName())
			.color(ChatColor.AQUA).tooltip("§7Select this line.")
			.command("/minetrainconfig setstationline " + ln.getLineId()).send(player);
    	}
    	new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
		player.sendMessage("§7§l§m-----");
    }
    
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("minetrain")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				if(args.length > 0) {
					String subcmd = args[0];
					//create subcommand
					if(subcmd.equalsIgnoreCase("create")) {
						//TODO permission
						if(player.isOp()) {
							if(!plugin.editor.containsKey(player)) {
								int linesnb = plugin.getFileManager().getLines().size();
								if(args.length == 1) {
									//if no line, create one
									if(linesnb == 0) {
										createLine(player, "Make your first line.");
										return true;
									}
									//if line exit, create line or station.
									else {
										player.sendMessage("§6➤ What you want to §lcreate§6 ? :D");
										new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then("Line").color(ChatColor.AQUA)
										.tooltip("§eMake a new line").command("/minetrain create line").send(player);
										new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then("Station").color(ChatColor.AQUA)
										.tooltip("§eMake a new station").command("/minetrain create station").send(player);
										return true;
									}
								} 
								//if arg is line or station
								else {
									if(args.length == 2) {
										String action = args[1];
										//Create a new line
										if(action.equalsIgnoreCase("line")) {
											createLine(player, "Make a new line.");
										}
										//Create a new station
										else if(action.equalsIgnoreCase("station")) {
											createStation(player);
										}
									}
									return true;
								}
							}
							player.sendMessage("§cYou are already in the editor.");
							return false;
						}
					}
					//List command
					if(subcmd.equalsIgnoreCase("list")) {
						//HashMap<Line,ArrayList<Station>> linesWstation = new HashMap<Line,ArrayList<Station>>();
						ArrayList<Station> stations = plugin.getFileManager().getStations();
						player.sendMessage("station nb: " + plugin.getFileManager().getStations().size());
						for(Station station : stations) {
							if(station != null) {
								player.sendMessage("§7" + station.getName() + " §b| loc: " + station.getStarts().toString());
							}
						}
						return true;
					}
				}
				player.sendMessage("§bMineTrain v" + plugin.getDescription().getVersion() + " by §lSlaymd§b.");
				player.sendMessage("§7§l§m-----");
				player.sendMessage("§6§lCREATE LINE OR STATION");
				player.sendMessage("§e/mtn §2create");
				player.sendMessage("§7§l§m-----");
			}
		}
    	return false;
    }
	
}
