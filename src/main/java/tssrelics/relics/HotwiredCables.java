package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HotwiredCables extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/runicSphere.png");
    public static final String ID = "Hotwired Cables";

    public HotwiredCables() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HotwiredCables();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
    public static class evokeOrbPatch {
        @SpirePostfixPatch
        public static void evokeOrb(AbstractPlayer player, AbstractOrb orb) {
            if (AbstractDungeon.player.hasRelic(ID) && player.maxOrbs > 0) {
                AbstractDungeon.actionManager.addToBottom(new AnimateOrbAction(1));
                AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1));
            }
        }
    }

}
