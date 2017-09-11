package fr.dariusmtn.minetrain;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EntityPusher {
	
	Random rd = new Random();

	public void pushEntity(Entity ent, Minecart cart, String action) {
		
		if(action.equals("PUSH")) {
			Location loc = ent.getLocation();
			//Checking where
			BlockFace[] bfaces = BlockFace.values();
			int rdint = rd.nextInt(bfaces.length);
			double basedistance = cart.getLocation().distance(ent.getLocation());
			BlockFace bface = bfaces[rdint];
			Block bloc = loc.getBlock().getRelative(bface);
			Vector dir = bloc.getLocation().toVector().subtract(ent.getLocation().toVector());
			double distance = bloc.getLocation().distance(ent.getLocation());
			while(bloc.getType().toString().contains("RAIL") && !dir.normalize().equals(cart.getVelocity().normalize()) && distance > basedistance 
					&& bface != BlockFace.DOWN && bface != BlockFace.UP && bface != BlockFace.SELF) {
				rdint = rd.nextInt(bfaces.length);
				bface = bfaces[rdint];
				bloc = loc.getBlock().getRelative(bface);
				dir = bloc.getLocation().toVector().subtract(ent.getLocation().toVector());
				distance = bloc.getLocation().distance(ent.getLocation());
			}
			loc = bloc.getLocation();
			
			//Teleport and effect
			ent.teleport(loc);
			ent.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc.add(0.5, 0.5, 0.5), 20, 0.1, 0.1, 0.1, 0.05);
		} else if(action.equals("KILL_ALL")) {
			ent.remove();
		} else if(action.equals("KILL_NOPLAYER")) {
			if(!(ent instanceof Player))
				ent.remove();
		}
	}
	
}
