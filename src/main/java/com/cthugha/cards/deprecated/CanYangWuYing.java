package com.cthugha.cards.deprecated;

import com.cthugha.actions.BreakBlockDamageAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.powers.BufferPower;

import basemod.AutoAdd;

@Deprecated
@AutoAdd.Ignore
public class CanYangWuYing extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(CanYangWuYing.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/083.png";
    private static final int COST = 3;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public CanYangWuYing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 24;
        this.magicNumber = this.baseMagicNumber = 2;
        this.exhaust = true;

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(12);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BreakBlockDamageAction(m, new DamageInfo(p, this.damage), lastDamageTaken -> {
            int hpPercent = 60;
            if (m.type == EnemyType.BOSS) {
                hpPercent = 30;
            }
            float percentConversion = hpPercent / 100.0F;

            int amount = (int) (m.maxHealth * percentConversion);
            if (lastDamageTaken >= amount) {
                this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, this.magicNumber)));
            } else if (m.isDying || m.currentHealth <= 0) {
                this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, this.magicNumber)));
            }
        }));
    }

}
