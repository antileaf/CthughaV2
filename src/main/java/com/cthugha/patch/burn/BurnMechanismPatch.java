package com.cthugha.patch.burn;

import basemod.ReflectionHacks;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.blight.TheBurningOne;
import com.cthugha.orbs.FireVampire;
import com.cthugha.relics.cthugha.EmeraldTabletVolumeVII;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.FenJiPower;
import com.cthugha.power.RiShiPower;
import com.cthugha.relics.cthugha.HuoTiHuoYan;
import com.cthugha.relics.cthugha.ShengLingLieYan;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class BurnMechanismPatch {
	private static boolean hasCthughaBlight() {
		return AbstractDungeon.player.hasBlight(TheBurningOne.ID);
	}

	private static String getCardStringForBurn(AbstractCard c) {
		CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CthughaHelper.makeID("Burn"));

		if (c.type == AbstractCard.CardType.ATTACK)
			return cardStrings.DESCRIPTION;

		if (CthughaHelper.isInBattle() && hasCthughaBlight())
			return !AbstractDungeon.player.hasRelic(ShengLingLieYan.ID) ?
					cardStrings.EXTENDED_DESCRIPTION[0] :
					cardStrings.EXTENDED_DESCRIPTION[1];

		return CardCrawlGame.languagePack.getCardStrings("Burn").DESCRIPTION;
	}

	@SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy", paramtypez = {})
	public static class MakeStatEquivalentCopyPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractCard.class, "makeCopy"));
				return new int[] { tmp[0] + 1 };
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"card"})
		public static void Insert(AbstractCard _inst, AbstractCard card) {
			if (_inst instanceof Burn && _inst.type == AbstractCard.CardType.ATTACK) {
				CthughaHelper.changeBurn(card);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription", paramtypez = {})
	public static class InitializeDescriptionPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard _inst) {
			if (_inst instanceof Burn)
				_inst.rawDescription = getCardStringForBurn(_inst);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "applyPowers", paramtypez = {})
	public static class ApplyPowersPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst) {
			if (_inst instanceof Burn && _inst.type == AbstractCard.CardType.STATUS && hasCthughaBlight()) {
				Burn burn = (Burn) _inst;

				int bonus = 0;
				if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID))
					bonus += HuoTiHuoYan.BONUS;
				if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID))
					bonus += ShengLingLieYan.BONUS;

				if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID))
					bonus += AbstractDungeon.player.getPower(FenJiPower.POWER_ID).amount;

				if (AbstractDungeon.player.hasPower(VigorPower.POWER_ID) &&
						AbstractDungeon.player.hasRelic(EmeraldTabletVolumeVII.ID)) {
					int vigor = Math.min(AbstractDungeon.player.getPower(VigorPower.POWER_ID).amount,
							burn.baseMagicNumber + bonus);

					bonus += vigor;
				}

				burn.magicNumber = burn.baseMagicNumber + bonus;
				burn.isMagicNumberModified = burn.magicNumber != burn.baseMagicNumber;
			}
		}
	}

	@SpirePatch(clz = Burn.class, method = "upgrade", paramtypez = {})
	public static class UpgradePatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(AbstractCard.class, "rawDescription"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(Burn _inst) {
//			if (_inst.type == AbstractCard.CardType.ATTACK)
//				ReflectionHacks.privateMethod(AbstractCard.class, "upgradeDamage", int.class)
//						.invoke(_inst, 2);
//		}

		@SpirePrefixPatch
		public static void Prefix(Burn _inst) {
			if (!_inst.upgraded && _inst.type == AbstractCard.CardType.ATTACK)
				ReflectionHacks.privateMethod(AbstractCard.class, "upgradeDamage", int.class)
						.invoke(_inst, 2);
		}
	}

	@SpirePatch(clz = Burn.class, method = "triggerOnEndOfTurnForPlayingCard", paramtypez = {})
	public static class TriggerOnEndOfTurnForPlayingCardPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Burn _inst) {
			if (_inst.type == AbstractCard.CardType.ATTACK)
				return SpireReturn.Return();
			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = Burn.class, method = "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
	public static class UsePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Burn _inst, AbstractPlayer p, AbstractMonster m) {
			if (_inst.type == AbstractCard.CardType.ATTACK) {
				if (p.hasPower(RiShiPower.POWER_ID))
					AbstractDungeon.actionManager.addToBottom(
							((RiShiPower) p.getPower(RiShiPower.POWER_ID)).getAction(m));

				AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
						new DamageInfo(p, _inst.damage, DamageInfo.DamageType.NORMAL),
						AbstractGameAction.AttackEffect.FIRE));
				return SpireReturn.Return();
			}
			else if (_inst.dontTriggerOnUseCard && hasCthughaBlight()) {
				AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(
						null, true, AbstractDungeon.cardRandomRng);
				if (monster != null) {
					if (p.hasPower(RiShiPower.POWER_ID))
						AbstractDungeon.actionManager.addToBottom(
								((RiShiPower) p.getPower(RiShiPower.POWER_ID)).getAction(monster));

					int damage = _inst.magicNumber;
					FireVampire vampire = (FireVampire) AbstractDungeon.player.orbs.stream()
							.filter(orb -> orb instanceof FireVampire && ((FireVampire) orb).charge > 0)
							.findFirst()
							.orElse(null);
					if (vampire != null) {
						damage += FireVampire.BONUS;
						vampire.charge--;
						vampire.passiveVFX();
					}

					AbstractDungeon.actionManager.addToBottom(new DamageAction(monster,
							new DamageInfo(p, damage, DamageInfo.DamageType.THORNS),
							AbstractGameAction.AttackEffect.FIRE));

					if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID))
						AbstractDungeon.actionManager.addToBottom(
								new DecreaseMonsterMaxHealthAction(monster, damage));
				}

				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}
