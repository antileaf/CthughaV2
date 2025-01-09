package com.cthugha.relics.cthugha;

import com.cthugha.Cthugha_Core;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.relics.OnRemoveCardFromMasterDeckRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;

public class LongXin extends CustomRelic implements OnRemoveCardFromMasterDeckRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(LongXin.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/龙心.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/龙心.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/龙心.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.BOSS;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	public LongXin() {
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
	public void onEquip() {
		AbstractDungeon.player.energy.energyMaster += 1;
	}

	@Override
	public void onUnequip() {
		AbstractDungeon.player.energy.energyMaster -= 1;
	}

	public String getUpdatedDescription() {
		return this.DESCRIPTIONS[0];
	}

	@Override
	public void onRemoveCardFromMasterDeck(AbstractCard card) {
		Cthugha_Core.logger.info("Removing {}", card.name);

		if (!CthughaHelper.isBurnCard(card)) {
			this.flash();

			AbstractDungeon.topLevelEffectsQueue.add(new FastCardObtainEffect(new Burn(),
					Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
		}
	}
}
