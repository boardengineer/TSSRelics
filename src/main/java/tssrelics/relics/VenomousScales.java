package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VenomousScales extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/venomousscales.png");
    public static final String ID = "Venomous Scales";

    private static final int POISON_AMOUNT = 2;

    public VenomousScales() {
        super(ID, IMAGE, RelicTier.COMMON, LandingSound.HEAVY);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != AbstractDungeon.player) {
            this.flash();
            this.addToTop(new ApplyPowerAction(info.owner, AbstractDungeon.player, new PoisonPower(info.owner, AbstractDungeon.player, POISON_AMOUNT), POISON_AMOUNT));
        }

        return damageAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VenomousScales();
    }
}
