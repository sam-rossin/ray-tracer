
public class Ray {
	double [] point = new double[3];
	double [] direction = new double[3];
	
	public double vLength2(){
		return (double) direction[0]*direction[0]+
				direction[1]*direction[1] + direction[2]*direction[2];
	}
	
	public Ray(double[] point, double [] direction) { 
		for (int i = 0; i < 3; i++) {
			this.point[i] = point[i];
			this.direction[i] = direction[i];
		}
	}
	
	double [] rayPoint(double t) {
		double p[] = new double[3];
		for (int i = 0; i < 3; i++) {
			p[i] = point[i] + t*direction[i];
		}
		return p;
	}
}
