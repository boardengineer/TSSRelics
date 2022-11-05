package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class JadeMysticKnot extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/jademysticknot.png");
    public static final String ID = "Jade Mystic Knot";
    private static final int HP_GAIN = 2;

    public JadeMysticKnot() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new JadeMysticKnot();
    }

    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {})
    public static class InstantObtainPatch {
        @SpirePrefixPatch
        public static void maybeGainMaxHp(AbstractRelic relic) {
            if (AbstractDungeon.player.hasRelic(ID)) {
                AbstractDungeon.player.increaseMaxHp(HP_GAIN, false);
            }
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "instantObtain", paramtypez = {AbstractPlayer.class, int.class, boolean.class})
    public static class OtherInstantObtainPatch {
        @SpirePrefixPatch
        public static void maybeGainMaxHp(AbstractRelic relic, AbstractPlayer p, int slot, boolean callOnEquip) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
                if (callOnEquip) {
                    AbstractDungeon.player.increaseMaxHp(HP_GAIN, false);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractRelic.class, method = "obtain")
    public static class obtainPatch {
        @SpirePrefixPatch
        public static void maybeGainMaxHp(AbstractRelic relic) {
            if (AbstractDungeon.player.hasRelic(ID)) {
                AbstractDungeon.player.increaseMaxHp(HP_GAIN, false);
            }
        }
    }
}
