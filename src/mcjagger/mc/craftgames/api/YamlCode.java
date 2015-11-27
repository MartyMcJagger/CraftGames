package mcjagger.mc.craftgames.api;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.configuration.ConfigurationSection;

public class YamlCode {
	
	public final String hash;
	public final String methodName;
	
	private final Class<?>[] paramTypes;
	private final Object[] parameters;
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		YamlCode testA = new YamlCode("substring(i4, i11)");
		System.out.println(testA.invoke(String.class, "AAAAWorked!1234567890"));
		
		YamlCode testB = new YamlCode("println(s\"Hello, \"World\"!\")");
		testB.invoke(System.out.getClass(), System.out);
	}
	
	public static YamlCode fromConfig(ConfigurationSection configurationSection, String path) {
		String hash = configurationSection.getString(path);
		if (hash == null || hash.length() < 1)
			return null;
			//throw new IllegalArgumentException("Could not find YamlCode in '" + path + "' of '" + config.getName() + "'");
		
		return new YamlCode(hash);
	}
	

	private Queue<Class<?>> typeQueue = new LinkedList<Class<?>>();
	private Queue<Object> paramQueue = new LinkedList<Object>();
	
	private char typeChar = '~';
	private String curToken = "";
	
	public YamlCode(String hash) {
		
		this.hash = hash;
		
		try {
			
			int openIdx = hash.indexOf("(");
			int closeIdx = hash.indexOf(")");
			
			this.methodName = hash.substring(0, openIdx);
			
			String paramStr = hash.substring(openIdx + 1, closeIdx);
			
			
			for (char c : paramStr.toCharArray()) {
				if (typeChar == '~') {
					
					if (!Character.isWhitespace(c))
						typeChar = c;
					
					continue;
				}
				
				if (c == ',' && (typeChar != 's' || curToken.endsWith("\""))) {	
					pushToken();
				} else {
					curToken += c;
				}
			}
			
			pushToken();
			
			paramTypes = typeQueue.toArray(new Class<?>[typeQueue.size()]);
			parameters = paramQueue.toArray(new Object[typeQueue.size()]);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new IllegalArgumentException("Could not parse YamlCode '"+hash+"'.");	
		}
	}
	
	private void pushToken() {
		if (typeChar == 'i' || typeChar == 'I') {
			typeQueue.add(int.class);
			paramQueue.add(Integer.parseInt(curToken));
		} else if (typeChar == 'd' || typeChar == 'D') {
			typeQueue.add(double.class);
			paramQueue.add(Double.parseDouble(curToken));
		} else if (typeChar == 'f' || typeChar == 'F') {
			typeQueue.add(float.class);
			paramQueue.add(Float.parseFloat(curToken));
		} else if (typeChar == 'l' || typeChar == 'L') {
			typeQueue.add(long.class);
			paramQueue.add(Long.parseLong(curToken));
		} else if (typeChar == 'b' || typeChar == 'B') {
			typeQueue.add(boolean.class);
			paramQueue.add(Boolean.parseBoolean(curToken));
		} else if (typeChar == 'c' || typeChar == 'C') {
			typeQueue.add(char.class);
			paramQueue.add(curToken);
		} else if (typeChar == 's' || typeChar == 'S') {
			typeQueue.add(String.class);
			paramQueue.add(curToken.substring(1, curToken.length() - 1));
		}

		typeChar = '~';
		curToken = "";
	}
	
	public Object invoke(Class<?> clazz, Object instance) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getMethod(methodName, paramTypes).invoke(instance, parameters);
	}
}
