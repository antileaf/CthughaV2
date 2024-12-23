package com.cthugha.patch;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.enums.MyPlayerClassEnum;
import com.cthugha.ui.SkinSelectScreen;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;

public class SkinSelectPatch {
  public static boolean isKaltsitSelected() {
    return (CardCrawlGame.chosenCharacter == MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS
        && (Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen,
			CharacterSelectScreen.class, "anySelected"));
  }

  @SpirePatch(clz = CharacterSelectScreen.class, method = "update")
  public static class UpdateButtonPatch {
    public static void Prefix(CharacterSelectScreen _inst) {
      if (SkinSelectPatch.isKaltsitSelected())
        SkinSelectScreen.Inst.update();
    }
  }

  @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
  public static class RenderPortraitPatch {
    @SpireInsertPatch(rloc = 62)
    public static void Insert(CharacterSelectScreen _inst, SpriteBatch sb) {
      if (SkinSelectPatch.isKaltsitSelected())
        SkinSelectScreen.Inst.renderPortrait(sb);
    }
  }

  @SpirePatch(clz = CharacterSelectScreen.class, method = "render")
  public static class RenderButtonPatch {
    public static void Postfix(CharacterSelectScreen _inst, SpriteBatch sb) {
      if (SkinSelectPatch.isKaltsitSelected())
        SkinSelectScreen.Inst.render(sb);
    }
  }

}
