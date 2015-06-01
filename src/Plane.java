
class Plane extends Shape{
	double[] norm;
	double constant;
	boolean checker;
	double[] color1;
	double[] color2;
	public Plane(int num, double spec, double diff, double ref, 
			double[] norm, double constant, boolean checker){
		this.number = num;
		this.checker = checker;
		this.specular = spec;
		this.diffuse = diff;
		this.reflection = ref;
		color = new double[]{120,100,100};
		color1 = new double[]{200,200,200};
		color2 = new double[]{200,40, 40};
		double normLen = Math.sqrt(dot(norm, norm));
		this.norm = new double[3];
		for(int i=0; i<3; i++) this.norm[i] = norm[i]/normLen;
		this.constant = constant;
	}
	public Intersection intersect(Ray r){
		double nDotP = dot(norm, r.point);
		double nDotV = dot(norm, r.direction);
		if( nDotV == 0) return null;
		double tval = (constant - nDotP)/nDotV;
		Intersection retval = new Intersection(this.number, tval);
		if(tval < 0){
			retval = null;
		}else{
			retval.setNormal(norm);
			retval.setPoint(r.rayPoint(tval));
		}
		return retval;
	}
	
	public double[] color(Intersection I){
		if (!checker) return color;
		
		//checkering only works for horizontal plains
		double val1 = (I.point[0]%4);
		double val2 =(I.point[1]%4);
		if (val1 < 0) val1 += 4;
		if (val2 < 0) val2 += 4;
		if ((val1 < 2 && val2 <2) || (val1 >= 2 && val2 >=2)) return color1;
		else return color2;
	}
}