package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import communicationmod.ChoiceScreenUtils;
import communicationmod.CommunicationMod;
import communicationmod.patches.ShopScreenPatch;

import java.util.ArrayList;
import java.util.Objects;

public class DiceOfFate extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("images/relics/test7.png");
    public static final Texture REROLL_IMAGE = new Texture("loadstate.png");
    public static final String ID = "Dice Of Fate";
    public static RerollStoreChoice rerollChoice = new RerollStoreChoice();

    public DiceOfFate() {
        super(ID, IMAGE, RelicTier.SHOP, LandingSound.SOLID);
        cost = 75;
    }

    @Override
    public int getPrice() {
        return 75;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DiceOfFate();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public static class RerollStoreChoice {
        public Hitbox hb = new Hitbox(X, Y, REROLL_IMAGE.getWidth(), REROLL_IMAGE.getHeight());
        public static final float X = 75;
        public static final float Y = 500;

        public void update() {
            boolean clicked = hb.clicked;
            this.hb.update();
            if (clicked || hb.hovered && InputHelper.justClickedLeft) {
                hb.clicked = false;
                if (isActive()) {
                    AbstractDungeon.shopScreen.init(getColorCards(), getColorlessCards());
                    CommunicationMod.mustSendGameState = true;
                    if (AbstractDungeon.player.hasRelic(ID)) {
                        AbstractDungeon.player.getRelic(ID).grayscale = true;
                    }
                }
            }
        }

        public void render(SpriteBatch sb) {
            if (isActive()) {
                sb.draw(REROLL_IMAGE, X, Y);
            }
        }
    }

    // Patch the shop screen to display and update the reroll button
    @SpirePatch(clz = ShopScreen.class, method = "render")
    public static class DrawRerollButtonPatch {
        @SpirePostfixPatch
        public static void render(ShopScreen shopScreen, SpriteBatch sb) {
            rerollChoice.render(sb);
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "update")
    public static class UpdateRerollButtonPatch {
        @SpirePostfixPatch
        public static void update(ShopScreen shopScreen) {
            rerollChoice.update();
        }
    }

    // Patch Communication Mod to include the reroll option and process it when selected
    @SpirePatch(clz = ChoiceScreenUtils.class, method = "getAvailableShopItems")
    public static class GetAvailableShopItemsPatch {
        @SpirePrefixPatch
        public static SpireReturn<ArrayList<Object>> addReroll() {
            return SpireReturn.Return(getAvailableShopItemsWithPossibleReroll());
        }
    }

    @SpirePatch(clz = ChoiceScreenUtils.class, method = "makeShopScreenChoice")
    public static class MakeShopScreenChoicePatch {
        @SpirePrefixPatch
        public static SpireReturn addReroll(int choice) {
            ArrayList<Object> shopItems = getAvailableShopItemsWithPossibleReroll();
            Object shopItem = shopItems.get(choice);
            if (shopItem instanceof String) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
                AbstractDungeon.gridSelectScreen.open(
                        CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                .getPurgeableCards()),
                        1, ShopScreen.NAMES[13], false, false, true, true);
            } else if (shopItem instanceof AbstractCard) {
                AbstractCard card = (AbstractCard) shopItem;
                ShopScreenPatch.doHover = true;
                ShopScreenPatch.hoverCard = card;
                card.hb.clicked = true;
            } else if (shopItem instanceof StoreRelic) {
                StoreRelic relic = (StoreRelic) shopItem;
                relic.relic.hb.clicked = true;
            } else if (shopItem instanceof StorePotion) {
                StorePotion potion = (StorePotion) shopItem;
                potion.potion.hb.clicked = true;
            } else if (shopItem instanceof RerollStoreChoice) {
                rerollChoice.hb.clicked = true;
            }

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = ChoiceScreenUtils.class, method = "getShopScreenChoices")
    public static class GetShopScreenChoicesPatch {
        @SpirePrefixPatch
        public static SpireReturn addReroll() {
            ArrayList<String> choices = new ArrayList<>();
            ArrayList<Object> shopItems = getAvailableShopItemsWithPossibleReroll();
            for (Object item : shopItems) {
                if (item instanceof String) {
                    choices.add((String) item);
                } else if (item instanceof AbstractCard) {
                    choices.add(((AbstractCard) item).name.toLowerCase());
                } else if (item instanceof StoreRelic) {
                    choices.add(((StoreRelic) item).relic.name);
                } else if (item instanceof StorePotion) {
                    choices.add(((StorePotion) item).potion.name);
                } else if (item instanceof RerollStoreChoice) {
                    choices.add("reroll");
                }
            }

            return SpireReturn.Return(choices);
        }
    }

    public static ArrayList<Object> getAvailableShopItemsWithPossibleReroll() {
        ArrayList<Object> choices = new ArrayList<>();
        ShopScreen screen = AbstractDungeon.shopScreen;
        if (screen.purgeAvailable && AbstractDungeon.player.gold >= ShopScreen.actualPurgeCost) {
            choices.add("purge");
        }

        for (AbstractCard card : ChoiceScreenUtils.getShopScreenCards()) {
            if (card.price <= AbstractDungeon.player.gold) {
                choices.add(card);
            }
        }

        for (StoreRelic relic : ChoiceScreenUtils.getShopScreenRelics()) {
            if (relic.price <= AbstractDungeon.player.gold) {
                choices.add(relic);
            }
        }

        for (StorePotion potion : ChoiceScreenUtils.getShopScreenPotions()) {
            if (potion.price <= AbstractDungeon.player.gold) {
                choices.add(potion);
            }
        }

        if (isActive()) {
            choices.add(rerollChoice);
        }

        return choices;
    }

    private static ArrayList<AbstractCard> getColorlessCards() {
        ArrayList<AbstractCard> result = new ArrayList<>();

        result.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON)
                                  .makeCopy());
        result.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE)
                                  .makeCopy());

        return result;
    }

    private static ArrayList<AbstractCard> getColorCards() {
        ArrayList<AbstractCard> result = new ArrayList<>();

        AbstractCard c;
        for (c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true)
                .makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true)
                .makeCopy()) {
        }

        result.add(c);

        for (c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true)
                .makeCopy(); Objects
                     .equals(c.cardID, result.get(result
                             .size() - 1).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true)
                .makeCopy()) {
        }

        result.add(c);

        for (c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true)
                .makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true)
                .makeCopy()) {
        }

        result.add(c);

        for (c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true)
                .makeCopy(); Objects.equals(c.cardID, result.get(result
                .size() - 1).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true)
                .makeCopy()) {
        }

        result.add(c);

        for (c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true)
                .makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon
                .getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true)
                .makeCopy()) {
        }

        result.add(c);
        return result;
    }

    private static boolean isActive() {
        return AbstractDungeon.player.hasRelic(ID) && !AbstractDungeon.player
                .getRelic(ID).grayscale;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (AbstractDungeon.player.hasRelic(ID)) {
            AbstractDungeon.player.getRelic(ID).grayscale = false;
        }
    }
}
