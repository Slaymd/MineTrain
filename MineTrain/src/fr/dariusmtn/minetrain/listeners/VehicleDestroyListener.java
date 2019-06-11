package fr.dariusmtn.minetrain.listeners;

import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import fr.dariusmtn.minetrain.Main;

public class VehicleDestroyListener implements Listener{
	
	private Main plugin;
	public VehicleDestroyListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent e) {
		if(e.getVehicle() instanceof Minecart) {
			if(e.getVehicle().getPassengers().size() > 0 && e.getVehicle().getPassengers().get(0) instanceof Player) {
				Player player = (Player) e.getVehicle().getPassengers().get(0);
				if(plugin.playerLastStation.containsKey(player)) {
					plugin.playerLastStation.remove(player);
				}
			}
		}
	}

}
