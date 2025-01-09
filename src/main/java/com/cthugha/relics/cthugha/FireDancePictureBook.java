package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cardmodifier.TempRetainModifier;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FireDancePictureBook extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(FireDancePictureBook.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/FireDancePictureBook.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/FireDancePictureBook.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/FireDancePictureBook.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.RARE;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

	public static final int STRENGTH = 3;

	private boolean activated = false;

	public FireDancePictureBook() {
		super(
				ID,
				ImageMaster.loadImage(IMG_PATH),
				ImageMaster.loadImage(IMG_OTL),
				RELIC_TIER,
				LANDING_SOUND
		);

		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}

	@Override
	public void justEnteredRoom(AbstractRoom room) {
		this.counter = -1;
	}

	@Override
	public void onEquip() {
		this.counter = 0;
	}

	@Override
	public void onCardDraw(AbstractCard card) {
		if (this.counter > 0)
			this.flash();
		this.counter = 0;
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		this.counter++;
		if (this.counter == 3) {
			this.flash();

			this.addToBot(new DrawCardAction(1, new AnonymousAction(() -> {
				if (DrawCardAction.drawnCards.isEmpty())
					return;

				AbstractCard card = DrawCardAction.drawnCards.get(0);
				if (AbstractDungeon.player.hand.contains(card))
					CardModifierManager.addModifier(card, new TempRetainModifier());
			})));
		}
	}

	@Override
	public void onVictory() {
		this.counter = -1;
	}

	@Override
	public String getUpdatedDescription() {
		return String.format(this.DESCRIPTIONS[0], STRENGTH);
	}
}
