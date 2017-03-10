import java.text.SimpleDateFormat;


public class Utf {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		int[] a = new int[]{1,4,3};
		int b = 1;
		int c = 0;
		int d = 0;
		for(int i=0;i<a.length;i++){
			c= b<<a[i];
			d+=c;
			System.out.println("0...."+c);
			System.out.println("1...."+d);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long st = System.currentTimeMillis();
		Thread.sleep(10000);
		long end = System.currentTimeMillis();
		System.out.println(end-st);
		System.out.println(sdf.format(end-10*100));
		Integer.parseInt(null);
	}
}
