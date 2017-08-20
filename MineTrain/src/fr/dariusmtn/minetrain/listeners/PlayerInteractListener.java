package fr.dariusmtn.minetrain.listeners;

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
import fr.dariusmtn.minetrain.object.PlayerEditor;
import fr.dariusmtn.minetrain.object.Station;
import mkremins.fanciful.FancyMessage;

public class PlayerInteractListener implements Listener {
	
	private Main plugin;
	public PlayerInteractListener(Main plugin) {
		this.plugin = plugin;
	}
	
	HashMap<Player,Location> stlocEditor = new HashMap<Player,Location>();
	
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
		if(plugin.editor.containsKey(player)) {
			PlayerEditor pe = plugin.editor.get(player);
			if(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				int phase = pe.getPhase();
				if(e.getClickedBlock().getType().toString().contains("RAIL")) {
					Location clickedLoc = e.getClickedBlock().getLocation();
					//Rail object
					Rails clickedRail = new Rails(e.getClickedBlock().getType(),e.getClickedBlock().getData());
					//Setting station block phase
					if(phase == 12) {
						e.setCancelled(true);
						Block bX = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX()-1,clickedLoc.getY(),clickedLoc.getZ()).getBlock();
						Block bZ = new Location(clickedLoc.getWorld(), clickedLoc.getX(),clickedLoc.getY(),clickedLoc.clone().getZ()-1).getBlock();
						Block bX2 = new Location(clickedLoc.getWorld(), clickedLoc.clone().getX()+1,clickedLoc.getY(),clickedLoc.getZ()).getBlock();
						Block bZ2 = new Location(clickedLoc.getWorld(), clickedLoc.getX(),clickedLoc.getY(),clickedLoc.clone().getZ()+1).getBlock();
						//if track direction coloration worked station is valid :D
						if(changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX, bZ, 2, true, BlockFace.SELF) || changeNextRails(clickedRail, clickedLoc, Material.POWERED_RAIL, bX2, bZ2, -2, true, BlockFace.SELF)) {
							if(!stlocEditor.containsKey(player) || pe.getStation().getStarts().size() == 0) {
								stlocEditor.remove(player);
								stlocEditor.put(player, clickedLoc);
								//set station to detector rail
								clickedLoc.getBlock().setType(Material.DETECTOR_RAIL);
								clickedRail.setDirection(BlockFace.SELF, false);
								e.getClickedBlock().setData(clickedRail.getData());
								//particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								//Messages
								player.sendMessage("§aAdded station point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
								player.sendMessage("§7§l§m-----");
								player.sendMessage("§b★ Click on the §ldirection track§b §e(Powered Rail)§b just next to the station point §o(in what direction the minecart will be launched)");
								player.sendMessage("§7★ Click on the §lstation track§7 §o(Detector Rail)§7 to set as a terminus");
								new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor").then(" ")
								.then("[Pause editor to edit world]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).tooltip("If you need to edit your world before this step, click here :D").command("/minetrainconfig pauseeditor").send(player);
								//New phase
								pe.setPhase(13);
								return;
							}
						}
					} else if(phase == 13) {
						e.setCancelled(true);
						if(stlocEditor.containsKey(player)) {
							Location stLoc = stlocEditor.get(player);
							if(e.getClickedBlock().getType() == Material.POWERED_RAIL && stLoc.distance(clickedLoc) <= 2) {
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
								e.getClickedBlock().setType(Material.RAILS);
								clickedRail.setDirection(BlockFace.SELF, false);
								e.getClickedBlock().setData(clickedRail.getData());
								//particles (useless, but it's good :33)
								clickedLoc.getWorld().spawnParticle(Particle.FLAME, clickedLoc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
								//Add start
								Station station = pe.getStation();
								station.addStarts(stLoc, clickedLoc);
								//Next phase
								stlocEditor.remove(player);
								pe.setPhase(14);
								//Messages
								player.sendMessage("§aAdded direction point: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
								player.sendMessage("§7§l§m-----");
								player.sendMessage("§eThis station has " + station.getStarts().size() + " point(s) of departure");
								new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
								.then(" ").then("[Add point of departure]").color(ChatColor.AQUA).tooltip("§aAdd point of departure (other directions ?)").command("/minetrainconfig addstationpoint")
								.then(" ").then("[Next Step]").color(ChatColor.GOLD).style(ChatColor.BOLD).tooltip("§aGo to next step").command("/minetrainconfig stationpointnextstep").send(player);
								return;
							}
							player.sendMessage("§cThis direction track isn't valid.");
							return;
						}
					}
				}
				if(phase == 15) {
					//Setting button in front of clicked face
					Location clickedLoc = e.getClickedBlock().getLocation();
					BlockFace clickedFace = e.getBlockFace();
					Block button = clickedLoc.getBlock().getRelative(clickedFace);
					button.setType(Material.WOOD_BUTTON);
					Button but = new Button(button.getType(), button.getData());
					but.setFacingDirection(clickedFace);
					button.setData(but.getData());
					//Station button adding
					Station station = pe.getStation();
					station.addButton(clickedLoc);
					//particles (useless, but it's good :33)
					clickedLoc.getWorld().spawnParticle(Particle.FLAME, button.getLocation().add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
					//phase
					pe.setPhase(16);
					//messages
					player.sendMessage("§aAdded departure button: §f(§7§o" + clickedLoc.getX() + "§f, §7§o" + clickedLoc.getY() + "§f, §7§o" + clickedLoc.getZ() + "§f)");
					player.sendMessage("§7§l§m-----");
					player.sendMessage("§eThis station has " + station.getButtons().size() + " departure button(s)");
					new FancyMessage("[Cancel]").color(ChatColor.RED).tooltip("§cCancel").command("/minetrainconfig canceleditor")
					.then(" ").then("[Add departure button]").color(ChatColor.AQUA).tooltip("§aAdd departure button").command("/minetrainconfig adddeparturebutton")
					.then(" ").then("[Finish and save]").color(ChatColor.GOLD).style(ChatColor.BOLD).tooltip("§aFinish").command("/minetrainconfig savestation").send(player);
					return;
				}
			}
		}
	}
	

}
