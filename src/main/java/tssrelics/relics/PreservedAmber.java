package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PreservedAmber extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/runicSphere.png");
    public static final String ID = "Preserved Amber";

    public PreservedAmber() {
        super(ID, IMAGE, RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PreservedAmber();
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            this.flash();

            AbstractDungeon.getCurrRoom().monsters.monsters.stream().forEach(monster ->
                    addToBot(new ApplyPowerAction(monster, AbstractDungeon.player, new StrengthPower(monster, -2), -2, true, AbstractGameAction.AttackEffect.NONE))
            );
        }
    }
    
    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
