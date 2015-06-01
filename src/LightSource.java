
public class LightSource {
	public LightSource(double[] color, double[] position){
		this.color = new double[3];
		this.position = new double[3];
		for(int i=0; i<3; i++){
			this.color[i] = color[i];
			this.position[i] = position[i];
		}
	}
	
	public double[] color;
	public double[] position;
}
