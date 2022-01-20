package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ComicBook extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/test1.png");
    public static final String ID = "Comic Book";

    public ComicBook() {
        super(ID, IMAGE, RelicTier.SHOP, LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.color == AbstractCard.CardColor.COLORLESS && !this.grayscale) {
            this.grayscale = true;
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }

            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float) Settings.HEIGHT / 2.0F;
            tmp.applyPowers();
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager
                    .addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            this.pulse = false;
        }

    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ComicBook();
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
    }
}
