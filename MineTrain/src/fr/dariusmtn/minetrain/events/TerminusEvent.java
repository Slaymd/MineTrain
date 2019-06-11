package fr.dariusmtn.minetrain.events;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TerminusEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private Player player;

    public TerminusEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }


    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }




}
