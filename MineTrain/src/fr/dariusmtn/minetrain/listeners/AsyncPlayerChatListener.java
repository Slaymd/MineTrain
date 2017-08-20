package fr.dariusmtn.minetrain.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class AsyncPlayerChatListener implements Listener{
	
	private Main plugin;
	public AsyncPlayerChatListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player player = e.getPlayer();
		if(plugin.editor.containsKey(player)) {
			e.setCancelled(true);
			PlayerEditor pe = plugin.editor.get(player);
			int phase = pe.getPhase();
			String msg = e.getMessage();
			//Line name
			if(phase == 1) { 
				Line line = pe.getLine();
				line.setLongname(msg);
				player.sendMessage("§aLine name: §f" + line.getLongname());
				player.sendMessage("§7§l§m-----");
				player.sendMessage("§6➤ What is the line acronym? §o(Ex: L1 or L-Red)");
				player.sendMessage("§b✏ Write it in the chat §o(color codes allowed)");
				player.sendMessage("§7✏ Write §f§onothing§7 for no acronym");
				new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").send(player);
				pe.setPhase(2);
			} 
			//Line acronym
			else if(phase == 2) {
				Line line = pe.getLine();
				//No acronym
				if(msg.equalsIgnoreCase("nothing"))
					msg = "";
				//Setting acronym
				line.setSmallname(msg);
				if(msg != "") {
					player.sendMessage("§aLine acronym: §f" + line.getSmallname());
					player.sendMessage("§7§l§m-----");
				}
				player.sendMessage("§2§l➤ That's right?");
				player.sendMessage("§eLine: §o" + line.getLineType().getName() + "§e " + line.getLongname() + "§e " + (msg == "" ? "" : "(" + line.getSmallname() + "§e)"));
				new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
				.then(" ").then("[Save it!]").color(ChatColor.GREEN).style(ChatColor.BOLD).tooltip("§aSave as a new line").command("/minetrainconfig saveline").send(player);
			} 
			//Station name
			else if(phase == 11) {
				Station st = pe.getStation();
				//Setting name
				st.setName(msg);
				player.sendMessage("§aStation name: §f" + st.getName());
				player.sendMessage("§7§l§m-----");
				player.sendMessage("§6➤ Add a minecart launcher");
				player.sendMessage("§b★ Click on the station track §o(where the minecart will be stopped)");
				new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
				.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("If you need to edit your world before this step, click here :D").command("/minetrainconfig pauseeditor").send(player);
				pe.setPhase(12);
			}
		}
	}

}
