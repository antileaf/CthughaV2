package com.cthugha.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.object.AbstractSpirit;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

@SpirePatch(clz = AbstractCard.class, method = "renderImage")
public class RenderSpirit {
  public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
    AbstractSpirit spirit = SpiritField.spirit.get(__instance);
    if (spirit != null && __instance != null) {
      spirit.setTexture();
      Texture img = spirit.getTexture();
      sb.setColor(Color.WHITE.cpy());
      sb.draw(img, __instance.current_x - 256.0F, __instance.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F,
          __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 512,
          512, false, false);
      // sb.draw(img, __instance.current_x, __instance.current_y, 0, 0, 500, 380);
    }
  }
}
