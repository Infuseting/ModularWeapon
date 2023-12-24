package com.modularwarfare.utility;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.RichPresenceButton;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;

import static fr.aym.acsguis.api.GuiAPIClientHelper.mc;

public class DiscordRPCLoader {

    public static long currentTime = System.currentTimeMillis();

    public static IPCClient client;

    public DiscordRPCLoader() {
        client = new IPCClient(Long.parseLong("1187888867953627138"));
        client.setListener(new IPCListener() {
            public void onReady(IPCClient client) {
                DiscordRPCLoader.updatePresence("En direction de la ligne de front");


            }
        });
        try {
            if (!client.getStatus().equals(PipeStatus.CONNECTED))
                client.connect(new DiscordBuild[0]);
        } catch (NoDiscordClientException e) {
            e.printStackTrace();
        }
    }

    public static void updatePresence(String details) {
        if (client == null)
            return;
        com.jagrosh.discordipc.entities.RichPresence.Builder builder = new RichPresence.Builder();
        RichPresenceButton firstbutton = new RichPresenceButton("https://discord.gg/DwFYdCd3", "Discord");
        RichPresenceButton[] discordButton = { firstbutton};
        builder.setDetails(details)
                .setStartTimestamp(currentTime)
                .setLargeImage("logo",  mc.getSession().getUsername())
                .setButtons(discordButton);
        if (client.getStatus().equals(PipeStatus.CONNECTED))
            client.sendRichPresence(builder.build());
    }

}
