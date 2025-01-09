package com.cthugha.cards.cthugha;

import com.cthugha.actions.ZhiLiaoAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class HongLianYeHuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(HongLianYeHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/117.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HongLianYeHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 2;
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 1;

        this.canZhiLiao = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

        this.addToBot(new ZhiLiaoAction(this, () -> {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

            if (m.hasPower(StrengthPower.POWER_ID)) {
                AbstractPower strength = m.getPower(StrengthPower.POWER_ID);
                if (strength.amount >= this.secondaryMagicNumber) {
                    this.addToBot(new AnonymousAction(() -> {
                        if (!m.hasPower(ArtifactPower.POWER_ID))
                            this.addToTop(new ApplyPowerAction(p, p,
                                    new StrengthPower(p, this.magicNumber), this.magicNumber));

                        this.addToTop(new ApplyPowerAction(m, p,
                                new StrengthPower(m, -this.magicNumber), -this.magicNumber));
                    }));
                }
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.initializeDescription();
        }
    }
}
