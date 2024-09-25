package com.cthugha.object;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.actions.RandomAttachSpiritAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class GiveFire extends AbstractSpirit {
  // private static final Keyword spiritString =
  // (Keyword)OnmyojiMod.MOD_DICTIONARY.get("Onmyoji:Eye");

  // private static final String texturePath =
  // "cthughaResources/image/512/spirit_eye.png";

  private static final Texture Texture_GiveFire1 = ImageMaster.loadImage("cthughaResources/img/512/give_fire_1.png");
  private static final Texture Texture_GiveFire2 = ImageMaster.loadImage("cthughaResources/img/512/give_fire_2.png");
  private static final Texture Texture_GiveFire3 = ImageMaster.loadImage("cthughaResources/img/512/give_fire_3.png");

  private Texture texture = null;

  public GiveFire(int amount) {
    super.amount = amount;
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture() {
    if (amount == 1) {
      this.texture = Texture_GiveFire1;
    } else if (amount == 2) {
      this.texture = Texture_GiveFire2;
    } else if (amount == 3) {
      this.texture = Texture_GiveFire3;
    }
  }

  public void onDraw() {
    // AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new
    // DiscardAction((AbstractCreature)AbstractDungeon.player,
    // (AbstractCreature)AbstractDungeon.player, 2, false));
  }

  public void onUse() {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, this.amount));
    AbstractDungeon.actionManager
        .addToBottom(new RandomAttachSpiritAction(new GiveFire(1), this.amount, x -> true, false, true));
  }

  public AbstractSpirit makeCopy() {
    return new GiveFire(1);
  }
}
