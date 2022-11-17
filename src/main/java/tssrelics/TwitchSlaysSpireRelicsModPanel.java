package tssrelics;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.io.IOException;

public class TwitchSlaysSpireRelicsModPanel extends ModPanel {
    public static final String EXPERIMENTAL_RELICS_KEY = "experimental_relics";

    TwitchSlaysSpireRelicsModPanel() {
        super();

        ModLabeledToggleButton experimentalRelicsToggle = new ModLabeledToggleButton("Enable Experimental Buttons", 350.0F, 750.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, true, this, (l) -> {
        }, (button) -> {
            TwitchSlaysSpireRelicsMod.modConfig.setBool(EXPERIMENTAL_RELICS_KEY, button.enabled);
            try {
                TwitchSlaysSpireRelicsMod.modConfig.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addUIElement(experimentalRelicsToggle);
    }
}
