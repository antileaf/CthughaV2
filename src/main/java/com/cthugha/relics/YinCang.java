// package com.example.relics;

// import com.example.helpers.ModHelper;
// import com.megacrit.cardcrawl.cards.AbstractCard;
// import com.megacrit.cardcrawl.helpers.ImageMaster;
// import com.megacrit.cardcrawl.relics.AbstractRelic;
// import com.megacrit.cardcrawl.relics.AbstractRelic.LandingSound;
// import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;

// import basemod.abstracts.CustomRelic;

// public class YinCang extends CustomRelic {
//     // 遗物ID
//     public static final String ID = ModHelper.MakePath(YinCang.class.getSimpleName());
//     // 图片路径
//     private static final String IMG_PATH = null;
//     // 遗物类型
//     private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
//     // 点击音效
//     private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

//     public YinCang() {
//         super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
//     }

//     // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
//     public String getUpdatedDescription() {
//         return this.DESCRIPTIONS[0];
//     }

// }