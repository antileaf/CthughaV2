package com.cthugha.relics.shared;

import basemod.abstracts.CustomRelic;
import com.cthugha.Cthugha_Core;
import com.cthugha.characters.Cthugha;
import com.cthugha.potions.BottledHell;
import com.cthugha.potions.EvilSunrise;
import com.cthugha.potions.RedLotusWater;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.*;

import java.util.ArrayList;

public class ChaliceMurmurous extends CustomRelic {
	// 遗物ID
	public static final String ID = CthughaHelper.makeID(ChaliceMurmurous.class.getSimpleName());
	// 图片路径
	private static final String IMG_PATH = "cthughaResources/img/relics/低语之觚.png";
	private static final String IMG_OTL = "cthughaResources/img/relics/outline/低语之觚.png";
	private static final String IMG_LARGE = "cthughaResources/img/relics/large/低语之觚.png";
	// 遗物类型
	private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
	// 点击音效
	private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

	private static final int SLOT = 3;

	public ChaliceMurmurous() {
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
		return String.format(this.DESCRIPTIONS[0], SLOT);
	}

	private AbstractPotion getPotion(AbstractPotion.PotionRarity rarity) {
		ArrayList<AbstractPotion> list = new ArrayList<>();

		if (AbstractDungeon.player instanceof Ironclad) {
			list.add(new BloodPotion());
			list.add(new Elixir());
			list.add(new HeartOfIron());
		}
		else if (AbstractDungeon.player instanceof TheSilent) {
			list.add(new PoisonPotion());
			list.add(new CunningPotion());
			list.add(new GhostInAJar());
		}
		else if (AbstractDungeon.player instanceof Defect) {
			list.add(new FocusPotion());
			list.add(new PotionOfCapacity());
			list.add(new EssenceOfDarkness());
		}
		else if (AbstractDungeon.player instanceof Watcher) {
			list.add(new BottledMiracle());
			list.add(new StancePotion());
			list.add(new Ambrosia());
		}
		else if (AbstractDungeon.player instanceof Cthugha) {
			list.add(new BottledHell());
			list.add(new EvilSunrise());
			list.add(new RedLotusWater());
		}

		AbstractPotion[] choices = list.stream()
				.filter(pot -> pot.rarity == rarity)
				.toArray(AbstractPotion[]::new);

		if (choices.length == 0)
			return null;

		return choices[AbstractDungeon.potionRng.random(choices.length - 1)];
	}

	@Override
	public void onEquip() {
		AbstractDungeon.player.potionSlots += SLOT;
		for (int i = 0; i < SLOT; i++)
			AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - SLOT + i));

		int qwq = AbstractDungeon.player.potionSlots - SLOT;

		for (AbstractPotion.PotionRarity rarity : new AbstractPotion.PotionRarity[]{
				AbstractPotion.PotionRarity.COMMON,
				AbstractPotion.PotionRarity.UNCOMMON,
				AbstractPotion.PotionRarity.RARE
		}) {
			AbstractPotion potion = this.getPotion(rarity);
			if (potion == null)
				potion = PotionHelper.getRandomPotion();

			Cthugha_Core.logger.info("ChaliceMurmurous: onEquip: {}", potion.ID);
			AbstractDungeon.player.obtainPotion(qwq++, potion);
		}
	}
}
