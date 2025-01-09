package com.cthugha.cards.cthugha;

import com.cthugha.Cthugha_Core;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.cthugha.patch.fire_vampire.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.stream.Stream;

public class Chixiao extends AbstractCthughaCard {
    public static final String ID = CthughaHelper.makeID(Chixiao.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/赤霄.png";
    private static final int COST = 0;
    private static final int UPGRADED_COST = 4;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Chixiao() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.exhaust = true;

        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null) {
            this.updateCost(0); // ?

            AbstractDungeon.actionManager.orbsChanneledThisCombat
                    .stream()
                    .filter(orb -> orb instanceof FireVampire)
                    .forEach(orb -> this.updateCost(2));

            GameActionManagerPatch.Fields.orbsRemovedThisCombat.get(AbstractDungeon.actionManager)
                    .stream()
                    .filter(orb -> orb instanceof FireVampire)
                    .forEach(orb -> this.updateCost(2));
        }
    }

    @Override
    public boolean hasEnoughEnergy() { // A hack to make sure there is always enough energy
        int backup = this.costForTurn;
        this.costForTurn = 0;
        boolean result = super.hasEnoughEnergy();
        this.costForTurn = backup;
        return result;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.costForTurn, DamageInfo.DamageType.HP_LOSS),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true));
        this.addToBot(new DecreaseMonsterMaxHealthAction(m, this.costForTurn));

        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.costForTurn)));
        if (!m.hasPower(ArtifactPower.POWER_ID))
            this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, this.costForTurn)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.cost += UPGRADED_COST;
            this.costForTurn += UPGRADED_COST;
            this.upgradedCost = true;
            this.initializeDescription();
        }
    }

    public static void updateAll() {
        Cthugha_Core.logger.info("Updating all Chixiao");

        Stream.of(AbstractDungeon.player.hand, AbstractDungeon.player.drawPile,
                        AbstractDungeon.player.discardPile, AbstractDungeon.player.exhaustPile,
                        AbstractDungeon.player.limbo)
                .flatMap(g -> g.group.stream())
                .filter(c -> c instanceof Chixiao)
                .forEach(c -> c.updateCost(2));
    }
}
