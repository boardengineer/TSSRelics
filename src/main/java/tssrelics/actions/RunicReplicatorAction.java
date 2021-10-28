package tssrelics.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RunicReplicatorAction extends AbstractGameAction {
    private static final float DURATION;
    private final AbstractPlayer p;

    public RunicReplicatorAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
        this.p = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
            } else if (this.p.hand.size() == 1) {
                addCopyToHand(p.hand.getBottomCard());
                this.isDone = true;
            } else {
                AbstractDungeon.handCardSelectScreen
                        .open("Copy a card, it will cost 0 this turn", 1, false, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard tmpCard = AbstractDungeon.handCardSelectScreen.selectedCards
                        .getBottomCard();

                addCopyToHand(tmpCard);

                AbstractDungeon.player.hand.addToHand(tmpCard);
                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }

            this.tickDuration();
        }
    }

    private void addCopyToHand(AbstractCard card) {
        AbstractCard copy = card.makeStatEquivalentCopy();
        copy.setCostForTurn(0);
        p.hand.addToTop(copy);
    }

    static {
        DURATION = Settings.ACTION_DUR_XFAST;
    }
}
