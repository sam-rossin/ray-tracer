
public class Intersection {
	double [] point;
	double [] normal;
	int objectNumber;
	double t;
	
	public Intersection(int object, double t) {
		this.objectNumber = object;
		this.t = t;
	}
	
	public Intersection() {
		this(0, 0);
	}
	
	void setPoint(double [] point){
		this.point = new double[3];
		for (int i = 0; i < 3; i++)
			this.point[i] = point[i];
	}
	void setNormal(double [] normal){
		this.normal = new double[3];
		for (int i = 0; i < 3; i++)
			this.normal[i] = normal[i];
	}
	
	void setObjectNumber(int i) {
		this.objectNumber = i;
	}
	
	void setT(int t) {
		this.t= t;
	}
}