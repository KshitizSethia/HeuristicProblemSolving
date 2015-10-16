package cocaine;

public class CocaineWeight {
	public final int weight;
	public final int position;
	public final PlayerName player;

	public CocaineWeight(int weight, int position, PlayerName player) {
		this.weight = weight;
		this.position = position;
		this.player = player;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + position;
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CocaineWeight other = (CocaineWeight) obj;
		if (player != other.player)
			return false;
		if (position != other.position)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return position + ": " + weight + " by " + player;
	}
}