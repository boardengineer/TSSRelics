//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class TopMask extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/nloth.png");
    public static final String ID = "TopMask";

    public TopMask() {
        super("TopMask", IMAGE, RelicTier.SPECIAL, LandingSound.FLAT);
        this.counter = 1;
    }

    public void onChestOpenAfter(boolean bossChest) {
        if (!bossChest && this.counter > 0) {
            --this.counter;
            this.flash();
            AbstractDungeon.getCurrRoom().removeOneRelicFromRewards();
            AbstractDungeon.getCurrRoom().rewards
                    .add(new RewardItem(new UnceasingTop().makeCopy()));
            if (this.counter == 0) {
                this.setCounter(-2);
            }
        }

    }

    public void setCounter(int setCounter) {
        this.counter = setCounter;
        if (setCounter == -2) {
            this.usedUp();
            this.counter = -2;
        }

    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new TopMask();
    }
}
