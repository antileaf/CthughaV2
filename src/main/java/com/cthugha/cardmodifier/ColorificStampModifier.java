package com.cthugha.cardmodifier;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.power.StampPower;
import com.cthugha.strings.CthughaCardModifierStrings;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.Reflex;
import com.megacrit.cardcrawl.cards.green.Tactician;
import com.megacrit.cardcrawl.cards.purple.DeusExMachina;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import com.megacrit.cardcrawl.relics.MedicalKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColorificStampModifier extends AbstractCardModifier {
	public static final String SIMPLE_NAME = ColorificStampModifier.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	private static CthughaCardModifierStrings strings;

	private static Map<ColorEnum, TextureAtlas.AtlasRegion> textures;

	public static boolean canUse(AbstractCard c) {
		if (c.type == AbstractCard.CardType.STATUS && c.costForTurn < -1 &&
				!AbstractDungeon.player.hasRelic(MedicalKit.ID))
			return false;

		if (c.type == AbstractCard.CardType.CURSE && c.costForTurn < -1 &&
				!AbstractDungeon.player.hasRelic(BlueCandle.ID))
			return false;
		
		if (c instanceof Tactician || c instanceof Reflex || c instanceof DeusExMachina)
			return false;

		return true;
	}

	public static void initialize() {
		textures = new HashMap<>();

		for (ColorEnum color : ColorEnum.values()) {
			Texture texture = ImageMaster.loadImage("cthughaResources/img/stamp/" +
					color.name().toLowerCase() + ".png");

			textures.put(color, new TextureAtlas.AtlasRegion(texture, 0, 0,
					texture.getWidth(), texture.getHeight()));
		}
	}

	public ColorEnum color = null;

	public ColorificStampModifier(ColorEnum color) {
		this.color = color;
		this.priority = 99;
	}

	@Override
	public void onRender(AbstractCard card, SpriteBatch sb) {
		if (this.color != null) {
			Color renderColor = ReflectionHacks.getPrivate(card, AbstractCard.class, "renderColor");

			TextureAtlas.AtlasRegion atlas = textures.get(this.color);

			ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper",
					SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class)
					.invoke(card, sb, renderColor, atlas, card.current_x, card.current_y);
		}
	}

	@Override
	public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
		if (this.color == ColorEnum.R)
			return Math.max(0.0F, damage - 2.0F);
		else
			return damage;
	}

	@Override
	public float modifyBaseBlock(float block, AbstractCard card) {
		if (this.color == ColorEnum.G)
			return Math.max(0.0F, block - 2.0F);
		else
			return block;
	}

	@Override
	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
		if (this.color == ColorEnum.B)
			this.addToBot(new DamageAction(AbstractDungeon.player,
					new DamageInfo(AbstractDungeon.player, 2, DamageInfo.DamageType.THORNS)));
		else if (this.color == ColorEnum.P) {
			this.addToBot(new AnonymousAction(() -> {
				for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
					if (!m.isDeadOrEscaped())
						m.heal(2);
			}));
		}

		if (this.color != null)
			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
					new StampPower(this.color, 1)));

		ArrayList<AbstractCard> cards = new ArrayList<>();
		cards.addAll(AbstractDungeon.player.hand.group);
		cards.addAll(AbstractDungeon.player.drawPile.group);
		cards.addAll(AbstractDungeon.player.discardPile.group);
		cards = cards.stream().filter(ColorificStampModifier::canUse)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		if (cards.stream()
				.filter(c -> c != card && CardModifierManager.hasModifier(c, ID))
				.noneMatch(c -> ((ColorificStampModifier) CardModifierManager.getModifiers(c, ID)
						.get(0)).color == this.color)) { // 如果本颜色只有一张可用牌
			cards = cards.stream()
					.filter(c -> !c.exhaust && c.type != AbstractCard.CardType.POWER)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll); // 则会尽量传递给不消耗的非能力牌
		}

		// 优先传递给没有颜色印记的牌
		if (cards.stream().anyMatch(c -> c != card && !CardModifierManager.hasModifier(c, ID))) {
			AbstractCard[] choices = cards.stream()
					.filter(c -> c != card && !CardModifierManager.hasModifier(c, ID))
					.toArray(AbstractCard[]::new);

			AbstractCard c = choices[AbstractDungeon.cardRandomRng.random(choices.length - 1)];
			this.addToTop(new AnonymousAction(() -> {
				CardModifierManager.addModifier(c, new ColorificStampModifier(this.color));
				if (AbstractDungeon.player.hand.contains(c))
					c.flash();
				CardModifierManager.removeSpecificModifier(card, this, true);
			}));
		}
		// 检查这张牌是否是消耗或是能力牌，如果是的话要避免和仅剩一张的颜色交换
		else {
			HashMap<ColorEnum, Integer> count = new HashMap<>();
			cards.forEach(c -> {
				if (CardModifierManager.hasModifier(c, ID)) {
					ColorEnum color = ((ColorificStampModifier) CardModifierManager.getModifiers(c, ID)
							.get(0)).color;
					count.put(color, count.getOrDefault(color, 0) + 1);
				}
			});

			cards = cards.stream()
					.filter(c -> c != card && CardModifierManager.hasModifier(c, ID) &&
							count.get(((ColorificStampModifier) CardModifierManager.getModifiers(c, ID)
									.get(0)).color) > 1)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

			if (!cards.isEmpty()) {
				AbstractCard c = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));

				if (c != card) {
					this.addToTop(new AnonymousAction(() -> {
						ColorEnum other = ((ColorificStampModifier) CardModifierManager.getModifiers(c, ID)
								.get(0)).color;

						CardModifierManager.getModifiers(c, ID)
								.forEach(mod -> ((ColorificStampModifier) mod).color = this.color);
						if (AbstractDungeon.player.hand.contains(c))
							c.flash();

						this.color = other;
					}));
				}
			}
		}
	}

	@Override
	public List<TooltipInfo> additionalTooltips(AbstractCard card) {
		if (strings == null)
			strings = CthughaCardModifierStrings.get(ID);

		String desc = strings.DESCRIPTION;
		if (this.color != null)
			desc += " NL " + strings.EXTENDED_DESCRIPTION[this.color.ordinal()];

		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(strings.NAME, desc));
		return tips;
	}

	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}

	@Override
	public boolean shouldApply(AbstractCard card) {
		return !CardModifierManager.hasModifier(card, ID);
	}

	@Override
	public AbstractCardModifier makeCopy() {
		return new ColorificStampModifier(this.color);
	}

	public enum ColorEnum {
		R, G, B, P; // Green, Purple, Blue, Red
	}

	public static ColorificStampModifier random(boolean[] stats) {
		int ordinal;

		if (stats[0] && stats[1] && stats[2] && stats[3])
			ordinal = AbstractDungeon.cardRandomRng.random(3);
		else {
			ArrayList<Integer> choices = new ArrayList<>();
			for (int i = 0; i < 4; i++)
				if (!stats[i])
					choices.add(i);

			ordinal = choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
		}

		return new ColorificStampModifier(ColorEnum.values()[ordinal]);
	}

	public static boolean[] statistics() {
		boolean[] stats = new boolean[4];

		for (CardGroup group : new CardGroup[] {
				AbstractDungeon.player.hand, AbstractDungeon.player.drawPile, AbstractDungeon.player.discardPile
		}) {
			for (AbstractCard c : group.group) {
				if (CardModifierManager.hasModifier(c, ID)) {
					ColorEnum color = ((ColorificStampModifier) CardModifierManager.getModifiers(c, ID)
							.get(0)).color;

					stats[color.ordinal()] = true;
				}
			}
		}

		return stats;
	}
}
