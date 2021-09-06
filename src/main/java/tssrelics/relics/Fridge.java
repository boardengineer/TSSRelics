//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.colorless.HandOfGreed;
import com.megacrit.cardcrawl.cards.purple.LessonLearned;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.HashSet;

public class Fridge extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/PrismaticBranch.png");

    public static final String ID = "Cheesy Fridge";
    private boolean cardsReceived = true;

    public Fridge() {
        super(ID, IMAGE, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        this.cardsReceived = false;

        FOOD.forEach(Fridge::addRelic);

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new LessonLearned(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));


        CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.2F, -0.3F));
    }

    private static void addRelic(String id) {
        AbstractRelic relic = RelicLibrary.getRelic(id).makeCopy();
        relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
    }

    public static HashSet<String> FOOD = new HashSet<String>() {{
        add(Ginger.ID);
        add(Turnip.ID);
        add(IceCream.ID);
        add(Pear.ID);
        add(Strawberry.ID);
        add(Mango.ID);
        add(Waffle.ID);
        add(MeatOnTheBone.ID);
        add(OddMushroom.ID);
    }};

    @Override
    public void atBattleStart() {
        this.grayscale = false;
        this.addToBot(new MakeTempCardInHandAction(new HandOfGreed(), 1, false));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MasterRealityPower(AbstractDungeon.player), 1, true));
    }

    @Override
    public void onGainGold() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.player.heal(3, true);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.grayscale) {
            this.grayscale = true;
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            tmp.applyPowers();
            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            this.pulse = false;
        }

    }
}
