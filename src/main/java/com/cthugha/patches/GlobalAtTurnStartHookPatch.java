package com.cthugha.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;

@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
public class GlobalAtTurnStartHookPatch {
//    @SpireInsertPatch(locator = Locator.class)
//    public static void Insert(GameActionManager __instance) {
//        StaticHelper.resetvaluesAtTurnStart();
//    }
//
//    private static class Locator extends SpireInsertLocator {
//        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
//            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class,
//                    "applyStartOfTurnRelics");
//            return LineFinder.findInOrder(ctMethodToPatch, (Matcher) methodCallMatcher);
//        }
//    }
}