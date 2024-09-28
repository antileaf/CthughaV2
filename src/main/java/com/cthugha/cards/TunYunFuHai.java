package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class TunYunFuHai extends CustomCard {

    public static final String ID = ModHelper.MakePath(TunYunFuHai.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/152.png";
    private static final int COST = 5;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private int rawCost;

    public TunYunFuHai() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.rawCost = this.cost;

        this.damage = this.baseDamage = 0;
        this.magicNumber = this.baseMagicNumber = 5;
        this.block = this.baseBlock = 15;
    }

    private int getBurnCount() {
        return (int) AbstractDungeon.player.hand.group.stream()
                .filter(ModHelper::IsBurn)
                .count();
    }

    @Override
    public void applyPowers() {
        int originalBaseDamage = this.baseDamage;

        int count = this.getBurnCount();
        this.baseDamage += this.magicNumber * count;
        super.applyPowers();

        this.baseDamage = originalBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;

        int curCost = this.rawCost - count;
        if (curCost < 0)
            curCost = 0;

        this.upgradeBaseCost(curCost);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int originalBaseDamage = this.baseDamage;

        this.baseDamage += this.magicNumber * this.getBurnCount();
        super.calculateCardDamage(mo);

        this.baseDamage = originalBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage)));
        this.addToBot(new GainBlockAction(p, this.block));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.upgradeBlock(7);
//            this.rawCost = this.cost;
            this.initializeDescription();
        }
    }
}
