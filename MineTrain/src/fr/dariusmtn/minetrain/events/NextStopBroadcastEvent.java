package fr.dariusmtn.minetrain.events;

import fr.dariusmtn.minetrain.object.Station;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NextStopBroadcastEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private Station station;
    private Player player;


    public NextStopBroadcastEvent(Player player, Station s) {
        this.station = s;
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


    public Station getNextStation() {
        return station;
    }



}
