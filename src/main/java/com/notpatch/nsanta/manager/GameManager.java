package com.notpatch.nsanta.manager;

import com.notpatch.nsanta.NSanta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public boolean gameRunning = false;
    private final List<Player> players;

    private NSanta main;

    public GameManager(){
        this.gameRunning = false;
        this.players = new ArrayList<>();
        this.main = NSanta.getInstance();
    }

    public boolean addPlayer(Player player) {
        if (gameRunning) {
            player.sendMessage(main.getLanguageLoader().get("arena-you-cant-join-while-started"));
            return false;
        }
        if (players.contains(player)) {
            return false;
        }
        players.add(player);
        player.sendMessage(main.getLanguageLoader().get("arena-you-joined"));
        return true;
    }

    public void startGame() {
        if (gameRunning) {
            return;
        }
        if (players.isEmpty()) {
            return;
        }
        gameRunning = true;
        Bukkit.broadcastMessage(main.getLanguageLoader().get("arena-started"));
    }

    public void stopGame() {
        if (!gameRunning) {
            return;
        }
        gameRunning = false;
        Bukkit.broadcastMessage(main.getLanguageLoader().get("arena-stopped"));
        players.clear();
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }


    public boolean removePlayer(Player player) {
        if (!players.contains(player)) {
            return false;
        }
        players.remove(player);
        player.sendMessage(main.getLanguageLoader().get("arena-you-left"));
        return true;
    }

    public int getPlayerCount() {
        return players.size();
    }

}
