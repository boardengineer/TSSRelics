package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TungstenShield extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/shield.png");
    public static final String ID = "Tungsten Shield";

    public TungstenShield() {
        super(ID, IMAGE, RelicTier.RARE, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TungstenShield();
    }

    @SpirePatch(clz = AbstractCreature.class, method = "loseBlock", paramtypez = {int.class})
    public static class CheckForShieldPatch {
        @SpirePrefixPatch
        public static SpireReturn checkForShield(AbstractCreature creature, int amount) {
            AbstractPlayer player = AbstractDungeon.player;
            if (creature == player) {
                if (player.hasRelic(ID) && amount < creature.currentBlock) {
                    player.getRelic(ID).flash();
                    player.loseBlock(amount - 2, false);
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
