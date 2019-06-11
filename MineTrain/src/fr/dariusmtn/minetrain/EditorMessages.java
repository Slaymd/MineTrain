package fr.dariusmtn.minetrain;

import java.util.EnumSet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.LineType;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class EditorMessages {

	private Main plugin;
	public EditorMessages(Main plugin) {
		this.plugin = plugin;
	}
	
	public void sendEditorMessage(Player player, int phase) {
		PlayerEditor pe = plugin.editor.get(player);
		FancyMessage fm = new FancyMessage();
		if(phase == 0) {
			player.sendMessage("§6➤ Select line type:");
			for(LineType lt : EnumSet.allOf(LineType.class)) {
				new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then(lt.getName())
				.color(ChatColor.AQUA).tooltip("§7Select §f" + lt.getName() + "§7 line type.")
				.command("/minetrainconfig setlinetype " + lt.toString()).send(player);
			}
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor");
		} else if(phase == 1) {
			player.sendMessage("§6➤ What is the line name? §o(Ex: Line 1 or Red Line)");
			player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor");
		} else if(phase == 2) {
			player.sendMessage("§6➤ What is the line acronym? §o(Ex: L1 or L-Red)");
			player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
			player.sendMessage("§7✏ Write §f§onothing§7 for no acronym");
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor");
		} else if(phase == 3) {
			Line line = pe.getLine();
			player.sendMessage("§2§l➤ That's right?");
			if(line.getLineType() != null)
				player.sendMessage("§eLine: §o" + line.getLineType().getName() + "§e " + line.getName() + "§e " + (line.getAcronym() == line.getName() ? "" : "(" + line.getAcronym() + "§e)"));
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
			.then(" ").then("[Save it!]").color(ChatColor.GREEN).style(ChatColor.BOLD).tooltip("§aSave as a new line").command("/minetrainconfig saveline");
		} else if(phase == 10) {
			player.sendMessage("§6➤ What is the station name? §o(Ex: City Hall)");
			player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
	    	fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor");
		} else if(phase == 11) {
			player.sendMessage("§6➤ Add a minecart launcher");
			player.sendMessage("§b★ §nLeft click§b on the station track §o(where the minecart will be stopped)");
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
			.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("§dIf you need to edit your world before this step, click here :D")
			.command("/minetrainconfig pauseeditor");
		} else if(phase == 12) {
			player.sendMessage("§b★ §nLeft click§b on the §ldirection track§b §e(Powered Rail)§b just next to the station point §o(in what direction the minecart will be launched)");
			player.sendMessage("§7★ Left click on the §lstation track§7 §o(Detector Rail)§7 to set as a terminus");
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
			.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("§dIf you need to edit your world before this step, click here :D")
			.command("/minetrainconfig pauseeditor");
		} else if(phase == 13) {
			player.sendMessage("§bTo which line this point of departure will be added?");
	    	for(Line ln : plugin.getFileManager().getLines()) {
	    		new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then(ln.getName() + "§o " + ln.getLineType().getName())
				.color(ChatColor.GREEN).tooltip("§7Select this line.")
				.command("/minetrainconfig setstartline " + ln.getLineId()).send(player);
	    	}
	    	fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor");
		} else if(phase == 14) {
			Station station = pe.getStation();
			player.sendMessage("§eThis station has " + station.getStarts().size() + " point(s) of departure");
			//Remove departures
			for(Location loc : station.getStartLocations()) {
				new FancyMessage(" • ").color(ChatColor.GOLD).then("§a" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "§f§o (" + (int)loc.distance(player.getLocation()) + "m)")
				.then(" ").then("[✕]").color(ChatColor.RED).tooltip("§cDelete this point")
				.command("/minetrainconfig deletedeparturepoint " + plugin.getFileUtils().locationToString(loc)).send(player);
			}
			//Command buttons
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
			.then(" ").then("[Add point of departure]").color(ChatColor.AQUA).tooltip("§aAdd point of departure (other directions ?)").command("/minetrainconfig addstationpoint")
			.then(" ").then("[Next Step]").color(ChatColor.GOLD).style(ChatColor.BOLD).tooltip("§aGo to next step").command("/minetrainconfig stationpointnextstep");
		} else if(phase == 15) {
			player.sendMessage("§6➤ Add a departure button");
			player.sendMessage("§b★ §nRight Click§b where you want a §ldeparture button§b §o(People will need to click on this button to take a minecart)");
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
			.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("§dIf you need to edit your world before this step, click here :D")
			.command("/minetrainconfig pauseeditor");
		} else if(phase == 16) {
			Station station = pe.getStation();
			player.sendMessage("§eThis station has " + station.getButtons().size() + " departure button(s)");
			//Remove buttons
			for(Location loc : station.getButtons()) {
				new FancyMessage(" • ").color(ChatColor.GOLD).then("§a" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "§f§o (" + (int)loc.distance(player.getLocation()) + "m)")
				.then(" ").then("[✕]").color(ChatColor.RED).tooltip("§cDelete this button")
				.command("/minetrainconfig deletebutton " + plugin.getFileUtils().locationToString(loc)).send(player);
			}
			fm = new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
			.then(" ").then("[Add departure button]").color(ChatColor.AQUA).tooltip("§aAdd departure button").command("/minetrainconfig adddeparturebutton")
			.then(" ").then("[Finish and save]").color(ChatColor.GOLD).style(ChatColor.BOLD).tooltip("§aFinish").command("/minetrainconfig savestation");
		} else {
			return;
		}
		//Button Next and Back
		backNextButton(player,phase, fm);
	}
	
	private void backNextButton(Player player, int phase, FancyMessage fm) {
		int backstep = phase-1;
		int nextstep = phase+1;
		
		if(phase == 10) {
			backstep = -1;
		} else if (phase == 11 ) {
			nextstep = 15;
		} else if (phase > 11 && phase < 14) {
			backstep = -1;
			nextstep = -1;
		} else if (phase == 14) {
			backstep = 10;
			nextstep = -1;
		} else if (phase == 15) {
			backstep = 11;
		} else if (phase == 16) {
			nextstep = -1;
		} else if (phase == 3) {
			nextstep = -1;
		}
		
		player.sendMessage("§7§l§m-----");
		//Final buttons
		if(backstep != -1 && nextstep != -1)
			fm.then(" ").then("[Back]").color(ChatColor.GRAY).tooltip("§7Return to the last step §8(#" + backstep + ")").command("/minetrainconfig goto " + backstep)
			.then(" ").then("[Next]").color(ChatColor.AQUA).tooltip("§bGo to next step §8(#" + nextstep + ")").command("/minetrainconfig goto " + nextstep).send(player);
		if(backstep == -1 && nextstep != -1)
			fm.then(" ").then("[Next]").color(ChatColor.AQUA).tooltip("§bGo to next step §8(#" + nextstep + ")").command("/minetrainconfig goto " + nextstep).send(player);
		if(backstep != -1 && nextstep == -1)
			fm.then(" ").then("[Back]").color(ChatColor.GRAY).tooltip("§7Return to the last step §8(#" + backstep + ")").command("/minetrainconfig goto " + backstep).send(player);
	}
	
	
}
