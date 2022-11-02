package net.foxmcloud.draconicadditions.capabilities;

public class ChaosInBlood implements IChaosInBlood {
	
	private float chaosInBlood = 0.0F;
	private float chaosInBloodLast = 0.0F;
	private final float maxChaosInBlood = 20.0F;
	
	@Override
	public float getChaos() {
		return chaosInBlood;
	}
	
	@Override
	public float getLastChaos() {
		return chaosInBloodLast;
	}
	
	@Override
	public boolean hasChaos() {
		return chaosInBlood > 0;
	}
	
	@Override
	public boolean hadChaosLastUpdate() {
		boolean hadChaos = chaosInBloodLast > 0;
		chaosInBloodLast = chaosInBlood;
		return hadChaos;
	}
	
	@Override
	public float addChaos(float chaos) {
		chaosInBloodLast = chaosInBlood;
		float chaosToAdd = Math.min(maxChaosInBlood - chaosInBlood, chaos);
		chaosInBlood += chaosToAdd;
		return chaos - chaosToAdd;
	}

	@Override
	public float removeChaos(float chaos) {
		chaosInBloodLast = chaosInBlood;
		float chaosToRemove = Math.min(chaosInBlood, chaos);
		chaosInBlood -= chaosToRemove;
		return chaosToRemove;
	}
	
	@Override
	public void setChaos(float chaos) {
		chaosInBloodLast = chaosInBlood;
		float chaosToSet = Math.min(maxChaosInBlood, chaos);
		chaosInBlood = chaosToSet;
	}
}
