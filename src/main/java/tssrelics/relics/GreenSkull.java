package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GreenSkull extends CustomRelic {
    public static final String ID = "Green Skull";

    private static final Texture IMAGE = new Texture("img/green_skull.png");
    public boolean isActive = false;

    public GreenSkull() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.isActive = false;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if (!GreenSkull.this.isActive && AbstractDungeon.player.isBloodied) {
                    GreenSkull.this.flash();
                    GreenSkull.this.pulse = true;
                    AbstractDungeon.player.addPower(new DexterityPower(AbstractDungeon.player, 3));
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, GreenSkull.this));
                    GreenSkull.this.isActive = true;
                    AbstractDungeon.onModifyPower();
                }

                this.isDone = true;
            }
        });
    }

    @Override
    public void onBloodied() {
        this.flash();
        this.pulse = true;
        if (!this.isActive && AbstractDungeon
                .getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 3), 3));
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.isActive = true;
            AbstractDungeon.player.hand.applyPowers();
        }
    }

    @Override
    public void onNotBloodied() {
        if (this.isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, -3), -3));
        }

        this.stopPulse();
        this.isActive = false;
        AbstractDungeon.player.hand.applyPowers();
    }
}
