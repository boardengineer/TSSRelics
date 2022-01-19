package tssrelics.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class FestivuePole extends CustomRelic {
    private static final Texture IMAGE = new Texture("img/pole.png");
    public static final String ID = "Festivus Pole";

    public FestivuePole() {
        super(ID, IMAGE, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FestivuePole();
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new AirGrievancesOption());
    }

    public static class AirGrievancesOption extends AbstractCampfireOption {
        public AirGrievancesOption() {
            this.label = "Air Grievances";
            this.description = "Have a lot of problems with some card? let it know!";
            this.img = new Texture("img/grieve.png");
        }

        public void useOption() {
            AbstractDungeon.effectList.add(new AirGrievencesEffect());
        }
    }

    public static class AirGrievencesEffect extends AbstractGameEffect {
        private boolean openedScreen = false;
        private final Color screenColor;

        public AirGrievencesEffect() {
            this.screenColor = AbstractDungeon.fadeColor.cpy();
            this.duration = 2.0F;
            this.screenColor.a = 0.0F;
            ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
        }

        public void update() {
            if (!AbstractDungeon.isScreenUp) {
                this.duration -= Gdx.graphics.getDeltaTime();
                this.updateBlackScreenColor();
            }

            if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards
                        .get(0);
                CardCrawlGame.sound.play("CARD_EXHAUST");
                AbstractDungeon.topLevelEffects
                        .add(new PurgeCardEffect(card, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                AbstractDungeon.player.masterDeck.removeCard(card);
                AbstractDungeon.transformCard(card, true, AbstractDungeon.miscRng);
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.TRANSFORM && AbstractDungeon.transformedCard != null) {
                    AbstractDungeon.topLevelEffectsQueue
                            .add(new ShowCardAndObtainEffect(AbstractDungeon
                                    .getTransformedCard(), (float) Settings.WIDTH / 3.0F, (float) Settings.HEIGHT / 2.0F, false));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

            if (this.duration < 1.0F && !this.openedScreen) {
                this.openedScreen = true;
                AbstractDungeon.gridSelectScreen.open(CardGroup
                        .getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                .getPurgeableCards()), 1, "choose a card to transform and upgrade", false, false, false, false);
            }

            if (this.duration < 0.0F) {
                this.isDone = true;
                if (CampfireUI.hidden) {
                    AbstractRoom.waitTimer = 0.0F;
                    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                    ((RestRoom) AbstractDungeon.getCurrRoom()).cutFireSound();
                }
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
}
