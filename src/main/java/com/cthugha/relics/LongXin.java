package com.cthugha.relics;

import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomRelic;

public class LongXin extends CustomRelic {
    // 遗物ID
    public static final String ID = ModHelper.makeID(LongXin.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "cthughaResources/img/relics/龙心.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public boolean flag = false;

    public LongXin() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    // 重置
    public void atBattleStart() {
        this.flag = false;
    }

    public void onPlayerEndTurn() {
        if (EnergyPanel.totalCount > 0) {
            this.flag = true;
        } else {
            this.flag = false;
        }
    }

    public void atTurnStart() {
        if (this.flag) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new GainEnergyAction(2));
            this.flag = false;
        }
    }

}
