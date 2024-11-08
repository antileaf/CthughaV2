package com.cthugha.relics;

import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;

public class YongRanShi extends CustomRelic {
  // 遗物ID
  public static final String ID = ModHelper.makeID(ShengLingLieYan.class.getSimpleName());
  // 图片路径
  private static final String IMG_PATH = "cthughaResources/img/relics/生灵烈焰.png";
  // 遗物类型
  private static final RelicTier RELIC_TIER = RelicTier.BOSS;
  // 点击音效
  private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

  public YongRanShi() {
    super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
  }

  // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void obtain() {
    BaseMod.addCard(new Burn());
  }

}
