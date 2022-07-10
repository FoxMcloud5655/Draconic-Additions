package net.foxmcloud.draconicadditions.capabilities;

public interface IChaosInBlood {
	public float getChaos();
	public float getLastChaos();
	public boolean hasChaos();
	public boolean hadChaosLastUpdate();
	public float addChaos(float chaos);
	public float removeChaos(float chaos);
	public void setChaos(float chaos);
}
