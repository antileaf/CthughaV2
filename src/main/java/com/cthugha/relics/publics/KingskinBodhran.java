package com.cthugha.relics.publics;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class KingskinBodhran extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(KingskinBodhran.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/王皮框鼓.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/王皮框鼓.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/王皮框鼓.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	public static final int MAX_HP = 40;

	public KingskinBodhran() {
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
		return String.format(this.DESCRIPTIONS[0], MAX_HP);
	}

	@Override
	public void onEquip() {
		AbstractDungeon.player.increaseMaxHp(MAX_HP, true);
	}
}
