package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PaperBomb extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/paperbomb.png");
    public static final String ID = "Paper Bomb";

    private static final int DAMAGE = 4;
    private static final int NUM_CARDS = 3;

    public PaperBomb() {
        super(ID, IMAGE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++this.counter;
            if (this.counter % NUM_CARDS == 0) {
                this.counter = 0;
                this.flash();
                this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                this.addToBot(new DamageAllEnemiesAction(null, DamageInfo
                        .createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));

            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PaperBomb();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + NUM_CARDS + this.DESCRIPTIONS[1] + DAMAGE + this.DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }
}
