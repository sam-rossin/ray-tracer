
class Sphere extends Shape{
	double[] center;
	double radius;
	public Sphere(int num, double spec, double diff, double ref,double r, double[] center){
		this.center = center;      
		this.radius = r;
		number = num;
		specular = spec;
		reflection = ref;
		diffuse = diff;
		amb = 1;
		color = new double[] {40, 140, 50};
	}
	
	public Intersection intersect(Ray ray){
		Intersection retval = null;
		double[]A = new double[3];
		for(int i=0; i<3; i++) {
			A[i] = center[i] - ray.point[i];
		}
		
		double AlengthSquare = A[0]*A[0]+A[1]*A[1]+A[2]*A[2];
		double vDotA = A[0]*ray.direction[0]+A[1]*ray.direction[1]+A[2]*ray.direction[2];
		
		double disc = (double) (4*vDotA*vDotA - 4*ray.vLength2()*(AlengthSquare - radius*radius));
		if (disc < 0) return null;
		double solution1 = (double) ((2*vDotA - Math.sqrt(disc))/(2*ray.vLength2()));
		double solution2 = (double) ((2*vDotA + Math.sqrt(disc))/(2*ray.vLength2()));
		double tval = 0;
		if (solution1 >= 0) {
			tval = solution1;
		}else if(solution2 >=0){
			tval = solution2;
		}else{
			return null;
		}
		retval = new Intersection(number, tval);
		double[] p = ray.rayPoint(tval);
		retval.setPoint(p);
		retval.setNormal(new double[]{(p[0]-center[0])/radius, 
				(p[1]-center[1])/radius, (p[2]-center[2])/radius});
		return retval;
	}
	

	public double[] color(Intersection i){
		return color;
	}
}