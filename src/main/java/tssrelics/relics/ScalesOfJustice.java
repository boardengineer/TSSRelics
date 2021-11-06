package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.WrathStance;

public class ScalesOfJustice extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/monocle.png");
    public static final String ID = "Scales of Justice";

    public ScalesOfJustice() {
        super(ID, IMAGE, RelicTier.SHOP, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MasterRealityPower(AbstractDungeon.player), 1, true));
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.player.stance instanceof WrathStance) {
            this.addToBot(new DrawCardAction(2));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.stance instanceof CalmStance) {
            this.addToBot(new RetainCardsAction(AbstractDungeon.player, 2));
        }
    }
}