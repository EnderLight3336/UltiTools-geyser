package com.ultikits.ultitools.commands;

import com.ultikits.abstracts.AbstractTabExecutor;
import me.enderlight3336.modified.form.TpaFormKit;
import com.ultikits.ultitools.tasks.TpTimerTask;
import com.ultikits.ultitools.ultitools.UltiTools;
import com.ultikits.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.ultikits.utils.MessagesUtils.warning;

public class TpaHereCommands extends AbstractTabExecutor {
    @Override
    protected boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        if (strings.length == 1){
            switch (strings[0]) {
                case "accept":
                    Player teleporter = TpTimerTask.tphereTemp.get(player);
                    if (teleporter == null) {
                        player.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_no_request")));
                        return true;
                    }
                    TpTimerTask.tphereTemp.put(player, null);
                    TpTimerTask.tphereTimer.put(player, 0);
                    player.teleport(teleporter.getLocation());
                    teleporter.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_teleport_success")));
                    return true;
                case "reject":
                    Player teleporter2 = TpTimerTask.tphereTemp.get(player);
                    teleporter2.sendMessage(MessagesUtils.warning(UltiTools.languageUtils.getString("tpa_teleport_rejected")));
                    player.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpa_rejected")));
                    TpTimerTask.tphereTemp.put(player, null);
                    TpTimerTask.tphereTimer.put(player, 0);
                    return true;
                default:
                    Player target = Bukkit.getPlayerExact(strings[0]);
                    if (target == null) {
                        player.sendMessage(warning(UltiTools.languageUtils.getString("tpa_player_not_found")));
                        return true;
                    }
                    TpTimerTask.tphereTemp.put(target, player);
                    TpTimerTask.tphereTimer.put(target, 20);
                    player.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("tpa_tp_send_successfully"), target.getName())));
                    target.sendMessage(MessagesUtils.info(String.format(UltiTools.languageUtils.getString("tpahere_enquire"), player.getName())));
                    target.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpahere_accept_tip")));
                    target.sendMessage(MessagesUtils.info(UltiTools.languageUtils.getString("tpahere_reject_tip")));

                    if(FloodgateApi.getInstance().isFloodgatePlayer(target.getUniqueId())) {
                        TpaFormKit.TpahereRequestForm(FloodgateApi.getInstance().getPlayer(target.getUniqueId()), target, player);
                    }
                    return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    protected List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] strings, @NotNull Player player) {
        return getTpTabList(strings);
    }

    @Nullable
    public static List<String> getTpTabList(@NotNull String[] strings) {
        List<String> tabCommands = new ArrayList<>();
        if (strings.length == 1) {
            tabCommands.add("accept");
            tabCommands.add("reject");
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                tabCommands.add(player1.getName());
            }
            return tabCommands;
        }
        return null;
    }
}
