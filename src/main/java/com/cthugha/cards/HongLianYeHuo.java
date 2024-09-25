package com.cthugha.cards;

import com.cthugha.actions.ZhiLiaoAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;

public class HongLianYeHuo extends CustomCard {

    public static final String ID = ModHelper.MakePath(HongLianYeHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/117.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HongLianYeHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 14;
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(4);
            this.upgradeMagicNumber(-2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage,
        // this.damageTypeForTurn),
        // AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        // this.addToBot(new GainEnergyAction(1));
        // this.addToBot(new DrawCardAction(p, this.magicNumber));

        this.addToBot(new ZhiLiaoAction(this, new AbstractGameAction() {
            public void update() {
                this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));

                if (m.hasPower(StrengthPower.POWER_ID)) {
                    AbstractPower strengthPower = m.getPower(StrengthPower.POWER_ID);
                    if (strengthPower.amount >= magicNumber) {
                        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -3), -3));
                    }
                }
                this.isDone = true;
            }
        }));

    }

}
