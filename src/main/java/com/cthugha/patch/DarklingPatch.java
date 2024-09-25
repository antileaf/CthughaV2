package com.cthugha.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;

@SpirePatch(clz = Darkling.class, method = "takeTurn")
public class DarklingPatch {
  public static SpireReturn Prefix(Darkling __instance) {
    if (__instance.maxHealth <= 0) {
      return SpireReturn.Return();
    }
    return SpireReturn.Continue();
  }
}
