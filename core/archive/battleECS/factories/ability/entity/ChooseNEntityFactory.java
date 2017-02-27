package com.comp460.screens.battleECS.factories.ability.entity;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.factories.ability.AbilityEntityFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by matth on 2/15/2017.
 */
public class ChooseNEntityFactory extends AbilityEntityFactory {
    private static Random rng = new Random();

    public int n;
    public List<AbilityEntityFactory> entities = new ArrayList<>();

    @Override
    public void addToScreen(BattleScreen screen, Entity owner) {
        List<Integer> choices = new ArrayList<>(entities.size());
        for (int i = 0; i < entities.size(); i++) {
            choices.add(i);
        }
        int choicesLeft = n;
        while (choicesLeft > 0 && choices.size() > 0) {
            int choice = rng.nextInt(choices.size());
            entities.get(choices.get(choice)).addToScreen(screen, owner);
            choices.removeIf((i)->i==choice);
            choicesLeft--;
        }
    }
}
