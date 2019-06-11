package fr.dariusmtn.minetrain.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MineTrainTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(cmd.getName().equalsIgnoreCase("minetrain")) {
			ArrayList<String> subcmds = new ArrayList<String>();
			if(args.length == 1) {
				subcmds.add("create");
				subcmds.add("list");
				subcmds.add("reload");
				return subcmds;
			} else if (args[0].equalsIgnoreCase("create") && args.length >= 2) {
				subcmds.add("line");
				subcmds.add("station");
				return subcmds;
			}
			return null;
		}
		return null;
	}
	
	

}
