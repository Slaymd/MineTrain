package fr.dariusmtn.minetrain.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class MineTrainConfigCommandExecutor implements CommandExecutor {
	
	private Main plugin;
    public MineTrainConfigCommandExecutor(Main instance) {
          this.plugin = instance; 
    }
    
    HashMap<Player,Integer> pausedEditor = new HashMap<Player,Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("minetrainconfig")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				if(args.length > 0) {
					String subcmd = args[0];
					if(plugin.editor.containsKey(player)) {
						PlayerEditor pe = plugin.editor.get(player);
						int phase = pe.getPhase();
						//Cancel editor
						if(subcmd.equalsIgnoreCase("canceleditor")) {
							plugin.editor.remove(player);
							player.sendMessage("§cEditor left.");
							return true;
						}
						//Save line
						if(subcmd.equalsIgnoreCase("saveline") && phase == 3) {
							Line line = plugin.editor.get(player).getLine();
							//Verifying line content
							player.sendMessage(" ");
							if(line.getName() != "") {
								if(line.getLineType() != null) {
									plugin.getFileManager().addLine(line);
									
									player.sendMessage("§a§l➤ Line saved! ;)");
									new FancyMessage("[Make a new Line/Station]").color(ChatColor.YELLOW).tooltip("§eMake a new line or station").command("/minetrain create").send(player);
									plugin.editor.remove(player);
									return true;
								}
								player.sendMessage("§4§l!§c Line has no §ltype§c.");
								plugin.getEditorMessages().sendEditorMessage(player, 0);
								pe.setPhase(0);
								return true;
							}
							player.sendMessage("§4§l!§c Line has no §lname§c.");
							plugin.getEditorMessages().sendEditorMessage(player, 1);
							pe.setPhase(1);
							return true;
						}
						//Line type phase
						if(subcmd.equalsIgnoreCase("setlinetype") && phase == 0 && args.length == 2 && LineType.valueOf(args[1]) != null) {
							LineType lt = LineType.valueOf(args[1]);
							plugin.editor.get(player).getLine().setLineType(lt);
							player.sendMessage(" ");
							player.sendMessage("§aLine type: " + lt.getName());
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 1);
							pe.setPhase(1);
							return true;
						}
						//Station line phase
						if(subcmd.equalsIgnoreCase("setstartline") && phase == 13 && args.length == 2 && UUID.fromString(args[1]) != null && plugin.stlocEditor.containsKey(player)) {
							UUID lineId = UUID.fromString(args[1]);
							ArrayList<Location> startcrea = plugin.stlocEditor.get(player);
							Station station = plugin.editor.get(player).getStation();
							station.addStarts(startcrea.get(0), startcrea.get(1), lineId);;
							plugin.stlocEditor.remove(player);
							//Messages
							player.sendMessage(" ");
							player.sendMessage("§aLine added: " + plugin.getFileManager().getLine(lineId).getName());
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 14);
							pe.setPhase(14);
							return true;
						}
						//Add point of departure
						if(subcmd.equalsIgnoreCase("addstationpoint") && phase == 14) {
							pe.setPhase(11);
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 11);
							return true;
						}
						//Station button
						if(subcmd.equalsIgnoreCase("stationpointnextstep") && phase == 14) {
							pe.setPhase(15);
							player.sendMessage(" ");
							plugin.getEditorMessages().sendEditorMessage(player, 15);
							return true;
						}
						//Station add button
						if(subcmd.equalsIgnoreCase("adddeparturebutton") && phase == 16) {
							pe.setPhase(15);
							player.sendMessage(" ");
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 15);
							return true;
						}
						//Pause editor
						if(subcmd.equalsIgnoreCase("pauseeditor")) {
							pausedEditor.put(player, pe.getPhase());
							pe.setPhase(666);
							player.sendMessage("§dEditor paused.");
							new FancyMessage("[Resume editor]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("§cResume editor where you paused it").command("/minetrainconfig resumeeditor").send(player);
							return true;
						}
						//Resume editor
						if(subcmd.equalsIgnoreCase("resumeeditor") && pausedEditor.containsKey(player)) {
							pe.setPhase(pausedEditor.get(player));
							pausedEditor.remove(player);
							player.sendMessage("§dEditor resumed where you paused it!");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
							return true;
						}
						//Save station
						if(subcmd.equalsIgnoreCase("savestation") && phase == 16) {
							Station station = plugin.editor.get(player).getStation();
							player.sendMessage(" ");
							//Verifying station content
							if(station.getButtons().size() > 0) {
								if(station.getStartLocations().size() > 0) {
									if(station.getName() != "") {
										plugin.getFileManager().addStation(station);
										
										player.sendMessage("§a§l➤ Station saved! ;)");
										new FancyMessage("[Make a new Station/Line]").color(ChatColor.YELLOW).tooltip("§eMake a new station or line").command("/minetrain create").send(player);
										plugin.editor.remove(player);
										return true;
									}
									player.sendMessage("§4§l!§c Station has no §lname§c.");
									plugin.getEditorMessages().sendEditorMessage(player, 10);
									pe.setPhase(10);
									return true;
								}
								player.sendMessage("§4§l!§c Station has no §lminecart stop/launcher§c.");
								plugin.getEditorMessages().sendEditorMessage(player, 11);
								pe.setPhase(11);
								return true;
							}
							player.sendMessage("§4§l!§c Station has no §lstart button§c.");
							plugin.getEditorMessages().sendEditorMessage(player, 15);
							pe.setPhase(15);
							return true;
						}
						//Goto step
						if(subcmd.equalsIgnoreCase("goto") && args.length == 2) {
							int newphase = Integer.parseInt(args[1]);
							player.sendMessage(" ");
							pe.setPhase(newphase);
							plugin.getEditorMessages().sendEditorMessage(player, newphase);
							return true;
						}
						//Delete departure point
						if(subcmd.equalsIgnoreCase("deletedeparturepoint") && args.length == 2 && phase == 14) {
							Location loc = plugin.getFileUtils().stringToLocation(args[1]);
							Station station = plugin.editor.get(player).getStation();
							station.removeStarts(loc);
							player.sendMessage(" ");
							player.sendMessage("§cDeparture deleted: §f(§7§o" + loc.getX() + "§f, §7§o" + loc.getY() + "§f, §7§o" + loc.getZ() + "§f)");
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 14);
						}
						//Delete button
						if(subcmd.equalsIgnoreCase("deletebutton") && args.length == 2 && phase == 16) {
							Location loc = plugin.getFileUtils().stringToLocation(args[1]);
							Station station = plugin.editor.get(player).getStation();
							station.removeButton(loc);
							loc.getBlock().setType(Material.AIR);
							player.sendMessage(" ");
							player.sendMessage("§cButton deleted: §f(§7§o" + loc.getX() + "§f, §7§o" + loc.getY() + "§f, §7§o" + loc.getZ() + "§f)");
							player.sendMessage("§7§l§m-----");
							plugin.getEditorMessages().sendEditorMessage(player, 16);
						}
					} else {
						if(subcmd.equalsIgnoreCase("editline") && args.length == 2 && UUID.fromString(args[1]) != null && player.hasPermission("minetrain.admin.edit")){
							//TODO permission
							UUID lineid = UUID.fromString(args[1]);
							Line line = plugin.getFileManager().getLine(lineid);
							plugin.editor.put(player, new PlayerEditor(0,line));
							//Messages
							player.sendMessage(" ");
					    	player.sendMessage("§d§l➤ Edit a line.");
							plugin.getEditorMessages().sendEditorMessage(player, 0);
						}
						if(subcmd.equalsIgnoreCase("editstation") && args.length == 2 && UUID.fromString(args[1]) != null && player.hasPermission("minetrain.admin.edit")){
							//TODO permission
							UUID stationid = UUID.fromString(args[1]);
							Station station = plugin.getFileManager().getStation(stationid);
							plugin.editor.put(player, new PlayerEditor(10,station));
							//Messages
							player.sendMessage(" ");
					    	player.sendMessage("§d§l➤ Edit a station.");
							plugin.getEditorMessages().sendEditorMessage(player, 10);
						}
						if(subcmd.equalsIgnoreCase("deleteline") && args.length == 2 && UUID.fromString(args[1]) != null && player.hasPermission("minetrain.admin.remove")){
							//TODO permission
							//Delete line
							UUID lineid = UUID.fromString(args[1]);
							Line line = plugin.getFileManager().getLine(lineid);
							plugin.getFileManager().removeLine(line);
							
							//Messages
							player.sendMessage(" ");
							player.sendMessage("§cDeleted line: §7§m" + ChatColor.stripColor(line.getName()));
							player.sendMessage("§7§l§m-----");
							player.performCommand("minetrain list");
						}
						if(subcmd.equalsIgnoreCase("deletestation") && args.length == 2 && UUID.fromString(args[1]) != null && player.hasPermission("minetrain.admin.remove")){
							//TODO permission
							//Delete station
							UUID stationid = UUID.fromString(args[1]);
							Station station = plugin.getFileManager().getStation(stationid);
							plugin.getFileManager().removeStation(station);
							
							//Messages
							player.sendMessage(" ");
							player.sendMessage("§cDeleted station: §7§m" + ChatColor.stripColor(station.getName()));
							player.sendMessage("§7§l§m-----");
							player.performCommand("minetrain list");
						}
					}
				}
			}
		}
		return false;
	}
	
}
