package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoxiousFumesPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NoxiousStone extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/widget.png");
    public static final String ID = "Noxious Stone";

    public NoxiousStone() {
        super(ID, IMAGE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NoxiousFumesPower(AbstractDungeon.player, 2), 2));

        AbstractDungeon.getMonsters().monsters.forEach(monster -> {
            this.addToTop(new ApplyPowerAction(monster, monster, new StrengthPower(monster, 1), 1));
        });
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NoxiousStone();
    }
}
