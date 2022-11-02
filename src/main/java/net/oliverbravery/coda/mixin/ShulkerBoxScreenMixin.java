package net.oliverbravery.coda.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.oliverbravery.coda.Coda;
import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.features.ShulkerBoxUnloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerBoxScreenMixin extends Screen {
    public ShulkerBoxScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(Boolean.parseBoolean(Config.GetValue("ShulkerBoxUnloadEnabled", "true"))) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 35 - 145, 50, 20, Text.of("Empty"), (button) -> {
                Coda.LOGGER.info("Pressed Shulker EMPTY button!");
                ShulkerBoxUnloader.UnloadShulkerBox();
            }));
        }
    }
}