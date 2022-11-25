package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RateLimiter extends CustomRelic {
    private static final int FOCUS_AMOUNT = 2;

    public static final String ID = "Rate Limiter";
    private static final Texture IMAGE = new Texture("img/widget.png");

    public RateLimiter() {
        super(ID, IMAGE, AbstractRelic.RelicTier.SHOP, AbstractRelic.LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, FOCUS_AMOUNT), FOCUS_AMOUNT));
        this.addToBot(new DecreaseMaxOrbAction(1));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
