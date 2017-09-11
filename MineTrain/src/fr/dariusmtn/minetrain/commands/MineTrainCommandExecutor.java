package fr.dariusmtn.minetrain.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class MineTrainCommandExecutor implements CommandExecutor{

	private Main plugin;
    public MineTrainCommandExecutor(Main instance) {
          this.plugin = instance; 
    }
    
    void createLine(Player player, String headmessage){
    	player.sendMessage(" ");
    	player.sendMessage("§d§l➤ " + headmessage);
		plugin.editor.put(player, new PlayerEditor(0,new Line()));
		//Generate Line ID
		plugin.editor.get(player).getLine().setLineId(UUID.randomUUID());
		//LineType
		
		plugin.getEditorMessages().sendEditorMessage(player, 0);
    }
    
    void createStation(Player player) {
    	if(plugin.getFileManager().getLines().size() > 0) {
	    	player.sendMessage(" ");
	    	player.sendMessage("§d§l➤ Make a new station.");
	    	plugin.editor.put(player, new PlayerEditor(10,new Station()));
	    	//Generate Station ID
	    	plugin.editor.get(player).getStation().setStationId(UUID.randomUUID());
	    	//Setting name
	    	plugin.getEditorMessages().sendEditorMessage(player, 10);
    	} else {
    		player.sendMessage("§cYou need to make a line first!");
    	}
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
						if(player.hasPermission("minetrain.admin.create")) {
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
						player.sendMessage("§cSorry! You don't have permission to do that :(");
						return false;
					}
					//Launch minecart (hidden command) (from station button)
					if(subcmd.equalsIgnoreCase("launchfrom")) {
						if(args.length == 2) {
							Location startloc = plugin.getFileUtils().stringToLocation(args[1]);
							if(player.getLocation().distance(startloc) <= 10) {
								Station station = plugin.getFileManager().getStationsStarts().get(startloc);
								Minecart cart = startloc.getWorld().spawn(startloc, Minecart.class);
								cart.addPassenger(player);
								cart.setVelocity(station.getStartDirection(startloc).multiply(0.1));
								plugin.playerLastStation.put(player, null);
							}
						}
						return true;
					}
					//List command
					if(subcmd.equalsIgnoreCase("list")) {
						if(player.hasPermission("minetrain.admin.list")) {
							//Lines
							player.sendMessage("§6§lLINES");
							for(Line line : plugin.getFileManager().getLines()) {
								FancyMessage fm = new FancyMessage(" • ").color(ChatColor.GOLD).then("§a" + line.getName());
								//Edit
								if(player.hasPermission("minetrain.admin.edit"))
									fm.then(" ").then("[✏]").color(ChatColor.AQUA).tooltip("§cEdit this line").command("/minetrainconfig editline " + line.getLineId().toString());
								//Delete
								if(player.hasPermission("minetrain.admin.remove"))
									fm.then(" ").then("[✕]").color(ChatColor.RED).tooltip("§cDelete this line").command("/minetrainconfig deleteline " + line.getLineId().toString());
								//Send
								fm.send(player);
							}
							//Stations
							player.sendMessage("§6§lSTATIONS");
							for(Station station : plugin.getFileManager().getStations()) {
								FancyMessage fm = new FancyMessage(" • ").color(ChatColor.GOLD).then("§a" + station.getName());
								//Edit
								if(player.hasPermission("minetrain.admin.edit"))
									fm.then(" ").then("[✏]").color(ChatColor.AQUA).tooltip("§cEdit this station").command("/minetrainconfig editstation " + station.getStationId().toString());
								//Delete
								if(player.hasPermission("minetrain.admin.remove"))
									fm.then(" ").then("[✕]").color(ChatColor.RED).tooltip("§cDelete this station").command("/minetrainconfig deletestation " + station.getStationId().toString());
								//Send
								fm.send(player);
							}
							return true;
						}
						player.sendMessage("§cSorry! You don't have permission to do that :(");
						return false;
					}
				}
				player.sendMessage("§bMineTrain v" + plugin.getDescription().getVersion() + " by §lSlaymd§b.");
				player.sendMessage("§7§l§m-----");
				player.sendMessage("§6§lCREATE LINE OR STATION");
				new FancyMessage("§e/mtn §2create").tooltip("§bClick here!").command("/minetrain create").send(player);
				player.sendMessage("§7§lLIST OF ALL LINES & STATIONS");
				new FancyMessage("§e/mtn §blist").tooltip("§bClick here!").command("/minetrain list").send(player);
				player.sendMessage("§7§l§m-----");
				player.sendMessage("§d§lJOIN US!§e Come on §nDiscord§e :D (discuss, help, bugs...)");
				player.sendMessage("§bhttps://discord.gg/w628upr");
			}
		}
    	return false;
    }
	
}
