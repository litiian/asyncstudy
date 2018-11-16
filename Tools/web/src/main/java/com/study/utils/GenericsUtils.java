package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericsUtils {
	
	public static void main(String[] args) throws Exception{
//		AlarmData a = new AlarmData();
//		System.out.println(a.getTopLevel());
//		setProp(a, "topLevel", "02");
//		System.out.println(a.getTopLevel());
		
		boolean[] a = new boolean[3];
		System.out.println(a.getClass().getCanonicalName());
		System.out.println(a.getClass().getName());
		System.out.println(a.getClass().getSimpleName());
		System.out.println(a.getClass().isArray());
	}

	public static <T> void setProp(T t, String propName, Object propVal){
		Class<?> cSource = t.getClass();
		for(Field f : cSource.getDeclaredFields()){
			if (f.getName().equals(propName)) {
				if ("[B".equals(f.getType().getName())) {
					propVal = SocketUtil.hexStr2Bytes(propVal.toString());
				}
			}
		}
		String setterName = getSetter(propName);
		for(Method m : cSource.getDeclaredMethods()){
			if (m.getName().equals(setterName)) {
				try {
					m.invoke(t, propVal);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new RuntimeException("set property failed!", e);
				}
				return ;
			}
		}
	}
	
//	public static <T> boolean equals(T t1, T t2){
//		Class<?> cSource = t1.getClass();
//		Field[] sFields = cSource.getDeclaredFields();
//		for (Field field : sFields) {
//			String getterName = getGetter(field.getName());
//			for(Method m : cSource.getDeclaredMethods()){
//				if (m.getName().equals(setterName)) {
//					m.invoke(t, propVal);
//					return ;
//				}
//			}
//		}
//		return false;
//	}
	
	private static String getGetter(String propName){
		return "get"+propName.substring(0, 1).toUpperCase()
				+propName.substring(1);
	}
	
	private static String getSetter(String propName){
		return "set"+propName.substring(0, 1).toUpperCase()
				+propName.substring(1);
	}
}
