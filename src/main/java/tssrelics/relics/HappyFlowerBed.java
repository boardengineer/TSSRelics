package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.HappyFlower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import java.util.ArrayList;

public class HappyFlowerBed extends CustomRelic {
    private static final Texture IMAGE = ImageMaster.loadImage("img/flowerbed.png");
    public static final String ID = "Happy Flower Bed";
    private boolean initialFlowersReceived = true;

    public HappyFlowerBed() {
        super(ID, IMAGE, RelicTier.BOSS, LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HappyFlowerBed();
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new PlantOption());
    }

    @Override
    public void onEquip() {
        this.initialFlowersReceived = false;
    }

    @Override
    public void update() {
        super.update();
        if (!this.initialFlowersReceived && !AbstractDungeon.isScreenUp) {

            int numFlowers = AbstractDungeon.actNum;
            if (AbstractDungeon.floorNum < 5) {
                numFlowers = 0;
            }

            System.err.println("we should plant " + numFlowers);

            if (numFlowers > 0) {
                AbstractDungeon.combatRewardScreen.open();
                AbstractDungeon.combatRewardScreen.rewards.clear();

                for (int i = 0; i < numFlowers; i++) {
                    AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem(new HappyFlower()));
                }

                AbstractDungeon.combatRewardScreen.positionRewards();
                AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
            }
            this.initialFlowersReceived = true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public static class PlantOption extends AbstractCampfireOption {
        public PlantOption() {
            this.label = "Plant";
            this.description = "Gain A Happy Flower";
            this.img = new Texture("img/plant.png");
        }

        public void useOption() {
            AbstractDungeon.effectList.add(new CampfireHappyFlowerBedEffect());
        }
    }

    public static class CampfireHappyFlowerBedEffect extends AbstractGameEffect {
        private boolean hasPlanted = false;
        private final Color screenColor;

        public CampfireHappyFlowerBedEffect() {
            this.screenColor = AbstractDungeon.fadeColor.cpy();
            this.duration = 2.0F;
            this.screenColor.a = 0.0F;
            ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
        }

        public void update() {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
            if (this.duration < 1.0F && !this.hasPlanted) {

                this.hasPlanted = true;
                new HappyFlower().instantObtain();

                AbstractDungeon.topLevelEffects
                        .add(new BorderFlashEffect(new Color(0.8F, 0.6F, 0.1F, 0.0F)));
            }

            if (this.duration < 0.0F) {
                this.isDone = true;
                ((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }

        }

        private void updateBlackScreenColor() {
            if (this.duration > 1.5F) {
                this.screenColor.a = Interpolation.fade
                        .apply(1.0F, 0.0F, (this.duration - 1.5F) * 2.0F);
            } else if (this.duration < 1.0F) {
                this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
            } else {
                this.screenColor.a = 1.0F;
            }

        }

        public void render(SpriteBatch sb) {
            sb.setColor(this.screenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        }

        public void dispose() {
        }
    }

    private static void obtainStaggeredHappyFlower() {
        AbstractRelic flower = new HappyFlower().makeCopy();

        long min = 1000;
        int best = 0;
        for (int counter = 0; counter < 3; counter++) {
            final int compareCounter = counter;

            long numFlowers = AbstractDungeon.player.relics.stream()
                                                           .filter(relic -> relic.relicId
                                                                   .equals(HappyFlower.ID) && relic.counter == compareCounter)
                                                           .count();

            if (numFlowers < min) {
                min = numFlowers;
                best = counter;
            }
        }

        flower.instantObtain();
        flower.counter = best;
    }

    @SpirePatch(clz = HappyFlower.class, method = "onEquip")
    public static class StaggedHappyFlowerOnEquipPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> staggerHappyFlower(HappyFlower happyFlower) {
            long min = 1000;
            int best = 0;
            for (int counter = 0; counter < 3; counter++) {
                final int compareCounter = counter;

                long numFlowers = AbstractDungeon.player.relics.stream()
                                                               .filter(relic -> relic.relicId
                                                                       .equals(HappyFlower.ID) && relic.counter == compareCounter)
                                                               .count();

                if (numFlowers < min) {
                    min = numFlowers;
                    best = counter;
                }
            }

            happyFlower.counter = best;

            return SpireReturn.Return(null);
        }
    }
}
