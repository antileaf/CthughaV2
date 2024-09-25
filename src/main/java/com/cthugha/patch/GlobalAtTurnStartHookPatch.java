package com.cthugha.patch;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.helpers.StaticHelper;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import javassist.CtBehavior;

@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
public class GlobalAtTurnStartHookPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(GameActionManager __instance) {
        StaticHelper.resetvaluesAtTurnStart();
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class,
                    "applyStartOfTurnRelics");
            return LineFinder.findInOrder(ctMethodToPatch, (Matcher) methodCallMatcher);
        }
    }
}