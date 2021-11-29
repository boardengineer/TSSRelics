package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class TacticalHarness extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/tacticalharness.png");
    public static final String ID = "Tactical Harness";

    public TacticalHarness() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TacticalHarness();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @SpirePatch(clz = MakeTempCardInHandAction.class, paramtypez = {AbstractCard.class, int.class}, method = SpirePatch.CONSTRUCTOR)
    public static class MakeMoreShivsPatch {
        @SpirePostfixPatch
        public static void makeMoreShivs(MakeTempCardInHandAction makeAction, AbstractCard card, int amount) {
            if (AbstractDungeon.player.hasRelic(ID) && card instanceof Shiv) {
                makeAction.amount++;
            }
        }
    }
}
