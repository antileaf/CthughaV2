package com.cthugha.cards;

import com.cthugha.actions.ForEachYanZhiJingAction;
import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class XingGui extends AbstractCthughaCard {

	public static final String ID = ModHelper.makeID(XingGui.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/074.png";
	private static final int COST = 1;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public XingGui() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.damage = this.baseDamage = 10;
		this.secondaryDamage = this.baseSecondaryDamage = 40;

		this.canBaoYan = true;
//        this.tags.add(CustomTags.BaoYan);
	}

	@Override
	public int getExtraYanZhiJing() {
		return 1;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ChannelAction(new YanZhiJing()));

		this.addToBot(new BaoYanAction(this, new DamageAction(m,
				new DamageInfo(p, this.secondaryDamage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY)));

		this.addToBot(new ForEachYanZhiJingAction(count -> {
			for (int i = 0; i < count; i++)
				this.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		}, true));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(3);
			this.upgradeSecondaryDamage(12);
			this.initializeDescription();
		}
	}
}
