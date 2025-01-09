package com.cthugha.relics.cthugha;

import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.abstracts.CustomRelic;

public class HuoTiHuoYan extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(HuoTiHuoYan.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/活体火焰.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/活体火焰.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/活体火焰.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.STARTER;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	public static final int BONUS = 3;

	public HuoTiHuoYan() {
		super(
				ID,
				ImageMaster.loadImage(IMG_PATH),
				ImageMaster.loadImage(IMG_OTL),
				RELIC_TIER,
				LANDING_SOUND
		);

		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}

	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	public float atDamageModify(float damage, AbstractCard c) {
		return CthughaHelper.isBurn(c) ? damage + BONUS : damage;
	}
}