package com.examples2.addon.mixin;

import com.examples2.addon.modules.ServerTextRemover;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Inject(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", at = @At("HEAD"), cancellable = true)
    private void onDrawText(TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow, CallbackInfoReturnable<Integer> cir) {
        if (shouldCancelTextString(text)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", at = @At("HEAD"), cancellable = true)
    private void onDrawTextComponent(TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow, CallbackInfoReturnable<Integer> cir) {
        if (shouldCancelText(text)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I", at = @At("HEAD"), cancellable = true)
    private void onDrawTextWithShadow(TextRenderer textRenderer, String text, int x, int y, int color, CallbackInfoReturnable<Integer> cir) {
        if (shouldCancelTextString(text)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I", at = @At("HEAD"), cancellable = true)
    private void onDrawTextWithShadowComponent(TextRenderer textRenderer, Text text, int x, int y, int color, CallbackInfoReturnable<Integer> cir) {
        if (shouldCancelText(text)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", at = @At("HEAD"), cancellable = true)
    private void onDrawCenteredTextWithShadow(TextRenderer textRenderer, String text, int centerX, int y, int color, CallbackInfo ci) {
        if (shouldCancelTextString(text)) {
            ci.cancel();
        }
    }

    @Inject(method = "drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", at = @At("HEAD"), cancellable = true)
    private void onDrawCenteredTextWithShadowComponent(TextRenderer textRenderer, Text text, int centerX, int y, int color, CallbackInfo ci) {
        if (shouldCancelText(text)) {
            ci.cancel();
        }
    }

    @Inject(method = "drawTextWithBackground", at = @At("HEAD"), cancellable = true)
    private void onDrawTextWithBackground(TextRenderer textRenderer, Text text, int x, int y, int width, int color, CallbackInfoReturnable<Integer> cir) {
        if (shouldCancelText(text)) {
            cir.setReturnValue(0);
        }
    }

    private boolean shouldCancelText(Text text) {
        if (text == null) return false;
        return shouldCancelTextString(text.getString());
    }

    private boolean shouldCancelTextString(String text) {
        if (text == null || text.isEmpty()) return false;
        
        ServerTextRemover module = Modules.get().get(ServerTextRemover.class);
        if (module == null || !module.isActive()) return false;

        String textString = text.toLowerCase();
        
        // Remove 2b2t.org text
        if (module.shouldRemove2b2tText() && (textString.contains("2b2t.org") || textString.contains("2b2t"))) {
            if (module.isDebugMode()) {
                module.info("Blocked 2b2t text: " + textString);
            }
            return true;
        }

        // Remove custom text if enabled
        if (module.shouldRemoveCustomText() && !module.getCustomText().isEmpty()) {
            String customText = module.getCustomText().toLowerCase();
            if (textString.contains(customText)) {
                if (module.isDebugMode()) {
                    module.info("Blocked custom text: " + textString);
                }
                return true;
            }
        }

        return false;
    }
} 