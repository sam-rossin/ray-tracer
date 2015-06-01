
public abstract class Shape{
	int number;
	double specular;
	double diffuse;
	double reflection;
	double amb;
	double[] color;
	abstract Intersection intersect(Ray r);
	abstract double[] color(Intersection i);
	
	protected double dot(double[] a, double[] b){
		double retval = 0;
		for(int i=0; i<a.length; i++) retval += a[i]*b[i];
		return retval;
	}
	protected double[] cross(double[] u, double[] v){
		double[] retval = new double[3];
		retval[0] = u[1]*v[2]- u[2]*v[1];
		retval[1] = u[2]*v[0] - u[0]*v[2];
		retval[2] = u[0]*v[1] - u[1]*v[0];
		return retval;
	}
	protected double[] minus(double[] u, double[] v){
		double[] retval = new double[3];
		for(int i=0; i<3; i++) retval[i] = u[i] - v[i];
		return retval;
	}
	
	protected double[] scale(double[] a, double b){
		double[] retval = new double[a.length];
		for(int i = 0; i<a.length; i++){
			retval[i] = a[i]*b;
		}return retval;
	}
}
