package com.cthugha.patch.shop;

import basemod.Pair;
import basemod.ReflectionHacks;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.shop.BloodShopScreen;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.Merchant;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@SuppressWarnings("unused")
public class MerchantPatch {
	private static Pair<ArrayList<AbstractCard>, ArrayList<AbstractCard>>
			getCards(Merchant _inst) {
		ArrayList<AbstractCard> cards1 = new ArrayList<>();
		ArrayList<AbstractCard> cards2 = new ArrayList<>();

		AbstractCard c;
		for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy()) {
		}

		cards1.add(c);

		for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy(); Objects.equals(c.cardID, (cards1.get(cards1.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.ATTACK, true).makeCopy()) {
		}

		cards1.add(c);

		for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy()) {
		}

		cards1.add(c);

		for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy(); Objects.equals(c.cardID, (cards1.get(cards1.size() - 1)).cardID) || c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.SKILL, true).makeCopy()) {
		}

		cards1.add(c);

		for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), AbstractCard.CardType.POWER, true).makeCopy()) {
		}

		cards1.add(c);
		cards2.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.UNCOMMON).makeCopy());
		cards2.add(AbstractDungeon.getColorlessCardFromPool(AbstractCard.CardRarity.RARE).makeCopy());

		return new Pair<>(cards1, cards2);
	}

	@SpirePatch(clz = Merchant.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {float.class, float.class, int.class})
	public static class ConstructorPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "shopScreen"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(Merchant _inst, float x, float y, int newShopScreen) {
			if (CardCrawlGame.dungeon instanceof TheTricuspidGate) {
				ArrayList<String> idleMessages = ReflectionHacks.getPrivate(_inst,
						Merchant.class, "idleMessages");

				idleMessages.clear();
				Collections.addAll(idleMessages, Merchant.ENDING_TEXT);

				AbstractDungeon.shopScreen = new BloodShopScreen();
				AbstractDungeon.shopScreen.init(
						ReflectionHacks.getPrivate(_inst, Merchant.class, "cards1"),
						ReflectionHacks.getPrivate(_inst, Merchant.class, "cards2")
				);

				for (int i = 1; i < 3; i++) {
					Pair<ArrayList<AbstractCard>, ArrayList<AbstractCard>> cards = getCards(_inst);
					AbstractDungeon.shopScreen.init(cards.getKey(), cards.getValue());
				}

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}
}
