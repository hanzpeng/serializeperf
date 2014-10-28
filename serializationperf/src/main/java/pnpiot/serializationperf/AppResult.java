package pnpiot.serializationperf;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AppResult {
	public static void printResult(String filename, long loops, long elapsedTime) throws Exception {
		String filePath = System.getProperty("user.dir") + "\\" + filename + "txt";
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");

		System.out.println(filename + " Result");
		writer.println(filename + " Result");

		System.out.println("Number of Iterations = " + loops);
		writer.println("Number of Iterations = " + loops);

		System.out.println("Mini seconds elapsed = " + elapsedTime);
		writer.println("Mini seconds elapsed = " + elapsedTime);

		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value = method.invoke(operatingSystemMXBean);
				System.out.println(method.getName().substring(3) + " = " + value);
				writer.println(method.getName().substring(3) + " = " + value);
			}
		}
		writer.close();
	}
}
