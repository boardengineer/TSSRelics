package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VampireFang extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/vampirefang.png");
    public static final String ID = "Vampire Fang";

    public VampireFang() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VampireFang();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.hasTag(AbstractCard.CardTags.STRIKE)) {
            addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1, 0.0F));
        }
    }
}
