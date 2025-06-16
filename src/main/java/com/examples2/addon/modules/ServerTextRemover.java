package com.examples2.addon.modules;

import com.examples2.addon.AddonTemplates;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ServerTextRemover extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> remove2b2tText = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-2b2t-text")
        .description("Removes the '2b2t.org' text that appears above the hotbar.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> removeCustomText = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-custom-text")
        .description("Remove custom text specified below.")
        .defaultValue(false)
        .build()
    );

    private final Setting<String> customText = sgGeneral.add(new StringSetting.Builder()
        .name("custom-text")
        .description("Custom text to remove from the screen.")
        .defaultValue("")
        .visible(removeCustomText::get)
        .build()
    );

    private final Setting<Boolean> debugMode = sgGeneral.add(new BoolSetting.Builder()
        .name("debug-mode")
        .description("Shows debug information about text being rendered.")
        .defaultValue(false)
        .build()
    );

    public ServerTextRemover() {
        super(
            AddonTemplates.CATEGORY,
            "ServerTextRemover",
            "Removes unwanted server advertisement text from the screen."
        );
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.player == null || mc.world == null) return;

        // The text rendering interception happens at a lower level
        // We'll use mixin for the actual text removal, but this module
        // provides the configuration interface
        
        if (debugMode.get()) {
            // This would show debug info if we had access to the text being rendered
            // In practice, the actual text interception happens via mixin
        }
    }

    public boolean shouldRemove2b2tText() {
        return isActive() && remove2b2tText.get();
    }

    public boolean shouldRemoveCustomText() {
        return isActive() && removeCustomText.get();
    }

    public String getCustomText() {
        return customText.get();
    }

    public boolean isDebugMode() {
        return isActive() && debugMode.get();
    }

    @Override
    public void onActivate() {
        // info("Server text remover activated");
        if (remove2b2tText.get()) {
            // info("Will remove '2b2t.org' text");
        }
        if (removeCustomText.get() && !customText.get().isEmpty()) {
            //info("Will remove custom text: '" + customText.get() + "'");
        }
    }

    @Override
    public void onDeactivate() {
        // nfo("Server text remover deactivated");
    }
} 