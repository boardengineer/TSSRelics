package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class FreshWater extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/bottle.png");
    public static final String ID = "Fresh Water";

    private static final int HEAL_AMOUNT = 50;
    private static final int BASE_PRICE = 50;

    public FreshWater() {
        super(ID, IMAGE, RelicTier.SHOP, LandingSound.HEAVY);
        cost = 50;
    }

    @Override
    public int getPrice() {
        return BASE_PRICE;
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.heal(HEAL_AMOUNT, true);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FreshWater();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + HEAL_AMOUNT + this.DESCRIPTIONS[1];
    }
}
