package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FiliformNeedle extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/filiformneedles.png");
    public static final String ID = "Filiform Needle";

    public FiliformNeedle() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    public void atTurnStartPostDraw() {
        AbstractDungeon.getMonsters().monsters.forEach(monster -> {
            addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new MarkPower(monster, 1), 1));
        });

        addToBot(new TriggerMarksAction(null));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FiliformNeedle();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @SpirePatch(clz = MarkPower.class, method = "triggerMarks")
    public static class allowNullCardsPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> allowNull(MarkPower markPower, AbstractCard card) {
            if (card == null) {
                AbstractDungeon.actionManager
                        .addToBottom(new LoseHPAction(markPower.owner, null, markPower.amount, AbstractGameAction.AttackEffect.FIRE));

                return SpireReturn.Return(null);
            }

            return SpireReturn.Continue();
        }
    }

}
