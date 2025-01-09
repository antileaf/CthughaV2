package com.cthugha.patch.shop;

import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.shop.BloodShopScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;

@SuppressWarnings("unused")
public class ShopScreenPatch {
	@SpirePatch(clz = ShopScreen.class, method = "purgeCard", paramtypez = {})
	public static class PurgeCardPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix() {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				BloodShopScreen.purgeCard();
				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}
}
