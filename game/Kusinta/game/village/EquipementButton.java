package village;

import player.Player;

public class EquipementButton extends Button{
	Player m_player;
	public EquipementButton(int x, int y, int w, int h, Player p) {
		super(x, y, w, h);
		m_player = p;
	}

	@Override
	public void action() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
