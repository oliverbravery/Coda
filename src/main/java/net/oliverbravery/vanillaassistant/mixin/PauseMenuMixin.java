package net.oliverbravery.vanillaassistant.mixin;

import net.oliverbravery.vanillaassistant.VanillaAssistantScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class PauseMenuMixin extends Screen {

    protected PauseMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void initWidgets(CallbackInfo info) {
        //10,10,90,20
        this.addDrawableChild(new ButtonWidget(width/2 - 102,this.height / 4 + 144 + -16,204,20, Text.literal("§6Vanilla §cAssistant"), button -> {
            this.client.setScreen(new VanillaAssistantScreen(this, this.client.options));
        }));
    }
}
