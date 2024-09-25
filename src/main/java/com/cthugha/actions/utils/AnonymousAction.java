//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.actions.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.Arrays;

public class AnonymousAction extends AbstractGameAction {
	ArrayList<Runnable> runnableList;
	
	public AnonymousAction(Runnable... runnableList) {
		this.actionType = ActionType.SPECIAL;
		
		this.runnableList = new ArrayList<>();
		this.runnableList.addAll(Arrays.asList(runnableList));
	}
	
	public void update() {
		if (!this.isDone) {
			for (Runnable o : this.runnableList)
				if (o != null)
					o.run();
			
			this.isDone = true;
		}
	}
}
