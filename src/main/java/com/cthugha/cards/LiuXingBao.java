package com.cthugha.cards;

import com.cthugha.actions.YanBaoAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.enums.CustomTags;
import com.cthugha.helpers.ModHelper;
import com.cthugha.helpers.StaticHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;

public class LiuXingBao extends CustomCard {

    public static final String ID = ModHelper.MakePath(LiuXingBao.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/123.png";
    private static final int COST = -1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public LiuXingBao() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 1;

        this.tags.add(CustomTags.Yan_Bao);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.multiDamage == null) {
            this.calculateCardDamage(null);
        }

        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1)
            effect = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }
        if (this.upgraded) {
            effect++;
        }
        int count = effect;

        LiuXingBao self = this;
        this.addToBot(new YanBaoAction(this, new AbstractGameAction() {
            public void update() {
                // for (int i = 0; i < count; i++) {
                //     this.addToTop(new DamageAllEnemiesAction(p, 20, DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                // }
                for (int i = 0; i < self.multiDamage.length; i++) {
                    self.multiDamage[i] *= 3;
                }
                // self.damage *= 3;
                this.isDone = true;
            }
        }));

        this.addToBot( new AbstractGameAction() {
            public void update() {
                for (int i = 0; i < count; i++) {
                    this.addToBot(new DamageAllEnemiesAction(p, self.multiDamage, DamageType.NORMAL, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                for (int i = 0; i < count; i++) {
                    this.addToBot(new ChannelAction(new YanZhiJing()));
                }
        
                this.isDone = true;
            }
        });
        
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        super.canUse(p, m);

        if (StaticHelper.usedEnergy == 0) {
            return true;
        }
        return false;

        // // EnergyPanel.totalCount: 实际的当前能量
        // // p.energy.energy: 面板的最大能量
        // if (EnergyPanel.totalCount == p.energy.energy) {
        //     return true;
        // }
        // return false;
    }

}
