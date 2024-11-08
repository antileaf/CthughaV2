package com.cthugha.patch.debugging;

import basemod.ReflectionHacks;
import com.cthugha.Cthugha_Core;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SuppressWarnings("unused")
public class ExhaustSpecificCardDebuggingPatch {
//	@SpirePatch(clz = ExhaustSpecificCardAction.class, method = "update")
//	public static class UpdatePatch {
//		@SpirePrefixPatch
//		public static void Prefix(ExhaustSpecificCardAction _inst) {
//			AbstractCard card = ReflectionHacks.getPrivate(_inst, ExhaustSpecificCardAction.class, "targetCard");
//			CardGroup group = ReflectionHacks.getPrivate(_inst, ExhaustSpecificCardAction.class, "group");
//			Cthugha_Core.logger.info("card = {}, this.group = {}", card, group);
//		}
//	}
}
