package vswe.stevescarts.old.Helpers;
import vswe.stevescarts.vehicles.entities.EntityModularCart;

public class BlockCoord{
	private int x;
	private int y;
	private int z;
	public BlockCoord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}		
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockCoord) {
			BlockCoord coord = (BlockCoord)obj;
			return this.x == coord.x &&
					this.y == coord.y &&
					this.z == coord.z;
		}else{
			return false;
		}
	}
	
	public double getDistToCartSquared(EntityModularCart cart) {
		int xDif = this.x - cart.x();
		int yDif = this.y - cart.y();
		int zDif = this.z - cart.z();
		
		return Math.pow(xDif, 2)
			 + Math.pow(yDif, 2)
			 + Math.pow(zDif, 2);
	}
	
	public double getHorizontalDistToCartSquared(EntityModularCart cart) {
		int xDif = this.x - cart.x();
		int zDif = this.z - cart.z();
		
		return Math.pow(xDif, 2)
			 + Math.pow(zDif, 2);
	}		
}