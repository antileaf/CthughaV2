package com.cthugha.relics.cthugha;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Overclock;
import com.megacrit.cardcrawl.cards.colorless.TheBomb;
import com.megacrit.cardcrawl.cards.purple.SimmeringFury;
import com.megacrit.cardcrawl.cards.purple.WreathOfFlame;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.Arrays;

public class EverBurningStone extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(EverBurningStone.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/EverBurningStone.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/EverBurningStone.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/EverBurningStone.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.RARE;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

	public static final String[] CARDS = {
			Burn.ID,
			TheBomb.ID,
			SearingBlow.ID,
			FlameBarrier.ID,
			BurningPact.ID,
			FireBreathing.ID,
			Inflame.ID,
			Combust.ID,
			FiendFire.ID,
			Immolate.ID,
			Overclock.ID,
			WreathOfFlame.ID,
			SimmeringFury.ID
	};

	public EverBurningStone() {
		super(
				ID,
				ImageMaster.loadImage(IMG_PATH),
				ImageMaster.loadImage(IMG_OTL),
				RELIC_TIER,
				LANDING_SOUND
		);

		this.largeImg = ImageMaster.loadImage(IMG_LARGE);

		this.tips.add(new PowerTip(this.DESCRIPTIONS[1],
				String.format(this.DESCRIPTIONS[2],
						String.join(" , ", Arrays.stream(CARDS)
								.map(id -> String.join(" ",
										Arrays.stream(CardLibrary.getCard(id).name.split(" "))
												.map(s -> "#y" + s)
												.toArray(String[]::new)))
								.toArray(String[]::new))))); // 诗人握持
	}

	private static ArrayList<AbstractCard> generate() {
		int size = 3;
		for (AbstractRelic r : AbstractDungeon.player.relics)
			size = r.changeNumberOfCardsInReward(size);

		if (ModHelper.isModEnabled("Binary"))
			size--;

		ArrayList<AbstractCard> cards = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			String id = CARDS[AbstractDungeon.cardRng.random(CARDS.length - 1)];
			if (cards.stream().noneMatch(c -> c.cardID.equals(id)))
				cards.add(CardLibrary.getCard(id).makeCopy());
			else
				i--;
		}

		float upgradeChance = ReflectionHacks.getPrivateStatic(AbstractDungeon.class, "cardUpgradedChance");

		for (AbstractCard card : cards) {
			if (card.rarity != AbstractCard.CardRarity.RARE &&
					AbstractDungeon.cardRng.randomBoolean(upgradeChance) &&
					card.canUpgrade())
				card.upgrade();
			else {
				for (AbstractRelic r : AbstractDungeon.player.relics)
					r.onPreviewObtainCard(card);
			}
		}

		return cards;
	}

	@Override
	public void onEquip() {
		int size = 3;
		for (AbstractRelic r : AbstractDungeon.player.relics)
			size = r.changeNumberOfCardsInReward(size);

		if (ModHelper.isModEnabled("Binary"))
			size--;

		if (size <= 0) {
			if (AbstractDungeon.player.hasRelic(BustedCrown.ID))
				AbstractDungeon.player.getRelic(BustedCrown.ID).flash();

			return;
		}

		AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
			private int counter = 2;
			private boolean wait = false;

			@Override
			public void update() {
				if (this.counter > 0 && !this.wait) {
					this.counter--;
					this.wait = true;

					AbstractDungeon.cardRewardScreen.customCombatOpen(EverBurningStone.generate(),
							CardRewardScreen.TEXT[1], true);
				}

				if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
					AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(
							AbstractDungeon.cardRewardScreen.discoveryCard,
							Settings.WIDTH * 0.5F,
							Settings.HEIGHT * 0.5F));
					AbstractDungeon.cardRewardScreen.discoveryCard = null;
					this.wait = false;

					if (this.counter == 0)
						this.isDone = true;
				}
			}

			@Override
			public void render(SpriteBatch sb) {}

			@Override
			public void dispose() {}
		});
	}

	@Override
	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}
}
