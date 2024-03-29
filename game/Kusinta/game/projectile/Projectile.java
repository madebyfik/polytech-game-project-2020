package projectile;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;

import automaton.Automaton;
import automaton.Direction;
import automaton.Entity;
import game.Coord;
import player.Character;

public abstract class Projectile extends Entity {

	public enum proj {
		ARROW, MAGIC_PROJECTILE, METEOR, LURE
	};

	protected enum State {
		OK_STATE, HIT_STATE
	};

	public static final int SIZE = 86;

	protected double m_angle;
	protected Direction m_direction;

	protected State m_State;

	protected Character m_shooter;

	protected long m_dead_time;

	protected float m_alpha;

	public Projectile(Automaton projectileAutomaton, Coord c, double angle, Character shooter, Direction direction,
			Image[] bImages, HashMap<Action, int[]> indiceAction) {
		super(projectileAutomaton, bImages, indiceAction);

		m_coord = new Coord(c);
		m_angle = angle;
		m_direction = direction;
		m_shooter = shooter;
		setM_model(shooter.getModel());
		m_State = State.OK_STATE;
		m_alpha = 1f;
		m_dead_time = 0;

	}

	@Override
	public boolean move(Direction dir) {
		int tmpX = m_coord.X();
		int tmpY = m_coord.Y();
		if (m_direction == Direction.E) {
			m_coord.translate((int) (X_MOVE * Math.cos(m_angle)), (int) (-X_MOVE * Math.sin(m_angle)));
		} else {
			m_coord.translate((int) (-X_MOVE * Math.cos(m_angle)), (int) (-X_MOVE * Math.sin(m_angle)));
		}
		hitBox.translate(m_coord.X() - tmpX, m_coord.Y() - tmpY);
		return true;

	}

	public void tick(long elapsed) {
		m_stepElapsed += elapsed;
		if (m_stepElapsed > m_stepTick) {
			m_stepElapsed = 0;
			m_automaton.step(this);
		}
	}

	public long getDeadTime() {
		return m_dead_time;
	}

	public State getState() {
		return m_State;
	}

	@Override
	public boolean explode() {
		if (m_dead_time == 0) {
			m_dead_time = System.currentTimeMillis();
		}
		currentAction = Action.DEATH;
		m_State = State.HIT_STATE;
		resetAnim();
		return true;
	}

	public void setSpeed(int speed) {
		X_MOVE = speed;
	}

	public Coord getCoord() {
		return m_coord;
	}

	public abstract void paint(Graphics g);
}
