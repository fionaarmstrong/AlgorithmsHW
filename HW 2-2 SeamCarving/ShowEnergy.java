/*************************************************************************
 *  Compilation:  javac ShowEnergy.java
 *  Execution:    java ShowEnergy input.png
 *  Dependencies: SeamCarver.java SCUtility.java Picture.java StdDraw.java
 *                
 *
 *  Read image from file specified as command line argument. Show original
 *  image (only useful if image is large enough).
 *
 *************************************************************************/

public class ShowEnergy {

	public static void utColorsAreCorrectlyConstructed(Picture inputImg, SeamCarver sc) {
        System.out.println("Colors");
        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++)
                System.out.print(sc.get(j, i) + " ");
            System.out.println();
        }
        
        System.out.println("SC");
        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++)
                System.out.print(inputImg.get(j, i) + " ");
            System.out.println();
        }
	}
	
    public static void main(String[] args)
    {
        //Picture inputImg = new Picture(args[0]);
    	Picture inputImg = new Picture("6x5.png");
    	System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        inputImg.show();        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

        for (int i = 0; i < sc.height(); i++) {
            for (int j = 0; j < sc.width(); j++)
                System.out.print(sc.energy(j, i) + " ");
            System.out.println();
        }
    
        //sc.findVerticalSeam();

    }

}