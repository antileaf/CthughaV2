package com.cthugha.cards.colorless;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.patches.misc.HPLostThisCombatPatch;
import com.cthugha.power.CounterOfLossMaxHpPower;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BloodboonRitual extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(BloodboonRitual.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/授血仪式.png";
    private static final int COST = 10;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public BloodboonRitual() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 10;

        this.exhaust = true;

        if (CthughaHelper.isInBattle())
            this.applyPowers();
    }

    @Override
    public boolean hasEnoughEnergy() {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            this.cantUseMessage = AbstractCard.TEXT[9];
            return false;
        }

        boolean cantUse =
                AbstractDungeon.player.powers.stream()
                        .anyMatch(p -> !p.canPlayCard(this)) ||
                AbstractDungeon.player.relics.stream()
                        .anyMatch(r -> !r.canPlay(this)) ||
                AbstractDungeon.player.blights.stream()
                        .anyMatch(b -> !b.canPlay(this)) ||
                AbstractDungeon.player.hand.group.stream()
                        .anyMatch(c -> !c.canPlay(this));

        if (cantUse) {
            this.cantUseMessage = AbstractCard.TEXT[13];
            return false;
        }

        if (HPLostThisCombatPatch.HPLostThisCombat < this.costForTurn && !this.freeToPlay() && !this.isInAutoplay) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
            return false;
        }

        return true;
    }

//    @SpireOverride
//    protected void renderDescription(SpriteBatch sb) {
//        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = HPLostThisCombatPatch.HPLostThisCombat;
//
//        SpireSuper.call(sb);
//    }
//
//    @SpireOverride
//    protected void renderDescriptionCN(SpriteBatch sb) {
//        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = HPLostThisCombatPatch.HPLostThisCombat;
//
//        SpireSuper.call(sb);
//    }

    @Override
    public void applyPowers() {
        this.secondaryMagicNumber = this.baseSecondaryMagicNumber = HPLostThisCombatPatch.HPLostThisCombat;
        super.applyPowers();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = cardStrings.DESCRIPTION;

        if (CthughaHelper.isInBattle() && this.secondaryMagicNumber != -1)
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];

        super.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!this.freeToPlay() && !this.isInAutoplay)
            HPLostThisCombatPatch.HPLostThisCombat -= this.costForTurn;

        this.addToBot(new HealAction(p, p, this.costForTurn));

        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters)
            if (!mo.isDeadOrEscaped()) {
                int hp = mo.maxHealth;
                if (mo.hasPower(CounterOfLossMaxHpPower.POWER_ID))
                    hp += mo.getPower(CounterOfLossMaxHpPower.POWER_ID).amount;

                hp = (int) ((float)hp * this.magicNumber / 100.0F);
                this.addToBot(new DamageAction(mo, new DamageInfo(p, hp, DamageInfo.DamageType.HP_LOSS),
                        AbstractGameAction.AttackEffect.FIRE));
            }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();

            this.upgradeMagicNumber(3);

            this.initializeDescription();
        }
    }
}
