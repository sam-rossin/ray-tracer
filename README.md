# Ray Tracer
This is a simple ray tracer. The main method is in RayTracer.java, which is also where the image to be traced is set up. The objects in a scene must be instances of a subclass of the abstract class Shape, and are stored in an array called "objects" in RayTracer.java. I have created a number of such subclasses (Sphere, Polygon, etc.), but the ones I have created are really just a start.

There are a few example images in the root directory.

### TODO
* Separate the setup from the other code in a more user friendly matter.
* Implement more primatives.
* Support CSG trees to allow for combinations of primatives.
* Refactor math to always use Matrix objects instead of using a hodge podge of different functions on arrays sometimes.
