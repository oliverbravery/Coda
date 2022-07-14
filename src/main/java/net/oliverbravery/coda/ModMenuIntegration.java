package net.oliverbravery.coda;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuOptionsScreen::new;
    }

    public class ModMenuOptionsScreen extends GameOptionsScreen {
        public ModMenuOptionsScreen(Screen parent) {
            super(parent, MinecraftClient.getInstance().options, Text.translatable("options.coda.title"));
        }


        protected void init() {
            this.client.setScreen(new CodaSettingsScreen(this, this.client.options));
        }
    }
}
