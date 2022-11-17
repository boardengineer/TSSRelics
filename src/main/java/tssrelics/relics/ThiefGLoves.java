package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

public class ThiefGLoves extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/gloves.png");
    public static final String ID = "Thief Gloves";

    public ThiefGLoves() {
        super(ID, IMAGE, RelicTier.RARE, AbstractRelic.LandingSound.SOLID);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.flash();

            AbstractCreature target = action.target;

            if (target != null) {
                AbstractDungeon.effectList.add(new GainPennyEffect(target.hb.cX, target.hb.cY));
            } else {
                AbstractDungeon.effectList.add(new RainingGoldEffect(1, true));
            }

            this.addToBot(new GainGoldAction(1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
