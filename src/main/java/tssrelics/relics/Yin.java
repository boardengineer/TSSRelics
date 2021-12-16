package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Yin extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/yin.png");
    public static final String ID = "Yin";

    public Yin() {
        super(ID, IMAGE, RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Yin();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
        if (card.type == AbstractCard.CardType.SKILL) {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            this.addToBot(new RelicAboveCreatureAction(p, this));
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
            this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, 1), 1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
