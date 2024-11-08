package com.cthugha.cards;

import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.BaoYanHelper;
import com.cthugha.helpers.EnergyHelper;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class LiuXingBao extends AbstractCthughaCard {

    public static final String ID = ModHelper.makeID(LiuXingBao.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/123.png";

    private static final int COST = -1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public LiuXingBao() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 8;
        this.isMultiDamage = true;
//        this.magicNumber = this.baseMagicNumber = 1;

        this.canBaoYan = true;
//        this.tags.add(CustomTags.BaoYan);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m))
            return false;

        if (EnergyHelper.energyUsedThisTurn == 0)
            return true;
        else {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (BaoYanHelper.canTriggerBaoYanGlowCheck(this)) {
            this.damage *= 3;
            this.isDamageModified = this.damage != this.baseDamage;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);

        if (BaoYanHelper.canTriggerBaoYanGlowCheck(this)) {
            this.damage *= 3;

            for (int i = 0; i < this.multiDamage.length; i++) {
                this.multiDamage[i] *= 3;
                this.isDamageModified = this.multiDamage[i] != this.baseDamage;
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BaoYanAction(this,
                () -> this.calculateCardDamage(null),
                () -> this.calculateCardDamage(null)
        ));

        this.addToBot(new AnonymousAction(() -> {
            int amount = this.energyOnUse + (this.upgraded ? 1 : 0);

            if (p.hasRelic(ChemicalX.ID))
                amount += ChemicalX.BOOST;

            if (amount > 0) {
                if (!this.freeToPlayOnce)
                    p.energy.use(this.energyOnUse);

                for (int i = 0; i < amount; i++)
                    this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));

                for (int i = 0; i < amount; i++)
                    this.addToBot(new ChannelAction(new YanZhiJing()));
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
