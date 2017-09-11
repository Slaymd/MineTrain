package fr.dariusmtn.minetrain.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.material.Rails;

import fr.dariusmtn.minetrain.Main;
import fr.dariusmtn.minetrain.object.Line;
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class PlayerInteractListener implements Listener {
	
	private Main plugin;
	public PlayerInteractListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	boolean changeNextRails(Rails clickedRail, Location clickedLoc, Material finalMaterial, Block bX, Block bZ, int mod, boolean particles, BlockFace face){
		if(bX.getType().toString().contains("RAIL")) {
			Rails bXRail = new Rails(bX.getType(),bX.getData());
			if(bXRail.getDirection() == clickedRail.getDirection()) {
				bX.setType(finalMaterial);
				bXRail.setDirection(face, false);
				bX.setData(bXRail.getData());
				Block bXFar = bX.getLocation().clone().add(mod, 0, 0).getBlock();
				if(bXFar.getType().toString().contains("RAIL")) {
					Rails bXFarRail = new Rails(bXFar.getType(),bXFar.getData());
					bXFar.setType(finalMaterial);
					bXFarRail.setDirection(face, false);
					bXFar.setData(bXFarRail.getData());
					if(particles)
						bXFar.getWorld().spawnParticle(Particle.END_ROD, bXFar.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
				}
				//particles (useless, but it's good :33)
				if(particles)
					bX.getWorld().spawnParticle(Particle.END_ROD, bX.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
				return true;
			}
		}
		if(bZ.getType().toString().contains("RAIL")) {
			Rails bZRail = new Rails(bZ.getType(),bZ.getData());
			if(bZRail.getDirection() == clickedRail.getDirection()) {
				bZ.setType(finalMaterial);
				bZRail.setDirection(face, false);
				bZ.setData(bZRail.getData());
				Block bZFar = bZ.getLocation().clone().add(0, 0, mod).getBlock();
				if(bZFar.getType().toString().contains("RAIL")) {
					Rails bZFarRail = new Rails(bZFar.getType(),bZFar.getData());
					bZFar.setType(finalMaterial);
					bZFarRail.setDirection(face, false);
					bZFar.setData(bZFarRail.getData());
					if(particles)
						bZFar.getWorld().spawnParticle(Particle.END_ROD, bZFar.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
				}
				//particles (useless, but it's good :33)
				if(particles)
					bZ.getWorld().spawnParticle(Particle.END_ROD, bZ.getLocation().add(0.5, 0.5, 0.5), 10, 0.1, 0.1, 0.1, 0.05);
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		//Editor
		if(plugin.editor.containsKey(player)) {
			PlayerEditor pe = plugin.editor.get(player);
			if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int phase = pe.getPhase();
				if(e.getClickedBlock().getType().toString().contains("RAIL")) {
					Location clickedLoc = e.getClickedBlock().getLocation();
					//Rail object
					Rails clickedRail = new Rails(e.getClickedBlock().getType(),e.getClickedBlock().getData());
					//Setting station block phase
					if(phase == 11) {
						e.setCancelled(true);
						Block bX = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX()-1,clickedLoc.getY(),clickedLoc.getZ()).getBlock();
						Block bZ = new Location(clickedLoc.getWorld(), clickedLoc.getX(),clickedLoc.getY(),clickedLoc.clone().getZ()-1).getBlock();
						Block bX2 = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX()+1,clickedLoc.getY(),clickedLoc.getZ()).getBlock();
						Block bZ2 = new Location(clickedLoc.getWorld(), clickedLoc.getX(),clickedLoc.getY(),clickedLoc.clone().getZ()+1).getBlock();
						//if track direction coloration worked station is valid :D
						if(changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX, bZ, 2, true, BlockFace.SELF) || changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX2, bZ2, -2, true, BlockFace.SELF)) {
							if(!plugin.stlocEditor.containsKey(player) || pe.getStation().getStarts().size() == 0) {
								plugin.stlocEditor.remove(player);
								//Saving station track
								ArrayList<Location> startcrea = new ArrayList<Location>();
								startcrea.add(0, clickedLoc);
								plugin.stlocEditor.put(player, startcrea);
								//set station to detector rail
								clickedLoc.getBlock().setType(Material.DETECTOR_RAIL);
								clickedRail.setDirection(BlockFace.SELF, false);
								e.getClickedBlock().setData(clickedRail.getData());
								//particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								//Messages
								player.sendMessage(" ");
								player.sendMessage("§aAdded station point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
								player.sendMessage("§7§l§m-----");
								plugin.getEditorMessages().sendEditorMessage(player, 12);
								//New phase
								pe.setPhase(12);
								return;
							}
						}
					} else if(phase == 12) {
						e.setCancelled(true);
						if(plugin.stlocEditor.containsKey(player)) {
							ArrayList<Location> startcrea = plugin.stlocEditor.get(player);
							Location stLoc = startcrea.get(0);
							if((e.getClickedBlock().getType() == Material.POWERED_RAIL || e.getClickedBlock().getType() == Material.DETECTOR_RAIL)  && stLoc.distance(clickedLoc) <= 2) {
								//Remove coloration
								Block bX = new Location(stLoc.getWorld(), stLoc.clone().getX()-1,stLoc.getY(),stLoc.getZ()).getBlock();
								Block bZ = new Location(stLoc.getWorld(), stLoc.getX(),stLoc.getY(),stLoc.clone().getZ()-1).getBlock();
								Block bX2 = new Location(stLoc.getWorld(), stLoc.clone().getX()+1,stLoc.getY(),stLoc.getZ()).getBlock();
								Block bZ2 = new Location(stLoc.getWorld(), stLoc.getX(),stLoc.getY(),stLoc.clone().getZ()+1).getBlock();
								changeNextRails(clickedRail, stLoc, Material.RAILS, bX, bZ, 2, false, BlockFace.SELF);
								changeNextRails(clickedRail, stLoc, Material.RAILS, bX2, bZ2, -2, false, BlockFace.SELF);
								changeNextRails(clickedRail, stLoc, Material.RAILS, bX, bZ, 2, false, BlockFace.SELF);
								changeNextRails(clickedRail, stLoc, Material.RAILS, bX2, bZ2, -2, false, BlockFace.SELF);
								//Activate direction track
								player.sendMessage(" ");
								if(e.getClickedBlock().getType() == Material.DETECTOR_RAIL) {
									player.sendMessage("§aSet this point as terminus.");
								} else {
									player.sendMessage("§aAdded direction point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
									e.getClickedBlock().setType(Material.RAILS);
									clickedRail.setDirection(BlockFace.SELF, false);
									e.getClickedBlock().setData(clickedRail.getData());
								}
								//Add direction track
								startcrea.add(1, clickedLoc);
								plugin.stlocEditor.put(player, startcrea);
								//particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								//Next phase
								pe.setPhase(13);
								//Messages
								player.sendMessage("§7§l§m-----");
								plugin.getEditorMessages().sendEditorMessage(player, 13);
								return;
							}
							player.sendMessage("§cThis direction track isn't valid.");
							return;
						}
					}
				}
				if(phase == 15) {
					e.setCancelled(true);
					if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
						return;
					Station station = pe.getStation();
					
					//Setting button in front of clicked face
					Location clickedLoc = e.getClickedBlock().getLocation();
					BlockFace clickedFace = e.getBlockFace();
					Block button = clickedLoc.getBlock().getRelative(clickedFace);
					
					//Checking nearby starts
					int count = 0;
					for(Location starts : station.getStartLocations()) {
						if(starts.distance(button.getLocation()) <= 10) {
							count++;
						}
					}
					
					//No nearby starts
					if(count == 0) {
						player.sendMessage("§4§l!§c There aren't nearby minecart starts §o(< 10m)§c at this location.");
						return;
					}
					
					//Setting button in world
					button.setType(Material.WOOD_BUTTON);
					Button but = new Button(button.getType(), button.getData());
					but.setFacingDirection(clickedFace);
					button.setData(but.getData());
					
					//Station button adding
					station.addButton(button.getLocation());
					
					//particles (useless, but it's good :33)
					clickedLoc.getWorld().spawnParticle(Particle.FLAME, button.getLocation().add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
					
					//phase
					pe.setPhase(16);
					
					//messages
					player.sendMessage(" ");
					player.sendMessage("§aAdded departure button: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
					player.sendMessage("§7§l§m-----");
					plugin.getEditorMessages().sendEditorMessage(player, 16);
					return;
				}
			}
		}
		//Click button destination
		else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location buttonLoc = e.getClickedBlock().getLocation();
			if(e.getClickedBlock().getType().toString().contains("BUTTON")) {
				if(plugin.getFileManager().isStationButton(buttonLoc)) {
					if(player.hasPermission("minetrain.use")) {
						Station station = plugin.getFileManager().getStationFromButton(buttonLoc);
						
						//gettint starts by lines
						HashMap<Line, ArrayList<Location>> startsByLine = new HashMap<Line, ArrayList<Location>>();
						for(Location startloc : station.getStartLocations()) {
							ArrayList<Location> startlocs = new ArrayList<Location>();
							Line line = plugin.getFileManager().getLine(station.getStartLineId(startloc));
							if(startsByLine.containsKey(line))
								startlocs = startsByLine.get(line);
							startlocs.add(startloc);
							startsByLine.put(line, startlocs);
						}
						
						//If just one direction
						int nbstarts = station.getStartLocations().size();
						Location firstartloc = station.getStartLocations().get(0);
						if(nbstarts == 1 && firstartloc.distance(station.getStartDirectionLocation(firstartloc)) != 0) {
							player.sendMessage("§eStation§b " + station.getName());
							player.performCommand("minetrain launchfrom " + plugin.getFileUtils().locationToString(station.getStartLocations().get(0)));
							return;
						}
						
						//Displaying destinations by lines
						player.sendMessage("§eStation§b " + station.getName() + "§e - Destinations");
						boolean warn_linedestinations = false;
						int count = 0;
						for(Line line : startsByLine.keySet()) {
							for(Location start : startsByLine.get(line)) {
								if(player.getLocation().distance(start) <= 10 && station.getStartDirection(start) != null) {
									count++;
									String nextstation = "§o?";
									if(plugin.getLinesMap().getNextStop(start) != null) {
										nextstation = plugin.getLinesMap().getNextStop(start).getName();
									} else { warn_linedestinations = true; }
									int distanceToPlayer = (int) start.distance(player.getLocation());
									new FancyMessage(" •").color(ChatColor.GOLD).then(" ").then(line.getAcronym() + "§a in direction of §f" + nextstation + " §7§o(" + distanceToPlayer + " m)").color(ChatColor.GOLD).tooltip("§7Select this direction.")
									.command("/minetrain launchfrom " + plugin.getFileUtils().locationToString(start)).send(player);
								}
							}
						}
						
						//Warns
						if(warn_linedestinations)
							player.sendMessage("§c§l!§6 Some directions are missing because nobody take it for now.");
						if(count == 0)
							player.sendMessage("§9§l!§b This station has no departures.");
						return;
					}
					player.sendMessage("§cSorry! You don't have permission to do that :(");
					return;
				}
			}
		}
	}
	

}
