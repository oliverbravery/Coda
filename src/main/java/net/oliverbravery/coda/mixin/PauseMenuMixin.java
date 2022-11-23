package net.oliverbravery.coda.mixin;

import net.oliverbravery.coda.config.Config;
import net.oliverbravery.coda.screens.CodaSettingsScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.oliverbravery.coda.utilities.Utils;
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
        if(Boolean.parseBoolean(Config.GetValue("CodaButtonEnabled", "true")))
        this.addDrawableChild(new ButtonWidget(width/2 - 102, Utils.SHOULD_COMPENSATE_FOR_MODMENU_BUTTON ? this.height / 4 + 168 + -16 : this.height / 4 + 144 + -16,204,20, Text.literal("§6Coda"), button -> {
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }));
    }
}
