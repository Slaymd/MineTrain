package fr.dariusmtn.minetrain.commands;

import java.util.HashMap;
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
						if(subcmd.equalsIgnoreCase("saveline")) {
							Line line = plugin.editor.get(player).getLine();
							plugin.getFileManager().addLine(line);
							player.sendMessage("§a§l➤ Line saved! ;)");
							new FancyMessage("[Make a new Line/Station]").color(ChatColor.YELLOW).tooltip("§eMake a new line or station").command("/minetrain create").send(player);
							plugin.editor.remove(player);
							return true;
						}
						//Line type phase
						if(subcmd.equalsIgnoreCase("setlinetype") && phase == 0 && args.length == 2 && LineType.valueOf(args[1]) != null) {
							LineType lt = LineType.valueOf(args[1]);
							plugin.editor.get(player).getLine().setLineType(lt);
							player.sendMessage("§aLine type: " + lt.getName());
							player.sendMessage("§7§l§m-----");
							player.sendMessage("§6➤ What is the line name? §o(Ex: Line 1 or Red Line)");
							player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
							pe.setPhase(1);
							return true;
						}
						//Station line phase
						if(subcmd.equalsIgnoreCase("setstationline") && phase == 10 && args.length == 2 && UUID.fromString(args[1]) != null) {
							UUID lineId = UUID.fromString(args[1]);
							plugin.editor.get(player).getStation().addLineId(lineId);
							player.sendMessage("§aLine added: " + plugin.getFileManager().getLine(lineId).getLongname());
							player.sendMessage("§7§l§m-----");
							player.sendMessage("§6➤ What is the station name? §o(Ex: City Hall)");
							player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
							pe.setPhase(11);
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
							return true;
						}
						//Add point of departure
						if(subcmd.equalsIgnoreCase("addstationpoint") && phase == 14) {
							pe.setPhase(12);
							player.sendMessage("§7§l§m-----");
							player.sendMessage("§6➤ Add a minecart launcher");
							player.sendMessage("§b★ Click on the station track §o(where the minecart will be stopped)");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
							.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("If you need to edit your world before this step, click here :D").command("/minetrainconfig pauseeditor").send(player);
							return true;
						}
						//Station button
						if(subcmd.equalsIgnoreCase("stationpointnextstep") && phase == 14) {
							pe.setPhase(15);
							Station station = pe.getStation();
							player.sendMessage("§eThis station has " + station.getStarts().size() + " point(s) of departure");
							player.sendMessage("§7§l§m-----");
							player.sendMessage("§b★ Click where you want a §ldeparture button§b §o(People will need to click on this button to take a minecart)");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
							.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("If you need to edit your world before this step, click here :D").command("/minetrainconfig pauseeditor").send(player);
							return true;
						}
						//Station add button
						if(subcmd.equalsIgnoreCase("adddeparturebutton") && phase == 16) {
							pe.setPhase(15);
							Station station = pe.getStation();
							player.sendMessage("§eThis station has " + station.getButtons().size() + " departure button(s)");
							player.sendMessage("§7§l§m-----");
							player.sendMessage("§b★ Click where you want a §ldeparture button§b §o(People will need to click on this button to take a minecart)");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
							.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("If you need to edit your world before this step, click here :D").command("/minetrainconfig pauseeditor").send(player);
							return true;
						}
						//Pause editor
						if(subcmd.equalsIgnoreCase("pauseeditor")) {
							pausedEditor.put(player, pe.getPhase());
							pe.setPhase(666);
							player.sendMessage("§dEditor paused.");
							new FancyMessage("[Resume editor]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("§cResume editor where you paused it").command("/minetrainconfig resumeeditor").send(player);
						}
						//Resume editor
						if(subcmd.equalsIgnoreCase("resumeeditor") && pausedEditor.containsKey(player)) {
							pe.setPhase(pausedEditor.get(player));
							pausedEditor.remove(player);
							player.sendMessage("§dEditor resumed where you paused it!");
							new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
						}
						//Save station
						if(subcmd.equalsIgnoreCase("savestation") && phase == 16) {
							Station station = plugin.editor.get(player).getStation();
							plugin.getFileManager().addStation(station);
							player.sendMessage("§a§l➤ Station saved! ;)");
							new FancyMessage("[Make a new Station/Line]").color(ChatColor.YELLOW).tooltip("§eMake a new station or line").command("/minetrain create").send(player);
							plugin.editor.remove(player);
						}
					}
				}
			}
		}
		return false;
	}
	
}
