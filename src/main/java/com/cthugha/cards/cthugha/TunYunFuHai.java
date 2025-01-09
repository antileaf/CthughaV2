package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;

public class TunYunFuHai extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(TunYunFuHai.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/152.png";
    private static final int COST = 5;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
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
                .filter(CthughaHelper::isBurn)
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
        if (m != null)
            this.addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY,
                    this.getBurnCount() + 1)));

        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
