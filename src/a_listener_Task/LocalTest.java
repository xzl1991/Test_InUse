package a_listener_Task;

import java.util.Locale;



public class LocalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Locale[] localList = Locale.getAvailableLocales();
		System.out.println("java所支持的国际语言"+Locale.getDefault());
		for(Locale local:localList){
			System.out.println("国家:"+local.getDisplayCountry()+":"+local.getCountry());
			System.out.println("  语言:"+local.getDisplayLanguage()+":"+local.getLanguage());
		}
	}

}
