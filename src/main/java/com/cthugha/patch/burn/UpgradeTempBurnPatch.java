package com.cthugha.patch.burn;

import basemod.ReflectionHacks;
import com.cthugha.power.DaoHuoShiYanPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class UpgradeTempBurnPatch {
	private static void workOnTempBurn(AbstractCard card) {
		if (card instanceof Burn && AbstractDungeon.player.hasPower(DaoHuoShiYanPower.POWER_ID))
			card.upgrade();
	}

	@SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR,
				paramtypez = {AbstractCard.class, boolean.class})
	public static class HandPatch {
		@SpirePostfixPatch
		public static void Postfix(MakeTempCardInHandAction _inst, AbstractCard card, boolean isOtherCardInCenter) {
			workOnTempBurn(ReflectionHacks.getPrivate(_inst, MakeTempCardInHandAction.class, "c"));
		}
	}

	@SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, int.class})
	public static class HandPatch2 {
		@SpirePostfixPatch
		public static void Postfix(MakeTempCardInHandAction _inst, AbstractCard card, int amount) {
			workOnTempBurn(ReflectionHacks.getPrivate(_inst, MakeTempCardInHandAction.class, "c"));
		}
	}

	@SpirePatch(clz = MakeTempCardInDrawPileAction.class, method = "update", paramtypez = {})
	public static class DrawPilePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findAllInOrder(ctBehavior,
						new Matcher.NewExprMatcher(ShowCardAndAddToDrawPileEffect.class));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"c"})
		public static void Insert(MakeTempCardInDrawPileAction _inst, AbstractCard c) {
			workOnTempBurn(c);
		}
	}

	@SpirePatch(clz = MakeTempCardInDiscardAction.class, method = "makeNewCard", paramtypez = {})
	public static class DiscardPatch {
		@SpirePostfixPatch
		public static AbstractCard Postfix(AbstractCard _ret, MakeTempCardInDiscardAction _inst) {
			workOnTempBurn(_ret);
			return _ret;
		}
	}

	@SpirePatch(clz = MakeTempCardAtBottomOfDeckAction.class, method = "update", paramtypez = {})
	public static class BottomPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "addToBottom"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"c"})
		public static void Insert(MakeTempCardAtBottomOfDeckAction _inst, AbstractCard c) {
			workOnTempBurn(c);
		}
	}

	@SpirePatch(clz = MakeTempCardInDiscardAndDeckAction.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class})
	public static class DiscardAndDrawPilePatch {
		@SpirePostfixPatch
		public static void Postfix(MakeTempCardInDiscardAndDeckAction _inst, AbstractCard card) {
			workOnTempBurn(ReflectionHacks.getPrivate(_inst, MakeTempCardInDiscardAndDeckAction.class,
					"cardToMake"));
		}
	}
}
