package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.actions.common.DrawSpecificCardAction;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class LettersOfNestar extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(LettersOfNestar.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/聂斯塔书简.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/聂斯塔书简.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/聂斯塔书简.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.BOSS;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	public static final int COUNT = 2;

	public LettersOfNestar() {
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
	public String getUpdatedDescription() {
		return String.format(this.DESCRIPTIONS[0], COUNT);
	}

	@Override
	public void atTurnStart() {
		if (AbstractDungeon.player.drawPile.group.stream()
				.anyMatch(CthughaHelper::isBurnCard))
			this.flash();

		this.addToBot(new DrawSpecificCardAction(CthughaHelper::isBurnCard, 2));
	}
}
