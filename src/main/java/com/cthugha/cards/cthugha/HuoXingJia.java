package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class HuoXingJia extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(HuoXingJia.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/094.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HuoXingJia() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = -999;
    }

    @Override
    public void applyPowers() {
        boolean hasNotUpdatedDesc = this.magicNumber == -999;
        this.magicNumber = this.baseMagicNumber = (int) AbstractDungeon.player.hand.group.stream()
                .filter(CthughaHelper::isBurnCard)
                .count() + (this.upgraded ? 1 : 0);

        super.applyPowers();
        if (hasNotUpdatedDesc && this.magicNumber != -999)
            this.initializeDescription();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = String.format(this.upgraded ?
                        cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION,
                CthughaHelper.isInBattle() && this.magicNumber != -999 ?
                        cardStrings.EXTENDED_DESCRIPTION[0] : "");

        super.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // boolean isLowestHealth = false;
        // for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
        //     if (monster != m && monster.isDead == false)  {
        //         if (m.currentHealth <= monster.currentHealth) {
        //             isLowestHealth = true;
        //         } else {
        //             isLowestHealth = false;
        //             break;
        //         }
        //     }
        // }

        int count = (int) AbstractDungeon.player.hand.group.stream()
                .filter(CthughaHelper::isBurnCard)
                .count() + (this.upgraded ? 1 : 0);

        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, count, false)));

        // if (isLowestHealth) {
            AbstractMonster otherM = AbstractDungeon.getMonsters().getRandomMonster(m, true);
            if (otherM != null && otherM != m) {
                this.addToBot(new DamageAction(otherM, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new ApplyPowerAction(otherM, p, new VulnerablePower(otherM, count, false)));
            }
        // }
    }

}
