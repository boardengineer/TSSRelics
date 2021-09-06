package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.powers.CreativeAIPower;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BirdFacedUrn;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.MummifiedHand;

public class CircularLogicWidget extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/widget.png");
    public static final String ID = "Circular Logic Widget";

    public CircularLogicWidget() {
        super(ID, IMAGE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        addRelic(BirdFacedUrn.ID);
        addRelic(MummifiedHand.ID);
        addRelic(Enchiridion.ID);
    }

    @Override
    public void atBattleStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MasterRealityPower(AbstractDungeon.player), 1, true));
    }

    @Override
    public void atTurnStart() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CreativeAIPower(AbstractDungeon.player, 1), 1, true));
    }

    private static void addRelic(String id) {
        AbstractRelic relic = RelicLibrary.getRelic(id).makeCopy();
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
    }

}
