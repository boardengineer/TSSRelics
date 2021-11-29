package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import tssrelics.actions.RunicReplicatorAction;

public class RunicReplicator extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/runicreplicator.png");
    public static final String ID = "Runic Replicator";

    public RunicReplicator() {
        super(ID, IMAGE, RelicTier.BOSS, LandingSound.HEAVY);
    }

    public void atTurnStartPostDraw() {
        if (!this.grayscale) {
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new RunicReplicatorAction());
            this.grayscale = true;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RunicReplicator();
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
