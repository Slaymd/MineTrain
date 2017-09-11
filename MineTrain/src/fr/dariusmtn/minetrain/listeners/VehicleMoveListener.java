package fr.dariusmtn.minetrain.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
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
	
	ArrayList<Minecart> stoppedcarts = new ArrayList<Minecart>();

	@EventHandler
	public void onVehicleMove(VehicleMoveEvent e) {
		if(e.getVehicle() instanceof Minecart) {
			Minecart cart = (Minecart) e.getVehicle();
			if(stoppedcarts.contains(cart)) {
				cart.setVelocity(cart.getVelocity().zero());
				return;
			}
			if(!cart.getPassengers().isEmpty()) {
				if(cart.getPassengers().get(0) instanceof Player) {
					Player player = (Player) cart.getPassengers().get(0);
					if(plugin.playerLastStation.containsKey(player)) {
						Location cartLoc = cart.getLocation();
						//getting nearest stations
						ArrayList<Location> nearestSt = new ArrayList<Location>();
						for(Location loc : plugin.getFileManager().getStationsStarts().keySet()) {
							if(loc.distance(cartLoc) <= 4)
								if(plugin.playerLastStation.containsKey(player) && plugin.playerLastStation.get(player) == loc) {
									//station déjà passée
								} else {
									nearestSt.add(loc);
								}
						}
						Vector cartvelo = cart.getVelocity();
						double norme = vectorNorm(cartvelo);
						if(nearestSt.size() > 0) {
							for(Location station : nearestSt) {
								Station st = plugin.getFileManager().getStationsStarts().get(station);
								//brake
								double dist = station.distance(cart.getLocation());
								if(dist <= 4 && dist >= 0.6) {
									double mult = norme < 0.5 ? norme : 0.9;
									if(norme >= 0.25)
										cartvelo.multiply(mult);
								} else if (dist <= 0.6) {
									//Cart stopped
									stoppedcarts.add(cart);
									cartvelo.zero();
									//LinesMap
									if(plugin.playerLastStation.get(player) != null) {
										plugin.getLinesMap().setNextStop(plugin.playerLastStation.get(player), st);
									}
									//Set last station
									plugin.playerLastStation.put(player, station);
									//Message
									player.sendTitle("§bStation", "§e" + st.getName(), 10, 40, 10);
									//Launch after 3 seconds stop
									Vector vector = st.getStartDirection(station);
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
										public void run(){
											stoppedcarts.remove(cart);
											if(cart.getPassengers().contains(player)) {
												if(vector == null) {
													//terminus
													player.sendMessage("§aTerminus.");
													cart.eject();
													cart.remove();
													plugin.playerLastStation.remove(player);
												} else {
													cart.setVelocity(vector.multiply(0.1));
													if(plugin.getLinesMap().getNextStop(station) != null) {
														Station nextstop = plugin.getLinesMap().getNextStop(station);
														player.sendTitle("", "§eNext stop:§b " + nextstop.getName(), 10, 40, 10);
														String corresp = "§e, change for:§7 ";
														if(nextstop.getLinesId().size() == 1) {
															corresp = "";
														} else {
															for(UUID lineId : nextstop.getLinesId()) {
																if(!lineId.equals(st.getStartLineId(station))) {
																	Line line = plugin.getFileManager().getLine(lineId);
																	corresp += line.getAcronym() + "§7";
																	corresp += " ";
																}
															}
														}
														player.sendMessage("§eNext Stop:§b " + nextstop.getName() + corresp);
													}
												}
											}
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
						try {
							cart.setVelocity(cartvelo);
						} catch (Exception ex) {
							//
						}
			
						//Entity pushing
						//If disabled, stop here.
						boolean entitymanager = plugin.getConfig().getBoolean("nearby_entities_manager");
						if(entitymanager == false)
							return;
						
						//If stopped, dont need to push or avoid collision
						if(stoppedcarts.contains(cart))
							return;
						
						List<Entity> nearbyEnts =  cart.getNearbyEntities(1.5, 1.5, 1.5);
						nearbyEnts.remove(cart);nearbyEnts.remove(player);
						if(nearbyEnts.size() > 0) {
							for(Entity ent : nearbyEnts) {
								if(ent instanceof Minecart) {
									Minecart othercart = (Minecart)ent;
									if(othercart.getPassengers().size() > 0 && othercart.getPassengers().get(0) instanceof Player) {
										Player otherplayer = (Player) othercart.getPassengers().get(0);
										if(plugin.playerLastStation.containsKey(otherplayer)) {
											//MineTrain cart with player
											final Vector velosaved = cart.getVelocity().clone();
											stoppedcarts.add(cart);
											int tickcount = 0;
											new BukkitRunnable() {
									            @Override
									            public void run() {
									                cart.setVelocity(cart.getVelocity().zero());
									                if(tickcount >= 60) {
									                	this.cancel();
									                	cart.setVelocity(velosaved);
									                	stoppedcarts.remove(cart);
									                }
									            }
									            
									        }.runTaskTimer(plugin, 0, 2);
											
										}
									} else {
										//Minecart not minetrain
										ent.remove();
									}
								} else {
									//Send action
									String action = plugin.getConfig().getString("nearby_entities_manager_mode");
									plugin.getEntityPusher().pushEntity(ent, cart, action);
								}
							}
						}
						
					}
				}
			}
		}
	}

}
