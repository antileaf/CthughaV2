package com.cthugha.relics;

import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.abstracts.CustomRelic;

public class HuoTiHuoYan extends CustomRelic {
    // 遗物ID
    public static final String ID = ModHelper.MakePath(HuoTiHuoYan.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "cthughaResources/img/relics/活体火焰.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public HuoTiHuoYan() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

}

// public class XinZHuoJian extends CustomRelic {
// // 遗物ID
// public static final String ID = ModHelper.MakePath("XinZhuoJian");
// // 图片路径
// private static final String IMG_PATH =
// "lihuowangResources/img/relics/xinzhuojian.png";
// private static final String OUTLINE =
// "lihuowangResources/img/relics/outline/xinzhuojian.png";

// // 遗物类型
// private static final RelicTier RELIC_TIER = RelicTier.RARE;
// // 点击音效
// private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

// public XinZHuoJian() {

// super(ID, ImageMaster.loadImage(IMG_PATH),
// ImageMaster.loadImage(OUTLINE),
// RELIC_TIER,
// LANDING_SOUND);

// // tips.clear();
// // tips.add(new PowerTip(name, description));
// }

// // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
// public String getUpdatedDescription() {
// return this.DESCRIPTIONS[0];
// }

// //效果不好就换三回合

// @Override
// public void atBattleStart() {
// super.atBattleStart();
// // this.counter=0;
// }

// @Override
// public void atTurnStart() {
// super.atTurnStart();
// }

// @Override
// public void atBattleStartPreDraw() {
// this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
// this.addToBot(new MakeTempCardInHandAction(new BreakSpace(), 1, false));
// }

// public AbstractRelic makeCopy() {
// return new XinZHuoJian();
// }

// }