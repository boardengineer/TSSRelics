package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class VampireFang extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/test8.png");
    public static final String ID = "Vampire Fang";

    public VampireFang() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VampireFang();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 1, 0.0F));
        }
    }
}
