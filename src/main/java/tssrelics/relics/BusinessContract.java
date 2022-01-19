package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;

public class BusinessContract extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("img/contract.png");
    public static final String ID = "Business Contract";

    public BusinessContract() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BusinessContract();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @SpirePatch(clz = ShopScreen.class, paramtypez = {}, method = "updatePurge")
    public static class BuffElitePatch {
        @SpirePostfixPatch
        public static void allowPurge(ShopScreen shopRoom) {
            if (AbstractDungeon.player.hasRelic(ID)) {
                AbstractDungeon.shopScreen.purgeAvailable = true;
            }
        }
    }
}
