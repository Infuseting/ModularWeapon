package com.modularwarfare.client.gui;

import com.modularwarfare.common.Thirst.CapabilityThirst;
import com.modularwarfare.common.Thirst.IThirst;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.optifine.CustomColors.random;

public class GuiMainHudModified extends Gui {

    public int tickCounter = 0;

    public GuiMainHudModified() {
    }
    public static final ResourceLocation OVERLAY = new ResourceLocation("modularwarfare:textures/gui/thirst.png");

    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();

    private int updateCounter;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && !minecraft.isGamePaused())
        {
            updateCounter++;
        }
    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {

        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (!Minecraft.getMinecraft().player.isCreative()) {
                event.setCanceled(true);
            }
        }
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        GuiIngameForge.right_height = 39;
        IThirst thirstStats = (IThirst) player.getCapability(CapabilityThirst.CAPABILITY_THIRST, null);
        int thirstLevel = (int)((thirstStats.getThirst()/144000F)*20F);
        if (minecraft.playerController.gameIsSurvivalOrAdventure())
        {
            minecraft.getTextureManager().bindTexture(OVERLAY);
            drawThirst(width, height, thirstLevel);
            GuiIngameForge.right_height += 10;
            minecraft.getTextureManager().bindTexture(Gui.ICONS);
        }
    }
    private void drawThirst(int width, int height, int thirstLevel)
    {
        int left = width / 2 + 91;
        int top = height - GuiIngameForge.right_height;

        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;
            int backgroundOffset = 0;
            int startX = left - i * 8 - 9;
            int startY = top;


            drawTexturedModalRect(startX, startY, backgroundOffset, 16, 9, 9);

            if (thirstLevel > dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 4) * 9, 16, 9, 9);
            }
            if (thirstLevel == dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 5) * 9, 16, 9, 9);
            }
        }
    }
}