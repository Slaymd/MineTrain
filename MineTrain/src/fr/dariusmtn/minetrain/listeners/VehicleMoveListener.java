package fr.dariusmtn.minetrain.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Station;

public class VehicleMoveListener implements Listener {
	
	private Main plugin;
	public VehicleMoveListener(Main plugin) {
		this.plugin = plugin;
	}
	
	private double vectorNorm(Vector vec) {
		double norme = Math.sqrt(Math.pow(vec.getX(), 2) + Math.pow(vec.getY(), 2) + Math.pow(vec.getZ(), 2));
		return norme;
	}
	
	HashMap<Player,Location> playerLastStation = new HashMap<Player,Location>();
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent e) {
		if(e.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) e.getVehicle();
			if(!cart.getPassengers().isEmpty()) {
				if(cart.getPassengers().get(0) instanceof Player) {
					Player player = (Player) cart.getPassengers().get(0);
					Location cartLoc = cart.getLocation();
					//getting nearest stations
					ArrayList<Location> nearestSt = new ArrayList<Location>();
					for(Location loc : plugin.getFileManager().getStationsStarts().keySet()) {
						if(loc.distance(cartLoc) <= 6)
							if(playerLastStation.containsKey(player) && playerLastStation.get(player) == loc) {
								//station déjà passée
							} else {
								nearestSt.add(loc);
							}
					}
					Vector cartvelo = cart.getVelocity();
					double norme = vectorNorm(cartvelo);
					if(nearestSt.size() > 0) {
						for(Location station : nearestSt) {
							final Station st = plugin.getFileManager().getStationsStarts().get(station);
							//brake
							double dist = station.distance(cart.getLocation());
							if(dist <= 6 && dist >= 0.6) {
								double mult = norme < 0.5 ? norme : 0.9;
								if(norme >= 0.25)
									cartvelo.multiply(mult);
							} else if (dist <= 0.6) {
								cartvelo.zero();
								//Set last station
								playerLastStation.put(player, station);
								//Message
								player.sendTitle("§bStation", "§e" + st.getName(), 10, 40, 10);
								//Launch after 3 seconds stop
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										Vector vector = st.getStarts().get(station).toVector().subtract(station.toVector());
										cart.setVelocity(vector.multiply(0.2));
									}
								},60);
							}
						}
					} else if(norme < 1.40) {
						double mult = 1/norme;
						if(mult > 1) {
							cartvelo.multiply(mult);
						} else {
							cartvelo.multiply(1.05);
						}
					}
					cart.setVelocity(cartvelo);
				}
			}
		}
	}

}
