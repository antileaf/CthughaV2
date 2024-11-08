package com.cthugha.cards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.interfaces.OnObtainKeyCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.cthugha.orbs.YanZhiJing;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class StarSpear extends AbstractCthughaCard implements OnObtainKeyCard {

	public static final String ID = ModHelper.makeID(StarSpear.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/153.png";
	private static final int COST = 1;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public StarSpear(boolean previewCard) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.damage = this.baseDamage = 4;
		this.magicNumber = this.baseMagicNumber = 1;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = 1;

		this.canBaoYan = true;

		if (!previewCard) {
			ArrayList<AbstractCard> preview = MultiCardPreview.multiCardPreview.get(this);

			for (int i = 1; i <= 3; i++) {
				StarSpear card = new StarSpear(true);
				for (int j = 0; j < i; j++)
					card.upgrade();

				preview.add(card);
			}
		}
	}

	public StarSpear() {
		this(false);
	}

	@Override
	public int getExtraYanZhiJing() {
		return this.magicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));


		for (int i = 0; i < this.magicNumber; i++)
			this.addToBot(new ChannelAction(new YanZhiJing()));

		this.addToBot(new BaoYanAction(this, () -> {
			for (int i = 0; i < this.secondaryMagicNumber; i++)
				this.addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}));
	}

	@Override
	public void onObtainKey(ObtainKeyEffect.KeyColor keyColor) {
		if (this.canUpgrade()) {
			this.upgrade();
			AbstractDungeon.player.bottledCardUpgradeCheck(this);
			AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));
			AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(
					Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
		}
	}

	@Override
	public boolean canUpgrade() {
		return this.timesUpgraded < 3;
	}

	@Override
	public void upgrade() {
		this.upgradeName();

		if (this.timesUpgraded == 1)
			this.upgradeDamage(4);
		else if (this.timesUpgraded == 2)
			this.upgradeMagicNumber(1);
		else if (this.timesUpgraded == 3) {
			this.upgradeSecondaryMagicNumber(1);
			this.rawDescription = UPGRADE_DESCRIPTION;
		}

		this.initializeDescription();

		ArrayList<AbstractCard> preview = MultiCardPreview.multiCardPreview.get(this);
		while (!preview.isEmpty() && preview.get(0).timesUpgraded <= this.timesUpgraded)
			preview.remove(0);
	}
}
