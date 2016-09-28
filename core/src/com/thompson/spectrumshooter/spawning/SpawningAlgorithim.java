package com.thompson.spectrumshooter.spawning;

import com.badlogic.gdx.utils.Array;
import com.thompson.spectrumshooter.enemy.Enemy;

/**
 * The way in which Enemies spawn into the world
 * @author Christopher Boyer
 */
public interface SpawningAlgorithim
{
	/**
	 * Update the given array with the given implementation of the algorithm.
	 * @param enemies		the current array of enemies
	 * @param deltaTime		the amount of time since the last update
	 * @return				the new array of enemies
	 */
	public Array<Enemy> update(Array<Enemy> enemies, float deltaTime);
}