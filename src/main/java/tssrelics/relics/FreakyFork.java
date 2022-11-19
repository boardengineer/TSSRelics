package tssrelics.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FreakyFork extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/fork.png");
    public static final String ID = "Freaky Fork";

    public FreakyFork() {
        super(ID, IMAGE, RelicTier.SHOP, AbstractRelic.LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FreakyFork();
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class MaybeDupePowerPatch {
        @SpirePrefixPatch
        public static void maybeDupePower(UseCardAction useCardAction) {
            if (AbstractDungeon.player.hasRelic(ID)) {
                float duration = ReflectionHacks
                        .getPrivate(useCardAction, AbstractGameAction.class, "duration");
                if (duration == 0.15F) {
                    AbstractCard targetCard = ReflectionHacks
                            .getPrivate(useCardAction, UseCardAction.class, "targetCard");

                    if (targetCard.type == AbstractCard.CardType.POWER && AbstractDungeon.cardRandomRng
                            .randomBoolean()) {
                        AbstractDungeon.player.getRelic(ID).flash();
                        AbstractDungeon.actionManager
                                .addToBottom(new MakeTempCardInDiscardAction(targetCard
                                        .makeStatEquivalentCopy(), 1));
                    }
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
