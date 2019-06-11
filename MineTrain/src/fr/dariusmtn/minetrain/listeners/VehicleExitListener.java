package fr.dariusmtn.minetrain.listeners;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

import fr.dariusmtn.minetrain.Main;

public class VehicleExitListener implements Listener {

	private Main plugin;
	public VehicleExitListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onVehicleExit(VehicleExitEvent e) {
		if(e.getExited() instanceof Player) {
			if(e.getVehicle() instanceof Minecart) {
				Player player = (Player) e.getExited();
				if(plugin.playerLastStation.containsKey(player)) {
					//Remove player last station
					plugin.playerLastStation.remove(player);
					e.getVehicle().remove();
				}
			}
		}
	}
	
}
