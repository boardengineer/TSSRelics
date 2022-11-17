package tssrelics.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.green.Eviscerate;
import com.megacrit.cardcrawl.cards.purple.Blasphemy;
import com.megacrit.cardcrawl.cards.red.SearingBlow;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theVacant.cards.Attacks.SoulBarrage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class PrismaticBranch extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/prismaticbranch.png");

    public static CardGroup commonCardPool;
    public static CardGroup uncommonCardPool;
    public static CardGroup rareCardPool;

    public static CardGroup srcCommonCardPool;
    public static CardGroup srcUncommonCardPool;
    public static CardGroup srcRareCardPool;

    public static ArrayList<AbstractCard> completeList = new ArrayList();

    public static final String ID = "PrismaticBranch";

    public PrismaticBranch() {
        super(ID, IMAGE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public void onExhaust(AbstractCard card) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new MakeTempCardInHandAction(returnRandomCard().makeCopy(), false));
        }
    }

    @Override
    public void atBattleStart() {
        this.grayscale = false;
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MasterRealityPower(AbstractDungeon.player), 1, true));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PrismaticBranch();
    }

    public static AbstractCard returnRandomCard() {
        return completeList.get(AbstractDungeon.cardRandomRng.random(completeList.size() - 1));
    }

    public static void initializeCardPools() {
        commonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        uncommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        rareCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        commonCardPool.clear();
        uncommonCardPool.clear();
        rareCardPool.clear();
        ArrayList<AbstractCard> tmpPool = new ArrayList();


        CardLibrary.addRedCards(tmpPool);
        CardLibrary.addGreenCards(tmpPool);
        CardLibrary.addBlueCards(tmpPool);
        CardLibrary.addPurpleCards(tmpPool);

        tmpPool.addAll(BaseMod.getCustomCardsToAdd());

        Iterator var4 = tmpPool.iterator();

        AbstractCard c;
        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            if (!CardLibrary.cards.containsKey(c.cardID)) {
                continue;
            }

            switch (c.rarity) {
                case COMMON:
                    commonCardPool.addToTop(c);
                    break;
                case UNCOMMON:
                    uncommonCardPool.addToTop(c);
                    break;
                case RARE:
                    rareCardPool.addToTop(c);
                    break;
                case CURSE:
                    break;
            }
        }

        srcRareCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        srcUncommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        srcCommonCardPool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        var4 = rareCardPool.group.iterator();

        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            srcRareCardPool.addToBottom(c);
        }

        var4 = uncommonCardPool.group.iterator();

        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            srcUncommonCardPool.addToBottom(c);
        }

        var4 = commonCardPool.group.iterator();

        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            srcCommonCardPool.addToBottom(c);
        }


        completeList.addAll(srcCommonCardPool.group);
        completeList.addAll(srcUncommonCardPool.group);
        completeList.addAll(srcRareCardPool.group);

        Iterator<AbstractCard> deleter = completeList.iterator();

        while (deleter.hasNext()) {
            if (ILLEGAL_CARD_IDS.contains(deleter.next().cardID)) {
                deleter.remove();
            }
        }
    }

    public static HashSet<String> ILLEGAL_CARD_IDS = new HashSet<String>() {{
        add(Eviscerate.ID);
        add(Blasphemy.ID);

        // There's a strange effect with cards that can be upgraded multiple times
        if (BaseMod.hasModID("VacantState:")) {
            add(SoulBarrage.ID);
        }
        add(SearingBlow.ID);
    }};
}
