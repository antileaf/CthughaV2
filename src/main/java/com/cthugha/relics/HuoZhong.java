package com.cthugha.relics;

import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.abstracts.CustomRelic;

public class HuoZhong extends CustomRelic {
  // 遗物ID
  public static final String ID = ModHelper.MakePath(HuoZhong.class.getSimpleName());
  // 图片路径
  private static final String IMG_PATH = "cthughaResources/img/relics/火种.png";
  // 遗物类型
  private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
  // 点击音效
  private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

  private int drawCount = 0;

  public HuoZhong() {
    super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
  }

  // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void atTurnStart() {
    if (this.drawCount > 0) {
      this.addToBot(new DrawCardAction(this.drawCount));
      this.drawCount = 0;
    }
  }

  public void atTurnStartPostDraw() {
    this.addToBot(new AbstractGameAction() {
      @Override
      public void update() {
        effect();
        this.isDone = true;
      }
    });
  }

  public void onPlayerEndTurn() {
    this.drawCount = 0;

    int count = 0;
    for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
      AbstractCard card = AbstractDungeon.player.hand.group.get(i);
      if (card.cardID == Burn.ID) {
        count++;
      }
    }

    for (int i = 0; i < count / 3; i++) {
      this.drawCount++;
    }
  }

  public void effect() {
    int count = 0;
    for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
      AbstractCard card = AbstractDungeon.player.hand.group.get(i);
      if (card.cardID == Burn.ID) {
        count++;
      }
    }

    for (int i = 0; i < count / 3; i++) {
      this.addToBot(new DrawCardAction(1));
    }

  }
}
