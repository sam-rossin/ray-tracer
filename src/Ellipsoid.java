
class Ellipsoid extends Shape{
	double[] center;
	double radius;
	double a;
	double b;
	double c;
	public Ellipsoid(int num, double spec, double diff, double ref, double[] center, 
			double a, double b, double c){
		this.center = center;      
		this.a = a;
		this.b = b;
		this.c = c;
		number = num;
		specular = spec;
		reflection = ref;
		diffuse = diff;
		amb = 1;
		color = new double[] {40, 140, 50};
	}
	
	public Intersection intersect(Ray ray){
		Matrix M = new Matrix(new double[][]{{1/a, 0, 0},{0, 1/b, 0},{0, 0, 1/c}});
		Matrix C = Matrix.rowVector(center);
		Matrix v = Matrix.rowVector(ray.direction);
		Matrix P0 = Matrix.rowVector(ray.point);
		Matrix v1 = v.times(M);
		Matrix P1 = P0.times(M).plus(C.times(M).scalarMult(-1));
		
		double CC = P1.dot(P1) - 1;
		double BB = 2*P1.dot(v1);
		double AA = v1.dot(v1);
		
		double det = BB*BB - 4*AA*CC;
		if(det < 0) return null;
		
		double solution1 = (-BB - Math.sqrt(det))/(2*AA);
		double solution2 = (-BB + Math.sqrt(det))/(2*AA);
		double tval = 0;
		if (solution1 >= 0) {
			tval = solution1;
		}else if(solution2 >=0){
			tval = solution2;
		}else{
			return null;
		}
		
		
		Intersection retval = new Intersection(number, tval);
		double[] p = ray.rayPoint(tval);
		retval.setPoint(p);
		double[] normal = new double[]{2*(p[0] - center[0])/(a*a), 
				2*(p[1] - center[1])/(b*b), 2*(p[2] - center[2])/(c*c)};
		retval.setNormal(scale(normal, 1/Math.sqrt(dot(normal, normal))));
		return retval;
		
	}
	

	public double[] color(Intersection i){
		return color;
	}
}
