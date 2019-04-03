package fr.dariusmtn.minetrain.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import org.bukkit.scheduler.BukkitRunnable;

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
				line.setName(msg);
				player.sendMessage(" ");
				player.sendMessage("§aLine name: §f" + line.getName());
				player.sendMessage("§7§l§m-----");
				new BukkitRunnable() {
					public void run() {
						plugin.getEditorMessages().sendEditorMessage(player, 2);
					}
				}.runTaskLater(plugin, 1);

				pe.setPhase(2);
			} 
			//Line acronym
			else if(phase == 2) {
				Line line = pe.getLine();
				//No acronym
				if(msg.equalsIgnoreCase("nothing"))
					msg = "";
				//Setting acronym
				line.setAcronym(msg);
				player.sendMessage(" ");
				if(msg != "") {
					player.sendMessage("§aLine acronym: §f" + line.getAcronym());
					player.sendMessage("§7§l§m-----");
				}
				new BukkitRunnable() {
					public void run() {
						plugin.getEditorMessages().sendEditorMessage(player, 3);
					}
				}.runTaskLater(plugin, 1);
				pe.setPhase(3);
			} 
			//Station name
			else if(phase == 10) {
				Station st = pe.getStation();
				//Setting name
				st.setName(msg);
				player.sendMessage(" ");
				player.sendMessage("§aStation name: §f" + st.getName());
				player.sendMessage("§7§l§m-----");
				new BukkitRunnable() {
					public void run() {
						plugin.getEditorMessages().sendEditorMessage(player, 11);
					}
				}.runTaskLater(plugin, 1);

				pe.setPhase(11);
			}
		}
	}

}
