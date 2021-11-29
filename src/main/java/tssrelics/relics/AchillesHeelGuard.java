package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class AchillesHeelGuard extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/heel.png");
    public static final String ID = "Achilles Heel Guard";

    public AchillesHeelGuard() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }


    @Override
    public void wasHPLost(int damageAmount) {
        if (damageAmount > 0 && AbstractDungeon
                .getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !this.grayscale) {
            this.flash();
            this.pulse = false;
            this.grayscale = true;
        }
    }

    public void atTurnStart() {
        if (!this.grayscale) {
            this.flash();
            this.addToTop(new GainEnergyAction(1));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AchillesHeelGuard();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
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
