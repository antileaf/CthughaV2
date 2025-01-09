package com.cthugha.relics.cthugha;

import basemod.abstracts.CustomRelic;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.HashMap;
import java.util.Map;

public class WilderingMirror extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(WilderingMirror.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/WilderingMirror.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/WilderingMirror.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/WilderingMirror.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

	public WilderingMirror() {
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
	public void onEnterRoom(AbstractRoom room) {
		this.counter = -1;
	}

	@Override
	public void onRefreshHand() {
		Map<String, Integer> map = new HashMap<>();

		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (map.containsKey(c.cardID)) {
				map.put(c.cardID, map.get(c.cardID) + 1);
			} else {
				map.put(c.cardID, 1);
			}
		}

		this.counter = map.values().stream()
				.mapToInt(Integer::intValue)
				.max()
				.orElse(0);
	}

	@Override
	public void onVictory() {
		this.counter = -1;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
}
