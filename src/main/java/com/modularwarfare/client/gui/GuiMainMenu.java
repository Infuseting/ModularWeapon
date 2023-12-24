package com.modularwarfare.client.gui;

import com.modularwarfare.ModularWarfare;
import fr.aym.acsguis.component.GuiComponent;
import fr.aym.acsguis.component.layout.GuiScaler;
import fr.aym.acsguis.component.panel.GuiFrame;
import fr.aym.acsguis.component.*;
import fr.aym.acsguis.component.style.PanelStyleManager;
import fr.aym.acsguis.component.textarea.GuiLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class GuiMainMenu extends GuiFrame {
    private GuiLabel load;
    public static final ResourceLocation CSSMAINMENU = new ResourceLocation(ModularWarfare.MOD_ID, "css/mainmenu.css");
    public GuiMainMenu() {

        super((GuiScaler)new GuiScaler.AdjustFullScreen());
        ((PanelStyleManager)this.style).setBackgroundColor(3);
        setCssClass("home");
        GuiLabel img = new GuiLabel("");
        img.setCssClass("img");
        add((GuiComponent)img);
        GuiLabel join = new GuiLabel("Rejoindre");
        join.setCssClass("join");
        join.addClickListener((x, y, bu) -> this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)getGuiScreen(), this.mc, "minecraft3094.omgserv.com", 10015)));
        add((GuiComponent)join);
        GuiLabel settings = new GuiLabel("Options");
        settings.setCssClass("settings");
        settings.addClickListener((x, y, bu) -> Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)getGuiScreen(), Minecraft.getMinecraft().gameSettings)));
        add((GuiComponent)settings);
        GuiLabel exit = new GuiLabel("Quitter");
        exit.setCssClass("exit");
        exit.addClickListener((x, y, bu) -> Minecraft.getMinecraft().shutdown());
        add((GuiComponent)exit);
        GuiLabel version = new GuiLabel("Version: 1.0");
        version.setCssClass("version");
        add((GuiComponent)version);
    }



    public List<ResourceLocation> getCssStyles() {
        return Collections.singletonList(CSSMAINMENU);
    }

    public boolean needsCssReload() {
        return false;
    }
}

