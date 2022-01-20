package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class HornOfPlenty extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/test5.png");
    public static final String ID = "Horn Of Plenty";

    public HornOfPlenty() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HornOfPlenty();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.rarity == AbstractCard.CardRarity.RARE && !this.grayscale) {
            this.flash();
            this.grayscale = true;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class FreeToPlayRareCardPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> freeToPlayCard(AbstractCard card) {
            AbstractPlayer player = AbstractDungeon.player;
            if (player.hasRelic(ID) && !player.getRelic(ID).grayscale) {
                if (card.rarity == AbstractCard.CardRarity.RARE) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
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
