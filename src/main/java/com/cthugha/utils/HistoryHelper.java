package com.cthugha.utils;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.enums.MyPlayerClassEnum;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.BattleStats;
import com.megacrit.cardcrawl.screens.stats.RunData;

import java.io.File;
import java.util.Objects;

public class HistoryHelper {
	public static boolean checkIsDeified(RunHistoryPath path) {
		if (path != null && !path.pathElements.isEmpty()) {
			boolean hasDefeatedAreshkagal = path.pathElements.stream()
					.filter(e -> e.nodeType == RunPathElement.PathNodeType.BOSS)
					.map(e -> (BattleStats) ReflectionHacks
							.getPrivate(e, RunPathElement.class, "battleStats"))
					.filter(Objects::nonNull)
					.anyMatch(s -> s.enemies.equals(Areshkagal.ID));

			RunPathElement last = path.pathElements.get(path.pathElements.size() - 1);

			return hasDefeatedAreshkagal && last.nodeType != RunPathElement.PathNodeType.BOSS;
		}

		return false;
	}

	public static RunHistoryPath getPath(RunData data) {
		RunHistoryPath path = new RunHistoryPath();
		path.setRunData(data);

		return path;
	}

	public static boolean checkHistories() {
		FileHandle[] subfolders = Gdx.files.local("runs" + File.separator).list();

		for (FileHandle subfolder : subfolders) {
			if (CardCrawlGame.saveSlot == 0) {
				if (subfolder.name().contains("0_") || subfolder.name().contains("1_") ||
						subfolder.name().contains("2_"))
					continue;
			}
			else {
				if (!subfolder.name().contains(CardCrawlGame.saveSlot + "_"))
					continue;
			}

			if (!subfolder.name().contains(MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS.name()))
				continue;

			Gson gson = new Gson();

			for (FileHandle file : subfolder.list()) {
				RunData data;

				try {
					data = gson.fromJson(file.readString(), RunData.class);
					if (data != null) {
						try {
							AbstractPlayer.PlayerClass.valueOf(data.character_chosen);
						}
						catch (NullPointerException | IllegalArgumentException e) {
							continue;
						}
					}
				}
				catch (JsonSyntaxException e) {
					continue;
				}

				if (checkIsDeified(getPath(data)))
					return true;
			}
		}

		return false;
	}
}
