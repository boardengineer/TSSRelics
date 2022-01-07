package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ClericsGoldenHelm extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/helmet.png");
    public static final String ID = "Clerics Golden Helm";

    private static final int HEAL_AMOUNT = 5;

    public ClericsGoldenHelm() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ClericsGoldenHelm();
    }

    public void onObtainCard(AbstractCard c) {
        AbstractDungeon.player.heal(HEAL_AMOUNT, true);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + HEAL_AMOUNT + this.DESCRIPTIONS[1];
    }
}
